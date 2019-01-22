package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private final ID id;
    @JsonProperty
    private final Version version;
    @JsonProperty
    private final URI clientUri;
    @JsonProperty
    private final URI pingUri;

    /**
     * Creates a new resource instance descriptor.
     *
     * @param id        the ID of the resource.
     * @param version   the version of this resource instance.
     * @param clientUri the URI that clients use to access this instance.
     * @param pingUri   the URI that the service directory pings to check the health of this instance.
     */
    @JsonCreator
    public ResourceInstanceDescriptor(@JsonProperty(value = "id", required = true) ID id,
                                      @JsonProperty(value = "version", required = true) Version version,
                                      @JsonProperty(value = "clientUri", required = true) URI clientUri,
                                      @JsonProperty(value = "pingUri", required = true) URI pingUri) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.version = Objects.requireNonNull(version, "version must not be null");
        this.clientUri = Objects.requireNonNull(clientUri, "clientUri must not be null");
        this.pingUri = Objects.requireNonNull(pingUri, "pingUri must not be null");
    }

    /**
     * Copy constructor.
     *
     * @param origin the resource instance descriptor to copy from.
     */
    protected ResourceInstanceDescriptor(ResourceInstanceDescriptor<ID> origin) {
        Objects.requireNonNull(origin, "origin must not be null");
        this.id = origin.id;
        this.version = origin.version;
        this.clientUri = origin.clientUri;
        this.pingUri = origin.pingUri;
    }

    /**
     * Returns the ID of the resource. Please note that this ID does not identify an individual instance but the
     * resource that the instance "implements". The {@link #getClientUri() client URI} should be enough to uniquely
     * identify an instance but to be on the safe side, it is recommended to use a combination of resource ID, version
     * and client URI as a unique identifier.
     */
    public final ID getId() {
        return id;
    }

    /**
     * Returns the version of the resource instance.
     */
    public final Version getVersion() {
        return version;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceInstanceDescriptor<?> that = (ResourceInstanceDescriptor<?>) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(version, that.version) &&
                Objects.equals(clientUri, that.clientUri) &&
                Objects.equals(pingUri, that.pingUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, clientUri, pingUri);
    }
}
