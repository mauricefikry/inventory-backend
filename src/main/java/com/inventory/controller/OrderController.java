package com.inventory.controller;

import com.inventory.payload.request.OrderRequest;
import com.inventory.payload.response.ApiResponse;
import com.inventory.payload.response.OrderResponse;
import com.inventory.payload.response.PagedResponse;
import com.inventory.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<OrderResponse>>> getAllInventory(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Get All Order successfully",
                        0,
                        orderService.getList(pageable)
                )
        );
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Get Order By Id successfully",
                        0,
                        orderService.getById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(
            @Valid @RequestBody OrderRequest request
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Create Order successfully",
                        0,
                        orderService.create(request)
                )
        );
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody OrderRequest request
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Update Order successfully",
                        0,
                        orderService.update(id, request)
                )
        );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> delete(
            @PathVariable UUID id
    ) {
        orderService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Delete Order successfully",
                        0,
                        null
                )
        );
    }


}
