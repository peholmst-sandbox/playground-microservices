package net.pkhapps.playground.microservices.portal.sample.app2.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;

@Route("")
public class MainRoute extends Div {

    public MainRoute() {
        add(new Label("This is sample application 2"));
    }
}
