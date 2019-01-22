package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.security.PublicKey;

/**
 * A service registration is a request to register a service with the service directory.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public final class ServiceRegistration extends ResourceRegistration<ServiceId> {

    /**
     * Creates a new service registration.
     *
     * @param descriptor the service descriptor.
     * @param publicKey  the public key of the service. The public key must support {@link PublicKey#getEncoded() encoding}.
     */
    public ServiceRegistration(ServiceDescriptor descriptor, PublicKey publicKey) {
        super(descriptor, publicKey);
    }

    /**
     * Constructor used by Jackson and unit tests only. Clients should not use directly.
     */
    @JsonCreator
    ServiceRegistration(@JsonProperty(value = "id", required = true) ServiceId id,
                        @JsonProperty(value = "name", required = true) String name,
                        @JsonProperty(value = "description") @Nullable String description,
                        @JsonProperty(value = "iconUri") @Nullable URI iconUri,
                        @JsonProperty(value = "algorithm", required = true) String algorithm,
                        @JsonProperty(value = "publicKey", required = true) String publicKey) {
        super(id, name, description, iconUri, algorithm, publicKey);
    }
}
