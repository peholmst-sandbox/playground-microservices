package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 * A frontend instance registration is a request to register a frontend instance with the service directory.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public final class FrontendInstanceRegistration extends ResourceInstanceRegistration<FrontendId, FrontendInstanceDescriptor> {

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
    FrontendInstanceRegistration(@JsonProperty(value = "descriptor", required = true) FrontendInstanceDescriptor descriptor,
                                 @JsonProperty(value = "algorithm", required = true) String algorithm,
                                 @JsonProperty(value = "signature", required = true) String signature) {
        super(descriptor, algorithm, signature);
    }

    @Override
    protected void updateSignature(Signature signature, FrontendInstanceDescriptor descriptor) throws SignatureException {
        super.updateSignature(signature, descriptor);
        var notificationsUri = descriptor.getNotificationsUri();
        if (notificationsUri.isPresent()) {
            signature.update(notificationsUri.get().toString().getBytes());
        }
    }
}
