package ro.cloudSoft.cloudDoc.dao.content;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.content.DocumentCreationInDefaultLocationView;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.UserMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

import com.google.common.collect.SetMultimap;

public interface DocumentTypeDao {

	/**
	 * Salveaza tipul de document.
	 * Daca tipul de document era nou, dupa salvare va avea ID-ul completat.
	 * 
	 * @return ID-ul tipului de document
	 */
	public Long saveDocumentType(DocumentType documentType);
	
	public DocumentType find(Long id);
	
	public void removeDocumentType(Long id);

	public List<DocumentType> getAllDocumentTypesLazy();
	
	public List<UserMetadataDefinition> getUserMetadataDefinitions(List<Long> documentTypeIds);
	
	/**
	 * Returneaza un Map cu etichetele item-ulor posibili din lista, grupate dupa
	 * valorile lor, apoi dupa numele metadatei de tip lista pentru care sunt item-ii.
	 * 
	 * @param documentTypeId ID-ul tipului de document pentru care sa se aduca valorile
	 */
	Map<String, Map<String, String>> getListItemLabelByListItemValueByMetadataName(Long documentTypeId);
	
	public List<DocumentType> getAvailableDocumentTypes(SecurityManager userSecurity);
	
	public List<DocumentCreationInDefaultLocationView> getDocumentCreationInDefaultLocationViews(SecurityManager userSecurity);

	public MetadataDefinition getMetadataDefinition(Long long1);
	
	public Map<Long, String> getDocumentTypesNameMap(Set<Long> ids);
	
	public List<DocumentType> getDocumentTypesWithNoWorkflow();
	
	DocumentTypeTemplate getTemplate(Long documentTypeId, String fileName);

	List<DocumentTypeTemplate> getTemplates(Long documentTypeId);
	
	SetMultimap<Long, String> getMetadataNamesByDocumentTypeId(Collection<Long> documentTypeIds);
	
	boolean isMimeTypeUsedInDocumentTypes(Long mimeTypeId);

	String getDocumentTypeName(Long id);
	
	boolean existDocumentTypeWithName(String name);
	
	public MetadataCollection getMetadataCollectionDefinition(Long collectionMetadataId);
	
	Map<Long, String> getMetadataDefinitionIdAndJrPropertyNameMapOfDocumentType(Long documentTypeId);
	
	Long getMetadataDefinitionIdByNameAndDocumentType(String name, Long documentTypeId);
	
	Map<Long, String> getMetadataCollectionIdAndJrPropertyNameMapOfDocumentType(Long documentTypeId);
	
	DocumentType getDocumentTypeByName(String documentTypeName);
	
	Map<Long, MetadataDefinition> getMetadataDefinionsMapByIds(List<Long> metadataDefinitionIds);
	
	List<DocumentType> getDocumentTypesByNames(String... documentTypeNames);
	
	public List<DocumentType> getArchivableDocumentTypes();
	
	void deleteAllValueSelectionFiltersOfNomenclatorMetadataDefinition(Long metadataDefinitionId);

}