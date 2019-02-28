package net.pkhapps.playground.microservices.portal.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route
@PageTitle("Frontend Portal")
public class MainRoute extends Div {

    MainRoute() {
        add(new Text("Hello World!"));
    }
}
