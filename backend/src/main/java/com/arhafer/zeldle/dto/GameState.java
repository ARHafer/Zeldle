package com.arhafer.zeldle.dto;

import com.arhafer.zeldle.constant.GameStatus;

public record GameState(int guessesRemaining, GameStatus gameStatus) {}
