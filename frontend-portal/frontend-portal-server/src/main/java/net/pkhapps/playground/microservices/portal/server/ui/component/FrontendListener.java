package net.pkhapps.playground.microservices.portal.server.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.dom.DomEvent;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * TODO Document me!
 */
@HtmlImport("frontend://src/components/frontend-listener.html")
@Tag("frontend-listener")
public class FrontendListener extends Component {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendListener.class);

    @Nullable
    private NotifyUserHandler notifyUserHandler;
    @Nullable
    private SendToFrontendHandler sendToFrontendHandler;
    @Nullable
    private OpenFrontendHandler openFrontendHandler;
    @Nullable
    private FrontendFrameInitializedHandler frontendFrameInitializedHandler;

    public FrontendListener() {
        getElement().addEventListener("notify-user", this::onNotifyUser).addEventData("event.detail");
        getElement().addEventListener("send-to-frontend", this::onSendToFrontend).addEventData("event.detail");
        getElement().addEventListener("open-frontend", this::onOpenFrontend).addEventData("event.detail");
        getElement().addEventListener("frontend-frame-initialized", this::onFrontendFrameInitialized).addEventData("event.detail");
    }

    private void onNotifyUser(DomEvent event) {
        if (notifyUserHandler != null) {
            LOGGER.debug("onNotifyUser: {}", event.getEventData());
            notifyUserHandler.onNotifyUser(extractUuid(extractDetail(event)));
        }
    }

    private void onSendToFrontend(DomEvent event) {
        if (sendToFrontendHandler != null) {
            LOGGER.debug("onSendToFrontend: {}", event.getEventData());
            var eventDetail = extractDetail(event);
            var uuid = extractUuid(eventDetail);
            var recipient = new FrontendId(extractString(eventDetail, "recipient"));
            var payload = extractJsonValue(eventDetail, "payload");
            sendToFrontendHandler.onSendToFrontend(uuid, recipient, payload);
        }
    }

    private void onOpenFrontend(DomEvent event) {
        if (openFrontendHandler != null) {
            LOGGER.debug("onOpenFrontend: {}", event.getEventData());
            var eventDetail = extractDetail(event);
            var uuid = extractUuid(eventDetail);
            var frontend = new FrontendId(extractString(eventDetail, "frontend"));
            openFrontendHandler.onOpenFrontend(uuid, frontend);
        }
    }

    private void onFrontendFrameInitialized(DomEvent event) {
        if (frontendFrameInitializedHandler != null) {
            LOGGER.debug("onFrontendFrameInitialized: {}", event.getEventData());
            frontendFrameInitializedHandler.onFrontendFrameInitialized(extractUuid(extractDetail(event)));
        }
    }

    private static JsonObject extractDetail(DomEvent event) {
        if (!event.getEventData().hasKey("event.detail")) {
            throw new IllegalArgumentException("DomEvent did not contain details");
        }
        return event.getEventData().getObject("event.detail");
    }

    private static UUID extractUuid(JsonObject eventDetail) {
        return UUID.fromString(extractString(eventDetail, "uuid"));
    }

    private static String extractString(JsonObject eventDetail, String attributeName) {
        return extractJsonValue(eventDetail, attributeName).asString();
    }

    private static JsonValue extractJsonValue(JsonObject eventDetail, String attributeName) {
        if (!eventDetail.hasKey(attributeName)) {
            throw new IllegalArgumentException("Event detail did not contain attribute " + attributeName);
        }
        return eventDetail.get(attributeName);
    }

    public void setNotifyUserHandler(NotifyUserHandler notifyUserHandler) {
        this.notifyUserHandler = notifyUserHandler;
    }

    public void setSendToFrontendHandler(SendToFrontendHandler sendToFrontendHandler) {
        this.sendToFrontendHandler = sendToFrontendHandler;
    }

    public void setOpenFrontendHandler(OpenFrontendHandler openFrontendHandler) {
        this.openFrontendHandler = openFrontendHandler;
    }

    public void setFrontendFrameInitializedHandler(FrontendFrameInitializedHandler frontendFrameInitializedHandler) {
        this.frontendFrameInitializedHandler = frontendFrameInitializedHandler;
    }

    @FunctionalInterface
    public interface NotifyUserHandler extends Serializable {
        /**
         * Notifies the user that something has happened inside an open frontend.
         *
         * @param openFrontendUuid the UUID of the {@link net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontend} that sent the notification.
         */
        void onNotifyUser(UUID openFrontendUuid);
    }

    @FunctionalInterface
    public interface SendToFrontendHandler extends Serializable {
        /**
         * Sends a message to another frontend. If the frontend is not already open, it will be opened before the
         * message is delivered (however, the portal will not switch to it).
         *
         * @param openFrontendUuid the UUID of the {@link net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontend} that sent the message.
         * @param recipient        the ID of the frontend that will receive the message.
         * @param message          the message.
         */
        void onSendToFrontend(UUID openFrontendUuid, FrontendId recipient, JsonValue message);
    }

    @FunctionalInterface
    public interface OpenFrontendHandler extends Serializable {
        /**
         * Opens another frontend. If the frontend is already open, the portal will switch to it.
         *
         * @param openFrontendUuid the UUID of the {@link net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontend} that sent the message.
         * @param frontendToOpen   the ID of the frontend to open.
         */
        void onOpenFrontend(UUID openFrontendUuid, FrontendId frontendToOpen);
    }

    @FunctionalInterface
    public interface FrontendFrameInitializedHandler extends Serializable {

        /**
         * TODO Document me!
         *
         * @param openFrontendUuid
         */
        void onFrontendFrameInitialized(UUID openFrontendUuid);
    }
}
