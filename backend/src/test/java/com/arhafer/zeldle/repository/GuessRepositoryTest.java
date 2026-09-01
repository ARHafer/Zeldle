package com.arhafer.zeldle.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
public class GuessRepositoryTest {

    @Autowired
    private GuessRepository guessRepo;

    @Autowired
    private JdbcTemplate template;

    private static final String INSERT_QUERY = "INSERT INTO guesses (player_id, game_date, guessed_item_id, time_of_guess) VALUES (?, ?, ?, ?)";
    private static final UUID FIXED_UUID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        template.update("""
                CREATE TABLE guesses(
                id SERIAL PRIMARY KEY,
                player_id UUID NOT NULL,
                game_date DATE NOT NULL,
                guessed_item_id INTEGER NOT NULL,
                time_of_guess TIMESTAMP NOT NULL);
                """);
    }

    /*
      * When this query is run given a player ID and date, the guessed item IDs will be returns in ascending order
      * based on time_of_guess.
     */
    @Test
    void getGuessedItemIds_returnsInOrder() {
        LocalDate gameDate = LocalDate.of(2026, 8, 31);
        LocalTime defaultTime = LocalTime.of(12, 0, 0);

        template.update(INSERT_QUERY, FIXED_UUID, gameDate, 1, LocalDateTime.of(gameDate, defaultTime));
        template.update(INSERT_QUERY, FIXED_UUID, gameDate.minusDays(1), 2, LocalDateTime.of(gameDate.minusDays(1), defaultTime));
        template.update(INSERT_QUERY, FIXED_UUID, gameDate, 3, LocalDateTime.of(gameDate, defaultTime.minusSeconds(1)));
        template.update(INSERT_QUERY, FIXED_UUID, gameDate, 4, LocalDateTime.of(gameDate, defaultTime.plusSeconds(1)));

        List<Integer> guessedItemIds = guessRepo.getGuessedItemIds(FIXED_UUID, gameDate);

        assertThat(guessedItemIds).containsExactly(3, 1, 4);
    }
}
