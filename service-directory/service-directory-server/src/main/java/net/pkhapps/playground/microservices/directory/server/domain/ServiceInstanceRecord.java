package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.ServiceId;
import net.pkhapps.playground.microservices.directory.api.ServiceInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.api.ServiceInstanceRegistration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "service_instances", uniqueConstraints = @UniqueConstraint(columnNames = {"resource_id", "client_uri"}))
public class ServiceInstanceRecord extends ResourceInstanceRecord<ServiceId, ServiceInstanceDescriptor> {

    ServiceInstanceRecord() {
    }

    public ServiceInstanceRecord(ServiceInstanceRegistration registration) {
        super(registration);
    }

    @Override
    public ServiceInstanceDescriptor toDescriptor() {
        return new ServiceInstanceDescriptor(getResourceId(), getClientUri(), getPingUri());
    }
}
