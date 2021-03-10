package ro.cloudSoft.cloudDoc.services.content;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ArrayListMultimap;

import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentOrdineDeZiCdConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentProcesVerbalCdConstants;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.content.TemplateType;
import ro.cloudSoft.cloudDoc.utils.metadataValueFormatters.MetadataValueFormatterForDocumentExport;
import ro.cloudSoft.common.utils.beans.BeanUtils;

public class ExportDocumentDataHelper {
	public static final String NR_CRT_FIELD_LABEL = "Nr. crt";
	public final static String NR_CRT_FIELD_NAME = "nr_crt";
	private final static String METADATA_NAME_TIME_SUFFIX = "_time";
	private final static String ORDINE_DE_ZI_SUBIECTE_OF_INFO_CAPITOL_LIST_NAME_ = "subiecte";
	private final static String PROCES_VERBAL_CD_SUBIECTE_OF_INFO_DISC_DEC_LIST_NAME_ = "subiecte";

	private static void addValuesFromMetadataCollectionFields(Map<String, Object> exportData, DocumentType documentType, Document document,
			MetadataValueFormatterForDocumentExport metadataValueFormatter,  TemplateType templateType) throws Exception {

		Map<Long, MetadataCollection> metadataCollectionDefinitionMappedById = new HashMap<>();
		Map<Long, MetadataDefinition> metadataDefinitionFromCollectionMappedById = new HashMap<>();

		Map<Long, List<CollectionInstance>> collectionInstanceListMap = document.getCollectionInstanceListMap();

		buildMetadataDefinitionsMapedByIds(documentType, metadataCollectionDefinitionMappedById, metadataDefinitionFromCollectionMappedById);

		for (Long key : collectionInstanceListMap.keySet()) {
			List<CollectionInstance> collectionInstanceList = collectionInstanceListMap.get(key);
			MetadataCollection metadataCollDef = metadataCollectionDefinitionMappedById.get(key);

			List<Map<String, Object>> collectionValuesMap = new ArrayList<Map<String, Object>>();
			int nrCrtIndex = 1;

			for (CollectionInstance collInstance : collectionInstanceList) {
				Map<String, Object> collectionRowValue = new HashMap<String, Object>();
				List<MetadataInstance> metadataInstanceList = collInstance.getMetadataInstanceList();
				collectionRowValue.put(NR_CRT_FIELD_NAME, nrCrtIndex);
				for (MetadataInstance metadataInstance : metadataInstanceList) {
					MetadataDefinition metadataDef = metadataDefinitionFromCollectionMappedById.get(metadataInstance.getMetadataDefinitionId());
					Object formattedMetadataValue = metadataValueFormatter.format(metadataInstance.getValues(), metadataDef);
					if (templateType.equals(TemplateType.X_DOC_REPORT) ) {
						formattedMetadataValue = formatValueForXDocTemplateType(exportData, metadataDef.getName(), formattedMetadataValue);
					} else if (templateType.equals(TemplateType.EXPORT_TO_IARCHIVE_DOCUMENT_METADATA) || templateType.equals(TemplateType.EXPORT_TO_IARCHIVE_TABLE)) {
						formattedMetadataValue = formatValueForExportIarchiveTemplateType(formattedMetadataValue, metadataDef);
					}
					collectionRowValue.put(metadataDef.getName(), formattedMetadataValue);
				}
				appendMissingMetadatasName(metadataCollDef.getMetadataDefinitions(), collectionRowValue);
				collectionValuesMap.add(collectionRowValue);
				nrCrtIndex++;
			}
			exportData.put(metadataCollDef.getName(), collectionValuesMap);
		}
	}

	private static void appendMissingMetadatasName(List<? extends MetadataDefinition> list, Map<String, Object> collectionRowValue) {
		list.forEach(metadataDefinition -> {
			if (!collectionRowValue.containsKey(metadataDefinition.getName())) {
				collectionRowValue.put(metadataDefinition.getName(), "");
			}
		});
	}

	private static void appendMissingMetadatasLabel(List<? extends MetadataDefinition> list, Map<String, Object> collectionRowValue) {
		list.forEach(metadataDefinition -> {
			if (!collectionRowValue.containsKey(metadataDefinition.getLabel())) {
				collectionRowValue.put(metadataDefinition.getLabel(), "");
			}
		});
	}

	private static void buildMetadataDefinitionsMapedByIds(DocumentType documentType, Map<Long, MetadataCollection> metadataCollectionMappedById,
			Map<Long, MetadataDefinition> metadataDefinitionFromCollectionMappedById) {

		List<MetadataCollection> metadataCollections = documentType.getMetadataCollections();
		for (MetadataCollection metadataCollection : metadataCollections) {
			metadataCollectionMappedById.put(metadataCollection.getId(), metadataCollection);
			for (MetadataDefinition metadataDefinition : metadataCollection.getMetadataDefinitions()) {
				metadataDefinitionFromCollectionMappedById.put(metadataDefinition.getId(), metadataDefinition);
			}
		}
	}

	private static void addValuesFromMetadataFields(Map<String, Object> exportData, DocumentType documentType, Document document,
			MetadataValueFormatterForDocumentExport metadataValueFormatter, TemplateType templateType) throws Exception {

		Map<Long, MetadataInstance> metadataInstanceByDefinitionId = BeanUtils.getAsMap(document.getMetadataInstanceList(), "metadataDefinitionId");

		for (MetadataDefinition metadataDefinition : documentType.getMetadataDefinitions()) {

			MetadataInstance metadataInstance = metadataInstanceByDefinitionId.get(metadataDefinition.getId());
			String metadataName = metadataDefinition.getName();
			Object metadataValue = null;
			if (metadataInstance != null) {
				metadataValue = metadataValueFormatter.format(metadataInstance.getValues(), metadataDefinition);
			}

			if (templateType.equals(TemplateType.X_DOC_REPORT)) {
				metadataValue = formatValueForXDocTemplateType(exportData, metadataName, metadataValue);
			} else if (templateType.equals(TemplateType.EXPORT_TO_IARCHIVE_DOCUMENT_METADATA) 
					|| templateType.equals(TemplateType.EXPORT_TO_IARCHIVE_TABLE)) {
				metadataValue = formatValueForExportIarchiveTemplateType(metadataValue, metadataDefinition);
			}
			exportData.put(metadataName, metadataValue);
		}
		if (templateType.equals(TemplateType.X_DOC_REPORT)) {
			appendMissingMetadatasName(documentType.getMetadataDefinitions(), exportData);
		}
	}

	private static Object formatValueForXDocTemplateType(Map<String, Object> exportData, String metadataName, Object metadataValue) {
		if ((metadataValue == null)) {
			return "";
		}
		if (metadataValue instanceof Date) {
			String metadataTimeName = metadataName + METADATA_NAME_TIME_SUFFIX;
			DateFormat dateFormatter = new SimpleDateFormat(FormatConstants.TIME_FOR_DISPLAY);
			
			String metadataTimeValue =  dateFormatter.format((Date) metadataValue);
			exportData.put(metadataTimeName, metadataTimeValue);

			dateFormatter = new SimpleDateFormat(FormatConstants.DATE_FOR_DISPLAY);
			return  dateFormatter.format((Date) metadataValue);

		}
		return metadataValue;
	}

	private static Object formatValueForExportIarchiveTemplateType( Object metadataValue, MetadataDefinition md) {
		if ((metadataValue == null)) {
			return "";
		}
		if (metadataValue instanceof Date) {
			DateFormat dateFormatter = null;
			if (md.getMetadataType().equals(MetadataDefinition.TYPE_DATE)) {
				dateFormatter = new SimpleDateFormat(FormatConstants.METADATA_DATE_FOR_DISPLAY);
			} else if (md.getMetadataType().equals(MetadataDefinition.TYPE_DATE_TIME)) {
				dateFormatter = new SimpleDateFormat(FormatConstants.METADATA_DATE_TIME_FOR_DISPLAY);
			} else {
				throw new RuntimeException("metadataValue type unknown for metadata definition type");
			}
			return  dateFormatter.format((Date) metadataValue);

		}
		return metadataValue.toString();
	}

	public static Map<String, Object> buildExportMap(Document document, DocumentType documentType, MetadataValueFormatterForDocumentExport metadataValueFormatter,
			TemplateType templateType, ArbConstants arbConstants) throws Exception {
		Map<String, Object> exportData = new HashMap<>();

		addValuesFromMetadataFields(exportData, documentType, document, metadataValueFormatter, templateType);
		addValuesFromMetadataCollectionFields(exportData, documentType, document, metadataValueFormatter, templateType);
		
		if (templateType.equals(TemplateType.JASPER_REPORTS) || templateType.equals(TemplateType.X_DOC_REPORT)) {
			formatExportDataForSpecificDocumentType(exportData, documentType, arbConstants);
		}
		
		return exportData;
	}



	private static void formatExportDataForSpecificDocumentType(Map<String, Object> data, DocumentType documentType, ArbConstants arbConstants) {

		if (documentType.getName().equals(arbConstants.getDocumentOrdineDeZiCdConstants().getDocumentTypeName())) {
			DocumentOrdineDeZiCdConstants documentConstants = arbConstants.getDocumentOrdineDeZiCdConstants();
			String collectionInformatiiCapitoleMetadataName = documentConstants.getInformatiiCapitole();
			String capitolOfInfoCapMetadataName = documentConstants.getCapitolOfInformatiiCapitoleMetadataName();
			String subiectPerCapitolOfInformatiiCapitoleMetadataName = documentConstants.getSubiectPerCapitolOfInformatiiCapitoleMetadataName();

			Object collectionInfoCapValues = data.get(collectionInformatiiCapitoleMetadataName);

			if (collectionInfoCapValues != null && collectionInfoCapValues instanceof List) {
				ArrayListMultimap<Object, String> subiecteByCapitolAsMultiMap = ArrayListMultimap.create();
				((List) collectionInfoCapValues).forEach(collectionInfoCapValue -> {
					Object key = ((Map<String, Object>) collectionInfoCapValue).get(capitolOfInfoCapMetadataName);
					String subiectPerCapitolValue = (String) ((Map<String, Object>) collectionInfoCapValue).get(subiectPerCapitolOfInformatiiCapitoleMetadataName);
					if (StringUtils.isNoneEmpty(subiectPerCapitolValue)) {
						subiecteByCapitolAsMultiMap.put(key, subiectPerCapitolValue);
					}
				});
				List collectionValuesGroupedByCapitol = new ArrayList<>();
				int nrCrt = 0;
				for(Object capitol: subiecteByCapitolAsMultiMap.keySet()) {
					Map collectionValue = new HashMap<>();
					List<String> subiecte = subiecteByCapitolAsMultiMap.get(capitol);
					List<Map> subiectePrefixedAsMapList = getSubiecteWithNumerotationPrefixesAsMapListForOrdineDeZiCd(subiecte, documentConstants);
					collectionValue.put(ORDINE_DE_ZI_SUBIECTE_OF_INFO_CAPITOL_LIST_NAME_, subiectePrefixedAsMapList);
					nrCrt++;
					collectionValue.put(capitolOfInfoCapMetadataName, "" + nrCrt + ". " + capitol);
					collectionValuesGroupedByCapitol.add(collectionValue);
				}
				data.put(collectionInformatiiCapitoleMetadataName, collectionValuesGroupedByCapitol);
			}
			
		}
		
		if (documentType.getName().equals(arbConstants.getDocumentProcesVerbalCdConstants().getDocumentTypeName())) {
			DocumentProcesVerbalCdConstants documentConstants = arbConstants.getDocumentProcesVerbalCdConstants();
			String collectionInformatiiCapitoleMetadataName = documentConstants.getInformatiiDiscutiiDeciziiMetadataName();
			String capitolOfInfoCapMetadataName = documentConstants.getCapitolOfInformatiiDiscutiiDeciziiMetadataName();
			String subiectOfInformatiiCapitoleMetadataName = documentConstants.getSubiectOfInformatiiDiscutiiDeciziiMetadataName();

			Object collectionInfoCapValues = data.get(collectionInformatiiCapitoleMetadataName);

			if (collectionInfoCapValues != null && collectionInfoCapValues instanceof List) {
				ArrayListMultimap<Object, Map> infoCapitolByCapitolAsMultiMap = ArrayListMultimap.create();
				((List) collectionInfoCapValues).forEach(collectionInfoCapValue -> {
					Object key = ((Map<String, Object>) collectionInfoCapValue).get(capitolOfInfoCapMetadataName);
						infoCapitolByCapitolAsMultiMap.put(key, (Map)collectionInfoCapValue);
				});
				List collectionValuesGroupedByCapitol = new ArrayList<>();
				int nrCrt = 0;
				for(Object capitol: infoCapitolByCapitolAsMultiMap.keySet()) {
					Map collectionValue = new HashMap<>();
					List<Map> infoCapitolList = infoCapitolByCapitolAsMultiMap.get(capitol);
					appendNumerotationPrefixToSubiecteForProcesVerbalCd(infoCapitolList, documentConstants);
					collectionValue.put(PROCES_VERBAL_CD_SUBIECTE_OF_INFO_DISC_DEC_LIST_NAME_, infoCapitolList);
					nrCrt++;
					collectionValue.put(capitolOfInfoCapMetadataName, "" + nrCrt + ". " + capitol);
					collectionValuesGroupedByCapitol.add(collectionValue);
				}
				data.put(collectionInformatiiCapitoleMetadataName, collectionValuesGroupedByCapitol);
			}
			
		}
	}

	private static void appendNumerotationPrefixToSubiecteForProcesVerbalCd(List<Map> infoCapitolList, DocumentProcesVerbalCdConstants documentConstants) {
		char indexChar = 'a';
		for(Map infoCapitol: infoCapitolList) {
			String subiect = (String) infoCapitol.get(documentConstants.getSubiectOfInformatiiDiscutiiDeciziiMetadataName());
			String subiectPrefixed = indexChar + ") " + subiect;
			infoCapitol.put(documentConstants.getSubiectOfInformatiiDiscutiiDeciziiMetadataName(), subiectPrefixed);
			indexChar++;
		}
	}
	
	private static List<Map> getSubiecteWithNumerotationPrefixesAsMapListForOrdineDeZiCd(List<String> subiecte, DocumentOrdineDeZiCdConstants documentConstants) {
		List<Map> subiectePrefixedAsMapList = new ArrayList<>();
		char indexChar = 'a';
		for(String subiect: subiecte) {
			Map subiectAsMap= new HashMap<>();
			subiectAsMap.put(documentConstants.getSubiectPerCapitolOfInformatiiCapitoleMetadataName(), indexChar + ") " + subiect);
			subiectePrefixedAsMapList.add(subiectAsMap);
			indexChar++;
		}
		return subiectePrefixedAsMapList;
	}

}
