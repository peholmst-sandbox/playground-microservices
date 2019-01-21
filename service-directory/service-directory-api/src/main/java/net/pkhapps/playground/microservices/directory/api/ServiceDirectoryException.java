package net.pkhapps.playground.microservices.directory.api;

/**
 * Base class for all exceptions thrown by {@link ServiceDirectory}.
 */
public abstract class ServiceDirectoryException extends RuntimeException {

    public ServiceDirectoryException() {
    }

    public ServiceDirectoryException(String message) {
        super(message);
    }
}
