package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceRegistration;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.net.URI;

@Entity
@Table(name = "frontend_instances", uniqueConstraints = @UniqueConstraint(columnNames = {"resource_id", "client_uri"}))
public class FrontendInstanceRecord extends ResourceInstanceRecord<FrontendId, FrontendInstanceDescriptor> {

    @Column(name = "notifications_uri")
    @Nullable
    private String notificationsUri;

    FrontendInstanceRecord() {
    }

    public FrontendInstanceRecord(FrontendInstanceRegistration registration) {
        super(registration);
        notificationsUri = registration.getDescriptor().getNotificationsUri().map(URI::toString).orElse(null);
    }

    @Nullable
    public URI getNotificationsUri() {
        return notificationsUri == null ? null : URI.create(notificationsUri);
    }

    @Override
    public FrontendInstanceDescriptor toDescriptor() {
        return new FrontendInstanceDescriptor(getResourceId(), getClientUri(), getPingUri(), getNotificationsUri());
    }
}
