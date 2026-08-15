package com.arhafer.zeldle.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(name = "games")
public class Game {

    @Id
    private final LocalDate date;
    private final int targetItemId;

    public Game(LocalDate date, int targetItemId) {
        this.date = date;
        this.targetItemId = targetItemId;
    }

    public LocalDate getDate() { return date; }
    public int getTargetItemId() { return targetItemId; }
}
