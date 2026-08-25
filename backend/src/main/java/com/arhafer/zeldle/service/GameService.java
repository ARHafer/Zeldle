package com.arhafer.zeldle.service;

import com.arhafer.zeldle.constant.GameStatus;
import com.arhafer.zeldle.dto.Feedback;
import com.arhafer.zeldle.dto.GameResponse;
import com.arhafer.zeldle.dto.GameState;
import com.arhafer.zeldle.entity.Game;
import com.arhafer.zeldle.entity.Guess;
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

    public GameService(GameRepository gameRepo,  ItemRepository itemRepo, GuessRepository guessRepo, GuessEvaluator evaluator, Clock clock) {
        this.gameRepo = gameRepo;
        this.itemRepo = itemRepo;
        this.guessRepo = guessRepo;
        this.evaluator = evaluator;
        this.clock = clock;
    }

    // Game creation can also happen on page load in case the server was down at midnight or something.
    public GameResponse initializeGame(UUID playerId) {
        Game game = getOrCreateGame();
        LocalDate date = game.getDate();

        List<Guess> guesses = guessRepo.getPlayerGuessesThisGame(playerId, date);
        List<Integer> guessedIds = getGuessedIds(guesses);

        GameState gameState = getGameState(guessedIds, game.getTargetItemId());
        List<Feedback> guessHistory = getGuessHistory(guesses, game.getTargetItemId());

        return new GameResponse(date, gameState, guessedIds, guessHistory);
    }

    private Game getOrCreateGame() {
        LocalDate today = LocalDate.now(clock);
        return gameRepo.findById(today).orElseGet(() -> createNewGame(today));
    }

    private Game createNewGame(LocalDate date) {
        int targetItemId = itemRepo.getRandomId(); // TODO: Implement algorithm for item selection.

        try {
            gameRepo.insert(date, targetItemId);
            return new Game(date, targetItemId);
        } catch (DuplicateKeyException e) {
            return gameRepo.findById(date).orElseThrow(GameInitializationException::new);
        }
    }

    private static List<Integer> getGuessedIds(List<Guess> guesses) {
        List<Integer> guessedIds = new ArrayList<>();

        for (Guess guess : guesses) {
            guessedIds.add(guess.getId());
        }

        return guessedIds;
    }

    private List<Feedback> getGuessHistory(List<Guess> guesses, int targetItemId) {
        List<Feedback> guessHistory = new ArrayList<>();
        Item targetItem = itemRepo.findById(targetItemId).orElseThrow(() -> new NoSuchItemException(targetItemId));

        for (Guess guess : guesses) {
            Item guessedItem = itemRepo.findById(guess.getGuessedItemId()).orElseThrow(() -> new NoSuchItemException(guess.getGuessedItemId()));
            /*
             * TODO: Find a better way to do this, this is atrocious.
             *  Maybe just get a list of guessed item IDs from the guesses table,
             *  and use a single database call to look up the Items for every ID.
             */

            guessHistory.add(evaluator.evaluateGuess(guessedItem, targetItem));
        }

        return guessHistory;
    }

    // #PackagePrivateGang #SharingIsCaring
    int getTodaysTargetItemId() {
        return getOrCreateGame().getTargetItemId();
    }

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
}
