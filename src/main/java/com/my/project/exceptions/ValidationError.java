package com.my.project.exceptions;

import com.my.project.constants.AttributeConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ValidationError {
    NONE (AttributeConstants.NONE_ATTR),
    EMAIL (AttributeConstants.EMAIL_ATTR),
    PASSWORD (AttributeConstants.PASSWORD_ATTR),
    PASSWORD_REPEAT (AttributeConstants.PASSWORD_REPEAT_ATTR),
    PHONE_NUMBER (AttributeConstants.PHONE_NUMBER),
    END_DATE (AttributeConstants.COURSE_END_DATE),
    DB_ERROR (AttributeConstants.DB_ERROR);
    private final String value;
}
