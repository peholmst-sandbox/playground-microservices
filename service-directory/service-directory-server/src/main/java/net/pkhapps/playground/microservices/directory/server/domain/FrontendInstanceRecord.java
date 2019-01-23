package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceRegistration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "frontend_instances", uniqueConstraints = @UniqueConstraint(columnNames = {"resource_id", "resource_version", "client_uri"}))
public class FrontendInstanceRecord extends ResourceInstanceRecord<FrontendId, FrontendInstanceDescriptor> {

    FrontendInstanceRecord() {
    }

    public FrontendInstanceRecord(FrontendInstanceRegistration registration) {
        super(registration);
    }

    @Override
    public FrontendInstanceDescriptor toDescriptor() {
        return new FrontendInstanceDescriptor(getResourceId(), getVersion(), getClientUri(), getPingUri());
    }
}
