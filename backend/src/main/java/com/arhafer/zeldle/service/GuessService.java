package com.arhafer.zeldle.service;

import com.arhafer.zeldle.constant.GameStatus;
import com.arhafer.zeldle.dto.*;
import com.arhafer.zeldle.entity.Item;
import com.arhafer.zeldle.constant.Result;
import com.arhafer.zeldle.exception.GameOverException;
import com.arhafer.zeldle.exception.DuplicateGuessException;
import com.arhafer.zeldle.exception.NoSuchItemException;
import com.arhafer.zeldle.repository.GuessRepository;
import com.arhafer.zeldle.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class GuessService {

    private static final int MAX_GUESSES = 6;

    private final ItemRepository itemRepo;
    private final GuessRepository guessRepo;
    private final GameService gameService;

    public GuessService(ItemRepository itemRepo, GuessRepository guessRepo, GameService gameService) {
        this.itemRepo = itemRepo;
        this.guessRepo = guessRepo;
        this.gameService = gameService;
    }

    public GuessResponse submitGuess(GuessRequest guess, UUID playerId) {
        LocalDate today = LocalDate.now(ZoneId.of("America/New_York")); // TODO: Find a new, universal home for this.
        int numOfGuesses = guessRepo.getNumOfGuessesThisGame(playerId, today);
        int targetItemId = gameService.getTodaysTargetItemId();

        Item guessedItem = itemRepo.findById(guess.itemId()).orElseThrow(() -> new NoSuchItemException(guess.itemId()));

        validateAndStoreGuess(guess, playerId, today, numOfGuesses, targetItemId); // Returns nothing and stores the guess if valid, throws exception if not.
        numOfGuesses++;

        Item targetItem = itemRepo.findById(targetItemId).orElseThrow(() -> new NoSuchItemException(targetItemId));

        Feedback feedback = evaluateGuess(guessedItem, targetItem);
        GameState gameState = getGameState(guess, numOfGuesses, targetItemId);

        return new GuessResponse(gameState, feedback);
    }

    private Feedback evaluateGuess(Item guessedItem, Item targetItem) {
        Results results = getResults(guessedItem, targetItem);
        ItemProperties properties = getItemProperties(guessedItem);

        return new Feedback(results, properties);
    }

    private static Results getResults(Item guessed, Item target) {
        return new Results(
                compareProperty(guessed.getName(), target.getName()),
                compareProperty(guessed.getGame(), target.getGame()),
                compareReleaseOrder(guessed.getGame(), target.getGame()),
                compareProperty(guessed.getPurpose(), target.getPurpose()),
                compareProperty(guessed.getConsumption(), target.getConsumption()),
                compareProperty(guessed.getAcquisition(), target.getAcquisition()),
                compareProperty(guessed.getRange(), target.getRange()),
                compareProperty(guessed.getEnemyInteraction(), target.getEnemyInteraction()),
                compareProperty(guessed.getControlMode(), target.getControlMode()));
    }

    private static Result compareProperty(Object guessed, Object target) {
        if (guessed.equals(target)) {
            return Result.CORRECT;
        } else {
            return Result.INCORRECT;
        }
    }

    private static Result compareReleaseOrder(Item.Game guessed, Item.Game target) {
        if (guessed == target) {
            return Result.TARGET_EQUAL;
        } else if (guessed.getReleaseOrder() < target.getReleaseOrder()) {
            return Result.TARGET_HIGHER;
        } else {
            return Result.TARGET_LOWER;
        }
    }

    private static ItemProperties getItemProperties(Item item) {
        return new ItemProperties(
                item.getName(),
                item.getGame().getFullName(),
                item.getPurpose(),
                item.getConsumption(),
                item.getAcquisition(),
                item.getRange(),
                item.getEnemyInteraction(),
                item.getControlMode());
    }

    private void validateAndStoreGuess(GuessRequest guess, UUID playerId, LocalDate today, int numOfGuesses, int targetItemId) {
        boolean itemPreviouslyGuessed = guessRepo.wasItemGuessedThisGame(playerId, today, guess.itemId());
        boolean gameWon = guessRepo.wasItemGuessedThisGame(playerId, today, targetItemId);
        boolean gameLost = numOfGuesses >= MAX_GUESSES;

        if (itemPreviouslyGuessed) {
            throw new DuplicateGuessException(guess.itemId());
        } if (gameWon) {
            throw new GameOverException(GameStatus.WON);
        } if (gameLost) {
            throw new GameOverException(GameStatus.LOST);
        }

        guessRepo.insert(playerId, today, guess.itemId());
    }

    private GameState getGameState(GuessRequest guess, int numOfGuesses, int targetItemId) {
        int guessesRemaining = MAX_GUESSES - numOfGuesses;

        GameStatus gameStatus;
        if (guess.itemId() == targetItemId) {
            gameStatus = GameStatus.WON;
        } else if (numOfGuesses == MAX_GUESSES) {
            gameStatus = GameStatus.LOST;
        } else {
            gameStatus = GameStatus.IN_PROGRESS;
        }

        return new GameState(guessesRemaining, gameStatus);
    }
}
