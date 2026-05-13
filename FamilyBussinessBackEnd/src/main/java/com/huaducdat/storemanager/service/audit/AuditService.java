package com.huaducdat.storemanager.service.audit;

import com.huaducdat.storemanager.model.entity.AuditLog;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.AuditAction;
import com.huaducdat.storemanager.model.response.AuditLogResponse;
import com.huaducdat.storemanager.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository repository;



    public AuditService(
            AuditLogRepository repository
    ) {

        this.repository =
                repository;
    }

    public void log(
            User user,
            AuditAction action,
            String description
    ) {

        AuditLog log =
                new AuditLog();

        log.setStore(
                user.getStore()
        );

        log.setUser(user);

        log.setAction(action);

        log.setDescription(
                description
        );

        repository.save(log);
    }

    public List<AuditLogResponse> history(
            User currentUser
    ) {

        return repository
                .findByStoreIdOrderByCreatedAtDesc(
                        currentUser
                                .getStore()
                                .getId()
                )
                .stream()

                .sorted((a, b) ->

                        b.getCreatedAt()
                                .compareTo(
                                        a.getCreatedAt()
                                )
                )

                .map(log ->

                        AuditLogResponse
                                .builder()

                                .username(
                                        log.getUser()
                                                .getUsername()
                                )

                                .action(
                                        log.getAction()
                                                .name()
                                )

                                .description(
                                        log.getDescription()
                                )

                                .createdAt(
                                        log.getCreatedAt()
                                )

                                .build()
                )

                .toList();
    }
}