package com.arhafer.zeldle.dto;

import com.arhafer.zeldle.entity.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemProperties(String name,
                             String game,
                             Item.Purpose purpose,
                             Item.Consumption consumption,
                             Item.Acquisition acquisition,
                             Item.Range range,
                             @JsonProperty("enemy_interaction") Item.EnemyInteraction enemyInteraction,
                             @JsonProperty("control_mode") Item.ControlMode controlMode) {}
