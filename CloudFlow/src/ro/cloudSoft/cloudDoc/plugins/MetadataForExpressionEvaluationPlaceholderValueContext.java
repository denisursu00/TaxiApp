package ro.cloudSoft.cloudDoc.plugins;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.domain.bpm.MetadataWrapper;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.utils.placeholderValueContexts.PlaceholderValueContext;

/**
 * Context de valori pentru evaluarea expresiilor / conditiilor de rutare in care se folosesc valori de metadate
 * 
 * 
 */
public class MetadataForExpressionEvaluationPlaceholderValueContext implements PlaceholderValueContext {

	private final Map<String, MetadataWrapper> metadataWrapperByMetadataName;
	
	public MetadataForExpressionEvaluationPlaceholderValueContext(Map<String, MetadataWrapper> metadataWrapperByMetadataName) {
		this.metadataWrapperByMetadataName = metadataWrapperByMetadataName;
	}

	@Override
	public String getValue(String placeholderName) {
		
		MetadataWrapper metadataWrapper = metadataWrapperByMetadataName.get(placeholderName);
		
		if (metadataWrapper == null) {
			return "''";
		}
		
		return getMetadataValueForUseInExpression(metadataWrapper);
	}
	
	/**
	 * Returneaza valoarea metadatei pentru a fi folosita in evaluarea unei expresii.
	 * 
	 * @param metadataWrapper wrapper-ul ce contine numele, tipul si valoarea / valorile metadatei
	 */
	private static String getMetadataValueForUseInExpression(MetadataWrapper metadataWrapper) {
		String metadataType = metadataWrapper.getType();
		if (StringUtils.isBlank(metadataWrapper.getValue())) {
			return "null";
		}
		if (metadataType.equals(MetadataDefinition.TYPE_TEXT)) {
			return ("'" + metadataWrapper.getValue() + "'");
		} else if (metadataType.equals(MetadataDefinition.TYPE_NUMERIC)) {
			return metadataWrapper.getValue();
		} else if (metadataType.equals(MetadataDefinition.TYPE_AUTO_NUMBER)) {
			return ("'" + metadataWrapper.getValue() + "'");
		} else if (metadataType.equals(MetadataDefinition.TYPE_DATE)) {
			return ("'" + metadataWrapper.getValue() + "'");
		} else if (metadataType.equals(MetadataDefinition.TYPE_DATE_TIME)) {
			return ("'" + metadataWrapper.getValue() + "'");
		} else if (metadataType.equals(MetadataDefinition.TYPE_MONTH)) {
			return ("'" + metadataWrapper.getValue() + "'");
		} else if (metadataType.equals(MetadataDefinition.TYPE_NOMENCLATOR)) {
			return metadataWrapper.getValue();
		} else if (metadataType.equals(MetadataDefinition.TYPE_LIST)) {
			return ("'#" + StringUtils.join(metadataWrapper.getValues(), '#') + "#'");
		} else if (metadataType.equals(MetadataDefinition.TYPE_USER)) {
			return metadataWrapper.getValue();
		} else if (metadataType.equals(MetadataDefinition.TYPE_TEXT_AREA)) {
			return ("'" + metadataWrapper.getValue() + "'");
		} else if (metadataType.equals(MetadataDefinition.TYPE_METADATA_COLLECTION)) {
			throw new UnsupportedOperationException("Nu sunt suportate colectii de metadate.");
		} else if (metadataType.equals(MetadataDefinition.TYPE_GROUP)) {
			return metadataWrapper.getValue();
		} else if (metadataType.equals(MetadataDefinition.TYPE_DOCUMENT)) {
			return ("'" + metadataWrapper.getValue() + "'");
		} else {
			throw new IllegalArgumentException("Nu exista acest tip de metadata in aplicatie: [" + metadataType + "].");
		}
	}
}