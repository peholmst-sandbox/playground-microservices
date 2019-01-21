package net.pkhapps.playground.microservices.directory.api;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * Value object for registering a new {@link Service} (or updating an existing one with the same ID). The registration
 * contains a public key that must be used by the service directory when verifying incoming
 * {@link ServiceInstanceRegistration}s.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public final class ServiceRegistration implements Serializable {

    @JsonProperty
    private final ServiceId id;
    @JsonProperty
    private final String algorithm;
    @JsonProperty
    private final String publicKey;

    @JsonIgnore
    private transient PublicKey cachedPublicKey;

    /**
     * Creates a new {@code ServiceRegistration}.
     *
     * @param id        the ID of the service.
     * @param publicKey the public key of the service. The public key must support {@link PublicKey#getEncoded() encoding}.
     */
    public ServiceRegistration(ServiceId id, PublicKey publicKey) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(publicKey, "publicKey must not be null");
        this.algorithm = publicKey.getAlgorithm();
        this.publicKey = Base64.getEncoder().encodeToString(Objects.requireNonNull(publicKey.getEncoded(),
                "publicKey cannot be encoded"));
        this.cachedPublicKey = publicKey;
    }

    /**
     * Constructor used by Jackson and unit tests only. Clients should not use directly.
     */
    @JsonCreator
    ServiceRegistration(@JsonProperty(value = "id", required = true) ServiceId id,
                        @JsonProperty(value = "algorithm", required = true) String algorithm,
                        @JsonProperty(value = "publicKey", required = true) String publicKey) {
        this.id = id;
        this.algorithm = algorithm;
        this.publicKey = publicKey;
    }

    /**
     * Returns the ID of the service.
     */
    public ServiceId getId() {
        return id;
    }

    /**
     * Returns the public key of the service. This key is used by the service directory to
     * {@link ServiceInstanceRegistration#verifySignature(PublicKey) verify} the signatures of all
     * {@link ServiceInstanceRegistration}s before they are accepted.
     *
     * @throws NoSuchAlgorithmException if the algorithm used to create the public key is not supported.
     * @throws InvalidKeySpecException  if {@code X509EncodedKeySpec} is not supported for creating public keys from an
     *                                  encoded key.
     */
    @JsonIgnore
    public PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (this.cachedPublicKey == null) {
            this.cachedPublicKey = KeyFactory.getInstance(algorithm)
                    .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
        }
        return this.cachedPublicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRegistration that = (ServiceRegistration) o;
        return id.equals(that.id) &&
                algorithm.equals(that.algorithm) &&
                publicKey.equals(that.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, algorithm, publicKey);
    }
}
