package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

/**
 * Base class for resource instance status objects that contain information about the state of a specific resource
 * instance.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson provided that the
 * {@code JavaTimeModule} has been registered with the object mapper.
 *
 * @param <RID> the type of the resource instance descriptor.
 */
public abstract class ResourceInstanceStatus<RID extends ResourceInstanceDescriptor<?>> {

    @JsonProperty
    private final RID descriptor;
    @JsonProperty
    private final int load;
    @JsonProperty
    private final Instant lastSeen;
    @JsonProperty
    private final ResourceInstanceState state;

    /**
     * Creates a new resource instance status object.
     *
     * @param descriptor the resource instance descriptor.
     * @param load       the load of the resource instance.
     * @param lastSeen   the instant at which the directory service was last in contact with the resource instance.
     * @param state      the last known state of the resource instance.
     */
    @JsonCreator
    public ResourceInstanceStatus(@JsonProperty(value = "descriptor", required = true) RID descriptor,
                                  @JsonProperty(value = "load", required = true) int load,
                                  @JsonProperty(value = "lastSeen", required = true) Instant lastSeen,
                                  @JsonProperty(value = "state", required = true) ResourceInstanceState state) {
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor must not be null");
        this.load = load;
        this.lastSeen = Objects.requireNonNull(lastSeen, "lastSeen must not be null");
        this.state = Objects.requireNonNull(state, "state must not be null");
    }

    /**
     * Returns the descriptor of the resource instance.
     */
    public final RID getDescriptor() {
        return descriptor;
    }

    /**
     * Returns the load of the resource instance. How the load is calculated is up to the resource itself but the higher
     * the number, the higher the load. The loads of instances of the same resource should be comparable. However, one
     * should normally not compare loads of instances of different resources.
     */
    public final int getLoad() {
        return load;
    }

    /**
     * Returns the instant at which the directory service was last in contact with the resource instance.
     */
    public final Instant getLastSeen() {
        return lastSeen;
    }

    /**
     * Returns the last known state of the resource instance.
     */
    public final ResourceInstanceState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceInstanceStatus<?> that = (ResourceInstanceStatus<?>) o;
        return load == that.load &&
                Objects.equals(descriptor, that.descriptor) &&
                Objects.equals(lastSeen, that.lastSeen) &&
                state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptor, load, lastSeen, state);
    }
}
