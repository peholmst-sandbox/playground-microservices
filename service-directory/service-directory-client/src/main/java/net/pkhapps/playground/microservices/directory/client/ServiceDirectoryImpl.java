package net.pkhapps.playground.microservices.directory.client;

import net.pkhapps.playground.microservices.directory.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * TODO Document me!
 */
class ServiceDirectoryImpl implements ServiceDirectory, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDirectoryImpl.class);

    private final ServiceDirectoryCache cache;
    private final ServiceDirectoryClient client;
    private final ScheduledExecutorService retryExecutorService;

    ServiceDirectoryImpl(ServiceDirectoryCache cache, ServiceDirectoryClient client) {
        this.cache = cache;
        this.client = client;
        LOGGER.info("Starting registration retry thread");
        retryExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public Stream<ServiceDescriptor> getServices() {
        return cache.getServices();
    }

    @Override
    public Stream<FrontendDescriptor> getFrontends() {
        return cache.getFrontends();
    }

    @Override
    public Stream<ServiceInstanceDescriptor> getInstances(ServiceId serviceId) {
        return cache.getInstances(serviceId);
    }

    @Override
    public Stream<FrontendInstanceDescriptor> getInstances(FrontendId frontendId) {
        return cache.getInstances(frontendId);
    }

    @Override
    public Optional<ServiceInstanceDescriptor> getInstance(ServiceId serviceId, Version version) {
        return cache.getInstance(serviceId, version);
    }

    @Override
    public Optional<FrontendInstanceDescriptor> getInstance(FrontendId frontendId, Version version) {
        return cache.getInstance(frontendId, version);
    }

    @Override
    public Optional<ServiceStatus> getStatus(ServiceId serviceId) {
        return cache.getStatus(serviceId);
    }

    @Override
    public Optional<FrontendStatus> getStatus(FrontendId frontendId) {
        return cache.getStatus(frontendId);
    }

    @Override
    public void registerService(ServiceRegistration registration) {
        scheduleRetryOnError(() -> client.registerService(registration));
    }

    @Override
    public void registerFrontend(FrontendRegistration registration) {
        scheduleRetryOnError(() -> client.registerFrontend(registration));
    }

    @Override
    public void registerInstance(ServiceInstanceRegistration registration) {
        scheduleRetryOnError(() -> client.registerInstance(registration));
    }

    @Override
    public void registerInstance(FrontendInstanceRegistration registration) {
        scheduleRetryOnError(() -> client.registerInstance(registration));
    }

    private void scheduleRetryOnError(Runnable operation) {
        try {
            operation.run();
        } catch (ServiceDirectoryException ex) {
            if (ex instanceof UnexpectedResponseException) {
                LOGGER.warn("Scheduling retry at a later time because of unexpected response", ex);
                scheduleRetry(operation);
            } else {
                throw ex;
            }
        } catch (Exception ex) {
            LOGGER.warn("Scheduling retry at a later time because of error", ex);
            scheduleRetry(operation);
        }
    }

    private void scheduleRetry(Runnable operation) {
        // TODO When we are using authentication, the security context must be transferred to the new thread.
        retryExecutorService.schedule(() -> scheduleRetryOnError(operation), 30, TimeUnit.SECONDS);
    }

    @Override
    public void destroy() {
        LOGGER.info("Shutting down registration retry thread");
        retryExecutorService.shutdown();
    }
}
