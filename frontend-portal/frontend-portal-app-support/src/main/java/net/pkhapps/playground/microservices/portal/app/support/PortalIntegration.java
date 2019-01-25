package net.pkhapps.playground.microservices.portal.app.support;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonObject;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.Version;

import java.util.HashSet;
import java.util.Set;

@HtmlImport("frontend://src/components/portal-integration.html")
@Tag("portal-integration")
public class PortalIntegration extends Component {

    private final Set<SerializableConsumer<JsonObject>> messageListeners = new HashSet<>();

    public void navigateTo(FrontendId frontend, Version version, String parameters) {
        getElement().callFunction("navigateTo", frontend.toString(), version.toString(), parameters);
    }

    public void forwardMessageTo(FrontendId frontend, Version version, JsonObject message) {
        getElement().callFunction("forwardMessageTo", frontend.toString(), version.toString(), message);
    }

    public Registration addMessageListener(SerializableConsumer<JsonObject> listener) {
        messageListeners.add(listener);
        return () -> messageListeners.remove(listener);
    }
}
