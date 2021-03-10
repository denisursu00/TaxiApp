package ro.cloudSoft.cloudDoc.presentation.client.shared.services.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBaseAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MimeTypeGxtServiceAsync extends GxtServiceBaseAsync {
	
	void getAllMimeTypes(AsyncCallback<List<MimeTypeModel>> callback);
	
	/* Daca nu se gaseste un tip de atasament cu id-ul dat ca parametru, se returneaza null. */
	void getMimeTypeById(Long mimeTypeId, AsyncCallback<MimeTypeModel> callback);
	
	void saveMimeType(MimeTypeModel mimeTypeModel, AsyncCallback<Void> callback);
	
	void deleteMimeType(Long mimeTypeId, AsyncCallback<Void> callback);
}
