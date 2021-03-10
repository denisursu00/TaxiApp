package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.DynamicallyChangeComboBoxListWidthByDisplayFieldLengthListener;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentLocationComboBox extends ComboBox<DocumentLocationModel> {
	
	public DocumentLocationComboBox() {
		setEditable(false);
		setForceSelection(true);
		setTriggerAction(TriggerAction.ALL);
		setDisplayField(DocumentLocationModel.PROPERTY_NAME);			
		
		// proxy pentru loader
		RpcProxy<List<DocumentLocationModel>> proxy = new RpcProxy<List<DocumentLocationModel>>() {
			@Override
			protected void load(Object loadConfig, final AsyncCallback<List<DocumentLocationModel>> callback) {
				LoadingManager.get().loading();
                GwtServiceProvider.getDocumentLocationService().getAllDocumentLocations(new AsyncCallback<List<DocumentLocationModel>>() {
                	@Override
                	public void onFailure(Throwable exception) {
                		callback.onFailure(exception);
                		LoadingManager.get().loadingComplete();
                	}
                	@Override
                	public void onSuccess(List<DocumentLocationModel> documentLocations) {
                		callback.onSuccess(documentLocations);
                		LoadingManager.get().loadingComplete();
                	}
                });
			}
		};
		// loader pentru store
		BaseListLoader<BaseListLoadResult<DocumentLocationModel>> workspaceComboBoxLoader = new BaseListLoader<BaseListLoadResult<DocumentLocationModel>>(proxy) {
            @Override
            protected void onLoadFailure(Object loadConfig, Throwable exception) {
				MessageUtils.displayError(exception);
            }
        };	        
        // store
        ListStore<DocumentLocationModel> listStore = new ListStore<DocumentLocationModel>(workspaceComboBoxLoader);
        // Lista cu document locations trebuie ordonata dupa nume.
        listStore.setStoreSorter(new StoreSorter<DocumentLocationModel>() {
        	@Override
        	public int compare(Store<DocumentLocationModel> store, DocumentLocationModel m1, DocumentLocationModel m2, String property) {
        		return m1.getName().compareToIgnoreCase(m2.getName());
        	}
        });
		setStore(listStore);
		
		DynamicallyChangeComboBoxListWidthByDisplayFieldLengthListener.attachTo(this);
	}
}