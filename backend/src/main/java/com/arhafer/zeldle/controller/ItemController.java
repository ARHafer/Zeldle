package com.arhafer.zeldle.controller;

import com.arhafer.zeldle.dto.ItemResponse;
import com.arhafer.zeldle.service.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public List<ItemResponse> getItems() {
        return itemService.getItems();
    }
}
