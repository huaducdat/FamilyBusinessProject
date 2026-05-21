package com.huaducdat.storemanager.audit.controller;

import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.audit.service.AuditService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(
            AuditService auditService
    ) {

        this.auditService =
                auditService;
    }

    // =========================
    // HISTORY
    // =========================

    @GetMapping
    public BaseResponse<?> history(
            HttpServletRequest request
    ) {
        User currentUser =
                CurrentUserUtil.get(
                        request
                );
        return BaseResponse.builder()
                .success(true)
                .message("Audit history")
                .data(
                        auditService.history(
                                currentUser
                        )
                )
                .build();
    }
}
