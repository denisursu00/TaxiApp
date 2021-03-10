package ro.cloudSoft.common.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 * 
 */
public class PropertiesFileUtils {
	
	/**
	 * Returneaza un obiect Properties asociat fisierului de configurare dat.
	 * 
	 * @param filePackagePath calea fisierului in codul sursa (de exemplu, pentru fisierul "conf.properties"
	 * din pachetul "com.business.config", calea este "/com/business/config/conf.properties")
	 */
	public static Properties getAsProperties(String filePackagePath) {
		
		Properties properties = new Properties();
		
		InputStream fileAsStream = null;
		try {
			fileAsStream = PropertiesFileUtils.class.getResourceAsStream(filePackagePath);
			properties.load(fileAsStream);
		} catch (Exception e) {
			String message = "Fisierul [" + filePackagePath + "] nu exista sau nu este un fisier de proprietati valid.";
			throw new IllegalArgumentException(message, e);
		} finally {
			IOUtils.closeQuietly(fileAsStream);
		}
		
		return properties;
	}
}