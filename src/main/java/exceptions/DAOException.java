package exceptions;

import lombok.experimental.StandardException;

/**
 * Standard exception that will be thrown from DAO layer of the application. <br>
 * Annotated with lombok <code>@StandardException</code> annotation, which means that
 * class contains all trivial Exception constructors.
 */
@StandardException
public class DAOException extends RuntimeException{
}
