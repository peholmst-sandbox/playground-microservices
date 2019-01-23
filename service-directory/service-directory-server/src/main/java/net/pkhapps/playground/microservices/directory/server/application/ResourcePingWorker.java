package net.pkhapps.playground.microservices.directory.server.application;

import net.pkhapps.playground.microservices.directory.api.ResourceInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.server.domain.ResourceCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

abstract class ResourcePingWorker<ID, RID extends ResourceInstanceDescriptor<ID>> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ScheduledExecutorService executorService;
    private final RestTemplate restTemplate;
    private final ResourceCache<ID, ?, RID, ?, ?, ?> cache;

    protected ResourcePingWorker(ScheduledExecutorService executorService, ResourceCache<ID, ?, RID, ?, ?, ?> cache,
                                 Duration pingInterval, Duration pingTimeout) {
        this.executorService = executorService;
        this.cache = cache;

        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) pingTimeout.toMillis());
        requestFactory.setReadTimeout((int) pingTimeout.toMillis());
        restTemplate = new RestTemplate(requestFactory);

        logger.info("Pinging resource instances with an interval of {} s and a timeout of {} ms",
                pingInterval.toSeconds(), pingTimeout.toMillis());
        executorService.scheduleWithFixedDelay(this::pingAllInstances, 0, pingInterval.getSeconds(), TimeUnit.SECONDS);
    }

    private void pingAllInstances() {
        cache.getInstances().forEach(instance -> executorService.submit(() -> ping(instance)));
    }

    private void ping(RID instance) {
        logger.trace("Pinging {}", instance.getPingUri());
        try {
            if (restTemplate.getForEntity(instance.getPingUri(), Void.class).getStatusCode().is2xxSuccessful()) {
                cache.pingSucceeded(instance);
            } else {
                cache.pingFailed(instance);
            }
        } catch (Exception ex) {
            logger.trace("Pinging " + instance.getPingUri() + " failed with exception", ex);
            cache.pingFailed(instance);
        }
    }
}
