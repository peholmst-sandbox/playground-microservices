package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.net.URI;
import java.util.Objects;

/**
 * Base class for resource instance descriptors.
 *
 * @param <ID> the resource ID type.
 */
public abstract class ResourceInstanceDescriptor<ID> implements Serializable {

    @JsonProperty
    private final ID resourceId;
    @JsonProperty
    private final URI clientUri;
    @JsonProperty
    private final URI pingUri;

    /**
     * Creates a new resource instance descriptor.
     *
     * @param resourceId the ID of the resource.
     * @param clientUri  the URI that clients use to access this instance.
     * @param pingUri    the URI that the service directory pings to check the health of this instance.
     */
    @JsonCreator
    public ResourceInstanceDescriptor(@JsonProperty(value = "resourceId", required = true) ID resourceId,
                                      @JsonProperty(value = "clientUri", required = true) URI clientUri,
                                      @JsonProperty(value = "pingUri", required = true) URI pingUri) {
        this.resourceId = Objects.requireNonNull(resourceId, "resourceId must not be null");
        this.clientUri = Objects.requireNonNull(clientUri, "clientUri must not be null");
        this.pingUri = Objects.requireNonNull(pingUri, "pingUri must not be null");
    }

    /**
     * Returns the ID of this resource instance.
     */
    @JsonIgnore
    public final ResourceInstanceId<ID> getId() {
        return new ResourceInstanceId<>(resourceId, clientUri);
    }

    /**
     * Returns the ID of the resource that the instance "implements".
     */
    public final ID getResourceId() {
        return resourceId;
    }

    /**
     * Returns the URI where clients can access the resource instance.
     */
    public final URI getClientUri() {
        return clientUri;
    }

    /**
     * Returns the URI where the resource instance can be pinged by the service directory server to check whether it is
     * still online. This resource instance should respond with a 200 or 204 status code if everything is fine.
     * Depending on the service directory server implementation, additional content can also be included in the
     * response.
     */
    public final URI getPingUri() {
        return pingUri;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceInstanceDescriptor<?> that = (ResourceInstanceDescriptor<?>) o;
        return Objects.equals(resourceId, that.resourceId) &&
                Objects.equals(clientUri, that.clientUri) &&
                Objects.equals(pingUri, that.pingUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId, clientUri, pingUri);
    }

    @Override
    public String toString() {
        return String.format("%s(resourceId: [%s], clientUri: [%s])", getClass().getSimpleName(),
                resourceId, clientUri);
    }
}
