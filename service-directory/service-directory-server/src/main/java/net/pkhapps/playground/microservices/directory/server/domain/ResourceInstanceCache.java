package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

public abstract class ResourceInstanceCache<ID, RD extends ResourceDescriptor<ID>, RID extends ResourceInstanceDescriptor<ID>, RIS extends ResourceInstanceStatus<RID>> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Clock clock;
    private final AtomicReference<RD> resourceDescriptor = new AtomicReference<>();
    private final ConcurrentMap<ResourceInstanceId<ID>, InstanceCacheEntry> cache = new ConcurrentHashMap<>();

    public ResourceInstanceCache(RD resourceDescriptor, Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
        setResourceDescriptor(resourceDescriptor);
    }

    public final void setResourceDescriptor(RD resourceDescriptor) {
        this.resourceDescriptor.set(Objects.requireNonNull(resourceDescriptor, "resourceDescriptor must not be null"));
    }

    public final RD getResourceDescriptor() {
        return resourceDescriptor.get();
    }

    public void addInstance(RID instance) {
        logger.debug("Adding instance {} to cache", instance);
        cache.put(new ResourceInstanceId<>(instance), new InstanceCacheEntry(instance));
    }

    public void removeInstance(RID instance) {
        logger.debug("Removing instance {} from cache", instance);
        cache.remove(new ResourceInstanceId<>(instance));
    }

    public void clear() {
        cache.clear();
    }

    public void pingSucceeded(RID instance) {
        logger.trace("Ping of instance {} succeeded", instance);
        getCacheEntry(instance).ifPresent(InstanceCacheEntry::pingSucceeded);
    }

    public void pingFailed(RID instance) {
        logger.trace("Ping of instance {} failed");
        getCacheEntry(instance).ifPresent(InstanceCacheEntry::pingFailed);
    }

    private Optional<InstanceCacheEntry> getCacheEntry(RID instance) {
        return Optional.ofNullable(cache.get(new ResourceInstanceId<>(instance)));
    }

    public Stream<RID> getInstances() {
        return cache.values().stream().map(InstanceCacheEntry::getInstance);
    }

    public Stream<RIS> getStatus() {
        return cache.values().stream().map(InstanceCacheEntry::getStatus);
    }

    public int size() {
        return cache.size();
    }

    protected abstract RIS createStatus(RID instance, ResourceInstanceState state, Instant lastSeen);

    private class InstanceCacheEntry {

        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private final RID instance;
        private ResourceInstanceState state;
        private Instant lastSeen;
        private int failedPings;
        private RIS status;

        InstanceCacheEntry(RID instance) {
            this.instance = Objects.requireNonNull(instance, "instance must not be null");
            pingSucceeded();
        }

        RID getInstance() {
            return instance;
        }

        RIS getStatus() {
            lock.readLock().lock();
            try {
                return status;
            } finally {
                lock.readLock().unlock();
            }
        }

        void pingFailed() {
            lock.writeLock().lock();
            try {
                failedPings++;
                if (failedPings < 3) {
                    state = ResourceInstanceState.INDETERMINATE;
                } else {
                    state = ResourceInstanceState.DOWN;
                }
                recreateStatus();
            } finally {
                lock.writeLock().unlock();
            }
        }

        void pingSucceeded() {
            lock.writeLock().lock();
            try {
                failedPings = 0;
                lastSeen = clock.instant();
                state = ResourceInstanceState.UP;
                recreateStatus();
            } finally {
                lock.writeLock().unlock();
            }
        }

        private void recreateStatus() {
            if (status == null || status.getState() != state || !status.getLastSeen().equals(lastSeen)) {
                if (status == null || status.getState() != state) {
                    logger.info("Instance with id: [{}], clientUri: [{}] is {}", instance.getResourceId(),
                            instance.getClientUri(), state);
                }
                status = createStatus(instance, state, lastSeen);
            }
        }
    }
}
