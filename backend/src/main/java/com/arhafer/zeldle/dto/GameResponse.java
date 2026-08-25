package com.arhafer.zeldle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record GameResponse(LocalDate date,
                           @JsonProperty("game_state") GameState gameState,
                           @JsonProperty("guessed_ids") List<Integer> guessedIds,
                           @JsonProperty("guess_history") List<Feedback> guessHistory) {}
