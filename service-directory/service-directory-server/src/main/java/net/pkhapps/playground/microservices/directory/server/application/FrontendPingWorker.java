package net.pkhapps.playground.microservices.directory.server.application;

import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.server.config.ServiceDirectoryServerProperties;
import net.pkhapps.playground.microservices.directory.server.domain.FrontendCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;

@Service
class FrontendPingWorker extends ResourcePingWorker<FrontendId, FrontendInstanceDescriptor> {

    FrontendPingWorker(@Qualifier("ping") ScheduledExecutorService executorService,
                       FrontendCache cache,
                       ServiceDirectoryServerProperties properties) {
        super(executorService, cache, properties.getPingInterval(), properties.getPingTimeout());
    }
}
