package com.arhafer.zeldle.dto;

import com.arhafer.zeldle.entity.Item;

public record ItemProperties(String name,
                             String game,
                             Item.Purpose purpose,
                             Item.Consumption consumption,
                             Item.Acquisition acquisition,
                             Item.Range range,
                             Item.EnemyInteraction enemyInteraction,
                             Item.ControlMode controlMode) {}
