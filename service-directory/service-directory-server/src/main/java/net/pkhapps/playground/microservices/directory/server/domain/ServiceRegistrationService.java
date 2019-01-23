package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.*;
import org.springframework.stereotype.Service;

@Service
public class ServiceRegistrationService extends ResourceRegistrationService<
        ServiceId,
        ServiceRecord,
        ServiceInstanceRecord,
        ServiceDescriptor,
        ServiceInstanceDescriptor,
        ServiceRegistration,
        ServiceInstanceRegistration> {

    public ServiceRegistrationService(ServiceRecordRepository serviceRecordRepository,
                                      ServiceInstanceRecordRepository serviceInstanceRecordRepository,
                                      ServiceCache serviceCache) {
        super(serviceRecordRepository, serviceInstanceRecordRepository, serviceCache,
                ServiceRecord::new, ServiceInstanceRecord::new);
    }
}
