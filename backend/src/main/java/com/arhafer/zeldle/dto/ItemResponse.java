package com.arhafer.zeldle.dto;

/*
 * The frontend is only ever going to receive ids and names.
 * That means you can't cheat with inspect element. >;)
 */

public record ItemResponse(Integer id, String name) {}
