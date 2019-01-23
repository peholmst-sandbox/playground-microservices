package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.*;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class FrontendCache extends ResourceCache<FrontendId, FrontendDescriptor, FrontendInstanceDescriptor, FrontendInstanceStatus, FrontendStatus, FrontendInstanceCache> {

    public FrontendCache(Clock clock) {
        super(clock, FrontendInstanceCache::new, FrontendStatus::new);
    }
}
