package com.inventory.payload.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {

    private UUID id;
    private String orderNo;
    private UUID itemId;
    private Integer quantity;
    private BigDecimal price;
}
