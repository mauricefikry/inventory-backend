package com.inventory;

import com.inventory.model.Item;
import com.inventory.payload.request.ItemRequest;
import com.inventory.payload.response.ItemResponse;
import com.inventory.payload.response.PagedResponse;
import com.inventory.repository.ItemRepository;
import com.inventory.service.impl.ItemServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemServiceImplTest {

	private ItemRepository repository;
	private ItemServiceImpl service;

	@BeforeEach
	void setup() {
		repository = mock(ItemRepository.class);
		service = new ItemServiceImpl(repository);
	}

	@Test
	void createItemTest() {
		ItemRequest dto = new ItemRequest( "Test Item", new BigDecimal("100.00"));
		Item savedItem = new Item(UUID.randomUUID(), "Test Item", new BigDecimal("100.00"));

		when(repository.save(ArgumentMatchers.any(Item.class))).thenReturn(savedItem);

		ItemResponse result = service.create(dto);

		assertNotNull(result.getId());
		assertEquals("Test Item", result.getName());
	}

	@Test
	void getItemFoundTest() {
		UUID id = UUID.randomUUID();
		Item item = new Item(id, "Found Item", new BigDecimal("55.00"));

		when(repository.findById(id)).thenReturn(Optional.of(item));

		ItemResponse result = service.getById(id);

		assertEquals(id, result.getId());
		assertEquals("Found Item", result.getName());
	}

	@Test
	void getItemNotFoundTest() {
		UUID id = UUID.randomUUID();

		when(repository.findById(id)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> service.getById(id));
	}

	@Test
	void updateItemSuccesstTest() {
		UUID id = UUID.randomUUID();
		Item existing = new Item(id, "Old", new BigDecimal("10"));
		Item updated = new Item(id, "Updated", new BigDecimal("20"));
		ItemRequest updateDto = new ItemRequest("Updated", new BigDecimal("20"));

		when(repository.findById(id)).thenReturn(Optional.of(existing));
		when(repository.save(ArgumentMatchers.any(Item.class))).thenReturn(updated);

		ItemResponse result = service.update(id, updateDto);

		assertEquals("Updated", result.getName());
		assertEquals(new BigDecimal("20"), result.getPrice());
	}

	@Test
	void deleteItemSuccessTest() {
		UUID id = UUID.randomUUID();

		when(repository.findById(id)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> service.delete(id));
		assertEquals("Delete Item By Id Failed, Item not found", exception.getMessage());

		verify(repository, never()).deleteById(id);
	}

	@Test
	void deleteItemNotFoundTest() {
		UUID id = UUID.randomUUID();

		when(repository.existsById(id)).thenReturn(false);

		assertThrows(EntityNotFoundException.class, () -> service.delete(id));
	}

	@Test
	void listItemsWithSearchTest() {
		String search = "test";
		Pageable pageable = PageRequest.of(0, 2,Sort.by("name"));

		List<Item> items = List.of(
				new Item(UUID.randomUUID(), "test item 1", new BigDecimal("10")),
				new Item(UUID.randomUUID(), "test item 2", new BigDecimal("20"))
		);

		Page<Item> page = new PageImpl<>(items, pageable, 2);

		when(repository.findByNameContainingIgnoreCase(search, pageable)).thenReturn(page);

		PagedResponse<ItemResponse> response = service.getList(search, pageable);

		assertEquals(2, response.getContent().size());
		assertEquals("test item 1", response.getContent().get(0).getName());
	}

}
