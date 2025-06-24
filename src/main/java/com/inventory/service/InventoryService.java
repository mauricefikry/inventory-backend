package com.inventory.service;

import com.inventory.payload.request.InventoryRequest;
import com.inventory.payload.response.InventoryResponse;
import com.inventory.payload.response.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InventoryService {

    PagedResponse<InventoryResponse> getList(Pageable pageable);
    InventoryResponse getById(UUID id);
    InventoryResponse create(InventoryRequest request);
    InventoryResponse update(UUID id, InventoryRequest request);
    void delete(UUID id);
}
