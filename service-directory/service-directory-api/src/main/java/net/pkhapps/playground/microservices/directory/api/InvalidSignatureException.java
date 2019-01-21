package net.pkhapps.playground.microservices.directory.api;

/**
 * Exception thrown when the signature of a {@link ServiceInstanceRegistration} is not valid.
 */
public class InvalidSignatureException extends ServiceDirectoryException {

    public InvalidSignatureException(String message) {
        super(message);
    }
}
