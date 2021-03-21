package ro.taxiApp.docs.security;

import ro.taxiApp.docs.domain.security.SecurityManager;

public class SecurityManagerHolder {
	
	private static SecurityManagerHolderStrategy strategy;
	
	static {
		initialize();
	}
	
	private static void initialize() {
		setStrategyAsThreadLocal();
	}
	
	private static void setStrategyAsThreadLocal() {
		strategy = new ThreadLocalSecurityManagerHolderStrategy();
	}
	
	public static SecurityManager getSecurityManager() {
		return strategy.getSecurityManager();
	}
	
	public static void setSecurityManager(SecurityManager securityManager) {
		strategy.setSecurityManager(securityManager);
	}
	
	public static void clearSecurityManager() {
		strategy.clearSecurityManager();
	}
}
