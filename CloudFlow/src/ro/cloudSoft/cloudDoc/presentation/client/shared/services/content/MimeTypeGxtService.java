package ro.cloudSoft.cloudDoc.presentation.client.shared.services.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBase;

public interface MimeTypeGxtService extends GxtServiceBase {
	
	List<MimeTypeModel> getAllMimeTypes() throws PresentationException;
	
	/* Daca nu se gaseste un tip de atasament cu id-ul dat ca parametru, se returneaza null. */
	MimeTypeModel getMimeTypeById(Long mimeTypeId) throws PresentationException;
	
	void saveMimeType(MimeTypeModel mimeTypeModel) throws PresentationException;
	
	void deleteMimeType(Long mimeTypeId) throws PresentationException;
}
