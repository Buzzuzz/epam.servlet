package com.servlet.ejournal.utils.tag_handlers;

import com.servlet.ejournal.exceptions.UtilException;
import com.servlet.ejournal.utils.DateFormatterUtil;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestDateOnlyTagHandler {
    Timestamp testDateTimestamp = DateFormatterUtil.getTimestamp("2023-01-24");
    static DateOnlyTagHandler handler;
    static PageContext pageContextMock;
    static JspWriter writerMock;


    TestDateOnlyTagHandler() throws UtilException {
    }

    @BeforeAll
    static void setup() {
        handler = new DateOnlyTagHandler();
        pageContextMock = mock(PageContext.class);
        writerMock = mock(JspWriter.class);
        when(pageContextMock.getOut()).thenReturn(writerMock);
    }

    @Test
    void testGetValidDateString() {
        handler.setDate(testDateTimestamp);
        handler.setPageContext(pageContextMock);
        assertDoesNotThrow(() -> handler.doStartTag());
    }

    @Test
    void testPassingNotTimestamp() {
        handler.setDate("not a date");
        assertThrows(JspException.class, () -> handler.doStartTag());
    }
}
