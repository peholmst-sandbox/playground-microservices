package net.pkhapps.playground.microservices.portal.sample.app1.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import elemental.json.Json;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.portal.app.support.PortalSupport;

import java.net.URI;

@Route("")
public class MainRoute extends Div {

    public MainRoute() {
        add(new Label("This is sample application 1"));
        var portalSupport = new PortalSupport(URI.create("http://localhost:8888"));
        add(portalSupport);
        add(new Button("Navigate to Sample App 2", event -> {
            portalSupport.showFrontend(new FrontendId("sample-app-2"));
            var message = Json.createObject();
            message.put("greeting", "hello from application 1");
            portalSupport.postMessage(new FrontendId("sample-app-2"), message);
        }));
    }
}
