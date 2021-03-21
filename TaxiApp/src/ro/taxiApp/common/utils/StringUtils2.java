package ro.taxiApp.common.utils;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Sets;
import com.sun.star.uno.RuntimeException;

/**
 * 
 */
public class StringUtils2 {

	public static Set<String> withoutDuplicatesWithDifferentCase(Collection<String> stringsWithPossibleDuplicates) {
		
		Set<String> stringsWithoutDuplicates = Sets.newHashSet();
		
		stringsWithPossibleDuplicatesIteration:
		for (String string : stringsWithPossibleDuplicates) {
			for (String uniqueString : stringsWithoutDuplicates) {
				if (uniqueString.toLowerCase().equals(string.toLowerCase())) {
					continue stringsWithPossibleDuplicatesIteration;
				}
			}
			stringsWithoutDuplicates.add(string);
		}
		
		return stringsWithoutDuplicates;
	}
	
	public static String appendToStringWithSeparator(String string, String stringToAppend, String separator) {
		
		if (StringUtils.isBlank(stringToAppend)) {
			throw new RuntimeException("The string you want to append cannot be null or empty");
		}
		
		if (StringUtils.isBlank(string)) {
			string = stringToAppend;
		} else {
			string += separator + stringToAppend;
		}
		return string;
	}

	
	public static String getStringValueOfObject(Object obj) {
		
		if (obj != null) {
			return obj.toString();
		}
		return "";
	}
}