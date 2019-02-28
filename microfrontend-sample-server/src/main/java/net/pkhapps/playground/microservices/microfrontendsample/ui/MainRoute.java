package net.pkhapps.playground.microservices.microfrontendsample.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import net.pkhapps.playground.microservices.portal.support.CurrentUser;

@Route("")
public class MainRoute extends Div {

    public MainRoute(CurrentUser currentUser) {
        add(new Text("Hello from Microfrontend Sample Server! The current user is " + currentUser.getUserName() + "."));
    }
}
