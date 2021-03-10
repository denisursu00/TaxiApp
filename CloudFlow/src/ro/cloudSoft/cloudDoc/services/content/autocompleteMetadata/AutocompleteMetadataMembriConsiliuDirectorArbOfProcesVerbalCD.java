package ro.cloudSoft.cloudDoc.services.content.autocompleteMetadata;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentPrezentaCdPvgConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentProcesVerbalCdConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.content.DocumentPlugin;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutocompleteMetadataResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class AutocompleteMetadataMembriConsiliuDirectorArbOfProcesVerbalCD extends AutocompleteMetadata {
	
	private static final LogHelper logger = LogHelper.getInstance(AutocompleteMetadataMembriConsiliuDirectorArbOfProcesVerbalCD.class);
	
	private DocumentPlugin documentPlugin;
	private DocumentTypeDao documentTypeDao;
	private DocumentPrezentaCdPvgConstants prezentaCdPvgConstants;
	private DocumentProcesVerbalCdConstants procesVerbalCdConstants;
	
	public AutocompleteMetadataMembriConsiliuDirectorArbOfProcesVerbalCD() {
		this.resolveDependencies();
	}
	
	private void resolveDependencies() {
		this.documentPlugin = SpringUtils.getBean(DocumentPlugin.class);
		this.documentTypeDao = SpringUtils.getBean(DocumentTypeDao.class);
		this.prezentaCdPvgConstants = SpringUtils.getBean(DocumentPrezentaCdPvgConstants.class);
		this.procesVerbalCdConstants = SpringUtils.getBean(DocumentProcesVerbalCdConstants.class);
	}
	
	@Override
	protected AutocompleteMetadataResponseModel doAutocomplete(AutocompleteMetadataRequestModel request, SecurityManager userSecurity) throws AppException {
		
		AutocompleteMetadataResponseModel response = new AutocompleteMetadataResponseModel();
		
		try {
		
			String sourceMetadataValue = request.getSourceMetadataValue();		
			if (StringUtils.isBlank(sourceMetadataValue)) {
				return response;
			}
	
			DocumentType prezentaCdPvgdocumentType = documentTypeDao.getDocumentTypeByName(prezentaCdPvgConstants.getDocumentTypeName());
			MetadataCollection pInformatiiMembriiCdMetadataDefinition = DocumentTypeUtils.getMetadataCollectionDefinitionByName(prezentaCdPvgdocumentType, prezentaCdPvgConstants.getInformatiiMembriiCdMetadataName());
			MetadataDefinition pMembruOfInformatiiMembriiCdMetadaDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pInformatiiMembriiCdMetadataDefinition.getMetadataDefinitions(), prezentaCdPvgConstants.getMembruOfInformatiiMembriiCdMetadataName());
			MetadataDefinition pFunctieMembruOfInformatiiMembriiCdMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pInformatiiMembriiCdMetadataDefinition.getMetadataDefinitions(), prezentaCdPvgConstants.getFunctieMembruOfInformatiiMembriiCdMetadataName());
	
			DocumentType procesVerbalCdDocumentType = documentTypeDao.getDocumentTypeByName(procesVerbalCdConstants.getDocumentTypeName());
			MetadataCollection pvMembriConsiliuDirectorArbMetadataDefinition = DocumentTypeUtils.getMetadataCollectionDefinitionByName(procesVerbalCdDocumentType, procesVerbalCdConstants.getMembriiConsiliuDirectorArbMetadataName());
			MetadataDefinition pvMembruOfMembriiConsiliuDirectorArbMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pvMembriConsiliuDirectorArbMetadataDefinition.getMetadataDefinitions(), procesVerbalCdConstants.getMembruOfMembriiConsiliuDirectorArbMetadataName());
			MetadataDefinition pvFunctieMembruOfMembriiConsiliuDirectorArbMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pvMembriConsiliuDirectorArbMetadataDefinition.getMetadataDefinitions(), procesVerbalCdConstants.getFunctieMembruOfMembriiConsiliuDirectorArbMetadataName());
			
			String[] workspaceAndDocumentId = StringUtils.splitByWholeSeparator(sourceMetadataValue, ":");
			Document document = documentPlugin.getDocumentById(workspaceAndDocumentId[1], workspaceAndDocumentId[0], userSecurity);
			
			List<CollectionInstanceModel> pvMembriConsiliuDirectorArbCollectionInstances = new ArrayList<>();
			
			List<CollectionInstance> pvInformatiiMembriCdCollectionInstances = DocumentUtils.getMetadataCollectionInstance(document, prezentaCdPvgdocumentType, pInformatiiMembriiCdMetadataDefinition.getName());
			if (CollectionUtils.isNotEmpty(pvInformatiiMembriCdCollectionInstances)) {			
				for (CollectionInstance collectionInstance : pvInformatiiMembriCdCollectionInstances) {
					CollectionInstanceModel pvMembriConsiliuDirectorArbCollectionInstance = new CollectionInstanceModel();
					List<MetadataInstanceModel> pvMembriConsiliuDirectorArbCollectionInstanceList = new ArrayList<>();
					for (MetadataInstance metadataInstance : collectionInstance.getMetadataInstanceList()) {					
						if (metadataInstance.getMetadataDefinitionId().equals(pMembruOfInformatiiMembriiCdMetadaDefinition.getId())) {
							MetadataInstanceModel instanceModel = createMetadataInstance(pvMembruOfMembriiConsiliuDirectorArbMetadataDefinition.getId(), metadataInstance.getValue());
							pvMembriConsiliuDirectorArbCollectionInstanceList.add(instanceModel);
						} else if (metadataInstance.getMetadataDefinitionId().equals(pFunctieMembruOfInformatiiMembriiCdMetadataDefinition.getId())) {
							MetadataInstanceModel instanceModel = createMetadataInstance(pvFunctieMembruOfMembriiConsiliuDirectorArbMetadataDefinition.getId(), metadataInstance.getValue());
							pvMembriConsiliuDirectorArbCollectionInstanceList.add(instanceModel);
						}
					}
					pvMembriConsiliuDirectorArbCollectionInstance.setMetadataInstanceList(pvMembriConsiliuDirectorArbCollectionInstanceList);
					pvMembriConsiliuDirectorArbCollectionInstances.add(pvMembriConsiliuDirectorArbCollectionInstance);
				}
			}
			
			MetadataCollectionInstanceModel pvMetadataCollectionInstance = new MetadataCollectionInstanceModel();
			pvMetadataCollectionInstance.setMetadataDefinitionId(pvMembriConsiliuDirectorArbMetadataDefinition.getId());
			pvMetadataCollectionInstance.setCollectionInstanceRows(pvMembriConsiliuDirectorArbCollectionInstances);		
		
			response.setMetadataCollectionInstance(pvMetadataCollectionInstance);
		
		} catch (Exception e) {
			logger.error("targetMetadataCollectionDefinitionId: " + request.getTargetMetadataCollectionDefinitionId() + ", " + e.getMessage(), "doAutocomplete", userSecurity);
			throw new AppException();
		}
		return response;
	}
}
