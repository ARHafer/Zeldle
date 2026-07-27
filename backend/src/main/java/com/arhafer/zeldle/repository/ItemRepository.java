package com.arhafer.zeldle.repository;

import com.arhafer.zeldle.entity.Item;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

@NullMarked
public interface ItemRepository extends CrudRepository<Item, Integer> {

    @Query("SELECT id FROM items ORDER BY RANDOM() LIMIT 1")
    int getRandomId();
}
