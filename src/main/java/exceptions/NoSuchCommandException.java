package exceptions;

import lombok.experimental.StandardException;

/**
 * Default implementation of exception for case when command is not in the
 * {@link controller.commands.CommandPool CommandPool}
 */
@StandardException
public class NoSuchCommandException extends CommandException{
}
