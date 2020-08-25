package com.github.espress91.kakaopaysprinkling.error;

import com.github.espress91.kakaopaysprinkling.util.MessageUtils;
import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;

public class NotFoundException extends ServiceRuntimeException{

    static final String MESSAGE = "NotFound";
    static final String MESSAGE_DETAILS = "Could not found '%s' with query values (%s)";

    public NotFoundException(Class cls, Object... values) {
        this(cls.getSimpleName(), values);
    }

    public NotFoundException(String targetName, Object... values) {
        super(MESSAGE,
                MESSAGE_DETAILS,
                new String[]{targetName, (values != null && values.length > 0) ? StringUtils.join(values, ",") : ""},
                HttpStatus.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return MessageUtils.getInstance().getMessage(MESSAGE_DETAILS, getParams());
    }

    @Override
    public String toString() {
        return MessageUtils.getInstance().getMessage(MESSAGE);
    }

}
