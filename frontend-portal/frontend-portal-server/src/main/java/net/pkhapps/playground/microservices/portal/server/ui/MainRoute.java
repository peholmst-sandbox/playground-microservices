package net.pkhapps.playground.microservices.portal.server.ui;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.ServiceDirectory;
import net.pkhapps.playground.microservices.directory.api.Version;

@Route("")
@Push
@Tag("main-layout")
@HtmlImport("frontend://src/layouts/main-layout.html")
@PageTitle("Frontend Portal")
public class MainRoute extends PolymerTemplate<TemplateModel> {

    @Id("sidebar")
    private Div sideBar;

    @Id("frontend-container")
    private Div frontendContainer;
    private final NavigationModel navigationModel;
    private final ServiceDirectory serviceDirectory;

    public MainRoute(ServiceDirectory serviceDirectory) {
        this.serviceDirectory = serviceDirectory;

        var openTabs = new Tabs();
        frontendContainer.add(openTabs);

        navigationModel = new NavigationModel();
        var sideBarController = new SideBarController(sideBar, navigationModel);
        var containerController = new FrontendContainerController(frontendContainer, navigationModel);
        var tabController = new TabController(openTabs, navigationModel);

        sideBarController.populate(serviceDirectory);

        getElement().addEventListener("navigate", this::onNavigate).addEventData("event.detail");
        // TODO Auto-detect when frontends are added or removed.
        // TODO Auto-detect when frontend instances go down and up

    }

    private void onNavigate(DomEvent event) {
        var detail = event.getEventData().getObject("event.detail");
        FrontendId frontendId = new FrontendId(detail.getString("frontend"));
        Version version = new Version(detail.getString("version"));
        // TODO Parameters are not passed yet
        // TODO This will lead to problems if there are more than one instance. This must be addressed in some way.
        serviceDirectory.getInstance(frontendId, version).ifPresent(navigationModel::open);
    }
}
