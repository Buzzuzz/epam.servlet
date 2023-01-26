package com.servlet.ejournal.controller.filters;

import jakarta.servlet.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.servlet.ejournal.constants.AttributeConstants.ENCODING_ATTR;
import static com.servlet.ejournal.constants.AttributeConstants.ENCODING_UTF_8;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TestEncodingFilter {
    private final EncodingFilter filter = new EncodingFilter();
    private FilterChain chainMock;
    private ServletRequest reqMock;
    private ServletResponse respMock;


    @BeforeEach
    void setup() {
        chainMock = mock(FilterChain.class);
        reqMock = mock(ServletRequest.class);
        respMock = mock(ServletResponse.class);
        filter.setConfig(mock(FilterConfig.class));
        when(filter.getConfig().getInitParameter(ENCODING_ATTR)).thenReturn(ENCODING_UTF_8);
    }

    @Test
    void testFilterMethodInvocationsAllParamsValid() throws ServletException, IOException {
        filter.doFilter(reqMock, respMock, chainMock);
        verify(reqMock, times(1)).setCharacterEncoding(filter.getConfig().getInitParameter(ENCODING_ATTR));
        verify(respMock, atMostOnce()).setContentType(String.format("text/html; charset=%s;", filter.getConfig().getInitParameter(ENCODING_ATTR)));
        verify(respMock, atMostOnce()).setCharacterEncoding(filter.getConfig().getInitParameter(ENCODING_ATTR));
        verify(chainMock, atMostOnce()).doFilter(reqMock, respMock);
    }

    @Test
    void testFilterMethodInvocationInvalidInitParam() {
        filter.setConfig(null);
        assertThrows(NullPointerException.class, () -> filter.doFilter(reqMock, respMock, chainMock));
    }

    @Test
    void testInitConfig() throws ServletException {
        filter.init(null);
        assertNull(filter.getConfig());
    }
}
