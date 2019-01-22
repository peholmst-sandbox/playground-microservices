package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Service status object that contains information about the state of a specific service and all of its instances.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson provided that the
 * {@code JavaTimeModule} has been registered with the object mapper.
 */
public final class ServiceStatus extends ResourceStatus<ServiceId, ServiceDescriptor, ServiceInstanceDescriptor,
        ServiceInstanceStatus> {

    /**
     * Creates a new service status object.
     *
     * @param descriptor the service descriptor.
     * @param instances  the status of all service instances.
     */
    @JsonCreator
    public ServiceStatus(@JsonProperty(value = "descriptor", required = true) ServiceDescriptor descriptor,
                         @JsonProperty(value = "instances", required = true) Collection<ServiceInstanceStatus> instances) {
        super(descriptor, instances);
    }
}
