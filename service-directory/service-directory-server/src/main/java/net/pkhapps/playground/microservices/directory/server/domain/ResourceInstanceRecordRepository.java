package net.pkhapps.playground.microservices.directory.server.domain;

import net.pkhapps.playground.microservices.directory.api.ResourceInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.api.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ResourceInstanceRecordRepository<ID, T extends ResourceInstanceRecord<ID, ?>> extends JpaRepository<T, Long> {

    Optional<T> findByResourceIdAndVersionAndClientUri(ID resourceId, Version version, String clientUri);

    default <D extends ResourceInstanceDescriptor<ID>> Optional<T> findByDescriptor(D descriptor) {
        return findByResourceIdAndVersionAndClientUri(descriptor.getResourceId(), descriptor.getVersion(), descriptor.getClientUri().toString());
    }
}
