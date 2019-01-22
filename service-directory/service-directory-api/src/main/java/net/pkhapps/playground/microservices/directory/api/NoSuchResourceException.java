package net.pkhapps.playground.microservices.directory.api;

/**
 * Exception thrown when an operation is attempted on a resource that does not exist.
 */
public class NoSuchResourceException extends ServiceDirectoryException {

    public NoSuchResourceException(String message) {
        super(message);
    }
}
