package com.arhafer.zeldle.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private JdbcTemplate template;

    private static final String INSERT_QUERY = "INSERT INTO games (date, target_item_id) VALUES (?, ?)";

    @BeforeEach
    void setUp() {
        template.update("""
                CREATE TABLE games(
                date DATE PRIMARY KEY,
                target_item_id INTEGER NOT NULL);
                """); // Not 100% accurate to the actual schema, but accurate enough for test to be accurate.
    }

    // When this query is run given a date, all dates before the given date should be deleted from the repo.
    @Test
    void deletePreviousGames_deletesCorrectGames() {
        LocalDate recirculationDate = LocalDate.of(2026, 8, 31);

        template.update(INSERT_QUERY, recirculationDate.plusDays(1), 1);
        template.update(INSERT_QUERY, recirculationDate, 2);
        template.update(INSERT_QUERY, recirculationDate.minusDays(1), 3);

        gameRepo.deletePreviousGames(recirculationDate);
        List<Integer> remainingIds = gameRepo.getTargetItemIds(); // Doubles as a getTargetItemIds test! :D

        assertThat(remainingIds).containsExactly(1, 2);
    }

    /*
     * When the query is run to insert something... it inserts the given thing. I only wrote this test for game repo
     * because it's all the same query, so if it works here, it'll work there.
     */
    @Test
    void insert_inserts() {
        LocalDate date = LocalDate.of(2026, 8, 31);

        gameRepo.insert(date, 1);
        gameRepo.insert(date.plusDays(1), 2);
        List<Integer> ids = gameRepo.getTargetItemIds();

        assertThat(ids).containsExactly(1, 2);
    }
}
