package com.arhafer.zeldle.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(name = "games")
public class Game {

    @Id
    private LocalDate gameDate;
    private int targetItemId;

    public Game(LocalDate gameDate, int targetItemId) {
        this.gameDate = gameDate;
        this.targetItemId = targetItemId;
    }

    public LocalDate getGameDate() { return gameDate; }
    public int getTargetItemId() { return targetItemId; }
}
