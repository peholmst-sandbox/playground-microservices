package net.pkhapps.playground.microservices.portal.app.support;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalSupport.class);
    private final Set<MessageListener> messageListeners = new HashSet<>();

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
        LOGGER.debug("Received message {}", event.getEventData());
        if (event.getEventData().hasKey("event.detail")) {
            var eventDetail = event.getEventData().getObject("event.detail");
            if (eventDetail.hasKey("sender") && eventDetail.hasKey("message")) {
                var sender = eventDetail.getString("sender");
                var message = eventDetail.get("message");
                var messageEvent = new MessageEvent(new FrontendId(sender), message);
                Set.copyOf(messageListeners).forEach(listener -> listener.onMessageReceived(messageEvent));
            }
        }
    }

    public Registration addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
        return () -> messageListeners.remove(listener);
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
}
