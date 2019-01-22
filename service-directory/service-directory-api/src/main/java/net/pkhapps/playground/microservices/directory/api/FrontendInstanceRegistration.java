package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.security.PrivateKey;

/**
 * A frontend instance registration is a request to register a frontend instance with the service directory.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public final class FrontendInstanceRegistration extends ResourceInstanceRegistration<FrontendId> {

    /**
     * Creates a new frontend instance registration.
     *
     * @param descriptor the frontend instance descriptor.
     * @param privateKey the private key to use when creating the digital signature. The private key will not be stored
     *                   anywhere.
     */
    public FrontendInstanceRegistration(FrontendInstanceDescriptor descriptor, PrivateKey privateKey) {
        super(descriptor, privateKey);
    }

    /**
     * Constructor used by Jackson and unit tests only. Clients should not use directly.
     */
    @JsonCreator
    protected FrontendInstanceRegistration(@JsonProperty(value = "id", required = true) FrontendId id,
                                           @JsonProperty(value = "version", required = true) Version version,
                                           @JsonProperty(value = "clientUri", required = true) URI clientUri,
                                           @JsonProperty(value = "pingUri", required = true) URI pingUri,
                                           @JsonProperty(value = "algorithm", required = true) String algorithm,
                                           @JsonProperty(value = "signature", required = true) String signature) {
        super(id, version, clientUri, pingUri, algorithm, signature);
    }
}
