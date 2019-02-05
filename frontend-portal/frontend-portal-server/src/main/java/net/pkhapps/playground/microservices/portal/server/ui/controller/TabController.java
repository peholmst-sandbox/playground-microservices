package net.pkhapps.playground.microservices.portal.server.ui.controller;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
        if (selectedTab != null && component.getComponentCount() > 1) {
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
        findByOpenFrontend(openFrontend).ifPresent(t -> {
            component.setSelectedTab(t.tab);
            t.hideNotification();
        });
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
        private Icon notificationIcon;

        OpenFrontendTab(OpenFrontend openFrontend) {
            this.openFrontend = Objects.requireNonNull(openFrontend, "openFrontend must not be null");
            notificationRegistration = openFrontend.addNotificationListener(this::showNotification);
            var closeButton = new Button(VaadinIcon.CLOSE.create(), event -> model.close(openFrontend));
            closeButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_SMALL);
            notificationIcon = VaadinIcon.INFO_CIRCLE.create();
            notificationIcon.setColor("red");
            notificationIcon.setVisible(false);
            tab = new Tab(new Span(openFrontend.getFrontend().getName()), notificationIcon, closeButton);
        }

        private void showNotification() {
            notificationIcon.setVisible(true);
        }

        private void hideNotification() {
            notificationIcon.setVisible(false);
        }

        @Override
        public void remove() {
            notificationRegistration.remove();
        }
    }
}
