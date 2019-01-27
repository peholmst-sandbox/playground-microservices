package net.pkhapps.playground.microservices.portal.app.support;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendId;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * TODO Document me!
 */
@HtmlImport("frontend://src/components/portal-support.html")
@Tag("portal-support")
public class PortalSupport extends Component {

    private final Set<MessageListener> messageListeners = new HashSet<>();
    private final Set<JsonConverter<?>> converters = new HashSet<>();

    /**
     * @param portalUri
     */
    public PortalSupport(URI portalUri) {
        getElement().setProperty("portalOrigin", String.format("%s://%s:%d", portalUri.getScheme(),
                portalUri.getHost(), portalUri.getPort()));
        getElement().addEventListener("messageFromFrontend", this::onMessageFromFrontend)
                .addEventData("event.detail");
    }

    private void onMessageFromFrontend(DomEvent event) {
        // TODO Implement me!
    }

    @Deprecated
    public Registration addMessageListener(MessageListener listener) {
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
     * tab. Otherwise, the portal will open the frontend in a new tab and switch to it.
     *
     * @param frontend the ID of the frontend to open.
     * @return this object, to allow for method chaining.
     */
    public PortalSupport showFrontend(FrontendId frontend) {
        Objects.requireNonNull(frontend, "frontend must not be null");
        getElement().callFunction("showFrontend", frontend.toString());
        return this;
    }

    /**
     * Sends the given message to the given frontend. If the frontend is already open in a tab, the message will be
     * delivered directly. Otherwise, the portal will open the frontend in a new tab and then deliver the message. In
     * both cases, the portal will not switch to the frontend.
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

    /**
     * Sends the given message to the given frontend. If the frontend is already open in a tab, the message will be
     * delivered directly. Otherwise, the portal will open the frontend in a new tab and then deliver the message. In
     * both cases, the portal will not switch to the frontend.
     *
     * @param frontend the ID of the frontend to send the message to.
     * @param message  the message to send.
     * @return this object, to allow for method chaining.
     * @throws IllegalArgumentException if the message cannot be converted to a JSON value.
     */
    public PortalSupport postMessage(FrontendId frontend, JsonValue message) {
        Objects.requireNonNull(frontend, "frontend must not be null");
        Objects.requireNonNull(message, "message must not be null");
        getElement().callFunction("postMessage", frontend.toString(), message);
        return this;
    }

    /**
     * Activates a user notification on the tab of this frontend. The notification will automatically be disabled once
     * the user navigates to the tab in question.
     */
    public void notifyUser() {
        getElement().callFunction("notifyUser");
    }

    @SuppressWarnings("unchecked")
    private <M> JsonConverter<M> getConverterForMessage(M message) {
        Objects.requireNonNull(message, "message must not be null");
        return (JsonConverter<M>) converters.stream()
                .filter(c -> c.supportsPojo(message))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find a converter for message " + message));
    }
}
