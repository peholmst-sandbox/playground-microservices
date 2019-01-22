package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value object representing the ID of a frontend. Essentially this is a wrapper around a string and so the actual ID
 * can be of any format.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public final class FrontendId implements Serializable {

    @JsonValue
    private final String id;

    /**
     * Creates a new {@code FrontendId}.
     *
     * @param id the service ID to wrap.
     */
    @JsonCreator
    public FrontendId(String id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrontendId that = (FrontendId) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
