package net.pkhapps.playground.microservices.portal.server.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;

import java.util.HashMap;
import java.util.Map;

class FrontendContainerController implements NavigationListener {

    private final Map<FrontendInstanceDescriptor, Element> openFrames = new HashMap<>();
    private final Div containerDiv;

    FrontendContainerController(Div containerDiv, NavigationModel navigationModel) {
        this.containerDiv = containerDiv;
        navigationModel.addListener(this);
    }

    @Override
    public void onClose(FrontendInstanceDescriptor frontend) {
        var frame = openFrames.remove(frontend);
        if (frame != null) {
            containerDiv.getElement().removeChild(frame);
        }
    }

    @Override
    public void onLeave(FrontendInstanceDescriptor frontend) {
        var frame = openFrames.get(frontend);
        if (frame != null) {
            frame.setVisible(false);
        }
    }

    @Override
    public void onEnter(FrontendInstanceDescriptor frontend) {
        openFrames.values().forEach(frame -> frame.setVisible(false));
        var frame = openFrames.get(frontend);
        if (frame == null) {
            frame = new Element("iframe");
            frame.setAttribute("src", frontend.getClientUri().toString()); // TODO Add additional parameters so that the client knows it is being embedded
            openFrames.put(frontend, frame);
            containerDiv.getElement().appendChild(frame);
        }
        frame.setVisible(true);
    }
}
