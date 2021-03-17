package ro.taxiApp.docs.config.environment;

import org.apache.commons.lang.StringUtils;

/**
 * Contine constante si metode legate de configurarea mediului de lucru al aplicatiei.
 * 
 * 
 */
public class AppEnvironmentConfig {

	private static final String ENVIRONMENT_PROPERTY = "cloudflow.environment";	
	public static final String SUFFIX_FOR_CURRENT_ENVIRONMENT = getSuffixForCurrentEnvironment();
	
	private static String getSuffixForCurrentEnvironment() {
		
		String foundSuffixForCurrentEnvironment = null;
		foundSuffixForCurrentEnvironment = System.getProperty(ENVIRONMENT_PROPERTY);
		if (StringUtils.isBlank(foundSuffixForCurrentEnvironment)) {
			throw new Error("nu s-a putut obtine o valoare din proprietatea de mediu [" + ENVIRONMENT_PROPERTY + "]");
		}
		
		AppEnvironment currentEnvironment = AppEnvironment.ofSuffix(foundSuffixForCurrentEnvironment);
		if (currentEnvironment == null) {
			throw new Error("Nu exista mediul de lucru cu sufixul [" + foundSuffixForCurrentEnvironment + "].");
		}
		
		return foundSuffixForCurrentEnvironment;
	}
	
	public static String getFilePackagePathWithSuffix(String filePackagePath) {
		
		String[] splitPath = filePackagePath.split("\\.");
		
		String pathWithoutExtension = splitPath[0];
		String extension = splitPath[1];
		
		String pathForEnvironment = pathWithoutExtension + "_" + SUFFIX_FOR_CURRENT_ENVIRONMENT + "." + extension;
		return pathForEnvironment;
	}
	
	public static AppEnvironment getCurrentEnvironment() {
		String envSuffix = getSuffixForCurrentEnvironment();
		AppEnvironment currentEnvironment = AppEnvironment.ofSuffix(envSuffix);
		if (currentEnvironment == null) {
			throw new Error("Nu exista mediul de lucru cu sufixul [" + envSuffix + "].");
		}
		return currentEnvironment;
	}
}