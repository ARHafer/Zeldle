package com.arhafer.zeldle.dto;

import com.arhafer.zeldle.constant.GameStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GameState(@JsonProperty("guesses_remaining") int guessesRemaining,
                        @JsonProperty("game_status") GameStatus gameStatus) {}
