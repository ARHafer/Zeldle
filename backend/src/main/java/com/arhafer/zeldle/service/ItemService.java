package com.arhafer.zeldle.service;

import com.arhafer.zeldle.dto.ItemResponse;
import com.arhafer.zeldle.entity.Item;
import com.arhafer.zeldle.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepo;

    public ItemService(ItemRepository itemRepo) {
        this.itemRepo = itemRepo;
    }

    public List<ItemResponse> getItems() {
        return convertToItemResponse(itemRepo.findAll());
    }

    private static List<ItemResponse> convertToItemResponse(Iterable<Item> items) {
        List<ItemResponse> itemResponseList = new ArrayList<>();

        for (Item item : items) {
            itemResponseList.add(new ItemResponse(item.getId(), item.getName()));
        }

        return itemResponseList;
    }
}
