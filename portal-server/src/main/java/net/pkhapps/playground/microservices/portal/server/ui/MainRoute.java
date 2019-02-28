package net.pkhapps.playground.microservices.portal.server.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.pkhapps.playground.microservices.portal.support.CurrentUser;

@Route("")
@PageTitle("Frontend Portal")
public class MainRoute extends Div {

    final CurrentUser currentUser;

    MainRoute(CurrentUser currentUser) {
        this.currentUser = currentUser;
        add(new Text(currentUser.getUserName()));
        currentUser.getEmail().ifPresent(email -> add(new Anchor("mailto:" + email, email)));
        currentUser.getAvatar().ifPresent(avatar -> add(new Image(avatar.toString(), "avatar")));

        var iframe = new Element("iframe");
        iframe.setAttribute("src", "http://localhost:8881/microfrontend-sample/");
        getElement().appendChild(iframe);
    }
}
