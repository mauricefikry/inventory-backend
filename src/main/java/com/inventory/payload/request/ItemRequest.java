package com.inventory.payload.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemRequest {

    @NotBlank
    @NotNull
    public String name;

    @NotNull
    @DecimalMin("0.0")
    public BigDecimal price;

    public ItemRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
