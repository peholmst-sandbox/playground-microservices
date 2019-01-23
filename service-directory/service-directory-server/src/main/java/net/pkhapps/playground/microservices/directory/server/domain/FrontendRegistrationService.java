package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.*;
import org.springframework.stereotype.Service;

@Service
public class FrontendRegistrationService extends ResourceRegistrationService<
        FrontendId,
        FrontendRecord,
        FrontendInstanceRecord,
        FrontendDescriptor,
        FrontendInstanceDescriptor,
        FrontendRegistration,
        FrontendInstanceRegistration> {

    public FrontendRegistrationService(FrontendRecordRepository frontendRecordRepository,
                                       FrontendInstanceRecordRepository frontendInstanceRecordRepository,
                                       FrontendCache frontendCache) {
        super(frontendRecordRepository, frontendInstanceRecordRepository, frontendCache,
                FrontendRecord::new, FrontendInstanceRecord::new);
    }
}
