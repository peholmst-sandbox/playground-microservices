package net.pkhapps.playground.microservices.directory.api;

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

/**
 * Value object representing the ID of a particular resource instance.
 *
 * @param <ID> the ID type of the resource (typically frontend or service).
 */
public class ResourceInstanceId<ID> implements Serializable {

    private final ID resourceId;
    private final URI clientUri;

    /**
     * Creates a new {@code ResourceInstanceId}.
     *
     * @param resourceId the ID of the resource that this instance is an instance of.
     * @param clientUri  the client URI of the resource instance.
     */
    public ResourceInstanceId(ID resourceId, URI clientUri) {
        this.resourceId = Objects.requireNonNull(resourceId, "resourceId must not be null");
        this.clientUri = Objects.requireNonNull(clientUri, "clientUri must not be null");
    }

    /**
     * Creates a new {@code ResourceInstanceId}.
     *
     * @param descriptor the resource instance for which the ID should be created.
     */
    public ResourceInstanceId(ResourceInstanceDescriptor<ID> descriptor) {
        this(descriptor.getResourceId(), descriptor.getClientUri());
    }

    /**
     * Returns the ID of the resource that this instance is an instance of.
     */
    public ID getResourceId() {
        return resourceId;
    }

    /**
     * Returns the client URI of the resource instance. This is the URI that clients use when they want to interact with
     * the resource instance.
     */
    public URI getClientUri() {
        return clientUri;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", resourceId, clientUri);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceInstanceId<?> that = (ResourceInstanceId<?>) o;
        return resourceId.equals(that.resourceId) &&
                clientUri.equals(that.clientUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId, clientUri);
    }
}
