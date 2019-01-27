package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.Optional;

/**
 * A frontend instance descriptor describes a particular instance of a {@link FrontendDescriptor frontend} that
 * has been registered with the service directory and that users can interact with.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public final class FrontendInstanceDescriptor extends ResourceInstanceDescriptor<FrontendId> {

    @JsonProperty
    @Nullable
    private final URI notificationsUri;

    /**
     * Creates a new frontend instance descriptor.
     *
     * @param id               the ID of the frontend.
     * @param clientUri        the URI that clients use to access this instance.
     * @param pingUri          the URI that the service directory pings to check the health of this instance.
     * @param notificationsUri the optional URI that the portal can use to retrieve notifications from the frontend
     *                         without it having a session active.
     */
    @JsonCreator
    public FrontendInstanceDescriptor(@JsonProperty(value = "resourceId", required = true) FrontendId id,
                                      @JsonProperty(value = "clientUri", required = true) URI clientUri,
                                      @JsonProperty(value = "pingUri", required = true) URI pingUri,
                                      @JsonProperty(value = "notificationsUri") @Nullable URI notificationsUri) {
        super(id, clientUri, pingUri);
        this.notificationsUri = notificationsUri;
    }

    /**
     * Returns the URI that the portal can use to retrieve notifications from the frontend without it having a session
     * active. What exactly this URI should return depends on the portal implementation.
     */
    @JsonIgnore
    public Optional<URI> getNotificationsUri() {
        return Optional.ofNullable(notificationsUri);
    }
}
