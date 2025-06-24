package com.inventory.service.impl;

import com.inventory.model.Item;
import com.inventory.payload.request.ItemRequest;
import com.inventory.payload.response.ItemResponse;
import com.inventory.payload.response.PagedResponse;
import com.inventory.repository.ItemRepository;
import com.inventory.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;


    @Override
    public PagedResponse<ItemResponse> getList(String search, Pageable pageable) {

        Page<ItemResponse> itemResponsePage;
        if (search != null && !search.isEmpty()) {
            itemResponsePage =  itemRepository.findByNameContainingIgnoreCase(search, pageable).map(this::toItemResponse);
        } else {
            itemResponsePage = itemRepository.findAll(pageable).map(this::toItemResponse);
        }
        return PagedResponse.fromPage(itemResponsePage);
    }

    @Override
    public ItemResponse getById(UUID id) {

        Item itemOptional = itemRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Get Item By Id Failed, Item not found")
                );

        return toItemResponse(itemOptional);
    }

    @Override
    public ItemResponse create(ItemRequest request) {

        Item item = new Item();
        item.setName(request.getName());
        item.setPrice(request.getPrice());

        item = itemRepository.save(item);

        return toItemResponse(item);
    }

    @Override
    public ItemResponse update(UUID id, ItemRequest request) {

        Item itemOptional = itemRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Update Item By Id Failed, Item not found")
                );

        if (null != request.getName()) itemOptional.setName(request.getName());
        if (null != request.getPrice()) itemOptional.setPrice(request.getPrice());

        itemRepository.save(itemOptional);

        return toItemResponse(itemOptional);
    }

    @Override
    public void delete(UUID id) {

        Item itemOptional = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Delete Item By Id Failed, Item not found"));
        itemRepository.delete(itemOptional);
    }

    private ItemResponse toItemResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .build();
    }
}
