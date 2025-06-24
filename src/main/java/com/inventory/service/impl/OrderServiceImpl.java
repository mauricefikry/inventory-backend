package com.inventory.service.impl;

import com.inventory.config.InsufficientStockException;
import com.inventory.model.Inventory;
import com.inventory.model.InventoryType;
import com.inventory.model.Item;
import com.inventory.model.Order;
import com.inventory.payload.request.InventoryRequest;
import com.inventory.payload.request.OrderRequest;
import com.inventory.payload.response.InventoryResponse;
import com.inventory.payload.response.OrderResponse;
import com.inventory.payload.response.PagedResponse;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.ItemRepository;
import com.inventory.repository.OrderRepository;
import com.inventory.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public PagedResponse<OrderResponse> getList(Pageable pageable) {

        Page<OrderResponse> orderResponsePage;
        orderResponsePage = orderRepository.findAll(pageable)
                .map(this::toOrderResponse);
        return PagedResponse.fromPage(orderResponsePage);
    }

    @Override
    public OrderResponse getById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Get Order Data By Id Failed, Order Data not found"
                        )
                );

        return toOrderResponse(order);
    }

    @Override
    public OrderResponse create(OrderRequest request) {
        Item itemOptional = itemRepository.findById(request.getItemId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Create Order Failed, Item not found")
                );

        Optional<Inventory> inventoryOptional = inventoryRepository.findByItemId(itemOptional.getId());
        if (inventoryOptional.isEmpty() ||
                request.getQuantity() > inventoryOptional.get().getQuantity() ||
                inventoryOptional.get().getQuantity() - request.getQuantity() < 0
        ) {
            throw new InsufficientStockException(
                    "Insufficient Stock, Create Order Failed"
            );
        }

        Order order = new Order();
        order.setQuantity(request.getQuantity());
        order.setItem(itemOptional);
        order.setPrice(request.getPrice());
        order.setOrderNo(request.getOrderNo());
        order = orderRepository.save(order);

        inventoryOptional.get().setQuantity(
                inventoryOptional.get().getQuantity()-request.getQuantity()
        );

        inventoryRepository.save(inventoryOptional.get());

        return toOrderResponse(order);
    }

    @Override
    public OrderResponse update(UUID id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "update Order Data By Id Failed, Order Data not found"
                        )
                );
        if (null != request.getOrderNo()) order.setOrderNo(request.getOrderNo());
        if (null != request.getPrice()) order.setPrice(request.getPrice());

        order = orderRepository.save(order);

        return toOrderResponse(order);
    }

    @Override
    public void delete(UUID id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Delete Order Data By Id Failed, Order Data not found"
                        )
                );
        orderRepository.delete(order);
    }

    private OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .itemId(order.getItem().getId())
                .orderNo(order.getOrderNo())
                .quantity(order.getQuantity())
                .price(order.getItem().getPrice())
                .build();
    }
}
