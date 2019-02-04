package net.pkhapps.playground.microservices.portal.sample.app1.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import elemental.json.Json;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.portal.app.support.PortalSupport;

import java.net.URI;
import java.time.Instant;

@Route("")
public class MainRoute extends Div {

    private PortalSupport portalSupport;

    public MainRoute() {
        portalSupport = new PortalSupport(URI.create("http://localhost:8888"));

        add(new Label("This is sample application 1"));
        add(portalSupport);
        add(new Button("Navigate to Sample App 2 and Say Hello", event -> {
            goToApp2();
            sayHelloToApp2();
        }));
        add(new Button("Navigate to Sample App 2", event -> goToApp2()));
        add(new Button("Say Hello", event -> sayHelloToApp2()));
    }

    private void goToApp2() {
        portalSupport.showFrontend(new FrontendId("sample-app-2"));
    }

    private void sayHelloToApp2() {
        var message = Json.createObject();
        message.put("greeting", "Hello from application 1! It is " + Instant.now().toString());
        portalSupport.postMessage(new FrontendId("sample-app-2"), message);
    }
}
