package com.servlet.ejournal.utils.tag_handlers;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Log4j2
@Getter
public class DateOnlyTagHandler extends TagSupport {
    private transient Object date;

    public void setDate(Object date) {
        this.date = date;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            Timestamp temp = (Timestamp) date;
            LocalDateTime stamp = temp.toLocalDateTime();
            String month = stamp.getMonthValue() < 10 ? "0" + stamp.getMonthValue() : String.valueOf(stamp.getMonthValue());
            String day = stamp.getDayOfMonth() < 10 ? "0" + stamp.getDayOfMonth() : String.valueOf(stamp.getDayOfMonth());
            pageContext.getOut().print(String.format("%s-%s-%s", stamp.getYear(), month, day));
        } catch (Exception e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }
}
