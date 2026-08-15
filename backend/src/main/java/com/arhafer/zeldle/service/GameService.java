package com.arhafer.zeldle.service;

import com.arhafer.zeldle.dto.GameResponse;
import com.arhafer.zeldle.entity.Game;
import com.arhafer.zeldle.repository.GameRepository;
import com.arhafer.zeldle.repository.ItemRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class GameService {

    private final GameRepository gameRepo;
    private final ItemRepository itemRepo;

    public GameService(GameRepository gameRepo,  ItemRepository itemRepo) {
        this.gameRepo = gameRepo;
        this.itemRepo = itemRepo;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/New_York")
    public void scheduledGameCreation() {
        getOrCreateTodaysGame();
    }

    // Game creation can also happen on page load in case the server was down at midnight or something.
    public GameResponse getOrCreateTodaysGame() {
        return new GameResponse(getTodaysGame().getDate());
    }

    // Used only in GuessService, hence the package-private.
    int getTodaysTargetItemId() {
        return getTodaysGame().getTargetItemId();
    }

    private Game getTodaysGame() {
        LocalDate today = getToday();
        return getOrCreateGame(today);
    }

    private Game getOrCreateGame(LocalDate today) {
        return gameRepo.findById(today).orElseGet(() -> createNewGame(today));
    }

    private Game createNewGame(LocalDate date) {
        int targetItemId = itemRepo.getRandomId(); // Just random for now, may change later to be a little more complex.

        try {
            gameRepo.insert(date, targetItemId);
            return new Game(date, targetItemId);
        } catch (DuplicateKeyException e) { // In case two clients open the page at the same time when the game hasn't yet been created.
            return gameRepo.findById(date).orElseThrow(IllegalStateException::new);
        }
    }

    private static LocalDate getToday() {
        return LocalDate.now(ZoneId.of("America/New_York"));
    }
}
