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
    @Query(value = "INSERT INTO games (date, target_item_id) VALUES (:date, :target_item_id)")
    void insert(@Param("date") LocalDate date, @Param("target_item_id") int targetItemId);

    @Modifying
    @Query(value = "DELETE FROM games WHERE date < :date")
    void deletePreviousGames(@Param("date") LocalDate date);
}
