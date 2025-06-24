package com.inventory;

import com.inventory.config.InsufficientStockException;
import com.inventory.model.Inventory;
import com.inventory.model.InventoryType;
import com.inventory.model.Item;
import com.inventory.payload.request.InventoryRequest;
import com.inventory.payload.request.ItemRequest;
import com.inventory.payload.response.InventoryResponse;
import com.inventory.payload.response.ItemResponse;
import com.inventory.payload.response.PagedResponse;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.ItemRepository;
import com.inventory.service.impl.InventoryServiceImpl;
import com.inventory.service.impl.ItemServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

	@Mock
	private InventoryRepository inventoryRepo;

	@Mock
	private ItemRepository itemRepo;

	@InjectMocks
	private InventoryServiceImpl service;

	private UUID itemId;
	private Item item;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		itemId = UUID.randomUUID();
		item = new Item();
		item.setId(itemId);
		item.setName("Test Item");
		item.setPrice(java.math.BigDecimal.valueOf(10));
	}

	@Test
	void testTopUpCreatesInventory() {
		InventoryRequest request = new InventoryRequest(itemId, 10, InventoryType.T);

		when(itemRepo.findById(eq(itemId))).thenReturn(Optional.of(item));
		when(inventoryRepo.findByItemId(itemId)).thenReturn(Optional.empty());

		Inventory inventory = new Inventory();
		inventory.setId(UUID.randomUUID());
		inventory.setItem(item);
		inventory.setQuantity(10);

		when(inventoryRepo.save(any())).thenReturn(inventory);

		InventoryResponse response = service.create(request);

		assertNotNull(response);
		assertEquals(10, response.getQuantity());
	}

	@Test
	void testWithdrawFailsOnInsufficientStock() {
		InventoryRequest request = new InventoryRequest(itemId, 5, InventoryType.W);

		when(itemRepo.findById(itemId)).thenReturn(Optional.of(item));
		when(inventoryRepo.findByItemId(itemId)).thenReturn(Optional.of(new Inventory() {{ setItem(item); setQuantity(2); }}));

		assertThrows(InsufficientStockException.class, () -> service.create(request));
	}
}
