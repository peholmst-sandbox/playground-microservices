package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Service instance status object that contains information about the state of a specific service instance.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson provided that the
 * {@code JavaTimeModule} has been registered with the object mapper.
 */
public final class ServiceInstanceStatus extends ResourceInstanceStatus<ServiceInstanceDescriptor> {

    /**
     * Creates a new service instance status object.
     *
     * @param descriptor the service instance descriptor.
     * @param load       the load of the service instance.
     * @param lastSeen   the instant at which the directory service was last in contact with the service instance.
     * @param state      the last known state of the service instance.
     */
    @JsonCreator
    public ServiceInstanceStatus(@JsonProperty(value = "descriptor", required = true) ServiceInstanceDescriptor descriptor,
                                 @JsonProperty(value = "load", required = true) int load,
                                 @JsonProperty(value = "lastSeen", required = true) Instant lastSeen,
                                 @JsonProperty(value = "state", required = true) ResourceInstanceState state) {
        super(descriptor, load, lastSeen, state);
    }
}
