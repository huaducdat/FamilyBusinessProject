package com.huaducdat.storemanager.product.dto.response;

import com.huaducdat.storemanager.model.enumtype.InventoryType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InventoryHistoryResponse {

    private Long id;

    private String username;

    private InventoryType type;

    private Integer quantity;

    private Integer beforeQuantity;

    private Integer afterQuantity;

    private String note;

    private LocalDateTime createdAt;
}