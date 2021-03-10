package ro.cloudSoft.cloudDoc.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;

/**
 * Ajuta la extragerea valorilor metadatelor in formatul corespunzator.
 * 
 * 
 */
public class MetadataValueHelper {
	
	public static String getText(String metadataValue) {
		return metadataValue;
	}
	
	public static BigDecimal getNumber(String metadataValue) {
		
		if (metadataValue == null) {
			return null;
		}
		
		try {
			return new BigDecimal(metadataValue);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException(nfe);
		}
	}
	
	public static String getAutoNumberValue(String metadataValue) {
		return metadataValue;
	}
	
	public static Date getDate(String metadataValue) {
		
		if (metadataValue == null) {
			return null;
		}
		
		DateFormat dateFormatter = new SimpleDateFormat(FormatConstants.METADATA_DATE_FOR_SAVING);
		try {
			return dateFormatter.parse(metadataValue);
		} catch (ParseException pe) {
			throw new IllegalArgumentException(pe);
		}
	}
	
	public static Date getDateTime(String metadataValue) {
		
		if (metadataValue == null) {
			return null;
		}
		DateFormat dateFormatter = new SimpleDateFormat(FormatConstants.METADATA_DATE_TIME_FOR_SAVING);
		try {
			return dateFormatter.parse(metadataValue);
		} catch (ParseException pe) {
			throw new IllegalArgumentException(pe);
		}
	}
	
	public static String toDateTimeValue(Date date) {
		if (date == null) {
			return null;
		}		
		DateFormat dateFormatter = new SimpleDateFormat(FormatConstants.METADATA_DATE_TIME_FOR_SAVING);
		return dateFormatter.format(date);
	}
	
	public static String getListItemValue(String metadataValue) {
		return metadataValue;
	}
	
	public static List<String> getListItemValues(Collection<String> metadataValues) {
		return Lists.newArrayList(metadataValues);
	}
	
	public static Long getUserId(String metadataValue) {
		return getAsLong(metadataValue);
	}
	
	public static Long getNomenclatorValueId(String metadataValue) {
		return getAsLong(metadataValue);
	}
	
	public static Long getGroupId(String metadataValue) {
		return getAsLong(metadataValue);
	}
	
	public static Long getCalendarId(String metadataValue) {
		return getAsLong(metadataValue);
	}
	
	private static Long getAsLong(String metadataValue) {
		if (metadataValue == null) {
			return null;
		}		
		try {
			return Long.valueOf(metadataValue);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException(nfe);
		}
	}
	
	public static String getTextAreaValue(String metadataValue) {
		return metadataValue;
	}
	
	public static String toDateValue(Date date) {
		if (date == null) {
			return null;
		}		
		DateFormat dateFormatter = new SimpleDateFormat(FormatConstants.METADATA_DATE_FOR_SAVING);
		return dateFormatter.format(date);
	}
	
	public static String toMetadataValue(DocumentIdentifier documentIdentifier) {
		return String.join(":", documentIdentifier.getDocumentLocationRealName(), documentIdentifier.getDocumentId());
	}
	
	public static DocumentIdentifier getDocumentIdentifier(String metadataValue) {
		String[] split = metadataValue.split(":");
		return new DocumentIdentifier(split[0], split[1]);
	}

}