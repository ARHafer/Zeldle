package com.arhafer.zeldle.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerIdFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private PlayerIdFilter playerIdFilter;

    private MockedStatic<UUID> mockedUUID;

    // Mocking static classes is such a pain in the ass, it's almost not even worth it.
    @BeforeEach
    public void setUp() {
        mockedUUID = mockStatic(UUID.class, Mockito.CALLS_REAL_METHODS);
    }

    @AfterEach
    public void tearDown() {
        mockedUUID.close();
    }

    // If the valid ID cookie is already present, the UUID stored in that cookie should be stored in the request.
    @Test
    void validCookieFound() throws Exception {
        Cookie existingCookie = new Cookie("zeldle_player_id", UUID.randomUUID().toString());
        when(request.getCookies()).thenReturn(new Cookie[] {existingCookie});

        playerIdFilter.doFilterInternal(request, response, filterChain);

        verify(request).setAttribute("zeldle_player_id", UUID.fromString(existingCookie.getValue()));
        verify(filterChain).doFilter(request, response);
    }

    // If an invalid ID cookie is present, a new cookie should be created and the player UUID should be stored in the request.
    @Test
    void invalidCookieFound() throws Exception {
        Cookie invalidCookie = new Cookie("zeldle_player_id", "bad horrible evil value");
        when(request.getCookies()).thenReturn(new Cookie[] {invalidCookie});

        UUID fixedUUID = UUID.randomUUID();
        mockedUUID.when(UUID::randomUUID).thenReturn(fixedUUID);

        playerIdFilter.doFilterInternal(request, response, filterChain);

        verify(request).setAttribute("zeldle_player_id", fixedUUID);
        verify(filterChain).doFilter(request, response);
    }

    // If no ID cookie is present, a new cookie should be created and the player UUID should be stored in the request.
    @Test
    void noCookieFound_existingCookies() throws Exception {
        when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("super_random_name", "super random value")});

        UUID fixedUUID = UUID.randomUUID();
        mockedUUID.when(UUID::randomUUID).thenReturn(fixedUUID);

        playerIdFilter.doFilterInternal(request, response, filterChain);

        verify(request).setAttribute("zeldle_player_id", fixedUUID);
        verify(filterChain).doFilter(request, response);
    }

    // If no ID cookie is present, a new cookie should be created and the player UUID should be stored in the request.
    @Test
    void noCookieFound_noCookies() throws Exception {
        when(request.getCookies()).thenReturn(null);

        UUID fixedUUID = UUID.randomUUID();
        mockedUUID.when(UUID::randomUUID).thenReturn(fixedUUID);

        playerIdFilter.doFilterInternal(request, response, filterChain);

        verify(request).setAttribute("zeldle_player_id", fixedUUID);
        verify(filterChain).doFilter(request, response);
    }
}
