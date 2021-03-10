package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;

public interface AclGxtService extends GxtServiceBase {

    public SecurityManagerModel getSecurityManager();
    public void invalidateSession();
}