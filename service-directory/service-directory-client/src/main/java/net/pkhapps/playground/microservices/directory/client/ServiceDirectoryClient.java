package net.pkhapps.playground.microservices.directory.client;

import net.pkhapps.playground.microservices.directory.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * TODO Implement me
 */
class ServiceDirectoryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDirectoryClient.class);

    private final RestTemplate restTemplate;
    private final URI uri;

    ServiceDirectoryClient(URI uri, Duration connectTimeout, Duration readTimeout) {
        LOGGER.info("Using service directory at {} with a connection timeout of {} ms and a read timeout of {} ms",
                uri, connectTimeout.toMillis(), readTimeout.toMillis());
        this.uri = uri;
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) connectTimeout.toMillis());
        requestFactory.setReadTimeout((int) readTimeout.toMillis());
        restTemplate = new RestTemplate(requestFactory);
    }

    private UriComponentsBuilder uriBuilder() {
        return UriComponentsBuilder.fromUri(uri).path("/api/v1");
    }

    Stream<ServiceStatus> retrieveServiceStatus() {
        LOGGER.info("Retrieving service status");
        return retrieveStatus("/service/status");
    }

    Stream<FrontendStatus> retrieveFrontendStatus() {
        LOGGER.info("Retrieving frontend status");
        return retrieveStatus("/frontend/status");
    }

    private <RS extends ResourceStatus<?, ?, ?, ?>> Stream<RS> retrieveStatus(String path) {
        try {
            var result = restTemplate.exchange(uriBuilder().path(path).build().toUri(), HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<RS>>() {
                    });
            switch (result.getStatusCode()) {
                case OK:
                    return Optional.ofNullable(result.getBody()).stream().flatMap(Collection::stream);
                case NO_CONTENT:
                    return Stream.empty();
                default:
                    throw new UnexpectedResponseException(result.getStatusCode());
            }
        } catch (HttpStatusCodeException ex) {
            throw new UnexpectedResponseException(ex.getStatusCode());
        }
    }

    void registerService(ServiceRegistration registration) {
        Objects.requireNonNull(registration, "registration must not be null");
        registerResource(registration, "/service", "service");
    }

    void registerFrontend(FrontendRegistration registration) {
        Objects.requireNonNull(registration, "registration must not be null");
        registerResource(registration, "/frontend", "frontend");
    }

    private void registerResource(ResourceRegistration<?> registration, String path, String resourceTypeName) {
        LOGGER.info("Registering {} [{}]", resourceTypeName, registration.getId());
        var id = registration.getId();
        var uri = uriBuilder().path(path).build(id);
        var entity = new HttpEntity<>(registration);
        // TODO Add authentication header to entity
        try {
            var result = restTemplate.postForEntity(uri, entity, Void.class);
            switch (result.getStatusCode()) {
                case OK:
                case NO_CONTENT:
                case CREATED:
                    return;
                default:
                    throw new UnexpectedResponseException(result.getStatusCode());
            }
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.FORBIDDEN || ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AccessDeniedException(String.format("Access to register a %s was denied", resourceTypeName));
            } else {
                throw new UnexpectedResponseException(ex.getStatusCode());
            }
        }
    }

    void registerInstance(ServiceInstanceRegistration registration) {
        Objects.requireNonNull(registration, "registration must not be null");
        registerInstance(registration, "/service/byId/{id}/instances", "service");
    }

    void registerInstance(FrontendInstanceRegistration registration) {
        Objects.requireNonNull(registration, "registration must not be null");
        registerInstance(registration, "/frontend/byId/{id}/instances", "frontend");
    }

    private void registerInstance(ResourceInstanceRegistration<?> registration, String path, String resourceTypeName) {
        LOGGER.info("Registering {} instance of [{}] with version [{}] and client URI [{}]", resourceTypeName, registration.getId(), registration.getVersion(), registration.getClientUri());
        var id = registration.getId();
        var uri = uriBuilder().path(path).build(id);
        try {
            var result = restTemplate.postForEntity(uri, registration, Void.class);
            switch (result.getStatusCode()) {
                case OK:
                case NO_CONTENT:
                case CREATED:
                    return;
                default:
                    throw new UnexpectedResponseException(result.getStatusCode());
            }
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NoSuchResourceException(String.format("A %s with ID %s was not found on the server", resourceTypeName, id));
            } else if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new InvalidSignatureException(String.format("The %s instance registration signature was not accepted by the server", resourceTypeName));
            } else {
                throw new UnexpectedResponseException(ex.getStatusCode());
            }
        }
    }
}
