package net.pkhapps.playground.microservices.directory.api;

import java.net.URI;

public class ResourceInstanceId<ID> {

    private final ID resourceId;
    private final URI clientUri;

    public ResourceInstanceId(ID resourceId, URI clientUri) {
        this.resourceId = resourceId;
        this.clientUri = clientUri;
    }
}
