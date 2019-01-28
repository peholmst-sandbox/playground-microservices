package net.pkhapps.playground.microservices.portal.server.ui.controller;

import com.vaadin.flow.component.html.Div;
import net.pkhapps.playground.microservices.portal.server.ui.component.FrontendFrame;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontend;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontendModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TODO Document me!
 */
public class FrontendFrameController implements Serializable {

    private final Map<OpenFrontend, FrontendFrame> openFrames = new HashMap<>();
    private final Div containerDiv;

    public FrontendFrameController(Div containerDiv, OpenFrontendModel openFrontendModel) {
        this.containerDiv = Objects.requireNonNull(containerDiv, "containerDiv must not be null");
        Objects.requireNonNull(openFrontendModel, "openFrontendModel must not be null");
        openFrontendModel.addCloseListener(this::onClose);
        openFrontendModel.addLeaveListener(this::onLeave);
        openFrontendModel.addOpenListener(this::onOpen);
    }

    private void onClose(OpenFrontend openFrontend) {
        var frame = openFrames.remove(openFrontend);
        if (frame != null) {
            containerDiv.getElement().removeChild(frame.getElement());
        }
    }

    private void onLeave(OpenFrontend openFrontend) {
        var frame = openFrames.get(openFrontend);
        if (frame != null) {
            frame.setVisible(false);
        }
    }

    private void onOpen(OpenFrontend openFrontend) {
        openFrames.values().forEach(frame -> frame.setVisible(false));
        var frame = openFrames.get(openFrontend);
        if (frame == null) {
            frame = new FrontendFrame(openFrontend);
            openFrames.put(openFrontend, frame);
            containerDiv.getElement().appendChild(frame.getElement());
        }
        frame.setVisible(true);
    }
}
