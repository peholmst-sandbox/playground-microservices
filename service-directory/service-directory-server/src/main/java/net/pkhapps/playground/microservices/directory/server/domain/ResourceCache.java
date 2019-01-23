package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.ResourceDescriptor;
import net.pkhapps.playground.microservices.directory.api.ResourceInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.api.ResourceInstanceStatus;
import net.pkhapps.playground.microservices.directory.api.ResourceStatus;

import java.time.Clock;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ResourceCache<ID, RD extends ResourceDescriptor<ID>, RID extends ResourceInstanceDescriptor<ID>,
        RIS extends ResourceInstanceStatus<RID>, RS extends ResourceStatus<ID, RD, RID, RIS>,
        C extends ResourceInstanceCache<ID, RD, RID, RIS>> {

    private final Clock clock;
    private final ConcurrentMap<ID, C> cacheMap = new ConcurrentHashMap<>();
    private final BiFunction<RD, Clock, C> cacheFactory;
    private final BiFunction<RD, Collection<RIS>, RS> statusFactory;

    protected ResourceCache(Clock clock,
                            BiFunction<RD, Clock, C> cacheFactory,
                            BiFunction<RD, Collection<RIS>, RS> statusFactory) {
        this.clock = clock;
        this.cacheFactory = cacheFactory;
        this.statusFactory = statusFactory;
    }

    public void addResource(RD resource) {
        cacheMap.computeIfAbsent(resource.getId(), id -> createCache(resource, clock)).setResourceDescriptor(resource);
    }

    public void removeResource(RD resource) {
        cacheMap.remove(resource.getId());
    }

    public void addInstance(RID instance) {
        getCache(instance.getId()).ifPresent(cache -> cache.addInstance(instance));
    }

    public void removeInstance(RID instance) {
        getCache(instance.getId()).ifPresent(cache -> cache.removeInstance(instance));
    }

    public void pingSucceeded(RID instance) {
        getCache(instance.getId()).ifPresent(cache -> cache.pingSucceeded(instance));
    }

    public void pingFailed(RID instance) {
        getCache(instance.getId()).ifPresent(cache -> cache.pingFailed(instance));
    }

    public Stream<RID> getInstances() {
        return cacheMap.values().stream().flatMap(ResourceInstanceCache::getInstances);
    }

    public Stream<RS> getStatus() {
        return cacheMap.values().stream().map(this::createStatus);
    }

    private RS createStatus(C cache) {
        return statusFactory.apply(cache.getResourceDescriptor(), cache.getStatus().collect(Collectors.toSet()));
    }

    private C createCache(RD resource, Clock clock) {
        return cacheFactory.apply(resource, clock);
    }

    private Optional<C> getCache(ID resourceId) {
        return Optional.ofNullable(cacheMap.get(resourceId));
    }
}
