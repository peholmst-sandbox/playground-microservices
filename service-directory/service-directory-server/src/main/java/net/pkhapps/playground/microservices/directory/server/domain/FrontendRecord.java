package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.FrontendDescriptor;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.FrontendRegistration;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "frontends")
public class FrontendRecord extends ResourceRecord<FrontendId, FrontendDescriptor> {

    FrontendRecord() {
    }

    public FrontendRecord(FrontendRegistration registration) {
        super(registration);
    }

    @Override
    public FrontendDescriptor toDescriptor() {
        return new FrontendDescriptor(getResourceId(), getName(), getDescription(), getIconUri());
    }
}
