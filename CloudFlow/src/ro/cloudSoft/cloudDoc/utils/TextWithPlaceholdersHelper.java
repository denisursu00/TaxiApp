package ro.cloudSoft.cloudDoc.utils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ro.cloudSoft.cloudDoc.utils.placeholderValueContexts.PlaceholderValueContext;
import ro.cloudSoft.common.utils.RegexUtils;

import com.google.common.collect.Sets;

/**
 * Inlocuieste marcaje cu valorile corespunzatoare in texte date.
 * Marcajele sunt de forma {nume_marcaj}.
 * 
 * 
 */
public class TextWithPlaceholdersHelper {
	
	public static final String PLACEHOLDER_DELIMITER_LEFT = "{";
	public static final String PLACEHOLDER_DELIMITER_RIGHT = "}";
	
	public static final String REGEX_PLACEHOLDER = (
		RegexUtils.escapeMetaChars(PLACEHOLDER_DELIMITER_LEFT) +
		"(\\w)+((\\s)*(\\w)+)*" +
		RegexUtils.escapeMetaChars(PLACEHOLDER_DELIMITER_RIGHT)
	);
	
	private static String getPlaceholderName(String placeholder) {
		
		if (!(placeholder.startsWith(PLACEHOLDER_DELIMITER_LEFT) && placeholder.endsWith(PLACEHOLDER_DELIMITER_RIGHT))) {
			throw new IllegalArgumentException("Valoarea data nu este un marcaj: [ " + placeholder + "].");
		}
		
		return placeholder.substring(PLACEHOLDER_DELIMITER_LEFT.length(), placeholder.length() - PLACEHOLDER_DELIMITER_RIGHT.length());
	}
	
	private static Set<String> getPlaceholders(String text) {
		
		Matcher matcher = Pattern.compile(REGEX_PLACEHOLDER).matcher(text);
		Set<String> placeholders = Sets.newHashSet();
		
		while (matcher.find()) {
			String placeholder = matcher.group();
			placeholders.add(placeholder);
		}
		
		return placeholders;
	}
	
	/**
	 * Returneaza numele marcajelor existente in textul dat.
	 */
	public static Set<String> getPlaceholderNames(String text) {
		
		Set<String> placeholders = getPlaceholders(text);
		Set<String> placeholderNames = Sets.newHashSet();
	
		for (String placeholder : placeholders) {
			String placeholderName = getPlaceholderName(placeholder);
			placeholderNames.add(placeholderName);
		}
		
		return placeholderNames;
	}

	/**
	 * Inlocuieste marcajele cu valorile corespunzatoare in textul dat.
	 * Valorile sunt obtinute dintr-un context de valori.
	 */
	public static String replacePlaceholders(String text, PlaceholderValueContext valueContext) {
		
		Set<String> placeholders = getPlaceholders(text);
		
		String resultText = text;
		
		for (String placeholder : placeholders) {
			
			String placeholderName = getPlaceholderName(placeholder);
			String valueFromContext = valueContext.getValue(placeholderName);
			
			resultText = resultText.replace(placeholder, valueFromContext);
		}
		
		return resultText;
	}
}