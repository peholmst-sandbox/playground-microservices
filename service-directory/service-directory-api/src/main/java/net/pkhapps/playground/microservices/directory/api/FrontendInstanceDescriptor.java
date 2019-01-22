package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

/**
 * A frontend instance descriptor describes a particular instance of a {@link FrontendDescriptor frontend} that
 * has been registered with the service directory and that users can interact with.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 */
public final class FrontendInstanceDescriptor extends ResourceInstanceDescriptor<FrontendId> {

    /**
     * Creates a new frontend instance descriptor.
     *
     * @param id        the ID of the frontend.
     * @param version   the version of this frontend instance.
     * @param clientUri the URI that clients use to access this instance.
     * @param pingUri   the URI that the service directory pings to check the health of this instance.
     */
    @JsonCreator
    public FrontendInstanceDescriptor(@JsonProperty(value = "id", required = true) FrontendId id,
                                      @JsonProperty(value = "version", required = true) Version version,
                                      @JsonProperty(value = "clientUri", required = true) URI clientUri,
                                      @JsonProperty(value = "pingUri", required = true) URI pingUri) {
        super(id, version, clientUri, pingUri);
    }
}
