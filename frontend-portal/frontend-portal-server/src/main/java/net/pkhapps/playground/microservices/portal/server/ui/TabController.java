package net.pkhapps.playground.microservices.portal.server.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;

import java.util.HashMap;
import java.util.Map;

public class TabController implements NavigationListener {

    private final Tabs tabs;
    private final Map<FrontendInstanceDescriptor, Tab> tabMap = new HashMap<>();
    private final NavigationModel navigationModel;

    public TabController(Tabs tabs, NavigationModel navigationModel) {
        this.tabs = tabs;
        this.tabs.addSelectedChangeListener(event -> {
            var selectedTab = tabs.getSelectedTab();
            if (selectedTab != null) {
                tabMap.entrySet().stream().filter(entry -> entry.getValue() == selectedTab).findAny().ifPresent(entry -> navigationModel.open(entry.getKey()));
            }
        });
        this.navigationModel = navigationModel;
        navigationModel.addListener(this);
    }

    @Override
    public void onEnter(FrontendInstanceDescriptor frontend) {
        var tab = tabMap.get(frontend);
        if (tab == null) {
            tab = new Tab(String.format("%s (%s)", frontend.getResourceId(), frontend.getVersion())); // TODO We need the descriptor, not only the ID here.
            var closeButton = new Button(new Icon(VaadinIcon.CLOSE));
            closeButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
            closeButton.addClickListener(event -> navigationModel.close(frontend));
            tab.add(closeButton);
            tabMap.put(frontend, tab);
            tabs.add(tab);
        }
        tabs.setSelectedTab(tab);
    }

    @Override
    public void onNotify(FrontendInstanceDescriptor frontend) {
        // TODO Highlight selected tab in some way
    }

    @Override
    public void onLeave(FrontendInstanceDescriptor frontend) {
        var tab = tabMap.get(frontend);
        if (tab != null) {
            tabs.setSelectedTab(null);
        }
    }

    @Override
    public void onClose(FrontendInstanceDescriptor frontend) {
        var tab = tabMap.remove(frontend);
        if (tab != null) {
            tabs.remove(tab);
        }
    }
}
