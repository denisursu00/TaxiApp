package ro.cloudSoft.cloudDoc.services.content;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.SetMultimap;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.DocumentCreationInDefaultLocationView;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.UserMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public interface DocumentTypeService {
	
	List<DocumentType> getAllDocumentTypesForDisplay();
	
	DocumentType getDocumentTypeById(Long id);
	
	DocumentType getDocumentTypeById(Long id, SecurityManager userSecurity);

	/**
	 * Salveaza tipul de document, impreuna cu template-urile sale.
	 * Daca tipul de document era nou, dupa salvare va avea ID-ul completat.
	 * 
	 * @param documentType tipul de document
	 * @param templates template-urile tipului de document
	 * @param namesForTemplatesToDelete numele template-urilor care trebuie sterse
	 * @param userSecurity datele utilizatorului curent
	 * 
	 * @return ID-ul tipului de document
	 */
	Long saveDocumentType(DocumentType documentType, Collection<DocumentTypeTemplate> templates,
		Collection<String> namesForTemplatesToDelete, SecurityManager userSecurity) throws AppException;
	
	void deleteDocumentType(Long id, SecurityManager userSecurity) throws AppException;
	
	List<UserMetadataDefinition> getUserMetadataDefinitions(List<Long> documentTypeIds);

	/**
	 * Returneaza un Map cu etichetele item-ulor posibili din lista, grupate dupa
	 * valorile lor, apoi dupa numele metadatei de tip lista pentru care sunt item-ii.
	 * 
	 * @param documentTypeId ID-ul tipului de document pentru care sa se aduca valorile
	 */
	Map<String, Map<String, String>> getListItemLabelByListItemValueByMetadataName(Long documentTypeId);
	
	List<DocumentType> getAvailableDocumentTypes(SecurityManager userSecurity);
	
	List<DocumentCreationInDefaultLocationView> getDocumentCreationInDefaultLocationViews(SecurityManager userSecurity);
	
	List<DocumentType> getAvailableDocumentTypesForSearch(SecurityManager userSecurity);
	
	Map<Long, String> getDocumentTypesNameMap(Set<Long> ids);
	
	List<DocumentType> getDocumentTypesWithNoWorkflow();
	
	DocumentTypeTemplate getTemplate(Long documentTypeId, String fileName);

	List<DocumentTypeTemplate> getTemplates(Long documentTypeId);
	
	/**
	 * Aduce numele tuturor metadatelor care apartin de tipurile de documente cu ID-urile specificate,
	 * grupate dupa ID-ul tipului de document.
	 */
	SetMultimap<Long, String> getMetadataNamesByDocumentTypeId(Collection<Long> documentTypeIds);

	boolean isMimeTypeUsedInDocumentTypes(Long mimeTypeId);
	
	String getDocumentTypeName(Long id);
	
	boolean existDocumentTypeWithName(String name);
	
	DocumentType getDocumentTypeByName(String documentTypeName);
	
	Long getDocumentTypeIdByName(String documentTypeName);
	
	List<DocumentType> getDocumentTypesByNames(String... documentTypeNames);
	
	Long getMetadataDefinitionIdByNameAndDocumentType(String name, Long documentTypeId);
	
}