package ro.cloudSoft.cloudDoc.config.environment;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Reprezinta un mediu in care poate rula aplicatia.
 * 
 * 
 */
public enum AppEnvironment {

	DEVELOPMENT("dev"),
	TESTING("test"),
	STAGING("staging"),
	PRODUCTION("prod");
	
	private static final Map<String, AppEnvironment> ENVIRONMENT_BY_SUFFIX;
	static {
		
		Map<String, AppEnvironment> environmentBySuffix = Maps.newHashMap();
		
		for (AppEnvironment environment: values()) {
			environmentBySuffix.put(environment.getSuffix(), environment);
		}
		
		ENVIRONMENT_BY_SUFFIX = Collections.unmodifiableMap(environmentBySuffix);
	}
	
	private final String suffix;
	
	private AppEnvironment(String suffix) {
		this.suffix = suffix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public static AppEnvironment ofSuffix(String suffix) {
		return ENVIRONMENT_BY_SUFFIX.get(suffix);
	}
}