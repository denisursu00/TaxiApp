package ro.cloudSoft.common.utils;

import java.util.Locale;

public class LocaleUtils {

	public static Locale toLocale(String code) {
		return new Locale(code);
	}
	
	public static String getCode(Locale locale) {
		return locale.getLanguage();
	}
}