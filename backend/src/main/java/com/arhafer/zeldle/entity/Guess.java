package com.arhafer.zeldle.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "guesses")
public class Guess {

    @Id
    private int id;
    private UUID playerId;
    private LocalDate gameDate;
    private int guessedItemId;
    private LocalDateTime timeOfGuess;

    public int getId() { return id; }
    public UUID getPlayerId() { return playerId; }
    public LocalDate getGameDate() { return gameDate; }
    public int getGuessedItemId() { return guessedItemId; }
    public LocalDateTime getTimeOfGuess() { return timeOfGuess; }
}
