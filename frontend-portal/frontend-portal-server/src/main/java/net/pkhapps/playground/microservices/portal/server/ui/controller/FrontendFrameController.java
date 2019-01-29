package net.pkhapps.playground.microservices.portal.server.ui.controller;

import com.vaadin.flow.component.html.Div;
import net.pkhapps.playground.microservices.portal.server.ui.component.FrontendFrame;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontend;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontendModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TODO Document me!
 */
public class FrontendFrameController implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendFrameController.class);
    private final Map<OpenFrontend, FrontendFrame> openFrames = new HashMap<>();
    private final Div containerDiv;

    public FrontendFrameController(Div containerDiv, OpenFrontendModel openFrontendModel) {
        this.containerDiv = Objects.requireNonNull(containerDiv, "containerDiv must not be null");
        Objects.requireNonNull(openFrontendModel, "openFrontendModel must not be null");
        openFrontendModel.addCloseListener(this::onClose);
        openFrontendModel.addLeaveListener(this::onLeave);
        openFrontendModel.addOpenListener(this::onOpen);
        openFrontendModel.addFocusListener(this::onFocus);
    }

    private void onClose(OpenFrontend openFrontend) {
        var frame = openFrames.remove(openFrontend);
        if (frame != null) {
            LOGGER.debug("Removing frame for {}", openFrontend);
            containerDiv.getElement().removeChild(frame.getElement());
        }
    }

    private void onLeave(OpenFrontend openFrontend) {
        var frame = openFrames.get(openFrontend);
        if (frame != null) {
            LOGGER.debug("Hiding frame for {}", openFrontend);
            frame.hide();
        }
    }

    private void onOpen(OpenFrontend openFrontend) {
        openFrames.values().forEach(FrontendFrame::hide);
        var frame = openFrames.get(openFrontend);
        if (frame == null) {
            LOGGER.debug("Adding frame for {}", openFrontend);
            frame = new FrontendFrame(openFrontend);
            openFrames.put(openFrontend, frame);
            containerDiv.getElement().appendChild(frame.getElement());
        }
    }

    private void onFocus(OpenFrontend openFrontend) {
        var frame = openFrames.get(openFrontend);
        if (frame != null) {
            LOGGER.debug("Showing frame for {}", openFrontend);
            frame.show();
        }
    }
}
