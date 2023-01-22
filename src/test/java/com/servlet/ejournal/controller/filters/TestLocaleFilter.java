package com.servlet.ejournal.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
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

        when(filter.getConfig().getInitParameter(LOCALE_ATTR)).thenReturn(LOCALE_EN);
        when(reqMock.getParameter(COMMAND_ATTR)).thenReturn(CHANGE_LOCALE_COMMAND);
        when(reqMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute(LOCALE_ATTR)).thenReturn(null);
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
    void testNoDefaultLocaleInSession() throws ServletException, IOException {
        when(reqMock.getParameter(COMMAND_ATTR)).thenReturn(null);
        filter.doFilter(reqMock, respMock, chainMock);
        verify(reqMock, atMostOnce()).getAttribute(LOCALE_ATTR);
        verify(reqMock, atMostOnce()).setAttribute(LOCALE_ATTR, filter.getConfig().getInitParameter(LOCALE_ATTR));
        verify(filter.getConfig(), times(2)).getInitParameter(LOCALE_ATTR);
        verify(chainMock, atMostOnce()).doFilter(reqMock, respMock);
    }

    @Test
    void testChangeLocaleChangeLocaleCommand() throws ServletException, IOException {
        when(reqMock.getSession().getAttribute(LOCALE_ATTR)).thenReturn(LOCALE_EN);
        filter.doFilter(reqMock, respMock, chainMock);
        verify(reqMock, atMostOnce()).getParameter(COMMAND_ATTR);
        verify(reqMock, atMostOnce()).getAttribute(LOCALE_ATTR);
        verify(reqMock, times(3)).getSession();

        verify(chainMock, atMostOnce()).doFilter(reqMock, respMock);
    }

    @Test
    void testChangeLocaleNoChangeLocaleCommand() throws ServletException, IOException {
        when(reqMock.getSession().getAttribute(LOCALE_ATTR)).thenReturn(LOCALE_EN);
        when(reqMock.getParameter(COMMAND_ATTR)).thenReturn(UPDATE_TOPIC);
        filter.doFilter(reqMock, respMock, chainMock);
        verify(reqMock, times(3)).getSession();
    }
}
