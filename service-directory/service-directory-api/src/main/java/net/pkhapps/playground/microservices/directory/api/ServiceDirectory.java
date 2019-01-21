package net.pkhapps.playground.microservices.directory.api;

import java.util.List;
import java.util.Optional;

/**
 * Interface for interacting with the service directory. This is used both by services that register themselves and
 * clients that need to look up services in order to interact with them.
 */
public interface ServiceDirectory {

    /**
     * Finds all registered services.
     */
    List<Service> findAll();

    /**
     * Finds the service with the given ID.
     *
     * @param id the service ID.
     * @return the service if found.
     */
    Optional<Service> findService(ServiceId id);

    /**
     * Finds the service instance with the given service ID and version.
     *
     * @param id      the service ID.
     * @param version the service instance version.
     * @return the service instance if found.
     */
    Optional<ServiceInstance> findInstance(ServiceId id, Version version);

    /**
     * Registers a service with the service directory. After this, new instances of the service can be registered
     * using {@link #registerInstance(ServiceInstanceRegistration)}. If a service with the same ID and public key has
     * already been registered, nothing happens. If the public key is different and the user has permission to register
     * new services, the old public key will be replaced with the new one. The existing instance registrations will
     * remain valid but any new instance registrations will be verified using the new public key.
     *
     * @param registration the service registration request.
     * @throws AccessDeniedException if the user does not have permission to register new services.
     */
    void registerService(ServiceRegistration registration);

    /**
     * Registers a new service instance with the service directory. The service must have already been registered using
     * {@link #registerService(ServiceRegistration)}. No extra permission is required for a service instance to register
     * itself but it must have a valid signature. If a service instance with the same ID and version already exists, it
     * will be replaced, provided that the signature is valid.
     *
     * @param registration the service instance registration request.
     * @throws NoSuchServiceException    if the service has not been registered.
     * @throws InvalidSignatureException if the signature of the registration is invalid.
     */
    void registerInstance(ServiceInstanceRegistration registration);
}
