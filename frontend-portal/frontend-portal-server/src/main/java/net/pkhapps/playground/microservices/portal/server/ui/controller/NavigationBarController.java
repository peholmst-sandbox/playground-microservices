package net.pkhapps.playground.microservices.portal.server.ui.controller;

import com.vaadin.flow.component.html.Div;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.ServiceDirectory;
import net.pkhapps.playground.microservices.portal.server.ui.component.NavigationBarItem;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontend;
import net.pkhapps.playground.microservices.portal.server.ui.model.OpenFrontendModel;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TODO Document me
 */
public class NavigationBarController implements Serializable {

    private final Div navigationBar;
    private final OpenFrontendModel openFrontendModel;
    private final Map<FrontendId, NavigationBarItem> itemMap = new HashMap<>();

    @Nullable
    private FrontendId selection;

    public NavigationBarController(Div navigationBar, OpenFrontendModel openFrontendModel) {
        this.navigationBar = Objects.requireNonNull(navigationBar, "navigationBar must not be null");
        this.openFrontendModel = Objects.requireNonNull(openFrontendModel, "openFrontendModel must not be null");
        openFrontendModel.addFocusListener(this::onFocus);
        openFrontendModel.addLeaveListener(this::onLeave);
    }

    public void populate(ServiceDirectory serviceDirectory) {
        navigationBar.removeAll();
        itemMap.clear();
        serviceDirectory.getFrontends().forEach(frontendDescriptor -> {
            var item = new NavigationBarItem(frontendDescriptor);
            item.addClickListener(event -> openFrontendModel.openAndFocus(frontendDescriptor.getId()));
            itemMap.put(frontendDescriptor.getId(), item);
            navigationBar.add(item);
            if (Objects.equals(selection, frontendDescriptor.getId())) {
                item.setSelected(true);
            }
        });
        if (!itemMap.containsKey(selection)) {
            selection = null;
        }
    }

    private void onLeave(OpenFrontend openFrontend) {
        if (Objects.equals(selection, openFrontend.getFrontend().getId())) {
            var item = itemMap.get(selection);
            item.setSelected(false);
            selection = null;
        }
    }

    private void onFocus(OpenFrontend openFrontend) {
        itemMap.values().forEach(item -> item.setSelected(false));
        var item = itemMap.get(openFrontend.getFrontend().getId());
        if (item != null) {
            item.setSelected(true);
            selection = openFrontend.getFrontend().getId();
        }
    }
}
