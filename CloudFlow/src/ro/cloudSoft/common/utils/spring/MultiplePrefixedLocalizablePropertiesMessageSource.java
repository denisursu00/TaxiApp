package ro.cloudSoft.common.utils.spring;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import ro.cloudSoft.common.utils.LocaleUtils;
import ro.cloudSoft.common.utils.RegexUtils;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Permite folosirea mai multor fisiere properties, localizate, ca sursa de mesaje.
 * 
 * Fiecarui fisier properties ii corespunde unui prefix. Astfel, pentru folosirea unei proprietati dintr-un anumit fisier,
 * se va folosi prefixul (de exemplu, "[erori]autentificare.parola_incorecta").
 * 
 * 
 */
public class MultiplePrefixedLocalizablePropertiesMessageSource implements MessageSource {
	
	private static final String PREFIX_DELIMITER_LEFT = "[";
	private static final String PREFIX_DELIMITER_RIGHT = "]";
	private static final String PATTERN_PREFIX_WITHOUT_DELIMITERS = "[a-zA-Z]+";
	private static final String PATTERN_PREFIX_WITH_DELIMITERS = (RegexUtils.escapeMetaChars(PREFIX_DELIMITER_LEFT) + PATTERN_PREFIX_WITHOUT_DELIMITERS + RegexUtils.escapeMetaChars(PREFIX_DELIMITER_RIGHT));

	private final Pattern prefixPattern;
	
	private final Map<PropertiesIdentifier, Properties> propertiesByIdentifier;
	private final Set<String> supportedPrefixes;
	private final Set<String> supportedLocaleCodes;
	

	public MultiplePrefixedLocalizablePropertiesMessageSource(Map<String, String> propertiesPackagePathForBaseNameByPrefix, Set<String> localeCodes) {

		prefixPattern = Pattern.compile(PATTERN_PREFIX_WITH_DELIMITERS);
		
		propertiesByIdentifier = Maps.newHashMap();
		supportedPrefixes = Sets.newHashSet();
		supportedLocaleCodes = Sets.newHashSet(localeCodes);
		
		for (Entry<String, String> propertiesPackagePathForBaseNameByPrefixEntry : propertiesPackagePathForBaseNameByPrefix.entrySet()) {

			String prefix = propertiesPackagePathForBaseNameByPrefixEntry.getKey();
			String propertiesPackagePathForBaseName = propertiesPackagePathForBaseNameByPrefixEntry.getValue();
			
			if (!prefix.matches(PATTERN_PREFIX_WITHOUT_DELIMITERS)) {
				throw new IllegalArgumentException("Prefixul [" + prefix + "] NU respecta conventia de nume (" + PATTERN_PREFIX_WITHOUT_DELIMITERS + ").");
			}
			supportedPrefixes.add(prefix);
			
			for (String localeCode : localeCodes) {
				
				Properties properties = new Properties();
				
				String propertiesPackagePath = (propertiesPackagePathForBaseName + "_" + localeCode + ".properties");
				
				InputStream propertiesAsStream = getClass().getResourceAsStream(propertiesPackagePath);
				if (propertiesAsStream == null) {
					throw new IllegalArgumentException("Nu s-a gasit fisierul properties [" + propertiesPackagePath + "].");
				}
				try {
					
					String encoding = "UTF-8";
					InputStreamReader propertiesReader = null;
					try {
						propertiesReader = new InputStreamReader(propertiesAsStream, encoding);
					} catch (UnsupportedEncodingException uee) {
						throw new RuntimeException("Nu s-a putut folosi encoding-ul [" + encoding + "].", uee);
					}
					
					try {
						properties.load(propertiesReader);
					} catch (IOException ioe) {
						throw new RuntimeException("Eroare la citirea din fisierul properties [" + propertiesPackagePath + "]", ioe);
					} finally {
						IOUtils.closeQuietly(propertiesReader);
					}
				} finally {
					IOUtils.closeQuietly(propertiesAsStream);
				}
				
				PropertiesIdentifier propertiesIdentifier = new PropertiesIdentifier(prefix, localeCode);
				propertiesByIdentifier.put(propertiesIdentifier, properties);
			}
		}
	}
	
	@Override
	public String getMessage(String messageCode, Object[] arguments, Locale locale) throws NoSuchMessageException {
		
		if (ArrayUtils.isNotEmpty(arguments)) {
			throw new UnsupportedOperationException("Nu sunt suportati parametri la mesaje.");
		}
		
		MessageCodeWithPrefix messageCodeWithPrefix = getMessageCodeWithPrefix(messageCode);
		
		String prefix = messageCodeWithPrefix.getPrefix();
		String localeCode = LocaleUtils.getCode(locale);
		
		PropertiesIdentifier propertiesIdentifier = new PropertiesIdentifier(prefix, localeCode);
		
		Properties properties = propertiesByIdentifier.get(propertiesIdentifier);
		if (properties == null) {
			
			if (!supportedPrefixes.contains(prefix)) {
				throw new MessageRelatedIllegalArgumentException("Nu este suportat prefixul [" + prefix + "], doar urmatoarele: [" + StringUtils.join(supportedPrefixes, ", ") + "].");
			}
			
			if (!supportedLocaleCodes.contains(localeCode)) {
				throw new MessageRelatedIllegalArgumentException("Nu este suportata limba [" + localeCode + "], doar urmatoarele: [" + StringUtils.join(supportedLocaleCodes, ", ") + "].");
			}
		}
		
		String realMessageCode = messageCodeWithPrefix.getRealMessageCode();
		
		String message = properties.getProperty(realMessageCode);
		if (message == null) {
			throw new MessageRelatedIllegalArgumentException("Nu s-a gasit mesajul avand codul [" + realMessageCode + "] (pentru lista cu prefixul [" + prefix + "] si limba [" + localeCode + "]).");
		}
		return message;
	}
	
	@Override
	public String getMessage(String messageCode, Object[] arguments, String defaultMessage, Locale locale) {
		try {
			return getMessage(messageCode, arguments, locale);
		} catch (MessageRelatedIllegalArgumentException mriae) {
			return defaultMessage;
		}
	}
	
	@Override
	public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
		throw new UnsupportedOperationException("Nu este suportata decat returnarea unui mesaj dupa cod.");
	}
	
	private MessageCodeWithPrefix getMessageCodeWithPrefix(String messageCode) {
		
		Matcher matcher = prefixPattern.matcher(messageCode);
		
		boolean foundPrefix = matcher.find();
		if (!foundPrefix) {
			throw new MessageRelatedIllegalArgumentException("Nu s-a gasit un prefix in codul de mesaj [" + messageCode + "].");
		}
		
		String prefixWithDelimiters = matcher.group();
		String prefix = prefixWithDelimiters.substring(PREFIX_DELIMITER_LEFT.length(), prefixWithDelimiters.length() - PREFIX_DELIMITER_RIGHT.length());
		
		String realMessageCode = messageCode.substring(prefixWithDelimiters.length());
		
		return new MessageCodeWithPrefix(prefix, realMessageCode);
	}
	
	private static class PropertiesIdentifier {
		
		private final String prefix;
		private final String localeCode;
		
		protected PropertiesIdentifier(String prefix, String localeCode) {
			this.prefix = prefix;
			this.localeCode = localeCode;
		}
		
		@Override
		public boolean equals(Object obj) {
			
			if (!(obj instanceof PropertiesIdentifier)) {
				return false;
			}
			
			PropertiesIdentifier other = (PropertiesIdentifier) obj;
			
			return (
				Objects.equal(prefix, other.prefix) &&
				Objects.equal(localeCode, other.localeCode)
			);
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(
				prefix,
				localeCode
			);
		}
		
		@Override
		public String toString() {
			return ("[" + prefix + ":" + localeCode + "]");
		}
	}
	
	private static class MessageCodeWithPrefix {
		
		private final String prefix;
		private final String realMessageCode;
		
		protected MessageCodeWithPrefix(String prefix, String realMessageCode) {
			this.prefix = prefix;
			this.realMessageCode = realMessageCode;
		}
		
		public String getPrefix() {
			return prefix;
		}
		
		public String getRealMessageCode() {
			return realMessageCode;
		}
	}
	
	private static class MessageRelatedIllegalArgumentException extends IllegalArgumentException {
		
		private static final long serialVersionUID = 1L;

		public MessageRelatedIllegalArgumentException(String message) {
			super(message);
		}
	}
}