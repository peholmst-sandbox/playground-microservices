package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.*;

import java.time.Clock;
import java.time.Instant;

public class FrontendInstanceCache extends ResourceInstanceCache<FrontendId, FrontendDescriptor, FrontendInstanceDescriptor, FrontendInstanceStatus> {

    public FrontendInstanceCache(FrontendDescriptor resourceDescriptor, Clock clock) {
        super(resourceDescriptor, clock);
    }

    @Override
    protected FrontendInstanceStatus createStatus(FrontendInstanceDescriptor instance, ResourceInstanceState state, Instant lastSeen) {
        return new FrontendInstanceStatus(instance, 0, lastSeen, state);
    }
}
