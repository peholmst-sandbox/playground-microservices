package net.pkhapps.playground.microservices.directory.server.application;

import net.pkhapps.playground.microservices.directory.api.*;
import net.pkhapps.playground.microservices.directory.server.domain.FrontendCache;
import net.pkhapps.playground.microservices.directory.server.domain.FrontendRegistrationService;
import net.pkhapps.playground.microservices.directory.server.domain.ServiceCache;
import net.pkhapps.playground.microservices.directory.server.domain.ServiceRegistrationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceDirectoryService {

    private final ServiceCache serviceCache;
    private final ServiceRegistrationService serviceRegistrationService;
    private final FrontendCache frontendCache;
    private final FrontendRegistrationService frontendRegistrationService;

    public ServiceDirectoryService(ServiceCache serviceCache, ServiceRegistrationService serviceRegistrationService, FrontendCache frontendCache, FrontendRegistrationService frontendRegistrationService) {
        this.serviceCache = serviceCache;
        this.serviceRegistrationService = serviceRegistrationService;
        this.frontendCache = frontendCache;
        this.frontendRegistrationService = frontendRegistrationService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) // TODO security check
    public void registerService(ServiceRegistration registration) {
        serviceRegistrationService.registerResource(registration);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) // TODO security check
    public void registerFrontend(FrontendRegistration registration) {
        frontendRegistrationService.registerResource(registration);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerInstance(ServiceInstanceRegistration registration) {
        serviceRegistrationService.registerInstance(registration);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerInstance(FrontendInstanceRegistration registration) {
        frontendRegistrationService.registerInstance(registration);
    }

    public List<ServiceStatus> getServiceStatus() {
        return serviceCache.getStatus().collect(Collectors.toList());
    }

    public List<FrontendStatus> getFrontendStatus() {
        return frontendCache.getStatus().collect(Collectors.toList());
    }
}
