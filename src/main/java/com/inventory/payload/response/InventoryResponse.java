package com.inventory.payload.response;

import com.inventory.model.InventoryType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InventoryResponse {

    private UUID id;
    private UUID itemId;
    private Integer quantity;
    private String name;
}
