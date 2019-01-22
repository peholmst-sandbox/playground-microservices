package net.pkhapps.playground.microservices.directory.client;

import net.pkhapps.playground.microservices.directory.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * Cache that maintains the status of all services and frontends in memory.
 */
class ServiceDirectoryCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDirectoryCache.class);
    private final ResourceCache<ServiceId, ServiceDescriptor, ServiceInstanceDescriptor, ServiceInstanceStatus, ServiceStatus> serviceCache = new ResourceCache<>();
    private final ResourceCache<FrontendId, FrontendDescriptor, FrontendInstanceDescriptor, FrontendInstanceStatus, FrontendStatus> frontendCache = new ResourceCache<>();

    static class ResourceCache<ID, RD extends ResourceDescriptor<ID>, RID extends ResourceInstanceDescriptor<ID>, RIS extends ResourceInstanceStatus<RID>, RS extends ResourceStatus<ID, RD, RID, RIS>> {

        private static Logger LOGGER = LoggerFactory.getLogger(ResourceCache.class);
        private final ConcurrentMap<ID, RS> statusMap = new ConcurrentHashMap<>();
        private final ConcurrentMap<URI, AtomicLong> clientUriCountMap = new ConcurrentHashMap<>();

        void updateStatus(Stream<RS> statusStream) {
            Set<ID> obsolete = new HashSet<>(statusMap.keySet());
            statusStream.forEach(status -> {
                var id = status.getDescriptor().getId();
                LOGGER.trace("Updating resource {}", id);
                statusMap.put(id, status);
                obsolete.remove(id);
            });
            LOGGER.trace("Removing obsolete resources: {}", obsolete);
            obsolete.forEach(statusMap::remove);
            clientUriCountMap.clear();
        }

        Stream<RD> getResources() {
            return statusMap.values().stream().map(ResourceStatus::getDescriptor);
        }

        Stream<RID> getInstances(ID resourceId) {
            var status = statusMap.get(resourceId);
            if (status == null) {
                return Stream.empty();
            } else {
                return status.getInstances().map(ResourceInstanceStatus::getDescriptor);
            }
        }

        Optional<RS> getStatus(ID resourceId) {
            return Optional.ofNullable(statusMap.get(resourceId));
        }

        Optional<URI> getClientUri(ID resourceId, Version version) {
            return getStatus(resourceId).flatMap(status -> getClientUri(version, status));
        }

        private Optional<URI> getClientUri(Version version, RS resourceStatus) {
            // TODO In the future, take the load into account as well.
            var uri = resourceStatus.getInstances().filter(status -> isUp(status) && hasVersion(version, status))
                    .min(Comparator.comparing(status -> getCount(status.getDescriptor().getClientUri())))
                    .map(status -> status.getDescriptor().getClientUri());
            uri.ifPresent(this::incCount);
            return uri;
        }

        private long getCount(URI clientUri) {
            return clientUriCountMap.computeIfAbsent(clientUri, key -> new AtomicLong()).get();
        }

        private void incCount(URI clientUri) {
            clientUriCountMap.computeIfAbsent(clientUri, key -> new AtomicLong()).incrementAndGet();
        }

        private static boolean isUp(ResourceInstanceStatus<?> status) {
            return status.getState() == ResourceInstanceState.UP;
        }

        private static boolean hasVersion(Version version, ResourceInstanceStatus<?> status) {
            return status.getDescriptor().getVersion().equals(version);
        }
    }

    void updateServiceStatus(Stream<ServiceStatus> serviceStatus) {
        LOGGER.info("Updating cache of services");
        serviceCache.updateStatus(serviceStatus);
    }

    void updateFrontendStatus(Stream<FrontendStatus> frontendStatus) {
        LOGGER.info("Updating cache of frontends");
        frontendCache.updateStatus(frontendStatus);
    }

    Stream<ServiceDescriptor> getServices() {
        return serviceCache.getResources();
    }

    Stream<FrontendDescriptor> getFrontends() {
        return frontendCache.getResources();
    }

    Stream<ServiceInstanceDescriptor> getInstances(ServiceId serviceId) {
        return serviceCache.getInstances(serviceId);
    }

    Stream<FrontendInstanceDescriptor> getInstances(FrontendId frontendId) {
        return frontendCache.getInstances(frontendId);
    }

    Optional<ServiceStatus> getStatus(ServiceId serviceId) {
        return serviceCache.getStatus(serviceId);
    }

    Optional<FrontendStatus> getStatus(FrontendId frontendId) {
        return frontendCache.getStatus(frontendId);
    }

    Optional<URI> getClientUri(ServiceId serviceId, Version version) {
        return serviceCache.getClientUri(serviceId, version);
    }

    Optional<URI> getClientUri(FrontendId frontendId, Version version) {
        return frontendCache.getClientUri(frontendId, version);
    }
}
