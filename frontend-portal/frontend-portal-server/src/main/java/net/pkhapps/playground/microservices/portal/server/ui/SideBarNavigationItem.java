package net.pkhapps.playground.microservices.portal.server.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import net.pkhapps.playground.microservices.directory.api.FrontendDescriptor;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.api.Version;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

class SideBarNavigationItem extends Composite<Div> {

    private final FrontendDescriptor frontendDescriptor;
    private final Map<Version, FrontendInstanceDescriptor> instances;
    private final NavigationModel navigationModel;

    SideBarNavigationItem(FrontendDescriptor frontendDescriptor,
                          Map<Version, FrontendInstanceDescriptor> instances,
                          NavigationModel navigationModel) {
        this.frontendDescriptor = frontendDescriptor;
        this.instances = instances;
        this.navigationModel = navigationModel;
    }

    @Override
    protected Div initContent() {
        var content = new Div();
        content.addClassName("frontend-navigation-item");
        // TODO Add icon
        content.add(new Span(frontendDescriptor.getName()));
        frontendDescriptor.getDescription().ifPresent(content::setTitle);

        var versions = instances.keySet().stream()
                .sorted(Comparator.comparing(Version::toString).reversed())
                .collect(Collectors.toList());

        if (versions.size() > 0) {
            var latestVersion = versions.get(0);
            content.addClickListener(event -> navigationModel.open(instances.get(latestVersion)));

            if (versions.size() > 1) {
                var versionMenu = new ContextMenu();
                versionMenu.setTarget(content);
                versions.forEach(v -> versionMenu.addItem(String.format("%s %s", frontendDescriptor.getName(), v), event -> navigationModel.open(instances.get(v))));
                // TODO Add some kind of UI indication that there are more than one version of the frontend available
            }
        }
        return content;
    }

    void setSelected(boolean selected) {
        if (selected) {
            getContent().addClassName("selected");
        } else {
            getContent().removeClassName("selected");
        }
    }
}
