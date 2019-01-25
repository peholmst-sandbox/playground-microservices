package net.pkhapps.playground.microservices.portal.server.ui;

import com.vaadin.flow.component.html.Div;
import net.pkhapps.playground.microservices.directory.api.FrontendId;
import net.pkhapps.playground.microservices.directory.api.FrontendInstanceDescriptor;
import net.pkhapps.playground.microservices.directory.api.ServiceDirectory;
import net.pkhapps.playground.microservices.directory.api.Version;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class SideBarController implements NavigationListener {

    private final Div sideBarDiv;
    private final NavigationModel navigationModel;
    private final Map<FrontendId, SideBarNavigationItem> itemMap = new HashMap<>();

    private FrontendId selection;

    SideBarController(Div sideBarDiv, NavigationModel navigationModel) {
        this.sideBarDiv = sideBarDiv;
        this.navigationModel = navigationModel;
        navigationModel.addListener(this);
    }

    void populate(ServiceDirectory serviceDirectory) {
        sideBarDiv.removeAll();
        itemMap.clear();
        serviceDirectory.getFrontends().forEach(frontendDescriptor -> {
            var item = new SideBarNavigationItem(frontendDescriptor,
                    buildInstanceMap(frontendDescriptor.getId(), serviceDirectory), navigationModel);
            itemMap.put(frontendDescriptor.getId(), item);
            sideBarDiv.add(item);
            if (Objects.equals(selection, frontendDescriptor.getId())) {
                item.setSelected(true);
            }
        });
        if (!itemMap.containsKey(selection)) {
            selection = null;
        }
    }

    private Map<Version, FrontendInstanceDescriptor> buildInstanceMap(FrontendId id, ServiceDirectory serviceDirectory) {
        var map = new HashMap<Version, FrontendInstanceDescriptor>();
        serviceDirectory.getVersions(id)
                .forEach(version -> serviceDirectory.getInstance(id, version)
                        .ifPresent(instance -> map.put(instance.getVersion(), instance)));
        return map;
    }

    @Override
    public void onLeave(FrontendInstanceDescriptor frontend) {
        if (Objects.equals(selection, frontend.getId())) {
            var item = itemMap.get(selection);
            item.setSelected(false);
            selection = null;
        }
    }

    @Override
    public void onEnter(FrontendInstanceDescriptor frontend) {
        itemMap.values().forEach(item -> item.setSelected(false));
        var item = itemMap.get(frontend.getId());
        if (item != null) {
            item.setSelected(true);
            selection = frontend.getId();
        }
    }
}
