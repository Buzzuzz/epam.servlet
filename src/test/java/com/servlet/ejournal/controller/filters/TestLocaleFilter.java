package com.servlet.ejournal.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.constants.CommandNameConstants.*;

// TODO : changed filter logic from session to cookies, rewrite tests

class TestLocaleFilter {
    private final LocaleFilter filter = new LocaleFilter();
    private HttpServletRequest reqMock;
    private HttpServletResponse respMock;
    private FilterChain chainMock;
    FilterConfig config;

    @BeforeEach
    void setup() {
        config = mock(FilterConfig.class);
        filter.setConfig(config);
        reqMock = mock(HttpServletRequest.class);
        respMock = mock(HttpServletResponse.class);
        chainMock = mock(FilterChain.class);
        HttpSession sessionMock = mock(HttpSession.class);
        Cookie[] cookies = new Cookie[]{new Cookie("not-locale", "definitely")};

        when(filter.getConfig().getInitParameter(LOCALE_ATTR)).thenReturn(LOCALE_EN);
        when(reqMock.getParameter(COMMAND_ATTR)).thenReturn(CHANGE_LOCALE_COMMAND);
        when(reqMock.getCookies()).thenReturn(cookies);
        when(reqMock.getSession()).thenReturn(sessionMock);
        when(reqMock.getParameter(LOCALE_ATTR)).thenReturn(LOCALE_EN);
    }

    @Test
    void testInitConfig() {
        filter.setConfig(null);
        assertNull(filter.getConfig());
        filter.setConfig(config);
        assertEquals(filter.getConfig(), config);
    }

    @Test
    void testNoDefaultLocaleInCookies() throws ServletException, IOException {
        when(reqMock.getParameter(COMMAND_ATTR)).thenReturn(null);
        filter.doFilter(reqMock, respMock, chainMock);
        verify(respMock, times(1)).addCookie(any(Cookie.class));
        verify(reqMock, times(1)).getSession();
        verify(reqMock.getSession(), times(1)).setAttribute(any(), any());
        verify(chainMock, times(1)).doFilter(reqMock, respMock);
    }

    @Test
    void testChangeLocaleChangeLocaleCommand() throws ServletException, IOException {
        filter.doFilter(reqMock, respMock, chainMock);
        verify(reqMock, times(3)).getCookies();
        verify(reqMock, times(2)).getParameter(any());
        verify(respMock, times(1)).addCookie(any());
        verify(chainMock, atMostOnce()).doFilter(reqMock, respMock);
    }

    @Test
    void testChangeLocaleNoChangeLocaleCommand() throws ServletException, IOException {
        when(reqMock.getParameter(COMMAND_ATTR)).thenReturn(null);
        when(reqMock.getCookies()).thenReturn(new Cookie[]{new Cookie(LOCALE_ATTR, LOCALE_EN)});
        filter.doFilter(reqMock, respMock, chainMock);
        verify(reqMock, times(1)).getCookies();
        verify(reqMock, times(1)).getParameter(any());
        verify(reqMock, times(1)).getSession();
    }
}
