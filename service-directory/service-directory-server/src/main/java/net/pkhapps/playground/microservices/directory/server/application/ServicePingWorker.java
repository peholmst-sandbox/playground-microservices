package net.pkhapps.playground.microservices.directory.server.application;

import net.pkhapps.playground.microservices.directory.api.ServiceId;
import net.pkhapps.playground.microservices.directory.api.ServiceInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.server.config.ServiceDirectoryServerProperties;
import net.pkhapps.playground.microservices.directory.server.domain.ServiceCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;

@Service
class ServicePingWorker extends ResourcePingWorker<ServiceId, ServiceInstanceDescriptor> {

    ServicePingWorker(@Qualifier("ping") ScheduledExecutorService executorService,
                      ServiceCache cache,
                      ServiceDirectoryServerProperties properties) {
        super(executorService, cache, properties.getPingInterval(), properties.getPingTimeout());
    }
}
