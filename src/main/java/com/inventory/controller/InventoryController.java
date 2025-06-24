package com.inventory.controller;


import com.inventory.payload.request.InventoryRequest;
import com.inventory.payload.response.ApiResponse;
import com.inventory.payload.response.InventoryResponse;
import com.inventory.payload.response.PagedResponse;
import com.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<InventoryResponse>>> getAllInventory(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Get All Item successfully",
                        0,
                        inventoryService.getList(pageable)
                )
        );
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Get Inventory By Id successfully",
                        0,
                        inventoryService.getById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryResponse>> create(
            @Valid @RequestBody InventoryRequest request
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Create Inventory successfully",
                        0,
                        inventoryService.create(request)
                )
        );
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody InventoryRequest request
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Update Inventory successfully",
                        0,
                        inventoryService.update(id, request)
                )
        );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> delete(
            @PathVariable UUID id
    ) {
        inventoryService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Delete Inventory successfully",
                        0,
                        null
                )
        );
    }
}
