package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 * A service instance descriptor describes a particular instance of a {@link ServiceDescriptor service} that has been
 * registered with the service directory and that clients can interact with.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public final class ServiceInstanceDescriptor extends ResourceInstanceDescriptor<ServiceId> {

    /**
     * Creates a new service instance descriptor.
     *
     * @param id        the ID of the service.
     * @param version   the version of this service instance.
     * @param clientUri the URI that clients use to access this instance.
     * @param pingUri   the URI that the service directory pings to check the health of this instance.
     */
    @JsonCreator
    public ServiceInstanceDescriptor(@JsonProperty(value = "id", required = true) ServiceId id,
                                     @JsonProperty(value = "version", required = true) Version version,
                                     @JsonProperty(value = "clientUri", required = true) URI clientUri,
                                     @JsonProperty(value = "pingUri", required = true) URI pingUri) {
        super(id, version, clientUri, pingUri);
    }
}
