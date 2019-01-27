package net.pkhapps.playground.microservices.portal.app.support;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.Version;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@HtmlImport("frontend://src/components/portal-integration.html")
@Tag("portal-integration")
public class PortalSupport extends Component {

    private final Set<SerializableConsumer<JsonObject>> messageListeners = new HashSet<>();
    private final Set<JsonConverter<?>> converters = new HashSet<>();

    public void navigateTo(FrontendId frontend, Version version, String parameters) {
        getElement().callFunction("navigateTo", frontend.toString(), version.toString(), parameters);
    }

    public void forwardMessageTo(FrontendId frontend, Version version, JsonObject message) {
        getElement().callFunction("forwardMessageTo", frontend.toString(), version.toString(), message);
    }

    public Registration addMessageListener(SerializableConsumer<JsonObject> listener) {
        messageListeners.add(listener);
        return () -> messageListeners.remove(listener);
    }

    /**
     * Adds a new converter for converting between POJOs and JSON.
     *
     * @param converter the converter to add.
     * @return this object, to allow for method chaining.
     */
    public PortalSupport addConverter(JsonConverter<?> converter) {
        converters.add(Objects.requireNonNull(converter, "converter must not be null"));
        return this;
    }

    /**
     * Shows the given frontend in the portal. If the frontend is already open in a tab, the portal will switch to that
     * tab. Otherwise, the portal will open the frontend in a new tab and switch to it. If the portal cannot switch
     * to the frontend, an error message will be shown to the user. No exceptions will be thrown by this method.
     *
     * @param frontend the ID of the frontend to open.
     * @return this object, to allow for method chaining.
     */
    public PortalSupport showFrontend(FrontendId frontend) {
        // Implement me!
        return this;
    }

    /**
     * Sends the given message to the given frontend. If the frontend is already open in a tab, the message will be
     * delivered directly. Otherwise, the portal will open the frontend in a new tab and then deliver the message. In
     * both cases, the portal will not switch to the frontend. If the portal cannot deliver the message to the frontend,
     * an error message will be shown to the user. No exceptions will be thrown by this method.
     * <p>
     * A {@link #addConverter(JsonConverter) converter} for the message must have been registered prior to calling this
     * method.
     *
     * @param frontend the ID of the frontend to send the message to.
     * @param message  the message to send.
     * @param <M>      the type of the message.
     * @return this object, to allow for method chaining.
     * @throws IllegalArgumentException if the message cannot be converted to a JSON value.
     */
    public <M> PortalSupport postMessage(FrontendId frontend, M message) {
        return postMessage(frontend, getConverterForMessage(message).convertToJson(message));
    }

    public PortalSupport postMessage(FrontendId frontend, JsonValue message) {

    }

    @SuppressWarnings("unchecked")
    private <M> JsonConverter<M> getConverterForMessage(M message) {
        return (JsonConverter<M>) converters.stream()
                .filter(c -> c.supportsPojo(message))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find a converter for message " + message));
    }
}
