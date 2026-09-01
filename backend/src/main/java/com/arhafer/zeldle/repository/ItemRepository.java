package com.arhafer.zeldle.repository;

import com.arhafer.zeldle.entity.Item;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NullMarked
public interface ItemRepository extends CrudRepository<Item, Integer> {

    @Query("SELECT id FROM items ORDER BY RANDOM() LIMIT 1")
    int getRandomId();

    @Query("SELECT id FROM items WHERE id NOT IN (:excluded_ids) ORDER BY RANDOM() LIMIT 1")
    int getRandomIdGivenExclusions(@Param("excluded_ids") List<Integer> excludedIds);

    @Query(value = "SELECT * FROM items WHERE id IN (:ids)")
    List<Item> getItems(@Param("ids") List<Integer> ids);
}
