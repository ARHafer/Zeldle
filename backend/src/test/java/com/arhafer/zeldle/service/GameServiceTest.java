package com.arhafer.zeldle.service;

import com.arhafer.zeldle.dto.GameResponse;
import com.arhafer.zeldle.entity.Game;
import com.arhafer.zeldle.repository.GameRepository;
import com.arhafer.zeldle.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepo;

    @Mock
    private ItemRepository itemRepo;

    @InjectMocks
    private GameService gameService;
    private final LocalDate date = LocalDate.now(ZoneId.of("America/New_York"));

    // If the game exists, then the date should simply be returned.
    @Test
    void gameExists() {
        Game existingGame = new Game(date, 1);
        when(gameRepo.findById(date)).thenReturn(Optional.of(existingGame));

        GameResponse response = gameService.getOrCreateTodaysGame();

        assertEquals(date, response.gameDate());
        verify(itemRepo, never()).getRandomId();
    }

    // If the game doesn't exist, it should create a new one and return its date.
    @Test
    void gameDoesNotExist() {
        when(gameRepo.findById(date)).thenReturn(Optional.empty());
        when(itemRepo.getRandomId()).thenReturn(1);

        GameResponse response = gameService.getOrCreateTodaysGame();

        verify(itemRepo).getRandomId();
        verify(gameRepo).insert(date, 1);
        assertEquals(date, response.gameDate());
    }

    /*
     * If two requests are made at the same time when the game doesn't yet exist, both should attempt to create the game.
     * One should succeed, the other should throw a DuplicateKey exception and return the existing game.
     */
    @Test
    void gameDoesNotExist_simultaneousRequests() {
        Game existingGame = new Game(date, 1);
        when(gameRepo.findById(date)).thenReturn(Optional.empty()).thenReturn(Optional.of(existingGame));
        when(itemRepo.getRandomId()).thenReturn(1);
        doThrow(new DuplicateKeyException("")).when(gameRepo).insert(date, 1);

        GameResponse response = gameService.getOrCreateTodaysGame();

        assertEquals(date, response.gameDate());
        verify(gameRepo, times(2)).findById(date);
    }
}
