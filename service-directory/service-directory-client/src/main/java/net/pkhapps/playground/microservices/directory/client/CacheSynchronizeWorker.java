package net.pkhapps.playground.microservices.directory.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TODO Document me!
 */
class CacheSynchronizeWorker implements DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheSynchronizeWorker.class);

    private final ServiceDirectoryClient client;
    private final ServiceDirectoryCache cache;
    private final ScheduledExecutorService executorService;

    CacheSynchronizeWorker(ServiceDirectoryClient client, ServiceDirectoryCache cache, Duration cacheSyncInterval) {
        this.client = client;
        this.cache = cache;
        LOGGER.info("Starting cache synchronization thread pool");
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(this::synchronizeCaches, 0, cacheSyncInterval.getSeconds(), TimeUnit.SECONDS);
    }

    private void synchronizeCaches() {
        var stopWatch = new StopWatch();
        stopWatch.start();
        synchronizeServiceCache();
        synchronizeFrontendCache();
        stopWatch.stop();
        LOGGER.debug("Caches synchronized in {} ms", stopWatch.getLastTaskTimeMillis());
    }

    private void synchronizeFrontendCache() {
        LOGGER.debug("Synchronizing frontend cache");
        try {
            cache.updateFrontendStatus(client.retrieveFrontendStatus());
        } catch (Exception ex) {
            LOGGER.error("Error reading frontend information from server", ex);
        }
    }

    private void synchronizeServiceCache() {
        LOGGER.debug("Synchronizing service cache");
        try {
            cache.updateServiceStatus(client.retrieveServiceStatus());
        } catch (Exception ex) {
            LOGGER.error("Error reading service information from server", ex);
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("Shutting down cache synchronization thread pool");
        executorService.shutdown();
    }
}
