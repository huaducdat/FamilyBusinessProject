package com.huaducdat.storemanager.payroll.service;

import com.huaducdat.storemanager.attendance.model.entity.Attendance;
import com.huaducdat.storemanager.payroll.model.entity.Payroll;
import com.huaducdat.storemanager.payroll.model.entity.SalaryConfig;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.payroll.dto.request.SetSalaryConfigRequest;
import com.huaducdat.storemanager.payroll.dto.response.PayrollResponse;
import com.huaducdat.storemanager.attendance.repository.AttendanceRepository;
import com.huaducdat.storemanager.payroll.repository.PayrollRepository;
import com.huaducdat.storemanager.payroll.repository.SalaryConfigRepository;
import com.huaducdat.storemanager.user.repository.UserRepository;
import com.huaducdat.storemanager.shared.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PayrollService {

    private final SalaryConfigRepository salaryConfigRepository;

    private final PayrollRepository payrollRepository;

    private final AttendanceRepository attendanceRepository;

    private final UserRepository userRepository;

    public PayrollService(
            SalaryConfigRepository salaryConfigRepository,
            PayrollRepository payrollRepository,
            AttendanceRepository attendanceRepository,
            UserRepository userRepository
    ) {

        this.salaryConfigRepository =
                salaryConfigRepository;

        this.payrollRepository =
                payrollRepository;

        this.attendanceRepository =
                attendanceRepository;

        this.userRepository =
                userRepository;
    }

    // =========================
    // SET CONFIG
    // =========================

    public void setConfig(
            User currentUser,
            SetSalaryConfigRequest request
    ) {

        PermissionUtil.requireManager(
                currentUser
        );

        User user =
                userRepository
                        .findByIdAndStoreId(
                                request.getUserId()
                                ,
                                currentUser.getStore()
                                        .getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        SalaryConfig config =
                salaryConfigRepository

                        .findByUserIdAndUserStoreId(
                                user.getId(),
                                currentUser.getStore()
                                        .getId()
                        )

                        .orElse(
                                new SalaryConfig()
                        );

        config.setUser(user);

        config.setHourlyRate(
                request.getHourlyRate()
        );

        config.setOvertimeRate(
                request.getOvertimeRate()
        );

        config.setBonusAmount(
                request.getBonusAmount()
        );

        config.setDeductionAmount(
                request.getDeductionAmount()
        );

        salaryConfigRepository.save(
                config
        );
    }

    // =========================
    // CALCULATE PAYROLL
    // =========================

    public PayrollResponse calculate(
            User currentUser,
            Long userId,
            Integer month,
            Integer year
    ) {

        User user =
                userRepository
                        .findByIdAndStoreId(
                                userId,
                                currentUser.getStore()
                                        .getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        SalaryConfig config =
                salaryConfigRepository

                        .findByUserIdAndUserStoreId(
                                userId,
                                currentUser.getStore()
                                        .getId()
                        )

                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Salary config not found"
                                )
                        );

        List<Attendance> attendances =
                attendanceRepository
                        .findByUserIdAndUserStoreIdAndWorkDateBetween(
                                userId,
                                currentUser.getStore()
                                        .getId(),
                                LocalDate.of(year, month, 1),
                                LocalDate.of(year, month, 1)
                                        .withDayOfMonth(
                                                LocalDate.of(year, month, 1)
                                                        .lengthOfMonth()
                                        )
                        );

        double totalHours = 0;

        for (Attendance attendance : attendances) {

            if (

                    attendance.getUser()
                            .getId()
                            .equals(userId)

                            &&

                            attendance.getCheckInTime()
                                    .getMonthValue()
                                    == month

                            &&

                            attendance.getCheckInTime()
                                    .getYear()
                                    == year

                            &&

                            attendance.getCheckOutTime()
                                    != null
            ) {

                long minutes =
                        Duration.between(

                                        attendance.getCheckInTime(),

                                        attendance.getCheckOutTime()
                                )

                                .toMinutes();

                totalHours +=
                        minutes / 60.0;
            }
        }

        double overtimeHours = 0;

        if (totalHours > 208) {

            overtimeHours =
                    totalHours - 208;
        }

        double baseSalary =
                totalHours
                        *
                        config.getHourlyRate();

        double overtimeSalary =
                overtimeHours
                        *
                        config.getOvertimeRate();

        double finalSalary =

                baseSalary
                        +

                        overtimeSalary
                        +

                        config.getBonusAmount()
                        -

                        config.getDeductionAmount();

        Payroll payroll =
                new Payroll();

        payroll.setUser(user);

        payroll.setPayrollMonth(month);

        payroll.setPayrollYear(year);

        payroll.setTotalHours(totalHours);

        payroll.setOvertimeHours(overtimeHours);

        payroll.setBaseSalary(baseSalary);

        payroll.setOvertimeSalary(overtimeSalary);

        payroll.setBonusAmount(
                config.getBonusAmount()
        );

        payroll.setDeductionAmount(
                config.getDeductionAmount()
        );

        payroll.setFinalSalary(finalSalary);

        payrollRepository.save(
                payroll
        );

        return PayrollResponse.builder()

                .employeeName(
                        user.getFullName()
                )

                .payrollMonth(
                        month
                )

                .payrollYear(
                        year
                )

                .totalHours(
                        totalHours
                )

                .overtimeHours(
                        overtimeHours
                )

                .finalSalary(
                        finalSalary
                )

                .build();
    }

    // =========================
    // MONTHLY PAYROLL
    // =========================

    public List<PayrollResponse>
    monthlyPayroll(
            User currentUser,
            Integer month,
            Integer year
    ) {

        return payrollRepository

                .findByUserStoreIdAndPayrollMonthAndPayrollYear(
                        currentUser.getStore()
                                .getId(),
                        month,
                        year
                )

                .stream()

                .map(payroll ->

                        PayrollResponse.builder()

                                .employeeName(
                                        payroll.getUser()
                                                .getFullName()
                                )

                                .payrollMonth(
                                        payroll.getPayrollMonth()
                                )

                                .payrollYear(
                                        payroll.getPayrollYear()
                                )

                                .totalHours(
                                        payroll.getTotalHours()
                                )

                                .overtimeHours(
                                        payroll.getOvertimeHours()
                                )

                                .finalSalary(
                                        payroll.getFinalSalary()
                                )

                                .build()
                )

                .toList();
    }
}
