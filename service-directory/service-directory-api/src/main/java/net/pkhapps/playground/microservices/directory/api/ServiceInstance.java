package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.net.URI;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Value object representing a registered instance of a registered {@link Service}. Clients can use any of the provided
 * URIs to interact with the service instance.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson (the {@code JavaTimeModule} must be
 * registered with the object mapper).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ServiceInstance implements Serializable {

    @JsonProperty
    private final ServiceId id;
    @JsonProperty
    private final Version version;
    @JsonProperty
    private final String name;
    @JsonProperty
    private final String description;
    @JsonProperty
    private final URI serviceUri;
    @JsonProperty
    private final URI pingUri;
    @JsonProperty
    private final URI documentationUri;
    @JsonProperty
    private final Instant lastSeen;

    /**
     * Creates a new {@link ServiceInstanceRegistration}.
     *
     * @param id               the ID of the service.
     * @param version          the version of the service instance.
     * @param name             the name of the service instance.
     * @param description      an optional description of the service instance.
     * @param serviceUri       the URI of the service instance.
     * @param pingUri          the URI where the service can be pinged to check whether it is still online.
     * @param documentationUri an optional URI containing the documentation of the service.
     * @param lastSeen         the date and time at which the service instance was last seen by the service directory.
     */
    @JsonCreator
    public ServiceInstance(@JsonProperty(value = "id", required = true) ServiceId id,
                           @JsonProperty(value = "version", required = true) Version version,
                           @JsonProperty(value = "name", required = true) String name,
                           @JsonProperty(value = "description") @Nullable String description,
                           @JsonProperty(value = "serviceUri", required = true) URI serviceUri,
                           @JsonProperty(value = "pingUri", required = true) URI pingUri,
                           @JsonProperty(value = "documentationUri") @Nullable URI documentationUri,
                           @JsonProperty(value = "lastSeen") Instant lastSeen) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.version = Objects.requireNonNull(version, "version must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.description = description;
        this.serviceUri = Objects.requireNonNull(serviceUri, "serviceUri must not be null");
        this.pingUri = Objects.requireNonNull(pingUri, "pingUri must not be null");
        this.documentationUri = documentationUri;
        this.lastSeen = Objects.requireNonNull(lastSeen, "lastSeen must not be null");
    }

    /**
     * Returns the ID of the service.
     */
    public ServiceId getId() {
        return id;
    }

    /**
     * Returns the version of the service instance.
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Returns the name of the service instance, to be shown to users.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an optional description of the service instance, to be shown to users.
     */
    @JsonIgnore
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    /**
     * Returns the URI where the service instance can be accessed by clients.
     */
    public URI getServiceUri() {
        return serviceUri;
    }

    /**
     * Returns the URI where the service instance can be pinged by the service directory server to check whether it is
     * still online. This endpoint should respond with a 200 or 204 status code if everything is fine. Depending on the
     * service directory server implementation, additional content can also be included in the response.
     */
    public URI getPingUri() {
        return pingUri;
    }

    /**
     * Returns an optional URI where the documentation of the service instance can be found. This is intended for new
     * clients that wish to utilize this service instance.
     */
    @JsonIgnore
    public Optional<URI> getDocumentationUri() {
        return Optional.ofNullable(documentationUri);
    }

    /**
     * Returns the date and time at which the service instance was last seen by the service directory.
     */
    public Instant getLastSeen() {
        return lastSeen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceInstance that = (ServiceInstance) o;
        return id.equals(that.id) &&
                version.equals(that.version) &&
                name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                serviceUri.equals(that.serviceUri) &&
                pingUri.equals(that.pingUri) &&
                Objects.equals(documentationUri, that.documentationUri) &&
                lastSeen.equals(that.lastSeen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, name, description, serviceUri, pingUri, documentationUri, lastSeen);
    }
}
