package com.arhafer.zeldle.exception;

import com.arhafer.zeldle.constant.GameStatus;

public class GameOverException extends RuntimeException {

    public GameOverException(GameStatus gameStatus) {
        super("The current game was " + gameStatus + " and is over; Guess not accepted.");
    }
}
