package com.arhafer.zeldle.controller;

import com.arhafer.zeldle.dto.FeedbackResponse;
import com.arhafer.zeldle.dto.GuessRequest;
import com.arhafer.zeldle.service.GuessService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GuessController {

    private final GuessService guessService;

    public GuessController(GuessService guessService) {
        this.guessService = guessService;
    }

    @PostMapping("/guess")
    public FeedbackResponse submitGuess(@RequestBody GuessRequest guess) {
        return guessService.submitGuess(guess);
    }
}
