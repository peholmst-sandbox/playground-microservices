package net.pkhapps.playground.microservices.portal.server.ui.model;

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

/**
 * TODO Document me
 */
public class Origin implements Serializable {

    private final URI origin;

    public Origin(URI uri) {
        Objects.requireNonNull(uri, "uri must not be null");
        origin = URI.create(String.format("%s://%s:%s", uri.getScheme(), uri.getHost(), uri.getPort()));
    }

    public Origin(String scheme, String host, int port) {
        Objects.requireNonNull(scheme, "scheme must not be null");
        Objects.requireNonNull(host, "host must not be null");
        origin = URI.create(String.format("%s://%s:%d", scheme, host, port));
    }

    public URI toURI() {
        return origin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Origin origin1 = (Origin) o;
        return origin.equals(origin1.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin);
    }
}
