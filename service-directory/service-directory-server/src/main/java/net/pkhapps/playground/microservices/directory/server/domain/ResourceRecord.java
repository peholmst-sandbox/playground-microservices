package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.ResourceDescriptor;
import net.pkhapps.playground.microservices.directory.api.ResourceRegistration;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.net.URI;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Objects;

@MappedSuperclass
public abstract class ResourceRecord<ID, RD extends ResourceDescriptor<ID>> extends AbstractPersistable<Long> {

    @Column(name = "resource_id", nullable = false, unique = true)
    private ID resourceId;

    @Column(name = "resource_name", nullable = false)
    private String name;

    @Column(name = "resource_description")
    private String description;

    @Column(name = "icon_uri")
    private String iconUri;

    @Column(name = "documentation_uri")
    private String documentationUri;

    @Column(name = "algorithm", nullable = false)
    private String algorithm;

    @Column(name = "public_key", nullable = false)
    @Lob
    private byte[] publicKey;

    @Transient
    private transient PublicKey cachedPublicKey;

    @LastModifiedDate
    @Column(name = "last_modified")
    private Instant lastModified;

    @CreatedDate
    @Column(name = "created")
    private Instant created;

    // TODO User who created or updated the record

    protected ResourceRecord() {
    }

    public ResourceRecord(ResourceRegistration<ID, RD> registration) {
        updateRecord(registration);
    }

    public void updateRecord(ResourceRegistration<ID, RD> registration) {
        Objects.requireNonNull(registration, "registration must not be null");
        var descriptor = registration.getDescriptor();
        this.resourceId = descriptor.getId();
        this.name = descriptor.getName();
        this.description = descriptor.getDescription().orElse(null);
        this.iconUri = descriptor.getIconUri().map(URI::toString).orElse(null);
        this.documentationUri = descriptor.getDocumentationUri().map(URI::toString).orElse(null);
        cachedPublicKey = registration.getPublicKey();
        this.algorithm = cachedPublicKey.getAlgorithm();
        this.publicKey = cachedPublicKey.getEncoded();
    }

    public ID getResourceId() {
        return resourceId;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public URI getIconUri() {
        return iconUri == null ? null : URI.create(iconUri);
    }

    @Nullable
    public URI getDocumentationUri() {
        return documentationUri == null ? null : URI.create(documentationUri);
    }

    @Nullable
    public Instant getLastModified() {
        return lastModified;
    }

    @Nullable
    public Instant getCreated() {
        return created;
    }

    public PublicKey getPublicKey() {
        if (cachedPublicKey == null) {
            try {
                this.cachedPublicKey = KeyFactory.getInstance(algorithm)
                        .generatePublic(new X509EncodedKeySpec(publicKey));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                throw new IllegalStateException("Could not generate public key", ex);
            }
        }
        return cachedPublicKey;
    }

    public abstract RD toDescriptor();
}
