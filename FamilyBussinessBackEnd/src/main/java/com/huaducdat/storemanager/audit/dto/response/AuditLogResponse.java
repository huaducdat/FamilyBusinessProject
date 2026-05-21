package com.huaducdat.storemanager.audit.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AuditLogResponse {

    private String username;

    private String action;

    private String description;

    private LocalDateTime createdAt;
}
