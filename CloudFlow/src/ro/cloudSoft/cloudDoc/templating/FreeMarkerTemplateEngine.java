package ro.cloudSoft.cloudDoc.templating;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

/**
 * 
 */
public class FreeMarkerTemplateEngine implements TemplateEngine {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(FreeMarkerTemplateEngine.class);
	
	private static final String TEMPLATES_PACKAGE_PATH = "/ro/cloudSoft/cloudDoc/templating/templates/";
	
	private final Configuration configuration;
	
	public FreeMarkerTemplateEngine() {
		configuration = new Configuration();
		configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		configuration.setClassForTemplateLoading(getClass(), TEMPLATES_PACKAGE_PATH);
	}

	@Override
	public String processFromFileTemplate(String templateFileName, Map<String, Object> contextMap) throws AppException {
		
		StringWriter stringWriter = new StringWriter();
		process(buildTemplateFromFile(templateFileName), contextMap, stringWriter);
		return stringWriter.toString();
	}
	
	private Template buildTemplateFromFile(String templateFileName) throws AppException {
		Template template = null;
		try {
			template = configuration.getTemplate(templateFileName);
		} catch (IOException ioe) {
			
			String logMessage = "Nu s-a putut citi template-ul cu numele [" + templateFileName + "] " +
				"in calea de pachet [" + TEMPLATES_PACKAGE_PATH + "].";
			LOGGER.error(logMessage, ioe, "process");
			
			throw new AppException();
		}
		return template;
	}

	@Override
	public String processFromStringTemplate(String templateString, Map<String, Object> contextMap) throws AppException {
		Template template = null;
		Reader reader = new StringReader(templateString);
		try {
			template = new Template("", reader, configuration);
		} catch (IOException e) {
			String logMessage = "Nu s-a putut citi template-ul cu textul [" + templateString + "].";
				LOGGER.error(logMessage, e, "process");
				throw new AppException();
		}
		StringWriter stringWriter = new StringWriter();
		process(template, contextMap, stringWriter);
		return stringWriter.toString();
	}
	
	@Override
	public byte[] processAndReturnAsBytes(String templateFileName, Map<String, Object> contextMap) throws AppException {
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
		
		process(buildTemplateFromFile(templateFileName), contextMap, outputStreamWriter);
		
		return byteArrayOutputStream.toByteArray();
	}
	
	private void process(Template template, Map<String, Object> contextMap, Writer writer) throws AppException {
		
		try {
			template.process(contextMap, writer);
		} catch (TemplateException te) {
			
			String logMessage = "Exceptie la procesarea template-ului cu numele [" + template.getName() + "]. " +
				"Contextul folosit pentru procesare este [" + contextMap + "].";
			LOGGER.error(logMessage, te, "process");
			
			throw new AppException();
		} catch (IOException ioe) {
			
			String logMessage = "Nu s-a putut scrie rezultatul procesarii template-ului cu numele [" + template.getName() + "].";
			LOGGER.error(logMessage, ioe, "process");
			
			throw new AppException();
		}
	}
}