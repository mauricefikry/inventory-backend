package com.inventory.controller;


import com.inventory.payload.request.ItemRequest;
import com.inventory.payload.response.ApiResponse;
import com.inventory.payload.response.ItemResponse;
import com.inventory.payload.response.PagedResponse;
import com.inventory.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ItemResponse>>> getAllItem(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(required = false) String search
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Get All Item successfully",
                        0,
                        itemService.getList(search, pageable)
                )
        );
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<ItemResponse>> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Get Item By Id successfully",
                        0,
                        itemService.getById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponse>> create(
            @Valid @RequestBody ItemRequest request
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Create Item successfully",
                        0,
                        itemService.create(request)
                )
        );
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<ItemResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ItemRequest request
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Update Item successfully",
                        0,
                        itemService.update(id, request)
                )
        );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ApiResponse<ItemResponse>> delete(
            @PathVariable UUID id
    ) {
        itemService.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Delete Item successfully",
                        0,
                        null
                )
        );
    }
}
