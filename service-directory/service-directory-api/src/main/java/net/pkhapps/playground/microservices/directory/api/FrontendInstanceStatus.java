package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Frontend instance status object that contains information about the state of a specific frontend instance.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson provided that the
 * {@code JavaTimeModule} has been registered with the object mapper.
 */
public final class FrontendInstanceStatus extends ResourceInstanceStatus<FrontendInstanceDescriptor> {

    /**
     * Creates a new frontend instance status object.
     *
     * @param instance the frontend instance descriptor.
     * @param load     the load of the frontend instance.
     * @param lastSeen the instant at which the directory service was last in contact with the frontend instance.
     * @param state    the last known state of the frontend instance.
     */
    @JsonCreator
    public FrontendInstanceStatus(@JsonProperty(value = "instance", required = true) FrontendInstanceDescriptor instance,
                                  @JsonProperty(value = "load", required = true) int load,
                                  @JsonProperty(value = "lastSeen", required = true) Instant lastSeen,
                                  @JsonProperty(value = "state", required = true) ResourceInstanceState state) {
        super(instance, load, lastSeen, state);
    }
}
