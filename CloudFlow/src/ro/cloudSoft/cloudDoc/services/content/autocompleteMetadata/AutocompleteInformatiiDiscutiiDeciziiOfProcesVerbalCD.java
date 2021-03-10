package ro.cloudSoft.cloudDoc.services.content.autocompleteMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentOrdineDeZiCdConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentProcesVerbalCdConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataItem;
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

public class AutocompleteInformatiiDiscutiiDeciziiOfProcesVerbalCD extends AutocompleteMetadata {
	
	private static final LogHelper logger = LogHelper.getInstance(AutocompleteInformatiiDiscutiiDeciziiOfProcesVerbalCD.class);
	
	private DocumentPlugin documentPlugin;
	private DocumentTypeDao documentTypeDao;
	private DocumentOrdineDeZiCdConstants ordineDeZiCdPvgConstants;
	private DocumentProcesVerbalCdConstants procesVerbalCdConstants;
	
	public AutocompleteInformatiiDiscutiiDeciziiOfProcesVerbalCD() {
		this.resolveDependencies();
	}
	
	private void resolveDependencies() {
		this.documentPlugin = SpringUtils.getBean(DocumentPlugin.class);
		this.documentTypeDao = SpringUtils.getBean(DocumentTypeDao.class);
		this.ordineDeZiCdPvgConstants = SpringUtils.getBean(DocumentOrdineDeZiCdConstants.class);
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
	
			DocumentType ordineDeZiCdDocumentType = documentTypeDao.getDocumentTypeByName(ordineDeZiCdPvgConstants.getDocumentTypeName());
			MetadataCollection oInformatiiCapitoleMetadataDefinition = DocumentTypeUtils.getMetadataCollectionDefinitionByName(ordineDeZiCdDocumentType, ordineDeZiCdPvgConstants.getInformatiiCapitole());
			ListMetadataDefinition oCapitolOfInformatiiCapitoleMetadaDefinition = (ListMetadataDefinition) DocumentTypeUtils.getMetadataDefinitionByName(oInformatiiCapitoleMetadataDefinition.getMetadataDefinitions(), ordineDeZiCdPvgConstants.getCapitolOfInformatiiCapitoleMetadataName());
			MetadataDefinition oSubiectPerCapitolOfInformatiiCapitoleMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(oInformatiiCapitoleMetadataDefinition.getMetadataDefinitions(), ordineDeZiCdPvgConstants.getSubiectPerCapitolOfInformatiiCapitoleMetadataName());
			
			DocumentType procesVerbalCdDocumentType = documentTypeDao.getDocumentTypeByName(procesVerbalCdConstants.getDocumentTypeName());
			MetadataCollection pvInformatiiDiscutiiDeciziiMetadataDefinition = DocumentTypeUtils.getMetadataCollectionDefinitionByName(procesVerbalCdDocumentType, procesVerbalCdConstants.getInformatiiDiscutiiDeciziiMetadataName());
			MetadataDefinition pvCapitolOfInformatiiDiscutiiDeciziiMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pvInformatiiDiscutiiDeciziiMetadataDefinition.getMetadataDefinitions(), procesVerbalCdConstants.getCapitolOfInformatiiDiscutiiDeciziiMetadataName());
			MetadataDefinition pvSubiectOfInformatiiDiscutiiDeciziiMetadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(pvInformatiiDiscutiiDeciziiMetadataDefinition.getMetadataDefinitions(), procesVerbalCdConstants.getSubiectOfInformatiiDiscutiiDeciziiMetadataName());
			
			String[] workspaceAndDocumentId = StringUtils.splitByWholeSeparator(sourceMetadataValue, ":");
			Document document = documentPlugin.getDocumentById(workspaceAndDocumentId[1], workspaceAndDocumentId[0], userSecurity);
			
			List<CollectionInstanceModel> pvInformatiiDiscutiiDeciziiCollectionInstances = new ArrayList<>();
			
			List<CollectionInstance> oInformatiiCapitoleCollectionInstances = DocumentUtils.getMetadataCollectionInstance(document, ordineDeZiCdDocumentType, oInformatiiCapitoleMetadataDefinition.getName());
			if (CollectionUtils.isNotEmpty(oInformatiiCapitoleCollectionInstances)) {			
				for (CollectionInstance oCollectionInstance : oInformatiiCapitoleCollectionInstances) {
					CollectionInstanceModel pvInformatiiDiscutiiDeciziiCollectionInstance = new CollectionInstanceModel();
					List<MetadataInstanceModel> pvInformatiiDiscutiiDeciziiCollectionInstanceList = new ArrayList<>();
					for (MetadataInstance oMetadataInstance : oCollectionInstance.getMetadataInstanceList()) {					
						if (oMetadataInstance.getMetadataDefinitionId().equals(oCapitolOfInformatiiCapitoleMetadaDefinition.getId())) {
							String metadataValue = null;
							for (ListMetadataItem oItem : oCapitolOfInformatiiCapitoleMetadaDefinition.getListItems()) {
								if (oItem.getValue().equals(oMetadataInstance.getValue())) {
									metadataValue = oItem.getLabel();
									break;
								}
							}
							MetadataInstanceModel instanceModel = createMetadataInstance(pvCapitolOfInformatiiDiscutiiDeciziiMetadataDefinition.getId(), metadataValue);
							pvInformatiiDiscutiiDeciziiCollectionInstanceList.add(instanceModel);
						} else if (oMetadataInstance.getMetadataDefinitionId().equals(oSubiectPerCapitolOfInformatiiCapitoleMetadataDefinition.getId())) {
							MetadataInstanceModel instanceModel = createMetadataInstance(pvSubiectOfInformatiiDiscutiiDeciziiMetadataDefinition.getId(), oMetadataInstance.getValue());
							pvInformatiiDiscutiiDeciziiCollectionInstanceList.add(instanceModel);
						}						
					}
					pvInformatiiDiscutiiDeciziiCollectionInstance.setMetadataInstanceList(pvInformatiiDiscutiiDeciziiCollectionInstanceList);
					pvInformatiiDiscutiiDeciziiCollectionInstances.add(pvInformatiiDiscutiiDeciziiCollectionInstance);
				}
			}
			
			MetadataCollectionInstanceModel pvMetadataCollectionInstance = new MetadataCollectionInstanceModel();
			pvMetadataCollectionInstance.setMetadataDefinitionId(pvInformatiiDiscutiiDeciziiMetadataDefinition.getId());
			sortInformatiiDiscutiiDecizii(pvInformatiiDiscutiiDeciziiCollectionInstances, pvCapitolOfInformatiiDiscutiiDeciziiMetadataDefinition.getId());
			pvMetadataCollectionInstance.setCollectionInstanceRows(pvInformatiiDiscutiiDeciziiCollectionInstances);		
		
			response.setMetadataCollectionInstance(pvMetadataCollectionInstance);
		
		} catch (Exception e) {
			logger.error("targetMetadataCollectionDefinitionId: " + request.getTargetMetadataCollectionDefinitionId() + ", " + e.getMessage(), "doAutocomplete", userSecurity);
			throw new AppException();
		}
		return response;
	}
	
	private void sortInformatiiDiscutiiDecizii(List<CollectionInstanceModel> informatiiDiscutiiDecizii, Long capitolMetadataDefinitionId) {
		
		if (CollectionUtils.isEmpty(informatiiDiscutiiDecizii)) {
			return;
		}
		
		Collections.sort(informatiiDiscutiiDecizii, new Comparator<CollectionInstanceModel>() {
			
			@Override
			public int compare(CollectionInstanceModel o1, CollectionInstanceModel o2) {
				String mci1Value = null;				
				for (MetadataInstanceModel mi1 : o1.getMetadataInstanceList()) {
					if (capitolMetadataDefinitionId.equals(mi1.getMetadataDefinitionId())) {
						mci1Value = mi1.getValue();
						break;
					}
				}
				String mci2Value = null;
				for (MetadataInstanceModel mi2 : o2.getMetadataInstanceList()) {
					if (capitolMetadataDefinitionId.equals(mi2.getMetadataDefinitionId())) {
						mci2Value = mi2.getValue();
						break;
					}
				}
				return mci1Value.compareTo(mci2Value);
			}
		});
	}
}
