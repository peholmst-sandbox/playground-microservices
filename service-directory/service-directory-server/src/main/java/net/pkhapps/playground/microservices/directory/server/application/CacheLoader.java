package net.pkhapps.playground.microservices.directory.server.application;

import net.pkhapps.playground.microservices.directory.api.ResourceDescriptor;
import net.pkhapps.playground.microservices.directory.api.ResourceInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.server.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
class CacheLoader {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final FrontendCache frontendCache;
    private final ServiceCache serviceCache;
    private final FrontendRecordRepository frontendRecordRepository;
    private final ServiceRecordRepository serviceRecordRepository;
    private final FrontendInstanceRecordRepository frontendInstanceRecordRepository;
    private final ServiceInstanceRecordRepository serviceInstanceRecordRepository;

    CacheLoader(FrontendCache frontendCache,
                ServiceCache serviceCache,
                FrontendRecordRepository frontendRecordRepository,
                ServiceRecordRepository serviceRecordRepository,
                FrontendInstanceRecordRepository frontendInstanceRecordRepository,
                ServiceInstanceRecordRepository serviceInstanceRecordRepository) {
        this.frontendCache = frontendCache;
        this.serviceCache = serviceCache;
        this.frontendRecordRepository = frontendRecordRepository;
        this.serviceRecordRepository = serviceRecordRepository;
        this.frontendInstanceRecordRepository = frontendInstanceRecordRepository;
        this.serviceInstanceRecordRepository = serviceInstanceRecordRepository;

        loadServiceCache();
        loadFrontendCache();
    }

    private void loadFrontendCache() {
        logger.info("Loading frontend cache from database");
        loadCache(frontendRecordRepository, frontendInstanceRecordRepository, frontendCache);
    }

    private void loadServiceCache() {
        logger.info("Loading service cache from database");
        loadCache(serviceRecordRepository, serviceInstanceRecordRepository, serviceCache);
    }

    private <ID,
            RD extends ResourceDescriptor<ID>,
            R extends ResourceRecord<ID, RD>,
            RID extends ResourceInstanceDescriptor<ID>,
            RI extends ResourceInstanceRecord<ID, RID>>
    void loadCache(ResourceRecordRepository<ID, R> resourceRecordRepository,
                   ResourceInstanceRecordRepository<ID, RI> resourceInstanceRecordRepository,
                   ResourceCache<ID, RD, RID, ?, ?, ?> cache) {
        var stopWatch = new StopWatch();
        stopWatch.start();
        resourceRecordRepository.findAll().stream()
                .map(ResourceRecord::toDescriptor)
                .forEach(cache::addResource);
        resourceInstanceRecordRepository.findAll().stream()
                .map(ResourceInstanceRecord::toDescriptor)
                .forEach(cache::addInstance);
        stopWatch.stop();
        logger.info("Cache loaded in {} ms", stopWatch.getLastTaskTimeMillis());
    }
}
