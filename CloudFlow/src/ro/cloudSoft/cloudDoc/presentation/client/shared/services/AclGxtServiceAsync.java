package ro.cloudSoft.cloudDoc.presentation.client.shared.services;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AclGxtServiceAsync extends GxtServiceBaseAsync {
	
    public abstract void getSecurityManager(AsyncCallback<SecurityManagerModel> asyncCallback);

    public abstract void invalidateSession(AsyncCallback<Void> asyncCallback);
}