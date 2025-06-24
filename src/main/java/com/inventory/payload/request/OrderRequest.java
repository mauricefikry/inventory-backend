package com.inventory.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderRequest {

    @NotBlank
    @NotNull
    private String orderNo;

    @NotNull
    private UUID itemId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;
}
