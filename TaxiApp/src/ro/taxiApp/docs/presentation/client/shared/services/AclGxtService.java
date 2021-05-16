package ro.taxiApp.docs.presentation.client.shared.services;

import ro.taxiApp.docs.domain.security.SecurityManager;
import ro.taxiApp.docs.presentation.client.shared.model.SecurityManagerModel;

public interface AclGxtService {
    public SecurityManagerModel getSecurityManager(SecurityManager securityManager);
}