package ro.cloudSoft.cloudDoc.presentation.client.shared.services.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentCreationInDefaultLocationViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBaseAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DocumentTypeGxtServiceAsync extends GxtServiceBaseAsync {

	void getAllDocumentTypesForDisplay(AsyncCallback<List<DocumentTypeModel>> callback);

	void getDocumentTypeById(Long id, AsyncCallback<DocumentTypeModel> callback);

	/**
	 * Pregateste pentru adaugare sau modificare.
	 * <br><br>
	 * NOTA: Aceasta metoda trebuie apelata inainte actiunilor propriu-zise pentru ca face "curatenie" in spatiul
	 * temporar de pe server. 
	 */
	void prepareForAddOrEdit(AsyncCallback<Void> callback);

	void saveDocumentType(DocumentTypeModel documentTypeModel, AsyncCallback<Void> callback);

	void deleteDocumentType(Long id, AsyncCallback<Void> callback);

	void getUserMetadataDefinitions(List<Long> documentTypeIds, AsyncCallback<List<UserMetadataDefinitionModel>> callback);
	
	void getAvailableDocumentTypes(AsyncCallback<List<DocumentTypeModel>> callback);
	
	void getDocumentCreationInDefaultLocationViews(AsyncCallback<List<DocumentCreationInDefaultLocationViewModel>> callback);

	void getAvailableDocumentTypesForSearch(AsyncCallback<List<DocumentTypeModel>> callback);

	void getDocumentTypesWithNoWorkflow(AsyncCallback<List<DocumentTypeModel>> callback);
	
	void clearTemporaryTemplates(AsyncCallback<Void> callback);	
}