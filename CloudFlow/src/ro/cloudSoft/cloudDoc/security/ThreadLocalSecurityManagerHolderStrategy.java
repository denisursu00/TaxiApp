package ro.cloudSoft.cloudDoc.security;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public class ThreadLocalSecurityManagerHolderStrategy implements SecurityManagerHolderStrategy {
	
	private static ThreadLocal<SecurityManager> holder = new ThreadLocal<>();
	
	@Override
	public SecurityManager getSecurityManager() {
		return holder.get();
	}

	@Override
	public void setSecurityManager(SecurityManager securityManager) {
		holder.set(securityManager);
	}

	@Override
	public void clearSecurityManager() {
		holder.remove();
	}
}
