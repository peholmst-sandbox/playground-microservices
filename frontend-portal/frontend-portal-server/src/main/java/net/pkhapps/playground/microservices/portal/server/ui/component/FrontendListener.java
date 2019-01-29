package net.pkhapps.playground.microservices.portal.server.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.dom.DomEvent;
import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * TODO Document me!
 */
@HtmlImport("frontend://src/components/frontend-listener.html")
@Tag("frontend-listener")
public class FrontendListener extends Component {

    @Nullable
    private NotifyUserHandler notifyUserHandler;
    @Nullable
    private SendToFrontendHandler sendToFrontendHandler;
    @Nullable
    private OpenFrontendHandler openFrontendHandler;

    public FrontendListener() {
        getElement().addEventListener("notify-user", this::onNotifyUser).addEventData("event.detail");
        getElement().addEventListener("send-to-frontend", this::onSendToFrontend).addEventData("event.detail");
        getElement().addEventListener("open-frontend", this::onOpenFrontend).addEventData("event.detail");
    }

    private void onNotifyUser(DomEvent event) {
        if (notifyUserHandler != null) {
            notifyUserHandler.onNotifyUser(UUID.fromString(event.getEventData().getString("event.detail.uuid")));
        }
    }

    private void onSendToFrontend(DomEvent event) {
        if (sendToFrontendHandler != null) {
            var uuid = UUID.fromString(event.getEventData().getString("event.detail.uuid"));
            var recipient = new FrontendId(event.getEventData().getString("event.detail.recipient"));
            var payload = event.getEventData().getObject("event.detail.payload"); // TODO Will this work with a simple string as well?
            sendToFrontendHandler.onSendToFrontend(uuid, recipient, payload);
        }
    }

    // TODO Access to event data is wrong, results in NPEs

    private void onOpenFrontend(DomEvent event) {
        if (openFrontendHandler != null) {
            var uuid = UUID.fromString(event.getEventData().getString("event.detail.uuid"));
            var frontend = new FrontendId(event.getEventData().getString("event.detail.frontend"));
            openFrontendHandler.onOpenFrontend(uuid, frontend);
        }
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
}
