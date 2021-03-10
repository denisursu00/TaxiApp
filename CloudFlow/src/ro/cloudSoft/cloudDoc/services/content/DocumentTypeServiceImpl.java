package ro.cloudSoft.cloudDoc.services.content;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.AppConstants;
import ro.cloudSoft.cloudDoc.dao.content.AutoNumberMetadataSequenceValueDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeTemplateDao;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.content.AutoNumberMetadataSequenceValue;
import ro.cloudSoft.cloudDoc.domain.content.DocumentCreationInDefaultLocationView;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.NomenclatorMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.NomenclatorMetadataDefinitionValueSelectionFilter;
import ro.cloudSoft.cloudDoc.domain.content.UserMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.domain.validators.DocumentTypeValidator;
import ro.cloudSoft.cloudDoc.services.AuditService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class DocumentTypeServiceImpl implements DocumentTypeService, InitializingBean {

	private FolderService folderService;
	private DocumentService documentService;
	
	private AuditService auditService;
	
    private DocumentTypeDao documentTypeDao;
    private DocumentTypeTemplateDao documentTypeTemplateDao;
    
    private AutoNumberMetadataSequenceValueDao autoNumberMetadataSequenceValueDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			folderService,
			documentService,
			
			auditService,
			
			documentTypeDao,
			documentTypeTemplateDao,
			autoNumberMetadataSequenceValueDao
		);
	}
	
	@Override
	public List<DocumentType> getAllDocumentTypesForDisplay() {
		/*
		 * TODO Deocamdata nu am o metoda care sa-mi returneze o lista cu toate
		 * tipurile de documente, la care sa fie completate doar ID-ul si
		 * campurile ce trebuie afisate in tabelul principal.
		 */
		return documentTypeDao.getAllDocumentTypesLazy();
	}
	
	@Override
	public DocumentType getDocumentTypeById(Long id) {
		return documentTypeDao.find(id);
	}
	
	@Override
	public DocumentType getDocumentTypeById(Long id, SecurityManager userSecurity) {
		DocumentType documentType = getDocumentTypeById(id);
		auditService.auditDocumentTypeOperation(userSecurity, documentType, AuditEntityOperation.READ);
		return documentType;
	}
	
	private void ensureAutoNumberMetadataSequencesExist(DocumentType documentType) {
		Set<Long> autoNumberMetadataDefinitionIds = Sets.newHashSet();
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_AUTO_NUMBER)) {
				autoNumberMetadataDefinitionIds.add(metadataDefinition.getId());
			}
		}
		if (!autoNumberMetadataDefinitionIds.isEmpty()) {
			Set<Long> definitionIdsForExistingSequences = autoNumberMetadataSequenceValueDao.getDefinitionIdsForExistingSequences(autoNumberMetadataDefinitionIds);	
			SetView<Long> definitionIdsForNonExistingSequences = Sets.difference(autoNumberMetadataDefinitionIds, definitionIdsForExistingSequences);
			for (Long definitionIdForNonExistingSequence : definitionIdsForNonExistingSequences) {
				AutoNumberMetadataSequenceValue autoNumberMetadataSequenceValue = new AutoNumberMetadataSequenceValue();
				autoNumberMetadataSequenceValue.setAutoNumberMetadataDefinitionId(definitionIdForNonExistingSequence);
				autoNumberMetadataSequenceValue.setSequenceValue(0);
				autoNumberMetadataSequenceValueDao.create(autoNumberMetadataSequenceValue);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public Long saveDocumentType(DocumentType documentType, Collection<DocumentTypeTemplate> templates,
			Collection<String> namesForTemplatesToDelete, SecurityManager userSecurity) throws AppException {
		
		boolean isDocumentTypeNew = documentType.isNew();
		
		new DocumentTypeValidator(documentType, folderService, userSecurity).validate();
		prepareJrMetadataPropertyNames(documentType);
		
		// Pentru ca modul de lucru intre layere este cum este, pentru filtrele de selectie la metadata nomenclator
		// vom sterge tot si vom adauga tot timpul (Mai trebuie si sa fie gata, sti?).		
		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
			if (metadataDefinition instanceof NomenclatorMetadataDefinition) {
				NomenclatorMetadataDefinition nomenclatorMetadataDefinition = (NomenclatorMetadataDefinition) metadataDefinition;
				if (nomenclatorMetadataDefinition.getId() != null) {
					this.documentTypeDao.deleteAllValueSelectionFiltersOfNomenclatorMetadataDefinition(nomenclatorMetadataDefinition.getId());
				}
				if (CollectionUtils.isNotEmpty(nomenclatorMetadataDefinition.getValueSelectionFilters())) {
					for (NomenclatorMetadataDefinitionValueSelectionFilter filter : nomenclatorMetadataDefinition.getValueSelectionFilters()) {
						filter.setId(null); // ca sa fie inserat
					}
				}
			}
		}
		
		Long documentTypeIdAfterSave = this.documentTypeDao.saveDocumentType(documentType);
		
		ensureAutoNumberMetadataSequencesExist(documentType);
		
		documentTypeTemplateDao.saveAll(documentTypeIdAfterSave, templates);
		documentTypeTemplateDao.delete(documentTypeIdAfterSave, namesForTemplatesToDelete);
		
		AuditEntityOperation operation = (isDocumentTypeNew) ? AuditEntityOperation.ADD : AuditEntityOperation.MODIFY;
		auditService.auditDocumentTypeOperation(userSecurity, documentType, operation);
		
		return documentTypeIdAfterSave;
	}
	
	private void prepareJrMetadataPropertyNames(DocumentType documentType) {
		
		if (CollectionUtils.isNotEmpty(documentType.getMetadataDefinitions())) {
			
			Map<Long, String> jrPropertyNameByDefinitionId = new HashMap<Long, String>();
			if (!documentType.isNew()) {
				jrPropertyNameByDefinitionId = documentTypeDao.getMetadataDefinitionIdAndJrPropertyNameMapOfDocumentType(documentType.getId());
			}			
			Collection<String> allMetadatasJrPropertyNames = jrPropertyNameByDefinitionId.values();
						
			int lastSuffixNrOfPrefixString = getLastSuffixNrOfJrPropertNamePrefix(AppConstants.PREFIX_JR_PROPERTY_NAME_METADATA_STRING, allMetadatasJrPropertyNames);
			int lastSuffixNrOfPrefixStringMultiple = getLastSuffixNrOfJrPropertNamePrefix(AppConstants.PREFIX_JR_PROPERTY_NAME_METADATA_STRING_MULTIPLE, allMetadatasJrPropertyNames);
			int lastSuffixNrOfPrefixNumber = getLastSuffixNrOfJrPropertNamePrefix(AppConstants.PREFIX_JR_PROPERTY_NAME_METADATA_NUMBER, allMetadatasJrPropertyNames);
			
			for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {
				if (metadataDefinition.getId() == null) {
					if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_NUMERIC)) {
						lastSuffixNrOfPrefixNumber++;
						metadataDefinition.setJrPropertyName(AppConstants.PREFIX_JR_PROPERTY_NAME_METADATA_NUMBER + lastSuffixNrOfPrefixNumber);
					} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_LIST) 
							|| metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_DOCUMENT)
							|| metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_PROJECT)) {
						lastSuffixNrOfPrefixStringMultiple++;
						metadataDefinition.setJrPropertyName(AppConstants.PREFIX_JR_PROPERTY_NAME_METADATA_STRING_MULTIPLE + lastSuffixNrOfPrefixStringMultiple);
					} else {
						lastSuffixNrOfPrefixString++;
						metadataDefinition.setJrPropertyName(AppConstants.PREFIX_JR_PROPERTY_NAME_METADATA_STRING + lastSuffixNrOfPrefixString);
					}
				} else {
					String existingJrPropertyName = jrPropertyNameByDefinitionId.get(metadataDefinition.getId());
					if (StringUtils.isBlank(existingJrPropertyName)) {
						throw new RuntimeException("metadata definition cu id [" + metadataDefinition.getId() + "] ar fi trebuit sa aibe JR property name");
					}
					metadataDefinition.setJrPropertyName(jrPropertyNameByDefinitionId.get(metadataDefinition.getId()));
				}
			}
		}
		
		if (CollectionUtils.isNotEmpty(documentType.getMetadataCollections())) {
			
			Map<Long, String> jrPropertyNameByDefinitionId = new HashMap<>();
			if (!documentType.isNew()) {
				jrPropertyNameByDefinitionId = documentTypeDao.getMetadataCollectionIdAndJrPropertyNameMapOfDocumentType(documentType.getId());				
			}
			Collection<String> allMetadataCollectionJrPropertyNames = jrPropertyNameByDefinitionId.values();
			
			int lastSuffixNrOfPrefixCollection = getLastSuffixNrOfJrPropertNamePrefix(AppConstants.PREFIX_JR_PROPERTY_NAME_METADATA_COLLECTION, allMetadataCollectionJrPropertyNames);
			
			for (MetadataCollection metadataCollection : documentType.getMetadataCollections()) {
				if (metadataCollection.getId() == null) {
					lastSuffixNrOfPrefixCollection++;
					metadataCollection.setJrPropertyName(AppConstants.PREFIX_JR_PROPERTY_NAME_METADATA_COLLECTION + lastSuffixNrOfPrefixCollection);
				} else {
					String existingJrPropertyName = jrPropertyNameByDefinitionId.get(metadataCollection.getId());
					if (StringUtils.isBlank(existingJrPropertyName)) {
						throw new RuntimeException("metadata collection definition cu id [" + metadataCollection.getId() + "] ar fi trebuit sa aibe JR property name");
					}
					metadataCollection.setJrPropertyName(jrPropertyNameByDefinitionId.get(metadataCollection.getId()));
				}
			}
		}
	}
	
	private int getLastSuffixNrOfJrPropertNamePrefix(String jrPropertyNamePrefix, Collection<String> allJrPropertyNames) {
		int maxSuffixNr = 0;
		for (String jrPropertyName : allJrPropertyNames) {
			if (jrPropertyName.startsWith(jrPropertyNamePrefix)) {
				String suffix = StringUtils.substringAfterLast(jrPropertyName, jrPropertyNamePrefix);
				if (StringUtils.isBlank(suffix)) {
					throw new RuntimeException("invalid jr property name detected");
				}
				int suffixAsInt = Integer.valueOf(suffix);
				if (maxSuffixNr < suffixAsInt) {
					maxSuffixNr = suffixAsInt;
				}
			}
		}
		return maxSuffixNr;
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteDocumentType(Long documentTypeId, SecurityManager userSecurity) throws AppException {
		
		if (documentService.existDocumentsOfType(documentTypeId, userSecurity)) {
			throw new AppException(AppExceptionCodes.CANNOT_DELETE_DOCUMENTS_OF_TYPE_EXIST);
		}
		
		DocumentType documentType = getDocumentTypeById(documentTypeId);
		
		folderService.removeDocumentTypeRestrictionForAllFolders(documentTypeId, userSecurity);
		
		documentTypeTemplateDao.deleteAll(documentTypeId);
		documentTypeDao.removeDocumentType(documentTypeId);
		
		auditService.auditDocumentTypeOperation(userSecurity, documentType, AuditEntityOperation.DELETE);
	}
	
	@Override
	public Long getMetadataDefinitionIdByNameAndDocumentType(String name, Long documentTypeId) {
		return documentTypeDao.getMetadataDefinitionIdByNameAndDocumentType(name, documentTypeId);
	}

	@Override
	public List<UserMetadataDefinition> getUserMetadataDefinitions(List<Long> documentTypeIds) {
		return documentTypeDao.getUserMetadataDefinitions(documentTypeIds);
	}
	
	@Override
	public Map<String, Map<String, String>> getListItemLabelByListItemValueByMetadataName(Long documentTypeId) {
		return documentTypeDao.getListItemLabelByListItemValueByMetadataName(documentTypeId);
	}
	
	@Override
	public List<DocumentType> getAvailableDocumentTypes(SecurityManager userSecurity) {
		return documentTypeDao.getAvailableDocumentTypes(userSecurity);
	}
	
	@Override
	public List<DocumentCreationInDefaultLocationView> getDocumentCreationInDefaultLocationViews(SecurityManager userSecurity) {
		return documentTypeDao.getDocumentCreationInDefaultLocationViews(userSecurity);
	}
	
	@Override
	public List<DocumentType> getAvailableDocumentTypesForSearch(SecurityManager userSecurity) {
		// Deocamdata, se aduc toate tipurile de documente.
		return getAllDocumentTypesForDisplay();
	}
	
	@Override
	public Map<Long, String> getDocumentTypesNameMap(Set<Long> ids) {
		return documentTypeDao.getDocumentTypesNameMap(ids);
	}

	@Override
	public List<DocumentType> getDocumentTypesWithNoWorkflow() {
		return documentTypeDao.getDocumentTypesWithNoWorkflow();
	}

	@Override
	public DocumentTypeTemplate getTemplate(Long documentTypeId, String fileName) {
		return this.documentTypeDao.getTemplate(documentTypeId, fileName);
	}

	@Override
	public List<DocumentTypeTemplate> getTemplates(Long documentTypeId) {
		return this.documentTypeDao.getTemplates(documentTypeId);
	}
	
	@Override
	public SetMultimap<Long, String> getMetadataNamesByDocumentTypeId(Collection<Long> documentTypeIds) {
		return documentTypeDao.getMetadataNamesByDocumentTypeId(documentTypeIds);
	}
	
	@Override
	public boolean isMimeTypeUsedInDocumentTypes(Long mimeTypeId) {
		return documentTypeDao.isMimeTypeUsedInDocumentTypes(mimeTypeId);
	}
	
	@Override
	public String getDocumentTypeName(Long id) {
		return documentTypeDao.getDocumentTypeName(id);
	}

	@Override
	public boolean existDocumentTypeWithName(String name) {
		return documentTypeDao.existDocumentTypeWithName(name);
	}
	
	@Override
	public DocumentType getDocumentTypeByName(String documentTypeName) {
		return documentTypeDao.getDocumentTypeByName(documentTypeName);
	}


	@Override
	public Long getDocumentTypeIdByName(String documentTypeName) {
		DocumentType prezentaAgaDocumentType = documentTypeDao.getDocumentTypeByName(documentTypeName);
		return prezentaAgaDocumentType.getId();
	}
	
	@Override
	public List<DocumentType> getDocumentTypesByNames(String... documentTypeNames) {
		return documentTypeDao.getDocumentTypesByNames(documentTypeNames);
	}
	
	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
	}
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}
	public void setDocumentTypeTemplateDao(DocumentTypeTemplateDao documentTypeTemplateDao) {
		this.documentTypeTemplateDao = documentTypeTemplateDao;
	}
	public void setAutoNumberMetadataSequenceValueDao(AutoNumberMetadataSequenceValueDao autoNumberMetadataSequenceValueDao) {
		this.autoNumberMetadataSequenceValueDao = autoNumberMetadataSequenceValueDao;
	}

}