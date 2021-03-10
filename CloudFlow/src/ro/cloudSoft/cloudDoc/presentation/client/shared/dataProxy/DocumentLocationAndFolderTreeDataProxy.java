package ro.cloudSoft.cloudDoc.presentation.client.shared.dataProxy;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentLocationAndFolderTreeDataProxy extends RpcProxy<List<ModelData>> {
	
	@Override
	protected void load(Object loadConfig, final AsyncCallback<List<ModelData>> callback) {
		if (loadConfig == null) {
			LoadingManager.get().loading();
			GwtServiceProvider.getDocumentLocationService().getAllDocumentLocationsAsModelData(new AsyncCallback<List<ModelData>>() {
				
				@Override
				public void onFailure(Throwable exception) {
					callback.onFailure(exception);
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				
				@Override
				public void onSuccess(List<ModelData> allDocumentLocations) {
					callback.onSuccess(allDocumentLocations);
					LoadingManager.get().loadingComplete();
				}
			});
		} else {
			if (loadConfig instanceof DocumentLocationModel) {
				
				DocumentLocationModel parentDocumentLocation = (DocumentLocationModel) loadConfig;
				String parentDocumentLocationRealName = parentDocumentLocation.getRealName();
				
				LoadingManager.get().loading();
				GwtServiceProvider.getDocumentLocationService().getFoldersFromDocumentLocation(parentDocumentLocationRealName, new AsyncCallback<List<ModelData>>() {
					
					@Override
					public void onFailure(Throwable exception) {
						callback.onFailure(exception);
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					
					@Override
					public void onSuccess(List<ModelData> folders) {
						callback.onSuccess(folders);
						LoadingManager.get().loadingComplete();
					}
				});
			} else if (loadConfig instanceof FolderModel) {
				
				FolderModel parentFolder = (FolderModel) loadConfig;
				String parentFolderId = parentFolder.getId();
				
				String parentDocumentLocationRealName = parentFolder.getDocumentLocationRealName();
				
				LoadingManager.get().loading();
				GwtServiceProvider.getFolderService().getFoldersFromFolder(parentDocumentLocationRealName, parentFolderId, new AsyncCallback<List<ModelData>>() {
					
					@Override
					public void onFailure(Throwable exception) {
						callback.onFailure(exception);
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					
					@Override
					public void onSuccess(List<ModelData> results) {
						callback.onSuccess(results);
						LoadingManager.get().loadingComplete();
					}
				});
			}
		}
	}
}