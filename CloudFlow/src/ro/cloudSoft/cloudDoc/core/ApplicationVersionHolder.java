package ro.cloudSoft.cloudDoc.core;

public class ApplicationVersionHolder {
	
	private static String version;
	
	private ApplicationVersionHolder() {
	}
	
	public static void setVersion(String version) {
		ApplicationVersionHolder.version = version;
	}
	
	public static String getVersion() {
		return version;
	}	
}
