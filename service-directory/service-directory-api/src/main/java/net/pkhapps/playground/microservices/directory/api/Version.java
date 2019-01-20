package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value object representing a version of some resource in the service directory. Essentially this is a wrapper around a
 * string and so the actual version can be of any format.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public class Version implements Serializable {

    @JsonValue
    private final String version;

    /**
     * Creates a new {@code Version}.
     *
     * @param version the version to wrap.
     */
    @JsonCreator
    public Version(String version) {
        this.version = Objects.requireNonNull(version, "version must not be null");
    }

    /**
     * Returns the wrapped version string.
     */
    @Override
    public String toString() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version1 = (Version) o;
        return version.equals(version1.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }
}
