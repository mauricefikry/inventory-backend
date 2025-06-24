package com.inventory.payload.request;

import com.inventory.model.InventoryType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class InventoryRequest {

    @NotNull
    public UUID itemId;

    @NotNull
    private Integer quantity;

    private InventoryType type;

    public InventoryRequest(UUID itemId, int quantity, InventoryType type) {
    }
}
