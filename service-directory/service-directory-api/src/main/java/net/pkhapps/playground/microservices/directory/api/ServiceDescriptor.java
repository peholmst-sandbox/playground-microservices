package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.net.URI;

/**
 * A service descriptor describes a service that has been registered with the service directory.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public final class ServiceDescriptor extends ResourceDescriptor<ServiceId> {

    /**
     * Creates a new service descriptor.
     *
     * @param id          the ID of the service.
     * @param name        the human readable name of the service.
     * @param description an optional human readable description of the service.
     * @param iconUri     an optional URI of an icon for the service.
     */
    @JsonCreator
    public ServiceDescriptor(@JsonProperty(value = "id", required = true) ServiceId id,
                             @JsonProperty(value = "name", required = true) String name,
                             @JsonProperty(value = "description") @Nullable String description,
                             @JsonProperty(value = "iconUri") @Nullable URI iconUri) {
        super(id, name, description, iconUri);
    }
}
