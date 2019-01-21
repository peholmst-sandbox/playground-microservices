package net.pkhapps.playground.microservices.directory.api;

/**
 * Exception thrown when the user does not have permission to perform the requested operation on the service directory.
 */
public class AccessDeniedException extends ServiceDirectoryException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
