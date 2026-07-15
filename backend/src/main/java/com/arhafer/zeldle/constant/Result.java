package com.arhafer.zeldle.constant;

public enum Result {
    CORRECT,
    INCORRECT,

    // Used only for the 'gameReleaseDate' feedback.
    HIGHER, // Guessed game was released after target game.
    LOWER, // Vice versa.
    EQUAL
}
