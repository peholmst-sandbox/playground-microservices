package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.ServiceDescriptor;
import net.pkhapps.playground.microservices.directory.api.ServiceId;
import net.pkhapps.playground.microservices.directory.api.ServiceRegistration;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "services")
public class ServiceRecord extends ResourceRecord<ServiceId, ServiceDescriptor> {

    ServiceRecord() {
    }

    public ServiceRecord(ServiceRegistration registration) {
        super(registration);
    }

    @Override
    public ServiceDescriptor toDescriptor() {
        return new ServiceDescriptor(getResourceId(), getName(), getDescription(), getIconUri(), getDocumentationUri());
    }
}
