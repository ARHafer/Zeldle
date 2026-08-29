package com.arhafer.zeldle.exception;

public class GameInitializationException extends RuntimeException {

    public GameInitializationException() {
        super("Attempted to create an already existing game, & then failed to retrieve said existing game. Please refresh the page.\n" +
                "This is exceptionally rare, some would say: \"It's a secret to everybody.\"");
    }
}
