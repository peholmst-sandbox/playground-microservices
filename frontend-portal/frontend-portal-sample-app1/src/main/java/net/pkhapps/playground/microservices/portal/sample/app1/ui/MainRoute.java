package net.pkhapps.playground.microservices.portal.sample.app1.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import elemental.json.Json;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.Version;
import net.pkhapps.playground.microservices.portal.app.support.PortalSupport;

@Route("")
public class MainRoute extends Div {

    public MainRoute() {
        add(new Label("This is sample application 1"));
        var portalIntegration = new PortalSupport();
        add(portalIntegration);
        add(new Button("Navigate to Sample App 2", event -> {
            portalIntegration.navigateTo(new FrontendId("sample-app-2"), new Version("1.0"), null);
            var message = Json.createObject();
            message.put("greeting", "hello from application 1");
            portalIntegration.forwardMessageTo(new FrontendId("sample-app-2"), new Version("1.0"), message);
        }));
    }
}
