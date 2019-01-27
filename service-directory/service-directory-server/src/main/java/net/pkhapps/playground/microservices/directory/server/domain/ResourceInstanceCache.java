package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final ConcurrentMap<InstanceId, InstanceCacheEntry> cache = new ConcurrentHashMap<>();

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
        cache.put(new InstanceId(instance), new InstanceCacheEntry(instance));
    }

    public void removeInstance(RID instance) {
        cache.remove(new InstanceId(instance));
    }

    public void clear() {
        cache.clear();
    }

    public void pingSucceeded(RID instance) {
        getCacheEntry(instance).ifPresent(InstanceCacheEntry::pingSucceeded);
    }

    public void pingFailed(RID instance) {
        getCacheEntry(instance).ifPresent(InstanceCacheEntry::pingFailed);
    }

    private Optional<InstanceCacheEntry> getCacheEntry(RID instance) {
        return Optional.ofNullable(cache.get(new InstanceId(instance)));
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

    private class InstanceId {

        private final ID resourceId;
        private final Version version;
        private final URI clientUri;

        InstanceId(RID instance) {
            Objects.requireNonNull(instance, "instance must not be null");
            this.resourceId = instance.getResourceId();
            this.version = instance.getVersion();
            this.clientUri = instance.getClientUri();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InstanceId that = (InstanceId) o;
            return resourceId.equals(that.resourceId) &&
                    version.equals(that.version) &&
                    clientUri.equals(that.clientUri);
        }

        @Override
        public int hashCode() {
            return Objects.hash(resourceId, version, clientUri);
        }
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
                    logger.info("Instance with id: [{}], version: [{}], clientUri: [{}] is {}", instance.getResourceId(), instance.getVersion(), instance.getClientUri(), state);
                }
                status = createStatus(instance, state, lastSeen);
            }
        }
    }
}
