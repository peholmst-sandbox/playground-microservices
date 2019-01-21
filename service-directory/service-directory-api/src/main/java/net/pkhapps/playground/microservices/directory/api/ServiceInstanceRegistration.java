package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.net.URI;
import java.security.*;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

/**
 * Value object for registering a new {@link ServiceInstance} (or updating an existing one with the same ID and version).
 * A service instance is a particular version of a particular {@link Service}. The registration contains a digital
 * signature that must be {@link #verifySignature(PublicKey) verified} with the corresponding {@link Service}'s public
 * key before the registration can be accepted on the server side.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ServiceInstanceRegistration implements Serializable {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    @JsonProperty
    private final ServiceId id;
    @JsonProperty
    private final Version version;
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String description;
    @JsonProperty
    private final URI serviceUri;
    @JsonProperty
    private final URI pingUri;
    @JsonProperty
    private final URI documentationUri;
    @JsonProperty
    private final String signature;

    /**
     * Creates a new {@link ServiceInstanceRegistration}.
     *
     * @param id               the ID of the service.
     * @param version          the version of the service instance.
     * @param name             the name of the service instance.
     * @param description      an optional description of the service instance.
     * @param serviceUri       the URI of the service instance.
     * @param pingUri          the URI where the service can be pinged to check whether it is still online.
     * @param documentationUri an optional URI containing the documentation of the service.
     * @param privateKey       the private key to use when creating the digital signature.
     * @throws NoSuchAlgorithmException if the {@value #SIGNATURE_ALGORITHM} algorithm does not exist.
     * @throws InvalidKeyException      if the {@code privateKey} is invalid.
     * @throws SignatureException       if something goes wrong while creating the signature.
     */
    public ServiceInstanceRegistration(ServiceId id, Version version, String name, @Nullable String description,
                                       URI serviceUri, URI pingUri, @Nullable URI documentationUri,
                                       PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.version = Objects.requireNonNull(version, "version must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.description = description;
        this.serviceUri = Objects.requireNonNull(serviceUri, "serviceUri must not be null");
        this.pingUri = Objects.requireNonNull(pingUri, "pingUri must not be null");
        this.documentationUri = documentationUri;

        var signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        updateSignature(signature);
        this.signature = Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * Constructor used by Jackson and unit tests only. Clients should not use directly.
     */
    @JsonCreator
    ServiceInstanceRegistration(@JsonProperty(value = "id", required = true) ServiceId id,
                                @JsonProperty(value = "version", required = true) Version version,
                                @JsonProperty(value = "name", required = true) String name,
                                @JsonProperty(value = "description") @Nullable String description,
                                @JsonProperty(value = "serviceUri", required = true) URI serviceUri,
                                @JsonProperty(value = "pingUri", required = true) URI pingUri,
                                @JsonProperty(value = "documentationUri") @Nullable URI documentationUri,
                                @JsonProperty(value = "signature", required = true) String signature) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.description = description;
        this.serviceUri = serviceUri;
        this.pingUri = pingUri;
        this.documentationUri = documentationUri;
        this.signature = signature;
    }

    private void updateSignature(Signature signature) throws SignatureException {
        signature.update(id.toString().getBytes());
        signature.update(version.toString().getBytes());
        signature.update(name.getBytes());
        if (description != null) {
            signature.update(description.getBytes());
        }
        signature.update(serviceUri.toString().getBytes());
        signature.update(pingUri.toString().getBytes());
        if (documentationUri != null) {
            signature.update(documentationUri.toString().getBytes());
        }
    }

    /**
     * Returns the ID of the service.
     */
    public ServiceId getId() {
        return id;
    }

    /**
     * Returns the version of the service instance.
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Returns the name of the service instance, to be shown to users.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an optional description of the service instance, to be shown to users.
     */
    @JsonIgnore
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    /**
     * Returns the URI where the service instance can be accessed by clients.
     */
    public URI getServiceUri() {
        return serviceUri;
    }

    /**
     * Returns the URI where the service instance can be pinged by the service directory server to check whether it is
     * still online. This endpoint should respond with a 200 or 204 status code if everything is fine. Depending on the
     * service directory server implementation, additional content can also be included in the response.
     */
    public URI getPingUri() {
        return pingUri;
    }

    /**
     * Returns an optional URI where the documentation of the service instance can be found. This is intended for new
     * clients that wish to utilize this service instance.
     */
    @JsonIgnore
    public Optional<URI> getDocumentationUri() {
        return Optional.ofNullable(documentationUri);
    }

    /**
     * Returns the digital signature of this instance registration object as a Base64 encoded string.
     *
     * @see #verifySignature(PublicKey)
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Checks whether the provided {@link #getSignature() signature} is valid. The signature is valid if the
     * value of this registration has not changed and the public key corresponds to the private key that was used
     * to create the signature in the first place.
     *
     * @param publicKey the public key to use when verifying the signature.
     * @return true if the signature is valid, false if it is not.
     * @throws NoSuchAlgorithmException if the {@value #SIGNATURE_ALGORITHM} algorithm does not exist.
     * @throws InvalidKeyException      if the {@code publicKey} is invalid.
     * @throws SignatureException       if something goes wrong while verifying the signature.
     */
    public boolean verifySignature(PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException,
            SignatureException {
        var signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        updateSignature(signature);
        return signature.verify(Base64.getDecoder().decode(this.signature));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceInstanceRegistration that = (ServiceInstanceRegistration) o;
        return id.equals(that.id) &&
                version.equals(that.version) &&
                name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                serviceUri.equals(that.serviceUri) &&
                pingUri.equals(that.pingUri) &&
                Objects.equals(documentationUri, that.documentationUri) &&
                signature.equals(that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, name, description, serviceUri, pingUri, documentationUri, signature);
    }
}
