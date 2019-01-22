package net.pkhapps.playground.microservices.directory.client;

import net.pkhapps.playground.microservices.directory.api.ServiceDirectoryException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an unexpected response is received from the service directory server.
 */
public class UnexpectedResponseException extends ServiceDirectoryException {

    private final HttpStatus statusCode;

    UnexpectedResponseException(HttpStatus statusCode) {
        super("Unexpected status code " + statusCode.value() + " was returned by server");
        this.statusCode = statusCode;
    }

    /**
     * Returns the HTTP status code returned by the server.
     */
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
