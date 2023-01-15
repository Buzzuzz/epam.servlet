package exceptions;

import constants.AttributeConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static constants.AttributeConstants.*;

@AllArgsConstructor
@Getter
public enum ErrorType {
    NONE (AttributeConstants.NONE_ATTR),
    EMAIL (EMAIL_ATTR),
    PASSWORD (PASSWORD_ATTR),
    PASSWORD_REPEAT (PASSWORD_REPEAT_ATTR),
    PHONE_NUMBER (AttributeConstants.PHONE_NUMBER);
    private final String value;
}
