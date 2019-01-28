package net.pkhapps.playground.microservices.portal.server.ui;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import net.pkhapps.playground.microservices.directory.api.ServiceDirectory;
import net.pkhapps.playground.microservices.portal.server.ui.component.FrontendListener;
import net.pkhapps.playground.microservices.portal.server.ui.controller.CommunicationController;
import net.pkhapps.playground.microservices.portal.server.ui.controller.FrontendFrameController;
import net.pkhapps.playground.microservices.portal.server.ui.controller.NavigationBarController;
import net.pkhapps.playground.microservices.portal.server.ui.controller.TabController;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontendModel;

@Route("")
@Push
@Tag("main-layout")
@HtmlImport("frontend://src/layouts/main-layout.html")
@PageTitle("Frontend Portal")
public class MainRoute extends PolymerTemplate<TemplateModel> {

    @Id("sidebar")
    private Div navigationBar;

    @Id("frontend-container")
    private Div frontendContainer;

    private final OpenFrontendModel openFrontendModel;
    private final CommunicationController communicationController;
    private final TabController tabController;
    private final NavigationBarController navigationBarController;
    private final FrontendFrameController frontendFrameController;

    public MainRoute(ServiceDirectory serviceDirectory) {
        var openTabs = new Tabs();
        frontendContainer.add(openTabs);

        var frontendListener = new FrontendListener();
        frontendContainer.add(frontendListener);

        openFrontendModel = new OpenFrontendModel(() -> serviceDirectory);
        communicationController = new CommunicationController(openFrontendModel);
        tabController = new TabController(openFrontendModel, openTabs);
        navigationBarController = new NavigationBarController(navigationBar, openFrontendModel);
        frontendFrameController = new FrontendFrameController(frontendContainer, openFrontendModel);

        frontendListener.setNotifyUserHandler(communicationController::onNotifyUser);
        frontendListener.setOpenFrontendHandler(communicationController::onOpenFrontend);
        frontendListener.setSendToFrontendHandler(communicationController::onSendToFrontend);

        navigationBarController.populate(serviceDirectory);

        // TODO Auto-detect when frontends are added or removed.
        // TODO Auto-detect when frontend instances go down and up
    }
}
