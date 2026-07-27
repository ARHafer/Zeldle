package com.arhafer.zeldle.repository;

import com.arhafer.zeldle.entity.Game;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

@NullMarked
public interface GameRepository extends CrudRepository<Game, LocalDate> {

    @Modifying
    @Query(value = "INSERT INTO games (game_date, target_item_id) VALUES (:game_date, :target_item_id)")
    void insert(@Param("game_date") LocalDate gameDate, @Param("target_item_id") int targetItemId);
}
