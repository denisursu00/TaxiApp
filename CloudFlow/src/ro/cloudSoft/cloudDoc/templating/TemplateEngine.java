package ro.cloudSoft.cloudDoc.templating;

import java.util.Map;

import ro.cloudSoft.cloudDoc.core.AppException;

/**
 * 
 */
public interface TemplateEngine {

	public String processFromFileTemplate(String templateFileName, Map<String, Object> contextMap) throws AppException;
	
	public String processFromStringTemplate(String templateString, Map<String, Object> contextMap) throws AppException;
	
	public byte[] processAndReturnAsBytes(String templateFileName, Map<String, Object> contextMap) throws AppException;
}