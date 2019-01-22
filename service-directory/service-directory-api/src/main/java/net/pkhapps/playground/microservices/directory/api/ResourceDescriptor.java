package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for resource descriptors.
 *
 * @param <ID> the resource ID type.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ResourceDescriptor<ID> implements Serializable {

    @JsonProperty
    private final ID id;
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String description;
    @JsonProperty
    private final URI iconUri;

    /**
     * Creates a new resource descriptor.
     *
     * @param id          the ID of the resource.
     * @param name        the human readable name of the resource.
     * @param description an optional human readable description of the resource.
     * @param iconUri     an optional URI of an icon for the resource.
     */
    @JsonCreator
    public ResourceDescriptor(@JsonProperty(value = "id", required = true) ID id,
                              @JsonProperty(value = "name", required = true) String name,
                              @JsonProperty(value = "description") @Nullable String description,
                              @JsonProperty(value = "iconUri") @Nullable URI iconUri) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.description = description;
        this.iconUri = iconUri;
    }

    /**
     * Copy constructor.
     *
     * @param origin the resource descriptor to copy from.
     */
    protected ResourceDescriptor(ResourceDescriptor<ID> origin) {
        Objects.requireNonNull(origin, "origin must not be null");
        this.id = origin.id;
        this.name = origin.name;
        this.description = origin.description;
        this.iconUri = origin.iconUri;
    }

    /**
     * Returns the ID of the resource.
     */
    public final ID getId() {
        return id;
    }

    /**
     * Returns the human readable name of the resource.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the optional human readable description of the resource.
     */
    @JsonIgnore
    public final Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    /**
     * Returns the optional icon URI of the resource.
     */
    @JsonIgnore
    public final Optional<URI> getIconUri() {
        return Optional.ofNullable(iconUri);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceDescriptor<?> that = (ResourceDescriptor<?>) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(iconUri, that.iconUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, iconUri);
    }
}
