package ro.cloudSoft.cloudDoc.presentation.client.shared.services.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentCreationInDefaultLocationViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.GxtServiceBase;

public interface DocumentTypeGxtService extends GxtServiceBase {
	
	List<DocumentTypeModel> getAllDocumentTypesForDisplay() throws PresentationException;
	
	DocumentTypeModel getDocumentTypeById(Long id) throws PresentationException;
	
	/**
	 * Pregateste pentru adaugare sau modificare.
	 * <br><br>
	 * NOTA: Aceasta metoda trebuie apelata inainte actiunilor propriu-zise pentru ca face "curatenie" in spatiul
	 * temporar de pe server. 
	 */
	void prepareForAddOrEdit() throws PresentationException;
	
	void saveDocumentType(DocumentTypeModel documentTypeModel) throws PresentationException;
	
	void deleteDocumentType(Long id) throws PresentationException;
	
	List<UserMetadataDefinitionModel> getUserMetadataDefinitions(List<Long> documentTypeIds) throws PresentationException;
	
	List<DocumentTypeModel> getAvailableDocumentTypes() throws PresentationException;
	
	List<DocumentCreationInDefaultLocationViewModel> getDocumentCreationInDefaultLocationViews() throws PresentationException;
	
	List<DocumentTypeModel> getAvailableDocumentTypesForSearch() throws PresentationException;

	List<DocumentTypeModel> getDocumentTypesWithNoWorkflow() throws PresentationException;

	void clearTemporaryTemplates();
}