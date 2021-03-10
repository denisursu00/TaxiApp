package ro.cloudSoft.cloudDoc.services.content.autocompleteMetadata;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentPrezentaCdPvgConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentProcesVerbalCdConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
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
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class AutocompleteInformatiiInvitatiExterniOfProcesVerbalCD extends AutocompleteMetadata {
	
	private static final LogHelper logger = LogHelper.getInstance(AutocompleteInformatiiInvitatiExterniOfProcesVerbalCD.class);
	
	private DocumentPlugin documentPlugin;
	private DocumentTypeDao documentTypeDao;
	private DocumentPrezentaCdPvgConstants prezentaCdPvgConstants;
	private DocumentProcesVerbalCdConstants procesVerbalCdConstants;
	private NomenclatorValueDao nomenclatorValueDao;
	
	public AutocompleteInformatiiInvitatiExterniOfProcesVerbalCD() {
		this.resolveDependencies();
	}
	
	private void resolveDependencies() {
		this.documentPlugin = SpringUtils.getBean(DocumentPlugin.class);
		this.documentTypeDao = SpringUtils.getBean(DocumentTypeDao.class);
		this.prezentaCdPvgConstants = SpringUtils.getBean(DocumentPrezentaCdPvgConstants.class);
		this.procesVerbalCdConstants = SpringUtils.getBean(DocumentProcesVerbalCdConstants.class);
		this.nomenclatorValueDao = SpringUtils.getBean(NomenclatorValueDao.class);
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
			MetadataCollection pInformatiiInvitatiExterniMetadataDefinition = DocumentTypeUtils.getMetadataCollectionDefinitionByName(prezentaCdPvgdocumentType, prezentaCdPvgConstants.getInformatiiInvitatiExterniMetadataName());
			MetadataDefinition pInvitatAcreditatOfInformatiiInvitatiExterniMetadaDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pInformatiiInvitatiExterniMetadataDefinition.getMetadataDefinitions(), prezentaCdPvgConstants.getInvitatAcreditatOfInformatiiInvitatiExterniMetadataName());
			MetadataDefinition pNumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pInformatiiInvitatiExterniMetadataDefinition.getMetadataDefinitions(), prezentaCdPvgConstants.getNumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataName());
			MetadataDefinition pPrenumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pInformatiiInvitatiExterniMetadataDefinition.getMetadataDefinitions(), prezentaCdPvgConstants.getPrenumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataName());
			MetadataDefinition pFunctieInvitatOfInformatiiInvitatiExterniMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pInformatiiInvitatiExterniMetadataDefinition.getMetadataDefinitions(), prezentaCdPvgConstants.getFunctieInvitatOfInformatiiInvitatiExterniMetadataName());
			
			DocumentType procesVerbalCdDocumentType = documentTypeDao.getDocumentTypeByName(procesVerbalCdConstants.getDocumentTypeName());
			MetadataCollection pvInformatiiInvitatiExterniMetadataDefinition = DocumentTypeUtils.getMetadataCollectionDefinitionByName(procesVerbalCdDocumentType, procesVerbalCdConstants.getInformatiiInvitatiExterniMetadataName());
			MetadataDefinition pvNumeInvitatExternOfInformatiiInvitatiExterniMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pvInformatiiInvitatiExterniMetadataDefinition.getMetadataDefinitions(), procesVerbalCdConstants.getNumeInvitatExternOfInformatiiInvitatiExterniMetadataName());
			MetadataDefinition pvPrenumeInvitatExternOfInformatiiInvitatiExterniMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pvInformatiiInvitatiExterniMetadataDefinition.getMetadataDefinitions(), procesVerbalCdConstants.getPrenumeInvitatExternOfInformatiiInvitatiExterniMetadataName());
			MetadataDefinition pvFunctieInvitatExternOfInformatiiInvitatiExterniMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pvInformatiiInvitatiExterniMetadataDefinition.getMetadataDefinitions(), procesVerbalCdConstants.getFunctieInvitatExternOfInformatiiInvitatiExterniMetadataName());
			
			String[] workspaceAndDocumentId = StringUtils.splitByWholeSeparator(sourceMetadataValue, ":");
			Document document = documentPlugin.getDocumentById(workspaceAndDocumentId[1], workspaceAndDocumentId[0], userSecurity);
			
			List<CollectionInstanceModel> pvInformatiiInvitatiExterniCollectionInstances = new ArrayList<>();
			
			List<CollectionInstance> pInformatiiMembriCdCollectionInstances = DocumentUtils.getMetadataCollectionInstance(document, prezentaCdPvgdocumentType, pInformatiiInvitatiExterniMetadataDefinition.getName());
			if (CollectionUtils.isNotEmpty(pInformatiiMembriCdCollectionInstances)) {			
				for (CollectionInstance pCollectionInstance : pInformatiiMembriCdCollectionInstances) {
					CollectionInstanceModel pvInformatiiInvitatiExterniCollectionInstance = new CollectionInstanceModel();
					List<MetadataInstanceModel> pvInformatiiInvitatiExterniCollectionInstancesCollectionInstanceList = new ArrayList<>();
					for (MetadataInstance metadataInstance : pCollectionInstance.getMetadataInstanceList()) {					
						if (metadataInstance.getMetadataDefinitionId().equals(pInvitatAcreditatOfInformatiiInvitatiExterniMetadaDefinition.getId())) {
							
							NomenclatorValue invitatAcreditat = nomenclatorValueDao.find(Long.valueOf(metadataInstance.getValue()));
							
							String nume = NomenclatorValueUtils.getAttributeValueAsString(invitatAcreditat, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME);							
							MetadataInstanceModel instanceModel = createMetadataInstance(pvNumeInvitatExternOfInformatiiInvitatiExterniMetadataDefinition.getId(), nume);
							pvInformatiiInvitatiExterniCollectionInstancesCollectionInstanceList.add(instanceModel);
							
							String prenume = NomenclatorValueUtils.getAttributeValueAsString(invitatAcreditat, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME);
							MetadataInstanceModel prenumeInstanceModel = createMetadataInstance(pvPrenumeInvitatExternOfInformatiiInvitatiExterniMetadataDefinition.getId(), prenume);
							pvInformatiiInvitatiExterniCollectionInstancesCollectionInstanceList.add(prenumeInstanceModel);
						
						} else if (metadataInstance.getMetadataDefinitionId().equals(pNumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataDefinition.getId())) {
							MetadataInstanceModel instanceModel = createMetadataInstance(pvNumeInvitatExternOfInformatiiInvitatiExterniMetadataDefinition.getId(), metadataInstance.getValue());
							pvInformatiiInvitatiExterniCollectionInstancesCollectionInstanceList.add(instanceModel);
						} else if (metadataInstance.getMetadataDefinitionId().equals(pPrenumeInvitatInlocuitorOfInformatiiInvitatiExterniMetadataDefinition.getId())) {
							MetadataInstanceModel instanceModel = createMetadataInstance(pvPrenumeInvitatExternOfInformatiiInvitatiExterniMetadataDefinition.getId(), metadataInstance.getValue());
							pvInformatiiInvitatiExterniCollectionInstancesCollectionInstanceList.add(instanceModel);
						} else if (metadataInstance.getMetadataDefinitionId().equals(pFunctieInvitatOfInformatiiInvitatiExterniMetadataDefinition.getId())) {
							MetadataInstanceModel instanceModel = createMetadataInstance(pvFunctieInvitatExternOfInformatiiInvitatiExterniMetadataDefinition.getId(), metadataInstance.getValue());
							pvInformatiiInvitatiExterniCollectionInstancesCollectionInstanceList.add(instanceModel);
						}						
					}
					pvInformatiiInvitatiExterniCollectionInstance.setMetadataInstanceList(pvInformatiiInvitatiExterniCollectionInstancesCollectionInstanceList);
					pvInformatiiInvitatiExterniCollectionInstances.add(pvInformatiiInvitatiExterniCollectionInstance);
				}
			}
			
			MetadataCollectionInstanceModel pvMetadataCollectionInstance = new MetadataCollectionInstanceModel();
			pvMetadataCollectionInstance.setMetadataDefinitionId(pvInformatiiInvitatiExterniMetadataDefinition.getId());
			pvMetadataCollectionInstance.setCollectionInstanceRows(pvInformatiiInvitatiExterniCollectionInstances);		
		
			response.setMetadataCollectionInstance(pvMetadataCollectionInstance);
		
		} catch (Exception e) {
			logger.error("targetMetadataCollectionDefinitionId: " + request.getTargetMetadataCollectionDefinitionId() + ", " + e.getMessage(), "doAutocomplete", userSecurity);
			throw new AppException();
		}
		return response;
	}
}