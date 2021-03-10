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

public class AutocompleteInformatiiInvitatiArbOfProcesVerbalCD extends AutocompleteMetadata {
	
	private static final LogHelper logger = LogHelper.getInstance(AutocompleteInformatiiInvitatiArbOfProcesVerbalCD.class);
	
	private DocumentPlugin documentPlugin;
	private DocumentTypeDao documentTypeDao;
	private DocumentPrezentaCdPvgConstants prezentaCdPvgConstants;
	private DocumentProcesVerbalCdConstants procesVerbalCdConstants;
	
	public AutocompleteInformatiiInvitatiArbOfProcesVerbalCD() {
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
			MetadataCollection pInformatiiInvitatiArbMetadataDefinition = DocumentTypeUtils.getMetadataCollectionDefinitionByName(prezentaCdPvgdocumentType, prezentaCdPvgConstants.getInformatiiInvitatiArbMetadataName());
			MetadataDefinition pInvitatArbOfInformatiiInvitatiArbMetadaDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pInformatiiInvitatiArbMetadataDefinition.getMetadataDefinitions(), prezentaCdPvgConstants.getInvitatArbOfInformatiiInvitatiArbMetadataName());
			MetadataDefinition pFunctieInvitatArbOfInformatiiInvitatiArbMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pInformatiiInvitatiArbMetadataDefinition.getMetadataDefinitions(), prezentaCdPvgConstants.getFunctieInvitatArbOfInformatiiInvitatiArbMetadataName());
			
			DocumentType procesVerbalCdDocumentType = documentTypeDao.getDocumentTypeByName(procesVerbalCdConstants.getDocumentTypeName());
			MetadataCollection pvInformatiiInvitatiArbMetadataDefinition = DocumentTypeUtils.getMetadataCollectionDefinitionByName(procesVerbalCdDocumentType, procesVerbalCdConstants.getInformatiiInvitatiArbMetadataName());
			MetadataDefinition pvInvitatExecutivArbOfInformatiiInvitatiArbMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pvInformatiiInvitatiArbMetadataDefinition.getMetadataDefinitions(), procesVerbalCdConstants.getInvitatExecutivArbOfInformatiiInvitatiArbMetadataName());
			MetadataDefinition pvFunctieInvitatExecutivArbOfInformatiiInvitatiArbMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pvInformatiiInvitatiArbMetadataDefinition.getMetadataDefinitions(), procesVerbalCdConstants.getFunctieInvitatExecutivArbOfInformatiiInvitatiArbMetadataName());
			
			String[] workspaceAndDocumentId = StringUtils.splitByWholeSeparator(sourceMetadataValue, ":");
			Document document = documentPlugin.getDocumentById(workspaceAndDocumentId[1], workspaceAndDocumentId[0], userSecurity);
			
			List<CollectionInstanceModel> pvInformatiiInvitatiArbCollectionInstances = new ArrayList<>();
			
			List<CollectionInstance> pInformatiiInvitatiArbCollectionInstances = DocumentUtils.getMetadataCollectionInstance(document, prezentaCdPvgdocumentType, pInformatiiInvitatiArbMetadataDefinition.getName());
			if (CollectionUtils.isNotEmpty(pInformatiiInvitatiArbCollectionInstances)) {			
				for (CollectionInstance collectionInstance : pInformatiiInvitatiArbCollectionInstances) {
					CollectionInstanceModel pvInformatiiInvitatiArbCollectionInstance = new CollectionInstanceModel();
					List<MetadataInstanceModel> pvInformatiiInvitatiArbCollectionInstancesCollectionInstanceList = new ArrayList<>();
					for (MetadataInstance metadataInstance : collectionInstance.getMetadataInstanceList()) {					
						if (metadataInstance.getMetadataDefinitionId().equals(pInvitatArbOfInformatiiInvitatiArbMetadaDefinition.getId())) {
							MetadataInstanceModel instanceModel = createMetadataInstance(pvInvitatExecutivArbOfInformatiiInvitatiArbMetadataDefinition.getId(), metadataInstance.getValue());
							pvInformatiiInvitatiArbCollectionInstancesCollectionInstanceList.add(instanceModel);
						} else if (metadataInstance.getMetadataDefinitionId().equals(pFunctieInvitatArbOfInformatiiInvitatiArbMetadataDefinition.getId())) {
							MetadataInstanceModel instanceModel = createMetadataInstance(pvFunctieInvitatExecutivArbOfInformatiiInvitatiArbMetadataDefinition.getId(), metadataInstance.getValue());
							pvInformatiiInvitatiArbCollectionInstancesCollectionInstanceList.add(instanceModel);
						}						
					}
					pvInformatiiInvitatiArbCollectionInstance.setMetadataInstanceList(pvInformatiiInvitatiArbCollectionInstancesCollectionInstanceList);
					pvInformatiiInvitatiArbCollectionInstances.add(pvInformatiiInvitatiArbCollectionInstance);
				}
			}
			
			MetadataCollectionInstanceModel pvMetadataCollectionInstance = new MetadataCollectionInstanceModel();
			pvMetadataCollectionInstance.setMetadataDefinitionId(pvInformatiiInvitatiArbMetadataDefinition.getId());
			pvMetadataCollectionInstance.setCollectionInstanceRows(pvInformatiiInvitatiArbCollectionInstances);		
		
			response.setMetadataCollectionInstance(pvMetadataCollectionInstance);
		
		} catch (Exception e) {
			logger.error("targetMetadataCollectionDefinitionId: " + request.getTargetMetadataCollectionDefinitionId() + ", " + e.getMessage(), "doAutocomplete", userSecurity);
			throw new AppException();
		}
		return response;
	}
}