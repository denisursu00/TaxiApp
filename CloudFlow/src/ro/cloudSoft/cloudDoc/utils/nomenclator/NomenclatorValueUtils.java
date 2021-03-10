package ro.cloudSoft.cloudDoc.utils.nomenclator;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.beans.BeanUtils;

public class NomenclatorValueUtils {
	
	public static String getAttributeValueAsString(NomenclatorValue nomenclatorValue, String attributeKey) {
		try {
			if (nomenclatorValue == null) {
				return null;
			}
			Class<? extends NomenclatorValue> clazz = nomenclatorValue.getClass();
			Field field = clazz.getDeclaredField(attributeKey);
			field.setAccessible(true);
			return (String) field.get(nomenclatorValue);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static String getAttributeValueAsString(NomenclatorValueModel nomenclatorValueModel, String attributeKey) {
		try {
			Class<? extends NomenclatorValueModel> clazz = nomenclatorValueModel.getClass();
			Field field = clazz.getDeclaredField(attributeKey);
			field.setAccessible(true);
			return (String) field.get(nomenclatorValueModel);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static void setAttributeValue(NomenclatorValue nomenclatorValue, String attributeKey, String value) {
		try {
			Class<? extends NomenclatorValue> clazz = nomenclatorValue.getClass();
			Field field = clazz.getDeclaredField(attributeKey);
			field.setAccessible(true);
			field.set(nomenclatorValue, value);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static void setAttributeValue(NomenclatorValueModel nomenclatorValueModel, String attributeKey, String value) {
		try {
			Class<? extends NomenclatorValueModel> clazz = nomenclatorValueModel.getClass();
			Field field = clazz.getDeclaredField(attributeKey);
			field.setAccessible(true);
			field.set(nomenclatorValueModel, value);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static Date getAttributeValueAsDate(NomenclatorValue nomenclatorValue, String attributeKey) {
		String attributeValueAsString = getAttributeValueAsString(nomenclatorValue, attributeKey);
		Date attributeValueAsDate = DateUtils.parseDate(attributeValueAsString, FormatConstants.NOMENCLATOR_ATTRIBUTE_DATE_FOR_SAVING);
		return attributeValueAsDate;
	}
	
	public static Long getAttributeValueAsLong(NomenclatorValue nomenclatorValue, String attributeKey) {
		String attributeValueAsString = getAttributeValueAsString(nomenclatorValue, attributeKey);
		return Long.valueOf(attributeValueAsString);
	}
	
	public static Integer getYearFromDateAttribute(NomenclatorValueModel nomenclatorValueModel, String attributeKey) {
		String data = BeanUtils.getPropertyValue(nomenclatorValueModel, attributeKey);
		if (StringUtils.isBlank(data)) {
			return null;
		}
		return Integer.parseInt(data.substring(0, 4));
	}
	
	public static Long getAttributeValueAsLong(NomenclatorValueModel nomenclatorValue, String attributeKey) {
		String attributeValueAsString = getAttributeValueAsString(nomenclatorValue, attributeKey);
		return Long.valueOf(attributeValueAsString);
	}
	
	public static Long getAttributeValueAsLongOrNull(NomenclatorValueModel nomenclatorValue, String attributeKey) {
		String attributeValueAsString = getAttributeValueAsString(nomenclatorValue, attributeKey);
		if (StringUtils.isEmpty(attributeValueAsString)) {
			return null;
		}
		return Long.valueOf(attributeValueAsString);
	}
	
	public static boolean getAttributeValueAsBoolean(NomenclatorValueModel nomenclatorValue, String attributeKey) {
		String attributeValueAsString = getAttributeValueAsString(nomenclatorValue, attributeKey);
		if (StringUtils.isEmpty(attributeValueAsString)) {
			return false;
		} else if (attributeValueAsString.equals(NomenclatorConstants.NOMENCLATOR_ATTR_BOOLEAN_VALUE_AS_TRUE)){
			return true;			 
		} else {
			return false;
		}
	}
}