package com.arhafer.zeldle.exception;

public class DuplicateGuessException extends RuntimeException {

    public DuplicateGuessException(int itemId) {
        super("Item ID [" +  itemId + "] was previously guessed this game; Guess not accepted.");
    }
}
