package com.arhafer.zeldle.dto;

import com.arhafer.zeldle.constant.Result;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Results(Result name,
                      Result game,
                      @JsonProperty("game_release_date") Result gameReleaseDate,
                      Result purpose,
                      Result consumption,
                      Result acquisition,
                      Result range,
                      @JsonProperty("enemy_interaction") Result enemyInteraction,
                      @JsonProperty("control_mode") Result controlMode) {}
