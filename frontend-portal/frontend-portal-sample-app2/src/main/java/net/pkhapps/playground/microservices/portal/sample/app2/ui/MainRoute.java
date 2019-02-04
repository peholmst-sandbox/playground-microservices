package net.pkhapps.playground.microservices.portal.sample.app2.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import net.pkhapps.playground.microservices.portal.app.support.PortalSupport;

import java.net.URI;

@Route("")
public class MainRoute extends Div {

    public MainRoute() {
        add(new Label("This is sample application 2"));
        var portalSupport = new PortalSupport(URI.create("http://localhost:8888"));
        portalSupport.addMessageListener(event -> {
            if (event.getMessage() instanceof JsonObject) {
                var obj = (JsonObject) event.getMessage();
                if (obj.hasKey("greeting")) {
                    var greeting = obj.getString("greeting");
                    add(new Span("Greeting " + greeting + " received by " + event.getSender()));
                    portalSupport.notifyUser();
                }
            }
        });
        add(portalSupport);

    }
}
