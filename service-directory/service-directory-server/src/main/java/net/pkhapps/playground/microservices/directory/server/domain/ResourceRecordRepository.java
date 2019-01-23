package net.pkhapps.playground.microservices.directory.server.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ResourceRecordRepository<ID, T extends ResourceRecord<ID, ?>> extends JpaRepository<T, Long> {

    Optional<T> findByResourceId(ID resourceId);
}
