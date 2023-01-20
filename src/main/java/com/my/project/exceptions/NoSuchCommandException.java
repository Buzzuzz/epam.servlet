package com.my.project.exceptions;

import com.my.project.controller.commands.CommandPool;
import lombok.experimental.StandardException;

/**
 * Default implementation of exception for case when command is not in the
 * {@link CommandPool CommandPool}
 */
@StandardException
public class NoSuchCommandException extends CommandException{
}
