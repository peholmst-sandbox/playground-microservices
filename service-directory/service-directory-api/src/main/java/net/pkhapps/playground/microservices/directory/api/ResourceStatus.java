package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Base class for resource status objects that contain information about the state of a specific resource and all of its
 * instances.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson provided that the
 * {@code JavaTimeModule} has been registered with the object mapper.
 *
 * @param <ID>  the type of the resource ID.
 * @param <RD>  the type of the resource descriptor.
 * @param <RID> the type of the resource instance descriptor.
 * @param <RIS> the type of the resource instance status.
 */
public abstract class ResourceStatus<ID, RD extends ResourceDescriptor<ID>, RID extends ResourceInstanceDescriptor<ID>,
        RIS extends ResourceInstanceStatus<RID>> implements Serializable {

    @JsonProperty
    private final RD descriptor;
    @JsonProperty
    private final Set<RIS> instances;

    /**
     * Creates a new resource status object.
     *
     * @param descriptor the resource descriptor.
     * @param instances  the status of all resource instances.
     */
    @JsonCreator
    public ResourceStatus(@JsonProperty(value = "descriptor", required = true) RD descriptor,
                          @JsonProperty(value = "instances", required = true) Collection<RIS> instances) {
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor must not be null");
        this.instances = Set.copyOf(Objects.requireNonNull(instances, "instances must not be null"));
    }

    /**
     * Returns the resource descriptor.
     */
    public RD getDescriptor() {
        return descriptor;
    }

    /**
     * Returns the status of all resource instances.
     */
    public Stream<RIS> getInstances() {
        return instances.stream();
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceStatus<?, ?, ?, ?> that = (ResourceStatus<?, ?, ?, ?>) o;
        return Objects.equals(descriptor, that.descriptor) &&
                Objects.equals(instances, that.instances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptor, instances);
    }
}
