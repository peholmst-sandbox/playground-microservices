package net.pkhapps.playground.microservices.directory.api;

/**
 * Exception thrown when an operation is attempted on a service that does not exist.
 */
public class NoSuchServiceException extends ServiceDirectoryException {

    public NoSuchServiceException(String message) {
        super(message);
    }
}
