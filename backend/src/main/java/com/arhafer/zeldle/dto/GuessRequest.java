package com.arhafer.zeldle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GuessRequest(@JsonProperty("item_id") int itemId) {}
