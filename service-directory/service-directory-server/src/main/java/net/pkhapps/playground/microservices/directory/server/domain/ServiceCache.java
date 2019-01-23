package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.*;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class ServiceCache extends ResourceCache<ServiceId, ServiceDescriptor, ServiceInstanceDescriptor, ServiceInstanceStatus, ServiceStatus, ServiceInstanceCache> {

    public ServiceCache(Clock clock) {
        super(clock, ServiceInstanceCache::new, ServiceStatus::new);
    }
}
