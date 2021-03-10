package ro.cloudSoft.cloudDoc.presentation.server.utils;

import javax.servlet.http.HttpSession;

import ro.cloudSoft.common.utils.spring.HttpSessionAttributeLocaleResolver;
import ro.cloudSoft.common.utils.spring.SpringUtils;

/**
 * Reprezinta o clasa utilitara pentru limba interfetei grafice.
 * Limba interfetei va fi stocata in sesiunea utilizatorului pe server.
 * 
 * 
 */
public class LocaleUtils {

	/**
	 * Schimba limba interfetei grafice cu cea avand codul precizat.
	 */
	public static void setLocaleByCode(String localeCode, HttpSession session) {
		HttpSessionAttributeLocaleResolver sessionLocaleResolver = getSessionLocaleResolver();
		sessionLocaleResolver.setLocaleByCode(session, localeCode);
	}
	
	/**
	 * Returneaza codul limbii curente a interfetei grafice.
	 * Daca nu a fost setata niciodata limba, atunci va returna codul celei implicite.
	 */
	public static String getLocaleCode(HttpSession session) {
		HttpSessionAttributeLocaleResolver sessionLocaleResolver = getSessionLocaleResolver();
		return sessionLocaleResolver.getLocaleCode(session);
	}
	
	private static HttpSessionAttributeLocaleResolver getSessionLocaleResolver() {
		return SpringUtils.getBean(HttpSessionAttributeLocaleResolver.class);
	}
}