package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;
import java.util.Optional;
import java.util.function.Function;

public abstract class ResourceRegistrationService<ID,
        R extends ResourceRecord<ID, RD>,
        RI extends ResourceInstanceRecord<ID, RID>,
        RD extends ResourceDescriptor<ID>,
        RID extends ResourceInstanceDescriptor<ID>,
        RR extends ResourceRegistration<ID, RD>,
        RIR extends ResourceInstanceRegistration<ID, RID>> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ResourceRecordRepository<ID, R> resourceRecordRepository;
    private final ResourceInstanceRecordRepository<ID, RI> instanceRecordRepository;
    private final ResourceCache<ID, RD, RID, ?, ?, ?> cache;
    private final Function<RR, R> resourceRecordFactory;
    private final Function<RIR, RI> instanceRecordFactory;

    protected ResourceRegistrationService(ResourceRecordRepository<ID, R> resourceRecordRepository,
                                          ResourceInstanceRecordRepository<ID, RI> instanceRecordRepository,
                                          ResourceCache<ID, RD, RID, ?, ?, ?> cache,
                                          Function<RR, R> resourceRecordFactory, Function<RIR, RI> instanceRecordFactory) {
        this.resourceRecordRepository = resourceRecordRepository;
        this.instanceRecordRepository = instanceRecordRepository;
        this.cache = cache;
        this.resourceRecordFactory = resourceRecordFactory;
        this.instanceRecordFactory = instanceRecordFactory;
    }

    public void registerResource(RR registration) {
        var record = resourceRecordRepository.findByResourceId(registration.getDescriptor().getId())
                .orElseGet(() -> resourceRecordFactory.apply(registration));
        if (!record.isNew()) {
            logger.info("Updating existing resource {}", registration.getDescriptor());
            record.updateRecord(registration);
        } else {
            logger.info("Registering new resource {}", registration.getDescriptor());
        }
        resourceRecordRepository.saveAndFlush(record);
        cache.addResource(registration.getDescriptor());
    }

    public void registerInstance(RIR registration) {
        var publicKey = getPublicKey(registration.getDescriptor().getId())
                .orElseThrow(() -> new NoSuchResourceException("Resource with ID " + registration.getDescriptor().getId() + " does not exist"));
        if (!registration.verifySignature(publicKey)) {
            logger.warn("Invalid signature for instance registration {}", registration.getDescriptor());
            throw new InvalidSignatureException("Signature of instance registration request was not valid");
        }

        var record = instanceRecordRepository.findByDescriptor(registration.getDescriptor())
                .orElseGet(() -> instanceRecordFactory.apply(registration));
        if (!record.isNew()) {
            logger.info("Updating existing instance {}", registration.getDescriptor());
            record.updateRecord(registration);
        } else {
            logger.info("Registering new instance {}", registration.getDescriptor());
        }
        instanceRecordRepository.save(record);
        cache.addInstance(registration.getDescriptor());
    }

    private Optional<PublicKey> getPublicKey(ID resourceId) {
        return resourceRecordRepository.findByResourceId(resourceId).map(ResourceRecord::getPublicKey);
    }
}
