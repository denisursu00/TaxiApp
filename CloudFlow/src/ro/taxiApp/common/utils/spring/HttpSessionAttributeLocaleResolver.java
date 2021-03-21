package ro.taxiApp.common.utils.spring;

import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import ro.taxiApp.common.utils.LocaleUtils;

/**
 * Determina limba curenta prin intermediul sesiunii HTTP.
 * 
 * 
 */
public class HttpSessionAttributeLocaleResolver extends AbstractLocaleResolver {

	private final String attributeName;
	private final BiMap<String, Locale> supportedLocaleByCode;
	
	public HttpSessionAttributeLocaleResolver(String attributeName, Set<String> supportedLocaleCodes, String defaultLocaleCode) {
		
		this.attributeName = attributeName;
		
		supportedLocaleByCode = HashBiMap.create();
		for (String supportedLocaleCode : supportedLocaleCodes) {
			Locale locale = LocaleUtils.toLocale(supportedLocaleCode);
			supportedLocaleByCode.put(supportedLocaleCode, locale);
		}
		
		if (!supportedLocaleCodes.contains(defaultLocaleCode)) {
			throw new IllegalArgumentException("Codul pentru limba implicita NU este suportat. Verificati parametrii trimisi.");
		}
		
		Locale defaultLocale = supportedLocaleByCode.get(defaultLocaleCode);
		setDefaultLocale(defaultLocale);
	}
	
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			return getDefaultLocale();
		}
		
		return getLocale(session);
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		HttpSession session = request.getSession();
		setLocale(session, locale);
	}
	
	/**
	 * Returneaza codul limbii curente.
	 * Daca nu a fost setata limba, atunci va returna codul limbii implicite.
	 */
	public String getLocaleCode(HttpSession session) {
		
		Locale locale = getLocale(session);
		
		BiMap<Locale, String> localeCodeBySupportedLocale = supportedLocaleByCode.inverse();
		
		String localeCode = localeCodeBySupportedLocale.get(locale);
		if (localeCode == null) {
			throw new IllegalStateException("Limba curenta nu este suportata: [" + locale + "].");
		}
		return localeCode;
	}
	
	public void setLocaleByCode(HttpSession session, String localeCode) {
		Locale locale = supportedLocaleByCode.get(localeCode);
		if (locale == null) {
			throw new IllegalArgumentException("Limba avand codul [" + localeCode + "] NU este suportata.");
		}
		setLocale(session, locale);
	}
	
	private Locale getLocale(HttpSession session) {
		Locale locale = (Locale) session.getAttribute(attributeName);
		if (locale == null) {
			return getDefaultLocale();
		}		
		return locale;
	}
	
	private void setLocale(HttpSession session, Locale locale) {
		session.setAttribute(attributeName, locale);
	}
}