package net.pkhapps.playground.microservices.portal.server.ui.controller;

import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontend;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontendModel;

import java.io.Serializable;
import java.util.UUID;

/**
 * TODO Document me!
 */
public class CommunicationController implements Serializable {

    private final OpenFrontendModel openFrontendModel;

    public CommunicationController(OpenFrontendModel openFrontendModel) {
        this.openFrontendModel = openFrontendModel;
    }

    public void onNotifyUser(UUID openFrontendUuid) {
        openFrontendModel.getByUuid(openFrontendUuid).ifPresent(OpenFrontend::notifyUser);
    }

    public void onSendToFrontend(UUID openFrontendUuid, FrontendId recipient, JsonValue message) {
        openFrontendModel.getByUuid(openFrontendUuid).ifPresent(
                sender -> openFrontendModel.open(recipient).sendMessage(sender.getFrontend().getId(), message)
        );
    }

    public void onOpenFrontend(UUID openFrontendUuid, FrontendId frontendToOpen) {
        if (openFrontendModel.getByUuid(openFrontendUuid).isPresent()) {
            openFrontendModel.openAndFocus(frontendToOpen);
        }
    }
}
