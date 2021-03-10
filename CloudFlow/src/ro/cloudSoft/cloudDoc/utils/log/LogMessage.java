package ro.cloudSoft.cloudDoc.utils.log;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public class LogMessage  {
	
	private static final String MESSAGE_FORMAT = "[MODULE: %s, OPERATION: %s, USER: %s] %s";
	
	private SecurityManager SecurityManager;
	
	private String message;
    private String module;
    private String operation;
    
    
	public LogMessage(SecurityManager SecurityManager, String message, String module, String operation) {
		this.SecurityManager = SecurityManager;
		this.message = message;
		this.module = module;
		this.operation = operation;
	}
	
	public LogMessage(String message)
	{
	    this.message = message;
	}

	@Override
	public String toString() {
		return String.format(MESSAGE_FORMAT, module, operation, SecurityManager, message);
	}
	
	public SecurityManager getSecurityManager() {
		return SecurityManager;
	}
	public void setSecurityManager(SecurityManager SecurityManager) {
		this.SecurityManager = SecurityManager;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
}