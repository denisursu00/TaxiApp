package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 
 */
public class GwtStringUtils {

	public static boolean isBlank(String string) {
		return ((string == null) || (string.trim().length() == 0));
	}
	
	public static boolean isNotBlank(String string) {
		return (!isBlank(string));
	}
	
	public static String join(Collection<String> strings, String separator) {
		
		StringBuilder joinedString = new StringBuilder();
		
		for (String string : strings) {
			joinedString.append(string);
			joinedString.append(separator);
		}
		
		if (joinedString.length() > 0) {
			joinedString.delete(joinedString.lastIndexOf(separator), joinedString.length());
		}
		
		return joinedString.toString();
	}
	
	public static List<String> split(String string, String separator) {
		String separatorRegex = GwtRegexUtils.escapeMetaChars(separator);
		String[] strings = string.split(separatorRegex);
		return Arrays.asList(strings);
	}
	
	public static String nullToEmpty(String string) {
		if (string == null) {
			return "";
		}
		return string;
	}
}