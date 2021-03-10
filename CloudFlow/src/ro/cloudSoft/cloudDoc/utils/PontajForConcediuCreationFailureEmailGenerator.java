package ro.cloudSoft.cloudDoc.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetForLeaveFailure;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetForLeavePersonNotFoundFailure;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetForLeaveProjectNotFoundFailure;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetForLeaveTaskNotFoundFailure;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetForLeaveTimesheetsExistFailure;
import ro.cloudSoft.cloudDoc.domain.mail.EmailMessage;
import ro.cloudSoft.cloudDoc.utils.placeholderValueContexts.MapBackedPlaceholderValueContext;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.Maps;

/**
 * 
 */
public class PontajForConcediuCreationFailureEmailGenerator implements InitializingBean {
	
	private static final String FORMAT_DATE = "dd.MM.yyyy";
	
	private String subject;

	private String templateForPersonNotFound;
	private String templateForProjectNotFound;
	private String templateForTaskNotFound;
	private String templateForTimesheetsExist;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			subject,
			
			templateForPersonNotFound,
			templateForProjectNotFound,
			templateForTaskNotFound,
			templateForTimesheetsExist
		);
	}
	
	private Map<String, String> getPlaceholderValueMapWithCommons(CreateTimesheetForLeaveFailure failure) {

		Map<String, String> placeholderValues = Maps.newHashMap();

		DateFormat dateFormatter = new SimpleDateFormat(FORMAT_DATE);
		
		String leaveDayAsString = dateFormatter.format(failure.getLeaveDay());
		placeholderValues.put("zi_concediu", leaveDayAsString);
		
		return placeholderValues;
	}
	
	private String getContentWithPlaceholderValues(String template, Map<String, String> placeholderValues) {
		return TextWithPlaceholdersHelper.replacePlaceholders(template, new MapBackedPlaceholderValueContext(placeholderValues));
	}
	
	private String getContent(CreateTimesheetForLeaveFailure failure) {
		
		Map<String, String> placeholderValues = getPlaceholderValueMapWithCommons(failure);
		
		if (failure instanceof CreateTimesheetForLeavePersonNotFoundFailure) {
			CreateTimesheetForLeavePersonNotFoundFailure personNotFoundFailure = (CreateTimesheetForLeavePersonNotFoundFailure) failure;
			placeholderValues.put("email_persoana", personNotFoundFailure.getRequesterEmail());
			return getContentWithPlaceholderValues(templateForPersonNotFound, placeholderValues);
		} else if (failure instanceof CreateTimesheetForLeaveProjectNotFoundFailure) {
			CreateTimesheetForLeaveProjectNotFoundFailure projectNotFoundFailure = (CreateTimesheetForLeaveProjectNotFoundFailure) failure;			
			placeholderValues.put("nume_departament", projectNotFoundFailure.getRequesterDepartmentName());			
			return getContentWithPlaceholderValues(templateForProjectNotFound, placeholderValues);
		} else if (failure instanceof CreateTimesheetForLeaveTaskNotFoundFailure) {
			CreateTimesheetForLeaveTaskNotFoundFailure taskNotFoundFailure = (CreateTimesheetForLeaveTaskNotFoundFailure) failure;
			placeholderValues.put("nume_proiect", taskNotFoundFailure.getProjectName());
			placeholderValues.put("nume_task_necesar", taskNotFoundFailure.getRequestedTaskName());
			return getContentWithPlaceholderValues(templateForTaskNotFound, placeholderValues);
		} else if (failure instanceof CreateTimesheetForLeaveTimesheetsExistFailure) {
			return getContentWithPlaceholderValues(templateForTimesheetsExist, placeholderValues);
		} else {
			String exceptionMessage = "Nu se poate genera mesaj de e-mail pentru tipul " +
				"[" + failure.getClass().getName() + "] pentru ca tipul nu este suportat.";
			throw new IllegalArgumentException(exceptionMessage);
		}
	}
	
	public EmailMessage getEmailForFailure(CreateTimesheetForLeaveFailure failure) {

		String toAddress = failure.getRequesterEmail();
		String content = getContent(failure);
		
		EmailMessage emailMessage = new EmailMessage(toAddress, subject, content);
		return emailMessage;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setTemplateForPersonNotFound(String templateForPersonNotFound) {
		this.templateForPersonNotFound = templateForPersonNotFound;
	}
	public void setTemplateForProjectNotFound(String templateForProjectNotFound) {
		this.templateForProjectNotFound = templateForProjectNotFound;
	}
	public void setTemplateForTaskNotFound(String templateForTaskNotFound) {
		this.templateForTaskNotFound = templateForTaskNotFound;
	}
	public void setTemplateForTimesheetsExist(String templateForTimesheetsExist) {
		this.templateForTimesheetsExist = templateForTimesheetsExist;
	}
}