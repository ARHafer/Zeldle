package com.arhafer.zeldle.controller;

import com.arhafer.zeldle.dto.GameResponse;
import com.arhafer.zeldle.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game")
    public GameResponse getTodaysGame(@RequestAttribute("zeldle_player_id") UUID playerId) {
        return gameService.initializeGame(playerId);
    }
}
