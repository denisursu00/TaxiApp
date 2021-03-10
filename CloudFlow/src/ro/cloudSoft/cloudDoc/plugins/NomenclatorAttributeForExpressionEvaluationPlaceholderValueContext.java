package ro.cloudSoft.cloudDoc.plugins;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttributeTypeEnum;
import ro.cloudSoft.cloudDoc.utils.placeholderValueContexts.PlaceholderValueContext;

public class NomenclatorAttributeForExpressionEvaluationPlaceholderValueContext implements PlaceholderValueContext {
	
	private Map<String, NomenclatorAttributeEvaluationWrapper> attributeEvaluationWrapperByAttributeKey;
	
	public NomenclatorAttributeForExpressionEvaluationPlaceholderValueContext(Map<String, NomenclatorAttributeEvaluationWrapper> attributeEvaluationWrapperByAttributeKey) {
		this.attributeEvaluationWrapperByAttributeKey = attributeEvaluationWrapperByAttributeKey;
	}
	
	@Override
	public String getValue(String placeholderName) {
		NomenclatorAttributeEvaluationWrapper attributeWrapper = attributeEvaluationWrapperByAttributeKey.get(placeholderName);
		if (attributeWrapper == null) {
			throw new RuntimeException("attributeWrapper cannot be null");
		}
		if (StringUtils.isBlank(attributeWrapper.getValue())) {
			return "null";
		}
		NomenclatorAttributeTypeEnum attributeType = attributeWrapper.getType();
		if (attributeType.equals(NomenclatorAttributeTypeEnum.TEXT)) {
			return ("'" + attributeWrapper.getValue() + "'");
		} else if (attributeType.equals(NomenclatorAttributeTypeEnum.NUMERIC)) {
			return attributeWrapper.getValue();
		} else if (attributeType.equals(NomenclatorAttributeTypeEnum.DATE)) {
			return ("'" + attributeWrapper.getValue() + "'");
		} else if (attributeType.equals(NomenclatorAttributeTypeEnum.BOOLEAN)) {
			return attributeWrapper.getValue();
		} else if (attributeType.equals(NomenclatorAttributeTypeEnum.NOMENCLATOR)) {
			return attributeWrapper.getValue();
		} else {
			throw new IllegalArgumentException("Nu exista acest tip de atribut pt. nomenclatoare: [" + attributeType + "].");
		}
	}
}
