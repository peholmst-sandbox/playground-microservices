package net.pkhapps.playground.microservices.directory.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.net.URI;

/**
 * A frontend descriptor describes a frontend that has been registered with the service directory.
 * <p>
 * This value object can be serialized to and deserialized from JSON using Jackson.
 *
 * @see FrontendInstanceDescriptor
 */
public final class FrontendDescriptor extends ResourceDescriptor<FrontendId> {

    /**
     * Creates a new frontend descriptor.
     *
     * @param id               the ID of the frontend.
     * @param name             the human readable name of the frontend.
     * @param description      an optional human readable description of the frontend.
     * @param iconUri          an optional URI of an icon for the frontend.
     * @param documentationUri an optional URI of the documentation for this frontend.
     */
    @JsonCreator
    public FrontendDescriptor(@JsonProperty(value = "id", required = true) FrontendId id,
                              @JsonProperty(value = "name", required = true) String name,
                              @JsonProperty(value = "description") @Nullable String description,
                              @JsonProperty(value = "iconUri") @Nullable URI iconUri,
                              @JsonProperty(value = "documentationUri") @Nullable URI documentationUri) {
        super(id, name, description, iconUri, documentationUri);
    }

    /**
     * Creates a new frontend descriptor.
     *
     * @param id   the ID of the frontend.
     * @param name the human readable name of the frontend.
     */
    public FrontendDescriptor(FrontendId id, String name) {
        this(id, name, null, null, null);
    }
}
