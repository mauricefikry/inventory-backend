package com.inventory.service;

import com.inventory.payload.request.ItemRequest;
import com.inventory.payload.response.ItemResponse;
import com.inventory.payload.response.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ItemService {

    PagedResponse<ItemResponse> getList(String search, Pageable pageable);
    ItemResponse getById(UUID id);
    ItemResponse create(ItemRequest request);
    ItemResponse update(UUID id, ItemRequest request);
    void delete(UUID id);
}
