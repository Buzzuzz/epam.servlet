package controller.commands;

import lombok.experimental.StandardException;

/**
 * Command Exception (will be thrown from command / controller) in
 * case anything goes wrong. Annotated with {@link StandardException} from lombok.
 */
@StandardException
public class CommandException extends RuntimeException{
}
