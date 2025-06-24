package com.inventory.service;

import com.inventory.payload.request.InventoryRequest;
import com.inventory.payload.request.OrderRequest;
import com.inventory.payload.response.OrderResponse;
import com.inventory.payload.response.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    PagedResponse<OrderResponse> getList(Pageable pageable);
    OrderResponse getById(UUID id);
    OrderResponse create(OrderRequest request);
    OrderResponse update(UUID id, OrderRequest request);
    void delete(UUID id);
}
