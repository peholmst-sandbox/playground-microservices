package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.*;

import java.time.Clock;
import java.time.Instant;

public class ServiceInstanceCache extends ResourceInstanceCache<ServiceId, ServiceDescriptor, ServiceInstanceDescriptor, ServiceInstanceStatus> {

    public ServiceInstanceCache(ServiceDescriptor resourceDescriptor, Clock clock) {
        super(resourceDescriptor, clock);
    }

    @Override
    protected ServiceInstanceStatus createStatus(ServiceInstanceDescriptor instance, ResourceInstanceState state, Instant lastSeen) {
        return new ServiceInstanceStatus(instance, 0, lastSeen, state);
    }
}
