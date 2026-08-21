package com.arhafer.zeldle.controller;

import com.arhafer.zeldle.dto.GuessResponse;
import com.arhafer.zeldle.dto.GuessRequest;
import com.arhafer.zeldle.service.GuessService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class GuessController {

    private final GuessService guessService;

    public GuessController(GuessService guessService) {
        this.guessService = guessService;
    }

    @PostMapping("/guess")
    public GuessResponse submitGuess(@RequestBody GuessRequest guess, @RequestAttribute("zeldle_player_id") UUID playerId) throws Exception {
        return guessService.submitGuess(guess, playerId);
    }
}
