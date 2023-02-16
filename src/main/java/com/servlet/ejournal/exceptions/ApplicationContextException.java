package com.servlet.ejournal.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception that signalizes that {@link com.servlet.ejournal.context.ApplicationContext} cannot be configured properly,
 * and as a result - application can't start
 */
@StandardException
public class ApplicationContextException extends Exception {
}
