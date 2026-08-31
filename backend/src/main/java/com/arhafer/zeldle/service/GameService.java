package com.arhafer.zeldle.service;

import com.arhafer.zeldle.constant.GameStatus;
import com.arhafer.zeldle.dto.Feedback;
import com.arhafer.zeldle.dto.GameResponse;
import com.arhafer.zeldle.dto.GameState;
import com.arhafer.zeldle.entity.Game;
import com.arhafer.zeldle.entity.Item;
import com.arhafer.zeldle.exception.GameInitializationException;
import com.arhafer.zeldle.exception.NoSuchItemException;
import com.arhafer.zeldle.repository.GameRepository;
import com.arhafer.zeldle.repository.GuessRepository;
import com.arhafer.zeldle.repository.ItemRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    @Scheduled(cron = "0 0 0 * * *", zone = "America/New_York")
    private void scheduledGameCreation() {
        getOrCreateGame();
    }

    static final int MAX_GUESSES = 6;

    private final GameRepository gameRepo;
    private final ItemRepository itemRepo;
    private final GuessRepository guessRepo;
    private final GuessEvaluator evaluator;
    private final Clock clock;

    public GameService(GameRepository gameRepo, ItemRepository itemRepo, GuessRepository guessRepo, GuessEvaluator evaluator, Clock clock) {
        this.gameRepo = gameRepo;
        this.itemRepo = itemRepo;
        this.guessRepo = guessRepo;
        this.evaluator = evaluator;
        this.clock = clock;
    }

    // Game creation can also happen on page load in case the server was down at midnight or something. //
    public GameResponse initializeGame(UUID playerId) {
        Game game = getOrCreateGame();
        LocalDate date = game.getDate();
        List<Integer> guessedIds = guessRepo.getGuessedItemIds(playerId, date);
        GameState gameState = getGameState(guessedIds, game.getTargetItemId());
        List<Feedback> guessHistory = getGuessHistory(guessedIds, game.getTargetItemId());

        return new GameResponse(date, gameState, guessedIds, guessHistory);
    }

    private Game getOrCreateGame() {
        LocalDate today = LocalDate.now(clock);
        return gameRepo.findById(today).orElseGet(() -> createNewGame(today));
    }

    private Game createNewGame(LocalDate today) {
        int targetItemId = selectTargetItemId(today);

        try {
            gameRepo.insert(today, targetItemId);
            return new Game(today, targetItemId);
        } catch (DuplicateKeyException e) {
            return gameRepo.findById(today).orElseThrow(GameInitializationException::new);
        }
    }

    private int selectTargetItemId(LocalDate today) {
        LocalDate recirculationDate = today.minusWeeks(2);
        gameRepo.deletePreviousGames(recirculationDate);
        List<Integer> excludedIds = gameRepo.getTargetItemIds();

        if (excludedIds.isEmpty()) {
            return itemRepo.getRandomId();
        } else {
            return itemRepo.getRandomIdGivenExclusions(excludedIds);
        }
    }

    private List<Feedback> getGuessHistory(List<Integer> guessedIds, int targetItemId) {
        List<Feedback> guessHistory = new ArrayList<>();

        if (guessedIds.isEmpty()) {
            return guessHistory;
        }

        List<Item> guessedItems = itemRepo.getItems(guessedIds);
        Item targetItem = itemRepo.findById(targetItemId).orElseThrow(() -> new NoSuchItemException(targetItemId));

        for (Item guessedItem : guessedItems) {
            guessHistory.add(evaluator.evaluateGuess(guessedItem, targetItem));
        }

        return guessHistory;
    }

    // #PackagePrivateGang #SharingIsCaring (Used in GuessService) //
    GameState getGameState(List<Integer> guessedIds, int targetItemId) {
        int numOfGuesses = guessedIds.size();
        int guessesRemaining = MAX_GUESSES - numOfGuesses;

        GameStatus gameStatus;
        if (guessedIds.contains(targetItemId)) {
            gameStatus = GameStatus.WON;
        } else if (numOfGuesses == MAX_GUESSES) {
            gameStatus = GameStatus.LOST;
        } else {
            gameStatus = GameStatus.IN_PROGRESS;
        }

        return new GameState(guessesRemaining, gameStatus);
    }

    int getTodaysTargetItemId() {
        return getOrCreateGame().getTargetItemId();
    }
}
