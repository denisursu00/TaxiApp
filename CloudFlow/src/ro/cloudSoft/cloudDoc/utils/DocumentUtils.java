package ro.cloudSoft.cloudDoc.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataItem;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;

public class DocumentUtils {
	
	public static MetadataInstance getMetadataInstance(Document document, DocumentType documentType, String metadataName) {		
		List<MetadataInstance> documentMetadataInstances = document.getMetadataInstanceList();
		MetadataDefinition metadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(documentType, metadataName); 
		for (MetadataInstance metadataInstance : documentMetadataInstances) {
			if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinition.getId())) {
				return metadataInstance;
			}
		}
		return null;
	}
	
	public static Date getMetadataValueAsDate(Document document, DocumentType documentType, String metadataName) throws ParseException {
		
		List<MetadataInstance> documentMetadataInstances = document.getMetadataInstanceList();
		MetadataDefinition metadataDefinition = DocumentTypeUtils.getMetadataDefinitionByName(documentType, metadataName);
		for (MetadataInstance metadataInstance : documentMetadataInstances) {
			if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinition.getId())) {
				return DateUtils.parse(metadataInstance.getValue(), FormatConstants.METADATA_DATE_FOR_SAVING);
			}
		}
		
		throw new RuntimeException("Cannot find metadata instance on document with id [" + document.getId() + "] by metadataName [" + metadataName + "]");
	}

	public static String getMetadataNomenclatorAttributeValue(NomenclatorValueDao nomenclatorValueDao, Document document, DocumentType documentType, 
			String metadataName, String attributeKey) {
		
		MetadataInstance metadataInstance = DocumentUtils.getMetadataInstance(document, documentType, metadataName);
		if (metadataInstance == null) {
			return null;
		}
		Long nomenclatorValueId = MetadataValueHelper.getNomenclatorValueId(metadataInstance.getValue());
		NomenclatorValue nomenclatorValue = nomenclatorValueDao.find(nomenclatorValueId);
		
		return NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, attributeKey);
	}
	
	public static String getMetadataValueAsString(Document document, DocumentTypeService documentTypeService, String metadataName) {		
		DocumentType documentType = documentTypeService.getDocumentTypeById(document.getDocumentTypeId());
		MetadataInstance metadataInstance = getMetadataInstance(document, documentType, metadataName);
		if (metadataInstance == null) {
			return null;
		}
		return metadataInstance.getValue();
	}
	
	public static List<String> getMetadataValuesAsStringList(Document document, DocumentTypeService documentTypeService, String metadataName) {		
		DocumentType documentType = documentTypeService.getDocumentTypeById(document.getDocumentTypeId());
		MetadataInstance metadataInstance = getMetadataInstance(document, documentType, metadataName);
		if (metadataInstance == null) {
			return null;
		}
		return metadataInstance.getValues();
	}
		
	public static String getMetadataValueAsString(Document document, DocumentType documentType, String metadataName) {		
		MetadataInstance metadataInstance = getMetadataInstance(document, documentType, metadataName);
		if (metadataInstance == null) {
			return null;
		}
		return metadataInstance.getValue();
	}
	
	public static MetadataInstance getMetadataInstance(Document document, Long metadataDefinitionId) {		
		List<MetadataInstance> documentMetadataInstances = document.getMetadataInstanceList();
		for (MetadataInstance metadataInstance : documentMetadataInstances) {
			if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionId)) {
				return metadataInstance;
			}
		}		
		return null;
	}
	
	public static List<CollectionInstance> getMetadataCollectionInstance(Document document, Long metadataCollectionDefinitionId) {		
		Map<Long, List<CollectionInstance>> documentMetadataInstances = document.getCollectionInstanceListMap();
		if (documentMetadataInstances != null) {
			return documentMetadataInstances.get(metadataCollectionDefinitionId);
		}
		return null;
	}
	
	public static int getSizeOfMetadataCollectionInstance(Document document, DocumentType documentType, String collectionMetadataName) {
		List<CollectionInstance> instances = getMetadataCollectionInstance(document, documentType, collectionMetadataName);
		if (instances == null) {
			return 0;
		}
		return instances.size();
	}
	
	public static List<CollectionInstance> getMetadataCollectionInstance(Document document, DocumentType documentType, String collectionMetadataName) {
		MetadataCollection metadataDefinition = DocumentTypeUtils.getMetadataCollectionDefinitionByName(documentType, collectionMetadataName);
		return getMetadataCollectionInstance(document, metadataDefinition.getId());
	}
	
	public static String getMetadataValueAsString(Document document, Long metadataDefinitonId) {		
		MetadataInstance metadataInstance = getMetadataInstance(document, metadataDefinitonId);
		if (metadataInstance == null) {
			return null;
		}
		return metadataInstance.getValue();
	}
	
	public static Long getMetadataValueAsLong(Document document, Long metadataDefinitonId) {		
		String metadataValueAsString = getMetadataValueAsString(document, metadataDefinitonId);
		if (StringUtils.isNotBlank(metadataValueAsString)) {
			return Long.valueOf(metadataValueAsString);
		}
		return null;
	}
	
	public static Long getMetadataValueAsLong(Document document, DocumentType documentType, String metadataName) {		
		String metadataValueAsString = getMetadataValueAsString(document, documentType, metadataName);
		if (StringUtils.isNotBlank(metadataValueAsString)) {
			return Long.valueOf(metadataValueAsString);
		}
		return null;
	}
	
	public static String getMetadataListValueAsLabel(Document document, DocumentType documentType, String metadataName) {		
		ListMetadataDefinition metadataDefinition = (ListMetadataDefinition) DocumentTypeUtils.getMetadataDefinitionByName(documentType, metadataName); 
		String metadataValue = getMetadataValueAsString(document, metadataDefinition.getId());
		if (StringUtils.isNotBlank(metadataValue)) {
			for (ListMetadataItem item : metadataDefinition.getListItems()) {
				if (item.getValue().equals(metadataValue)) {
					return item.getLabel();
				}
			}
		}
		return null;
	}
	
	public static String getMetadataListLabelByValue(DocumentType documentType, String metadataCollectionName, String metadataName, String metadataValue) {		
		ListMetadataDefinition metadataDefinition = (ListMetadataDefinition) DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(documentType, metadataCollectionName, metadataName);

		if (StringUtils.isNotBlank(metadataValue)) {
			for (ListMetadataItem item : metadataDefinition.getListItems()) {
				if (item.getValue().equals(metadataValue)) {
					return item.getLabel();
				}
			}
		}
		return null;
	}
	
	public static Date getMetadataDateValue(Document document, DocumentType documentType, String metadataName) {
		String metadataValueAsString  = getMetadataValueAsString(document, documentType, metadataName);
		if (StringUtils.isNotBlank(metadataValueAsString)) {
			return DateUtils.parseDate(metadataValueAsString, FormatConstants.METADATA_DATE_FOR_SAVING);
		}
		return null;
	}
	
	public static Date getMetadataDateTimeValue(Document document, DocumentType documentType, String metadataName) {
		String metadataValueAsString  = getMetadataValueAsString(document, documentType, metadataName);
		if (StringUtils.isNotBlank(metadataValueAsString)) {
			return DateUtils.parseDate(metadataValueAsString, FormatConstants.METADATA_DATE_TIME_FOR_SAVING);
		}
		return null;
	}
	
	public static Long getMetadataUserValue(Document document, DocumentType documentType, String metadataName) {
		return getMetadataValueAsLong(document, documentType, metadataName);
	}
	
	public static Long getMetadataGroupValue(Document document, DocumentType documentType, String metadataName) {
		return getMetadataValueAsLong(document, documentType, metadataName);
	}
	
	public static Long getMetadataCalendarValue(Document document, DocumentType documentType, String metadataName) {
		return getMetadataValueAsLong(document, documentType, metadataName);
	}
	
	public static Long getMetadataNomenclatorValue(Document document, DocumentType documentType, String metadataName) {
		return getMetadataValueAsLong(document, documentType, metadataName);
	}
	
	public static Map<String, Long> getMetadataDefinitionsMapIdByNameFromMetadataCollections(List<MetadataCollection> metadataCollections) {
		
		Map<String, Long> metadataDefinitionsIdByName = new HashMap<>();
		
		metadataCollections.forEach(metadataCollection -> {
			metadataDefinitionsIdByName.put(metadataCollection.getName(), metadataCollection.getId());
			metadataCollection.getMetadataDefinitions().forEach(metadataDefintion -> {
				metadataDefinitionsIdByName.put(metadataDefintion.getName(), metadataDefintion.getId());
			});
		});
		
		return metadataDefinitionsIdByName;
		
	}
	
	public static boolean isMetadataCompleted(Long metadataDefinitionId, List<MetadataInstance> metadataInstanceList) {
		if (metadataDefinitionId == null) {
			throw new IllegalArgumentException("metadataDefinitionId cannot be null");
		}
		boolean isCompleted = false;
		if (CollectionUtils.isNotEmpty(metadataInstanceList)) {
			for (MetadataInstance mi : metadataInstanceList) {
				if (metadataDefinitionId.equals(mi.getMetadataDefinitionId()) && CollectionUtils.isNotEmpty(mi.getValues())) {
					isCompleted = true;
				}
			}
		}
		return isCompleted;
	}
	
	public static boolean isNotMetadataCompleted(Long metadataDefinitionId, List<MetadataInstance> metadataInstanceList) {
		return !isMetadataCompleted(metadataDefinitionId, metadataInstanceList);
	}
}
