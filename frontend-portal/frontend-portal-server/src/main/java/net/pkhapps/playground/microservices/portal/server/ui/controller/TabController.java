package net.pkhapps.playground.microservices.portal.server.ui.controller;

import com.vaadin.flow.component.tabs.Tabs;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontendModel;

import java.io.Serializable;

/**
 * TODO Document me!
 */
public class TabController implements Serializable {

    private final OpenFrontendModel openFrontendModel;
    private final Tabs tabs;

    public TabController(OpenFrontendModel openFrontendModel, Tabs tabs) {
        this.openFrontendModel = openFrontendModel;
        this.tabs = tabs;
    }
}
