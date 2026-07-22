package com.arhafer.zeldle.controller;

import com.arhafer.zeldle.dto.FeedbackResponse;
import com.arhafer.zeldle.dto.GuessRequest;
import com.arhafer.zeldle.dto.ItemPropertyResponse;
import com.arhafer.zeldle.service.GuessService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/guess")
public class GuessController {

    private final GuessService guessService;

    public GuessController(GuessService guessService) {
        this.guessService = guessService;
    }

    @PostMapping
    public FeedbackResponse submitGuess(@RequestBody GuessRequest guess) {
        return guessService.submitGuess(guess);
    }
    
    @GetMapping("/item/{itemId}")
    public ItemPropertyResponse getItemProperties(@PathVariable int itemId) {
        return guessService.getItemProperties(itemId);
    }
}
