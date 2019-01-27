package net.pkhapps.playground.microservices.portal.server.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendId;

import java.io.Serializable;
import java.util.UUID;

/**
 * TODO Document me!
 */
@HtmlImport("frontend://src/components/frontend-listener.html")
@Tag("frontend-listener")
public class FrontendListener extends Component {

    private NotifyUserHandler notifyUserHandler;
    private SendToFrontendHandler sendToFrontendHandler;
    private OpenFrontendHandler openFrontendHandler;

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
