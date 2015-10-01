package be.wegenenverkeer.common.resteasy.exception;

/**
 * Exception that is thrown in case of a conflict.
 */
public class ConflictException extends AbstractRestException {

    /**
     * Constructor.
     * @param message A message explaining the nature of the error.
     */
    public ConflictException(String message) {
        super(message);
    }
}
