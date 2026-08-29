package com.arhafer.zeldle.service;

import com.arhafer.zeldle.constant.GameStatus;
import com.arhafer.zeldle.dto.*;
import com.arhafer.zeldle.entity.Item;
import com.arhafer.zeldle.exception.GameOverException;
import com.arhafer.zeldle.exception.DuplicateGuessException;
import com.arhafer.zeldle.exception.NoSuchItemException;
import com.arhafer.zeldle.repository.GuessRepository;
import com.arhafer.zeldle.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.arhafer.zeldle.service.GameService.MAX_GUESSES;

@Service
public class GuessService {

    private final ItemRepository itemRepo;
    private final GuessRepository guessRepo;
    private final GameService gameService;
    private final GuessEvaluator evaluator;
    private final Clock clock;

    public GuessService(ItemRepository itemRepo, GuessRepository guessRepo, GameService gameService, GuessEvaluator evaluator, Clock clock) {
        this.itemRepo = itemRepo;
        this.guessRepo = guessRepo;
        this.gameService = gameService;
        this.evaluator = evaluator;
        this.clock = clock;
    }

    public GuessResponse submitGuess(GuessRequest guess, UUID playerId) {
        LocalDate today = LocalDate.now(clock);
        List<Integer> guessedIds = guessRepo.getGuessedItemIds(playerId, today);
        int targetItemId = gameService.getTodaysTargetItemId();

        Item guessedItem = itemRepo.findById(guess.itemId()).orElseThrow(() -> new NoSuchItemException(guess.itemId()));

        validateGuess(guess, guessedIds, targetItemId);
        guessRepo.insert(playerId, today, guess.itemId());
        guessedIds.add(guess.itemId());

        Item targetItem = itemRepo.findById(targetItemId).orElseThrow(() -> new NoSuchItemException(targetItemId));

        Feedback feedback = evaluator.evaluateGuess(guessedItem, targetItem);
        GameState gameState = gameService.getGameState(guessedIds, targetItemId);

        return new GuessResponse(gameState, feedback);
    }

    private void validateGuess(GuessRequest guess, List<Integer> guessedIds, int targetItemId) {
        int numOfGuesses = guessedIds.size();
        boolean itemPreviouslyGuessed = guessedIds.contains(guess.itemId());
        boolean gameWon = guessedIds.contains(targetItemId);
        boolean gameLost = numOfGuesses >= MAX_GUESSES;

        if (itemPreviouslyGuessed) {
            throw new DuplicateGuessException(guess.itemId());
        } if (gameWon) {
            throw new GameOverException(GameStatus.WON);
        } if (gameLost) {
            throw new GameOverException(GameStatus.LOST);
        }
    }
}
