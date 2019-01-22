package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Frontend status object that contains information about the state of a specific frontend and all of its instances.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson provided that the
 * {@code JavaTimeModule} has been registered with the object mapper.
 */
public final class FrontendStatus extends ResourceStatus<FrontendId, FrontendDescriptor, FrontendInstanceDescriptor,
        FrontendInstanceStatus> {

    /**
     * Creates a new frontend status object.
     *
     * @param descriptor the frontend descriptor.
     * @param instances  the status of all frontend instances.
     */
    @JsonCreator
    public FrontendStatus(@JsonProperty(value = "descriptor", required = true) FrontendDescriptor descriptor,
                          @JsonProperty(value = "instances", required = true) Collection<FrontendInstanceStatus> instances) {
        super(descriptor, instances);
    }
}
