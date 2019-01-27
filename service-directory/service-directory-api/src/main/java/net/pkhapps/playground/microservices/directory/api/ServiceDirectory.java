package net.pkhapps.playground.microservices.directory.api;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface for interacting with the service directory. This is used both by services and frontends that register
 * themselves and by clients that need to look up services or frontends in order to interact with them.
 */
public interface ServiceDirectory {

    /**
     * Returns all registered services.
     */
    Stream<ServiceDescriptor> getServices();

    /**
     * Returns all registered frontends.
     */
    Stream<FrontendDescriptor> getFrontends();

    /**
     * Returns the descriptor for the specified service ID.
     *
     * @param serviceId the ID of the service.
     */
    default Optional<ServiceDescriptor> getService(ServiceId serviceId) {
        Objects.requireNonNull(serviceId, "serviceId must not be null");
        return getServices().filter(d -> d.getId().equals(serviceId)).findAny();
    }

    /**
     * Returns the descriptor for the specified frontend ID.
     *
     * @param frontendId the ID of the frontend.
     */
    default Optional<FrontendDescriptor> getFrontend(FrontendId frontendId) {
        Objects.requireNonNull(frontendId, "frontendId must not be null");
        return getFrontends().filter(d -> d.getId().equals(frontendId)).findAny();
    }

    /**
     * Returns all registered service instances of the specified service.
     *
     * @param serviceId the ID of the service.
     */
    Stream<ServiceInstanceDescriptor> getInstances(ServiceId serviceId);

    /**
     * Returns all registered frontend instances of the specified frontend.
     *
     * @param frontendId the ID of the frontend.
     */
    Stream<FrontendInstanceDescriptor> getInstances(FrontendId frontendId);

    /**
     * Returns the descriptor of some instance of the specified service. This method is intended to be used by clients
     * that want to interact with the service. The implementation may decide which instance to return if there are more
     * than one to choose from.
     *
     * @param serviceId the ID of the service.
     */
    Optional<ServiceInstanceDescriptor> getInstance(ServiceId serviceId);

    /**
     * Returns the descriptor of some instance of the specified frontend. This method is intended to be used by clients
     * that want to interact with the frontend. The implementation may decide which instance to return if there are more
     * than one to choose from.
     *
     * @param frontendId the ID of the frontend.
     */
    Optional<FrontendInstanceDescriptor> getInstance(FrontendId frontendId);

    /**
     * Returns the client URI of some instance of the specified service. This method is intended to be used by clients
     * that want to interact with the service. The implementation may decide which instance to return if there are more
     * than one to choose from.
     *
     * @param serviceId the ID of the service.
     */
    default Optional<URI> getClientUri(ServiceId serviceId) {
        return getInstance(serviceId).map(ServiceInstanceDescriptor::getClientUri);
    }

    /**
     * Returns the client URI of some instance of the specified frontend. This method is intended to be used by clients
     * that want to interact with the frontend. The implementation may decide which instance to return if there are more
     * than one to choose from.
     *
     * @param frontendId the ID of the frontend.
     */
    default Optional<URI> getClientUri(FrontendId frontendId) {
        return getInstance(frontendId).map(FrontendInstanceDescriptor::getClientUri);
    }

    /**
     * Returns the status of the specified service.
     *
     * @param serviceId the ID of the service.
     */
    Optional<ServiceStatus> getStatus(ServiceId serviceId);

    /**
     * Returns the status of the specified frontend.
     *
     * @param frontendId the ID of the frontend.
     */
    Optional<FrontendStatus> getStatus(FrontendId frontendId);

    /**
     * Registers a service with the service directory. After this, new instances of the service can be registered
     * using {@link #registerInstance(ServiceInstanceRegistration)}. If a service with the same ID has already been
     * registered, its record will be updated with data from this registration.
     *
     * @param registration the service registration request.
     * @throws AccessDeniedException if the user does not have permission to register new services.
     */
    void registerService(ServiceRegistration registration);

    /**
     * Registers a frontend with the service directory. After this, new instances of the frontend can be registered
     * using {@link #registerInstance(FrontendInstanceRegistration)}. If a frontend with the same ID has already been
     * registered, its record will be updated with data from this registration.
     *
     * @param registration the frontend registration request.
     * @throws AccessDeniedException if the user does not have permission to register new frontends.
     */
    void registerFrontend(FrontendRegistration registration);

    /**
     * Registers a new service instance with the service directory. The service must have already been registered using
     * {@link #registerService(ServiceRegistration)}. No extra permission is required for a service instance to register
     * itself but it must have a valid signature. If a service instance  with the same service ID, version and
     * client URI has already been registered, its record will be updated with data from this registration.
     *
     * @param registration the service instance registration request.
     * @throws NoSuchResourceException   if the service has not been registered.
     * @throws InvalidSignatureException if the signature of the registration is invalid.
     */
    void registerInstance(ServiceInstanceRegistration registration);

    /**
     * Registers a new frontend instance with the service directory. The frontend must have already been registered
     * using {@link #registerFrontend(FrontendRegistration)}. No extra permission is required for a frontend instance to
     * register itself but it must have a valid signature. If a frontend instance with the same frontend ID, version and
     * client URI has already been registered, its record will be updated with data from this registration.
     *
     * @param registration the frontend instance registration request.
     * @throws NoSuchResourceException   if the frontend has not been registered.
     * @throws InvalidSignatureException if the signature of the registration is invalid.
     */
    void registerInstance(FrontendInstanceRegistration registration);
}
