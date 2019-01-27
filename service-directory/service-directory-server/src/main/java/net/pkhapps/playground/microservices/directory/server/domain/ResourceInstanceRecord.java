package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.ResourceInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.api.ResourceInstanceRegistration;
import net.pkhapps.playground.microservices.directory.api.Version;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.net.URI;
import java.time.Instant;
import java.util.Objects;

@MappedSuperclass
public abstract class ResourceInstanceRecord<ID, RID extends ResourceInstanceDescriptor<ID>> extends AbstractPersistable<Long> {

    @Column(name = "resource_id", nullable = false)
    private ID resourceId;

    @Column(name = "resource_version", nullable = false)
    private Version version;

    @Column(name = "client_uri", nullable = false)
    private String clientUri;

    @Column(name = "ping_uri", nullable = false)
    private String pingUri;

    @LastModifiedDate
    @Column(name = "last_modified")
    private Instant lastModified;

    @CreatedDate
    @Column(name = "created")
    private Instant created;

    protected ResourceInstanceRecord() {
    }

    public ResourceInstanceRecord(ResourceInstanceRegistration<ID, RID> registration) {
        updateRecord(registration);
    }

    public void updateRecord(ResourceInstanceRegistration<ID, RID> registration) {
        Objects.requireNonNull(registration, "registration must not be null");
        var descriptor = registration.getDescriptor();
        this.resourceId = descriptor.getResourceId();
        this.version = descriptor.getVersion();
        this.clientUri = descriptor.getClientUri().toString();
        this.pingUri = descriptor.getPingUri().toString();
    }

    public ID getResourceId() {
        return resourceId;
    }

    public Version getVersion() {
        return version;
    }

    public URI getClientUri() {
        return URI.create(clientUri);
    }

    public URI getPingUri() {
        return URI.create(pingUri);
    }

    @Nullable
    public Instant getLastModified() {
        return lastModified;
    }

    @Nullable
    public Instant getCreated() {
        return created;
    }

    public abstract RID toDescriptor();
}
