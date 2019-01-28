package net.pkhapps.playground.microservices.portal.server.ui.controller;

import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.shared.Registration;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontend;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontendModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * TODO Document me!
 */
public class TabController implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TabController.class);

    private final OpenFrontendModel model;
    private final Tabs component;
    private Set<OpenFrontendTab> tabs = new HashSet<>();

    public TabController(OpenFrontendModel model, Tabs component) {
        this.model = model;
        this.component = component;
        this.component.addSelectedChangeListener(this::onTabSelectionChange);
        this.model.addOpenListener(this::onOpen);
        this.model.addFocusListener(this::onFocus);
        this.model.addCloseListener(this::onClose);
    }

    private void onTabSelectionChange(Tabs.SelectedChangeEvent event) {
        var selectedTab = component.getSelectedTab();
        if (selectedTab != null) {
            findByTab(component.getSelectedTab()).ifPresent(t -> model.focus(t.openFrontend));
        }
    }

    private void onOpen(OpenFrontend openFrontend) {
        LOGGER.debug("Creating new tab for {}", openFrontend);
        var tab = new OpenFrontendTab(openFrontend);
        tabs.add(tab);
        component.add(tab.tab);
    }

    private void onFocus(OpenFrontend openFrontend) {
        LOGGER.debug("Selecting tab for {}", openFrontend);
        findByOpenFrontend(openFrontend).ifPresent(t -> component.setSelectedTab(t.tab));
    }

    private void onClose(OpenFrontend openFrontend) {
        LOGGER.debug("Removing tab for {}", openFrontend);
        findByOpenFrontend(openFrontend).ifPresent(t -> {
            component.remove(t.tab);
            t.remove();
        });
    }

    private Optional<OpenFrontendTab> findByOpenFrontend(OpenFrontend openFrontend) {
        return tabs.stream().filter(t -> t.openFrontend.equals(openFrontend)).findAny();
    }

    private Optional<OpenFrontendTab> findByTab(Tab tab) {
        return tabs.stream().filter(t -> t.tab.equals(tab)).findAny();
    }

    private class OpenFrontendTab implements Serializable, Registration {
        private final Tab tab;
        private final OpenFrontend openFrontend;
        private final Registration notificationRegistration;

        OpenFrontendTab(OpenFrontend openFrontend) {
            this.openFrontend = Objects.requireNonNull(openFrontend, "openFrontend must not be null");
            notificationRegistration = openFrontend.addNotificationListener(this::onNotification);
            tab = new Tab(openFrontend.getFrontend().getName());
        }

        private void onNotification() {
            // TODO Apply some style to the tab or something similar to make it stand out. Then remove it once the user navigates to it.
        }

        @Override
        public void remove() {
            notificationRegistration.remove();
        }
    }
}
