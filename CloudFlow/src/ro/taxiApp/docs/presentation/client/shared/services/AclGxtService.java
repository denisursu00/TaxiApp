package ro.taxiApp.docs.presentation.client.shared.services;

import ro.taxiApp.docs.presentation.client.shared.model.SecurityManagerModel;

public interface AclGxtService extends GxtServiceBase {

    public SecurityManagerModel getSecurityManager();
    public void invalidateSession();
}