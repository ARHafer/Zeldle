package com.arhafer.zeldle.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Component
public class PlayerIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        boolean hasPlayerId = false;
        Cookie playerIdCookie = null;
        UUID playerId;

        if (cookies != null) {
            hasPlayerId = Arrays.stream(cookies).anyMatch(cookie -> cookie.getName().equals("zeldle_player_id"));
        }

        if (hasPlayerId) {

            try { // Just in case someone manually makes a cookie not containing a valid UUID for some reason.
                String cookieValue = "invalid";

                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("zeldle_player_id")) {
                        cookieValue = cookie.getValue();
                        break;
                    }
                }

                playerId = UUID.fromString(cookieValue);

            } catch (IllegalArgumentException e) {
                playerId = UUID.randomUUID();

                playerIdCookie = createPlayerIdCookie(playerId);
            }

        } else {
            playerId = UUID.randomUUID();
            playerIdCookie = createPlayerIdCookie(playerId);
        }

        if (playerIdCookie != null) {
            response.addCookie(playerIdCookie);
        }

        request.setAttribute("zeldle_player_id", playerId);
        filterChain.doFilter(request, response);
    }

    private static Cookie createPlayerIdCookie(UUID playerId) {
        Cookie cookie = new Cookie("zeldle_player_id", playerId.toString());
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);

        return cookie;
    }
}
