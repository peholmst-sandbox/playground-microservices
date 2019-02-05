package net.pkhapps.playground.microservices.portal.server.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonValue;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontend;

import java.util.Objects;

/**
 * Component that wraps an {@code iframe} that will contain an instance of a frontend. All messages that are posted from
 * the portal to the frontend instance are handled by this component. Messages posted from the frontend instance
 * to the portal are handled by {@link FrontendListener}.
 */
@HtmlImport("frontend://src/components/frontend-frame.html")
@Tag("frontend-frame")
public class FrontendFrame extends Component {

    private final OpenFrontend openFrontend;

    private Registration openFrontendListenerRegistration;

    /**
     * Creates a new {@code FrontendFrame} for the specified frontend.
     *
     * @param openFrontend the frontend to load into the frame.
     */
    public FrontendFrame(OpenFrontend openFrontend) {
        this.openFrontend = Objects.requireNonNull(openFrontend, "openFrontend must not be null");
        getElement().callFunction("initialize",
                openFrontend.getInstance().getClientUri().toString(),
                openFrontend.getOrigin().toURI().toString(),
                openFrontend.getUuid().toString());
        openFrontendListenerRegistration = openFrontend.addMessageListener(this::postMessage);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        openFrontendListenerRegistration.remove();
    }

    private void postMessage(FrontendId sender, JsonValue message) {
        getElement().callFunction("postMessage", sender.toString(), message);
    }

    public void hide() {
        getElement().getStyle().set("display", "none");
    }

    public void show() {
        getElement().getStyle().set("display", null);
    }
}
