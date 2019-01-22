package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * Base class for resource registration objects. The registration contains a public key that must be used by the service
 * directory when verifying incoming resource instance registrations.
 *
 * @param <ID> the resource ID type.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ResourceRegistration<ID> extends ResourceDescriptor<ID> {

    @JsonProperty
    private final String algorithm;
    @JsonProperty
    private final String publicKey;
    @JsonIgnore
    private transient PublicKey cachedPublicKey;

    /**
     * Creates a new resource registration.
     *
     * @param descriptor the resource descriptor.
     * @param publicKey  the public key of the resource. The public key must support {@link PublicKey#getEncoded() encoding}.
     */
    public ResourceRegistration(ResourceDescriptor<ID> descriptor, PublicKey publicKey) {
        super(descriptor);
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
    protected ResourceRegistration(@JsonProperty(value = "id", required = true) ID id,
                                   @JsonProperty(value = "name", required = true) String name,
                                   @JsonProperty(value = "description") @Nullable String description,
                                   @JsonProperty(value = "iconUri") @Nullable URI iconUri,
                                   @JsonProperty(value = "algorithm", required = true) String algorithm,
                                   @JsonProperty(value = "publicKey", required = true) String publicKey) {
        super(id, name, description, iconUri);
        this.algorithm = algorithm;
        this.publicKey = publicKey;
    }

    /**
     * Returns the public key of the resource. This key is used by the service directory to verify the signatures of all
     * resource instance registrations before they are accepted.
     */
    @JsonIgnore
    public final PublicKey getPublicKey() {
        try {
            if (this.cachedPublicKey == null) {
                this.cachedPublicKey = KeyFactory.getInstance(algorithm)
                        .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
            }
            return this.cachedPublicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalStateException("Could not generate public key", ex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ResourceRegistration<?> that = (ResourceRegistration<?>) o;
        return Objects.equals(algorithm, that.algorithm) &&
                Objects.equals(publicKey, that.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), algorithm, publicKey);
    }
}
