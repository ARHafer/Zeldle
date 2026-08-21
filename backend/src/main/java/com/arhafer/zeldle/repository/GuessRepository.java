package com.arhafer.zeldle.repository;

import com.arhafer.zeldle.entity.Guess;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

@NullMarked
public interface GuessRepository extends CrudRepository<Guess, Integer> {

    // I'm aware these method names are kinda long, but what else would I call this? lol
    @Query(value = "SELECT COUNT(*) FROM guesses WHERE player_id = :player_id AND game_date = :game_date")
    int getNumOfGuessesThisGame(@Param("player_id") UUID playerId, @Param("game_date") LocalDate gameDate);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM guesses WHERE player_id = :player_id AND game_date = :game_date AND guessed_item_id = :guessed_item_id)")
    boolean wasItemGuessedThisGame(@Param("player_id") UUID playerId, @Param("game_date") LocalDate gameDate, @Param("guessed_item_id") int guessedItemId);

    @Modifying
    @Query(value = "INSERT INTO guesses (player_id, game_date, guessed_item_id) VALUES (:player_id, :game_date, :guessed_item_id)")
    void insert(@Param("player_id") UUID playerId, @Param("game_date") LocalDate gameDate, @Param("guessed_item_id") int guessedItemId);
}
