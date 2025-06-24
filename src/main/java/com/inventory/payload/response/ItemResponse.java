package com.inventory.payload.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ItemResponse {

    private UUID id;
    public String name;
    public BigDecimal price;
}
