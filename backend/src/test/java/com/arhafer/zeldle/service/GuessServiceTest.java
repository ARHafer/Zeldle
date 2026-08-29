package com.arhafer.zeldle.service;

import com.arhafer.zeldle.dto.GuessRequest;
import com.arhafer.zeldle.exception.GameOverException;
import com.arhafer.zeldle.exception.DuplicateGuessException;
import com.arhafer.zeldle.repository.GuessRepository;
import com.arhafer.zeldle.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.arhafer.zeldle.ZeldleApplicationTests.createItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GuessServiceTest {

    @Mock
    private ItemRepository itemRepo;

    @Mock
    private GuessRepository guessRepo;

    @Mock
    private GameService gameService;

    private GuessService guessService;
    private final UUID playerId = UUID.randomUUID();
    private final GuessEvaluator evaluator = new GuessEvaluator();
    private final Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    private final LocalDate date = LocalDate.now(clock);

    @BeforeEach
    public void setUp() {
        guessService = new GuessService(itemRepo, guessRepo, gameService, evaluator, clock);
    }

    // If a guess is valid it should be inserted into the database.
    @Test
    void validGuess() {
        GuessRequest guess = new GuessRequest(1);
        List<Integer> guessedIds = new ArrayList<>();

        when(guessRepo.getGuessedItemIds(playerId, date)).thenReturn(guessedIds);
        when(gameService.getTodaysTargetItemId()).thenReturn(2);
        when(itemRepo.findById(1)).thenReturn(Optional.of(createItem(1)));
        when(itemRepo.findById(2)).thenReturn(Optional.of(createItem(2)));

        guessService.submitGuess(guess, playerId);

        verify(guessRepo).insert(playerId, date, guess.itemId());
    }

    /*
     * If the guessed item has been previously guessed, an error should be thrown and the guess should not be inserted
     * into the database.
     */
    @Test
    void invalidGuess_DuplicateGuess() {
        GuessRequest guess = new GuessRequest(1);
        List<Integer> guessedIds = new ArrayList<>(List.of(1));

        when(itemRepo.findById(1)).thenReturn(Optional.of(createItem(1)));
        when(guessRepo.getGuessedItemIds(playerId, date)).thenReturn(guessedIds);
        when(gameService.getTodaysTargetItemId()).thenReturn(2);

        assertThrows(DuplicateGuessException.class, () -> guessService.submitGuess(guess, playerId));
        verify(guessRepo, never()).insert(playerId, date, guess.itemId());
    }

    /*
     * If the correct item was previously guessed, an error should be thrown and the guess should not be inserted into
     * the database.
     */

    @Test
    void invalidGuess_GameWon() {
        GuessRequest guess = new GuessRequest(1);
        List<Integer> guessedIds = new ArrayList<>(List.of(2));

        when(itemRepo.findById(1)).thenReturn(Optional.of(createItem(1)));
        when(guessRepo.getGuessedItemIds(playerId, date)).thenReturn(guessedIds);
        when(gameService.getTodaysTargetItemId()).thenReturn(2);

        assertThrows(GameOverException.class, () -> guessService.submitGuess(guess, playerId));
        verify(guessRepo, never()).insert(playerId, date, guess.itemId());
    }

    /*
     * If all 6 guesses were previously used, an error should be thrown and the guess should not be inserted into the
     * database.
     */

    @Test
    void invalidGuess_GameLost() {
        GuessRequest guess = new GuessRequest(7);
        List<Integer> guessedIds = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));

        when(itemRepo.findById(7)).thenReturn(Optional.of(createItem(7)));
        when(guessRepo.getGuessedItemIds(playerId, date)).thenReturn(guessedIds);
        when(gameService.getTodaysTargetItemId()).thenReturn(8);

        assertThrows(GameOverException.class, () -> guessService.submitGuess(guess, playerId));
        verify(guessRepo, never()).insert(playerId, date, guess.itemId());
    }
}
