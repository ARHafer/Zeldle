package com.arhafer.zeldle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record GuessResponse(@JsonProperty("game_state") GameState gameState,
                            Feedback feedback) {}
