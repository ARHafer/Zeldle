package com.arhafer.zeldle.service;

import com.arhafer.zeldle.constant.GameStatus;
import com.arhafer.zeldle.dto.Feedback;
import com.arhafer.zeldle.dto.GameResponse;
import com.arhafer.zeldle.entity.Game;
import com.arhafer.zeldle.entity.Item;
import com.arhafer.zeldle.exception.GameInitializationException;
import com.arhafer.zeldle.repository.GameRepository;
import com.arhafer.zeldle.repository.GuessRepository;
import com.arhafer.zeldle.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.arhafer.zeldle.ZeldleApplicationTests.createItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepo;

    @Mock
    private ItemRepository itemRepo;

    @Mock
    private GuessRepository guessRepo;

    private GameService gameService;
    private final GuessEvaluator evaluator = new GuessEvaluator();
    private final Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    private final LocalDate date = LocalDate.now(clock);
    private final UUID playerId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        gameService = new GameService(gameRepo, itemRepo, guessRepo, evaluator, clock);
    }

    /*
     * If the game exists and there is no guess history, no item should be selected, six guesses should remain, and
     * the guess history list should be empty.
     */
    @Test
    void gameExists_noHistory() {
        Game existingGame = new Game(date, 1);

        when(gameRepo.findById(date)).thenReturn(Optional.of(existingGame));
        when(guessRepo.getGuessedItemIds(playerId, date)).thenReturn(Collections.emptyList());

        GameResponse response = gameService.initializeGame(playerId);

        assertEquals(date, response.date());
        assertEquals(GameStatus.IN_PROGRESS, response.gameState().gameStatus());
        assertEquals(6, response.gameState().guessesRemaining());
        assertEquals(Collections.emptyList(), response.guessHistory());
        verify(itemRepo, never()).getRandomId();
        verify(itemRepo, never()).getItems(Collections.emptyList());
    }

    /*
     * If the game exists and there is a guess history, no item should be selected, there should be less than six
     * guesses, and the guess history list should not be empty.
     */
    @Test
    void gameExists_inProgress() {
        Game existingGame = new Game(date, 1);
        List<Integer> guessedItemIds = new ArrayList<>(List.of(2));
        List<Item> guessedItems = new ArrayList<>(List.of(createItem(2)));

        when(gameRepo.findById(date)).thenReturn(Optional.of(existingGame));
        when(guessRepo.getGuessedItemIds(playerId, date)).thenReturn(guessedItemIds);
        when(itemRepo.getItems(guessedItemIds)).thenReturn(guessedItems);
        lenient().when(itemRepo.findById(1)).thenReturn(Optional.of(createItem(1)));
        lenient().when(itemRepo.findById(2)).thenReturn(Optional.of(createItem(2)));

        GameResponse response = gameService.initializeGame(playerId);

        assertEquals(date, response.date());
        assertEquals(GameStatus.IN_PROGRESS, response.gameState().gameStatus());
        assertEquals(5, response.gameState().guessesRemaining());
        assertEquals(List.of(evaluator.evaluateGuess(createItem(2), createItem(1))), response.guessHistory());
        verify(itemRepo, never()).getRandomId();
    }

    /*
     * If the game exists and there is a guess history containing the target item, the game status should be "WON."
     * Along with this, there should be less than six guesses and the guess history should not be empty.
     */
    @Test
    void gameExists_previouslyWon() {
        Game existingGame = new Game(date, 1);
        List<Integer> guessedItemIds = new ArrayList<>(List.of(1));
        List<Item> guessedItems = new ArrayList<>(List.of(createItem(1)));

        when(gameRepo.findById(date)).thenReturn(Optional.of(existingGame));
        when(guessRepo.getGuessedItemIds(playerId, date)).thenReturn(guessedItemIds);
        when(itemRepo.getItems(guessedItemIds)).thenReturn(guessedItems);
        when(itemRepo.findById(1)).thenReturn(Optional.of(createItem(1)));

        GameResponse response = gameService.initializeGame(playerId);

        assertEquals(date, response.date());
        assertEquals(GameStatus.WON, response.gameState().gameStatus());
        assertEquals(5, response.gameState().guessesRemaining());
        assertEquals(List.of(evaluator.evaluateGuess(createItem(1), createItem(1))), response.guessHistory());
        verify(itemRepo, never()).getRandomId();
    }

    /*
     * If the game exists and all guesses were exhausted before guessing the correct item, the game status should be
     * "LOST." Along with this, there should be zero guesses and the guess history should contain all six previous
     * guesses.
     */
    @Test
    void gameExists_previouslyLost() {
        Game existingGame = new Game(date, 7);
        List<Integer> guessedItemIds = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        List<Item> guessedItems = new ArrayList<>(List.of(createItem(1), createItem(2), createItem(3),
                createItem(4), createItem(5), createItem(6)));

        when(gameRepo.findById(date)).thenReturn(Optional.of(existingGame));
        when(guessRepo.getGuessedItemIds(playerId, date)).thenReturn(guessedItemIds);
        when(itemRepo.getItems(guessedItemIds)).thenReturn(guessedItems);

        for (int i = 1; i <= 7; i++) {
            lenient().when(itemRepo.findById(i)).thenReturn(Optional.of(createItem(i)));
        }

        GameResponse response = gameService.initializeGame(playerId);

        assertEquals(date, response.date());
        assertEquals(GameStatus.LOST, response.gameState().gameStatus());
        assertEquals(0, response.gameState().guessesRemaining());

        List<Feedback> guessHistory = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            guessHistory.add(evaluator.evaluateGuess(createItem(i), createItem(7)));
        }
        assertEquals(guessHistory, response.guessHistory());
        verify(itemRepo, never()).getRandomId();
    }

    /*
     * If the game doesn't exist, a new item should be selected, a new game should be inserted into the database, there
     * should be six guesses remaining, and the guess history list should be empty.
     */
    @Test
    void gameDoesNotExist() {
        when(gameRepo.findById(date)).thenReturn(Optional.empty());
        when(itemRepo.getRandomId()).thenReturn(1);
        when(guessRepo.getGuessedItemIds(playerId, date)).thenReturn(Collections.emptyList());

        GameResponse response = gameService.initializeGame(playerId);

        assertEquals(date, response.date());
        assertEquals(GameStatus.IN_PROGRESS, response.gameState().gameStatus());
        assertEquals(6, response.gameState().guessesRemaining());
        assertEquals(Collections.emptyList(), response.guessHistory());
        verify(gameRepo).insert(date, 1);
        verify(itemRepo, times(1)).getRandomId();
        verify(itemRepo, never()).getItems(Collections.emptyList());
    }

    /*
     * If two requests are made at the same time when the game doesn't yet exist, both should attempt to create the game.
     * One should succeed, the other should throw a DuplicateKey exception and return an existing game with no history.
     */
    @Test
    void gameDoesNotExist_simultaneousRequests() {
        Game existingGame = new Game(date, 1);
        when(gameRepo.findById(date)).thenReturn(Optional.empty()).thenReturn(Optional.of(existingGame));
        when(itemRepo.getRandomId()).thenReturn(1);
        doThrow(new DuplicateKeyException("")).when(gameRepo).insert(date, 1);
        when(guessRepo.getGuessedItemIds(playerId, date)).thenReturn(Collections.emptyList());

        GameResponse response = gameService.initializeGame(playerId);

        assertEquals(date, response.date());
        assertEquals(GameStatus.IN_PROGRESS, response.gameState().gameStatus());
        assertEquals(6, response.gameState().guessesRemaining());
        assertEquals(Collections.emptyList(), response.guessHistory());
        verify(gameRepo).insert(date, 1);
        verify(itemRepo, times(1)).getRandomId();
        verify(itemRepo, never()).getItems(Collections.emptyList());
    }

    /*
     * If two requests are made at the same time when the game doesn't yet exist, both should attempt to create the game.
     * One should succeed, the ohter should throw a DuplicateKey exception. However, if the game were deleted, a
     * GameInitilization exception should be thrown.
     */
    @Test
    void gameDoesNotExist_simultaneousRequests_gameDeleted() {
        when(gameRepo.findById(date)).thenReturn(Optional.empty());
        when(itemRepo.getRandomId()).thenReturn(1);

        doThrow(new DuplicateKeyException("")).when(gameRepo).insert(date, 1);
        assertThrows(GameInitializationException.class, () -> gameService.initializeGame(playerId));
    }
}
