package com.arhafer.zeldle.dto;

/*
 * The frontend only needs to know the current date, nothing more. Target item ID is backend only and the rest of the
 * game state is stored in localStorage on the frontend. I only made this a record in case it changes in the future.
 */

import java.time.LocalDate;

public record GameResponse(LocalDate gameDate) {}
