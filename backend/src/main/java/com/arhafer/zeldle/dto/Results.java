package com.arhafer.zeldle.dto;

import com.arhafer.zeldle.constant.Result;

public record Results(Result name,
                      Result game,
                      Result gameReleaseDate,
                      Result purpose,
                      Result consumption,
                      Result acquisition,
                      Result range,
                      Result enemyInteraction,
                      Result controlMode) {}
