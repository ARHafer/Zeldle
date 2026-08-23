package com.arhafer.zeldle.service;

import com.arhafer.zeldle.constant.GameStatus;
import com.arhafer.zeldle.dto.GuessRequest;
import com.arhafer.zeldle.dto.GuessResponse;
import com.arhafer.zeldle.entity.Item;
import com.arhafer.zeldle.exception.GameOverException;
import com.arhafer.zeldle.exception.DuplicateGuessException;
import com.arhafer.zeldle.repository.GuessRepository;
import com.arhafer.zeldle.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

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

    @InjectMocks
    private GuessService guessService;

    private final UUID playerId = UUID.randomUUID();

    private static Item createItem(int id) {
        Item item = new Item();
        item.setId(id);
        item.setName("Test Item of Testing (For Testing)");
        item.setGame(Item.Game.OOT);
        item.setPurpose(Item.Purpose.COMBAT);
        item.setConsumption(Item.Consumption.MATERIAL);
        item.setAcquisition(Item.Acquisition.OVERWORLD);
        item.setRange(Item.Range.SELF);
        item.setEnemyInteraction(Item.EnemyInteraction.DAMAGE);
        item.setControlMode(Item.ControlMode.IMMEDIATE);
        return item;
    }

    // If a guess is valid, it should be inserted into the database, and the game status should equal IN_PROGRESS.
    @Test
    void validGuess_GameInProgress() {
        GuessRequest guess = new GuessRequest(1);
        when(guessRepo.getNumOfGuessesThisGame(any(), any())).thenReturn(0);
        when(gameService.getTodaysTargetItemId()).thenReturn(2);

        Item guessedItem = createItem(1);
        Item targetItem = createItem(2);
        when(itemRepo.findById(1)).thenReturn(Optional.of(guessedItem));
        when(itemRepo.findById(2)).thenReturn(Optional.of(targetItem));

        GuessResponse response = guessService.submitGuess(guess, playerId);

        verify(guessRepo).insert(any(), any(), anyInt());
        assertEquals(GameStatus.IN_PROGRESS, response.gameState().gameStatus());
    }

    // If the guess is correct, the game status should equal WON.
    @Test
    void validGuess_GameWon() {
        GuessRequest guess = new GuessRequest(1);
        when(guessRepo.getNumOfGuessesThisGame(any(), any())).thenReturn(0);
        when(gameService.getTodaysTargetItemId()).thenReturn(1);

        Item item = createItem(1);
        when(itemRepo.findById(any())).thenReturn(Optional.of(item));

        GuessResponse response = guessService.submitGuess(guess, playerId);

        verify(guessRepo).insert(any(), any(), anyInt());
        assertEquals(GameStatus.WON, response.gameState().gameStatus());
    }

    // If the final guess was incorrect, the game status should equal LOST.
    @Test
    void validGuess_GameLost() {
        GuessRequest guess = new GuessRequest(1);
        when(guessRepo.getNumOfGuessesThisGame(any(), any())).thenReturn(5);
        when(gameService.getTodaysTargetItemId()).thenReturn(2);

        Item guessedItem = createItem(1);
        Item targetItem = createItem(2);

        when(itemRepo.findById(1)).thenReturn(Optional.of(guessedItem));
        when(itemRepo.findById(2)).thenReturn(Optional.of(targetItem));

        GuessResponse response = guessService.submitGuess(guess, playerId);

        verify(guessRepo).insert(any(), any(), anyInt());
        assertEquals(GameStatus.LOST, response.gameState().gameStatus());
    }

    // If the guessed item has been previously guessed, an error should be thrown, and the guess should NOT be inserted into the database.
    @Test
    void invalidGuess_DuplicateGuess() {
        GuessRequest guess = new GuessRequest(1);
        when(itemRepo.findById(1)).thenReturn(Optional.of(createItem(1)));
        when(guessRepo.getNumOfGuessesThisGame(any(), any())).thenReturn(1);
        when(guessRepo.wasItemGuessedThisGame(any(), any(), eq(1))).thenReturn(true);
        when(gameService.getTodaysTargetItemId()).thenReturn(2);

        verify(guessRepo, never()).insert(any(), any(), anyInt());
        assertThrows(DuplicateGuessException.class, () -> guessService.submitGuess(guess, playerId));
    }

    // If the correct item was previously guessed, an error should be thrown.
    @Test
    void invalidGuess_GameWon() {
        GuessRequest guess = new GuessRequest(1);
        lenient().when(itemRepo.findById(1)).thenReturn(Optional.of(createItem(1)));
        // I hate Mockito. Strict stubbing? Really? Because calling a function multiple times is unheard of, apparently.
        when(guessRepo.getNumOfGuessesThisGame(any(), any())).thenReturn(1);
        when(gameService.getTodaysTargetItemId()).thenReturn(2);
        lenient().when(guessRepo.wasItemGuessedThisGame(any(), any(), eq(2))).thenReturn(true);

        verify(guessRepo, never()).insert(any(), any(), anyInt());
        assertThrows(GameOverException.class, () -> guessService.submitGuess(guess, playerId));
    }

    // If all 6 guesses were previously used, an error should be thrown.
    @Test
    void invalidGuess_GameLost() {
        GuessRequest guess = new GuessRequest(1);
        when(itemRepo.findById(1)).thenReturn(Optional.of(createItem(1)));
        when(guessRepo.getNumOfGuessesThisGame(any(), any())).thenReturn(6);
        when(gameService.getTodaysTargetItemId()).thenReturn(2);

        verify(guessRepo, never()).insert(any(), any(), anyInt());
        assertThrows(GameOverException.class, () -> guessService.submitGuess(guess, playerId));
    }
}
