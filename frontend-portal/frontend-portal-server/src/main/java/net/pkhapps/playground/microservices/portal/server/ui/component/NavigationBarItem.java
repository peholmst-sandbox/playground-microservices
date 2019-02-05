package net.pkhapps.playground.microservices.portal.server.ui.component;

import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import net.pkhapps.playground.microservices.directory.api.FrontendDescriptor;

public class NavigationBarItem extends Composite<Div> implements ClickNotifier<NavigationBarItem> {

    private final FrontendDescriptor frontendDescriptor;

    public NavigationBarItem(FrontendDescriptor frontendDescriptor) {
        this.frontendDescriptor = frontendDescriptor;
    }

    @Override
    protected Div initContent() {
        var content = new Div();
        content.addClassName("frontend-navigation-item");
        // TODO Add icon
        content.add(new Span(frontendDescriptor.getName()));
        frontendDescriptor.getDescription().ifPresent(content::setTitle);
        return content;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            getContent().addClassName("selected");
        } else {
            getContent().removeClassName("selected");
        }
    }

    // TODO Notification bubble
}
