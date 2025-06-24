package com.inventory.service.impl;

import com.inventory.config.InsufficientStockException;
import com.inventory.model.Inventory;
import com.inventory.model.InventoryType;
import com.inventory.model.Item;
import com.inventory.payload.request.InventoryRequest;
import com.inventory.payload.response.InventoryResponse;
import com.inventory.payload.response.PagedResponse;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.ItemRepository;
import com.inventory.service.InventoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;

    @Override
    public PagedResponse<InventoryResponse> getList(Pageable pageable) {

        Page<InventoryResponse> inventoryResponsePage;
        inventoryResponsePage = inventoryRepository.findAll(pageable)
                .map(this::toInventoryResponse);
        return PagedResponse.fromPage(inventoryResponsePage);

    }

    @Override
    public InventoryResponse getById(UUID id) {
        Inventory inventoryOptional = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Get Inventory Data By Id Failed, Inventory Data not found"
                        )
                );

        return toInventoryResponse(inventoryOptional);
    }

    @Override
    public InventoryResponse create(InventoryRequest request) {

        Item itemOptional = itemRepository.findById(request.getItemId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Create Inventory Failed, Item not found")
                );

        Optional<Inventory> inventoryOptional = inventoryRepository.findByItemId(itemOptional.getId());

        if (inventoryOptional.isEmpty()) {
            if (request.getType().equals(InventoryType.T)) {
                Inventory inventory = new Inventory();
                inventory.setItem(itemOptional);
                inventory.setQuantity(request.getQuantity());
                inventory = inventoryRepository.save(inventory);
                return toInventoryResponse(inventory);
            } else {
                throw new InsufficientStockException(
                        "Insufficient Quantity, Create Inventory Data and Withdraw Failed"
                );
            }

        } else {
            if (request.getType().equals(InventoryType.W) &&
                            inventoryOptional.get().getQuantity() < request.getQuantity()
            ) {
                throw new InsufficientStockException(
                        "Insufficient Quantity, Create Inventory Data and Withdraw Failed"
                );
            }
            inventoryOptional.get().setQuantity(
                    request.getType().equals(InventoryType.T) ?
                            inventoryOptional.get().getQuantity() + request.getQuantity()
                            : inventoryOptional.get().getQuantity() - request.getQuantity()
            );
            inventoryRepository.save(inventoryOptional.get());
        }
        return toInventoryResponse(inventoryOptional.get());
    }

    @Override
    public InventoryResponse update(UUID id, InventoryRequest request) {

        Inventory inventoryOptional = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Update Inventory By Id Failed, Inventory Id not found"
                        )
                );
        if (null != request.getQuantity()) inventoryOptional.setQuantity(request.getQuantity());
        if (null != request.getItemId()) {
            Item itemOptional = itemRepository.findById(id)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Update Item By Id Failed, Item not found"
                            )
                    );
            inventoryOptional.setItem(itemOptional);
        }
        inventoryOptional = inventoryRepository.save(inventoryOptional);

        return toInventoryResponse(inventoryOptional);
    }

    @Override
    public void delete(UUID id) {

        Inventory inventoryOptional = inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Delete Inventory By Id Failed, Inventory Id not found"
                        )
                );
        inventoryRepository.delete(inventoryOptional);
    }

    private InventoryResponse toInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .itemId(inventory.getItem().getId())
                .quantity(inventory.getQuantity())
                .name(inventory.getItem().getName())
                .build();
    }
}
