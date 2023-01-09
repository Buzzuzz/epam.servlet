package tag_handlers;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;
import lombok.Getter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class DateOnlyTagHandler extends TagSupport {
    private Object date;

    public void setDate(Object date) {
        this.date = date;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            Timestamp temp = (Timestamp) date;
            LocalDateTime stamp = temp.toLocalDateTime();
            pageContext.getOut().print(
                    stamp.getDayOfMonth() + "/" +
                    stamp.getMonthValue() + "/" +
                    stamp.getYear());
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }
}
