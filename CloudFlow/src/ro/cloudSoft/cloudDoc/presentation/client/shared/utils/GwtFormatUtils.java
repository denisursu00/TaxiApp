package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.Collection;
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class GwtFormatUtils {

	public static String convertDateToString(String format, Date date) {
		return DateTimeFormat.getFormat(format).format(date);
	}

	public static Date getDateFromString(String format, String dateString) {
		return DateTimeFormat.getFormat(format).parse(dateString);
	}
	
	/**
     * Reprezinta o colectie de valori sub forma unei liste delimitate.
     * Exemplu: "valoare1, valoare2, valoare3".
     * @param values colectia de valori
     * @return un <code>String</code> cu valorile delimitate
     */
    public static String getDelimitedString(Collection<? extends Object> values) {
    	if (!GwtValidateUtils.hasElements(values)) {
    		return "";
    	}
    	
    	StringBuilder delimitedString = new StringBuilder();    	
    	for (Object value : values) {
    		delimitedString.append(value.toString());
    		delimitedString.append(", ");
    	}    	
    	delimitedString.delete(delimitedString.lastIndexOf(", "), delimitedString.length());
    	
    	return delimitedString.toString();
    }
}