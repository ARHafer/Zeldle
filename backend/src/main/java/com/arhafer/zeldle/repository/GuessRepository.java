package com.arhafer.zeldle.repository;

import com.arhafer.zeldle.entity.Guess;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NullMarked
public interface GuessRepository extends CrudRepository<Guess, Integer> {

    @Modifying
    @Query(value = "INSERT INTO guesses (player_id, game_date, guessed_item_id) VALUES (:player_id, :game_date, :guessed_item_id)")
    void insert(@Param("player_id") UUID playerId, @Param("game_date") LocalDate gameDate, @Param("guessed_item_id") int guessedItemId);

    @Query(value = "SELECT guessed_item_id FROM guesses WHERE player_id = :player_id AND game_date = :game_date ORDER BY time_of_guess ASC")
    List<Integer> getGuessedItemIds(@Param("player_id") UUID playerId, @Param("game_date") LocalDate gameDate);
}
