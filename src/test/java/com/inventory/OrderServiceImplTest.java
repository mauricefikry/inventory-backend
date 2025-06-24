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
class OrderServiceImplTest {

}
