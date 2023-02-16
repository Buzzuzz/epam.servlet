package com.servlet.ejournal.exceptions;

import lombok.experimental.StandardException;

/**
 * Exception created for marking problems with configuration files (no proper config file, proper value etc.)
 */
@StandardException
public class ConfigException extends Exception {
}
