package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.security.PublicKey;

/**
 * A frontend registration is a request to register a frontend with the service directory.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 *
 * @see FrontendInstanceRegistration
 */
public final class FrontendRegistration extends ResourceRegistration<FrontendId> {

    /**
     * Creates a new frontend registration.
     *
     * @param descriptor the frontend descriptor.
     * @param publicKey  the public key of the frontend. The public key must support {@link PublicKey#getEncoded() encoding}.
     */
    public FrontendRegistration(FrontendDescriptor descriptor, PublicKey publicKey) {
        super(descriptor, publicKey);
    }

    /**
     * Constructor used by Jackson and unit tests only. Clients should not use directly.
     */
    @JsonCreator
    FrontendRegistration(@JsonProperty(value = "id", required = true) FrontendId id,
                         @JsonProperty(value = "name", required = true) String name,
                         @JsonProperty(value = "description") @Nullable String description,
                         @JsonProperty(value = "iconUri") @Nullable URI iconUri,
                         @JsonProperty(value = "algorithm", required = true) String algorithm,
                         @JsonProperty(value = "publicKey", required = true) String publicKey) {
        super(id, name, description, iconUri, algorithm, publicKey);
    }
}
