package com.arhafer.zeldle.dto;

import java.time.LocalDate;
import java.util.List;

public record GameResponse(LocalDate date,
                           GameState gameState,
                           List<Integer> guessedIds,
                           List<Feedback> guessHistory) {}
