package ro.cloudSoft.cloudDoc.plugins.bpm.variables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.jbpm.api.Execution;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Task;

import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentIdentifier;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

import com.google.common.collect.Maps;

/**
 * 
 */
public class WorkflowVariableHelper {
	
	private static final String DOCUMENT_CREATED_DATE_FORMAT = "yyyy.MM.dd HH:mm:ss,SSS";
	
	/** Pregateste si returneaza un Map cu variabilele ce vor fi puse pe fluxul documentului. */
	public static Map<String, Object> prepareVariablesForNewProcessInstance(Document document, SecurityManager userSecurity) {
		
		Map<String, Object> variableValueByName = Maps.newHashMap();
		
		Long senderUserId = userSecurity.getUserId();
		String senderUserIdAsString = senderUserId.toString();
		variableValueByName.put(WorkflowVariableNames.SENDER_USER_ID, senderUserIdAsString);
		String initiatorIdAsString = userSecurity.getUserIdAsString();
		variableValueByName.put(WorkflowVariableNames.INITIATOR_ID, initiatorIdAsString);
		String documentIdAsString = document.getId();
		variableValueByName.put(WorkflowVariableNames.DOCUMENT_ID, documentIdAsString);
		String documentLocationRealNameAsString = document.getDocumentLocationRealName();
		variableValueByName.put(WorkflowVariableNames.DOCUMENT_LOCATION_REAL_NAME, documentLocationRealNameAsString);
		String documentTypeIdAsString = document.getDocumentTypeId().toString();
		variableValueByName.put(WorkflowVariableNames.DOCUMENT_TYPE_ID, documentTypeIdAsString);
		String documentAuthorIdAsString = document.getAuthor();
		variableValueByName.put(WorkflowVariableNames.DOCUMENT_AUTHOR_ID, documentAuthorIdAsString);
		String documentNameAsString = document.getName();
		variableValueByName.put(WorkflowVariableNames.DOCUMENT_NAME, documentNameAsString);
		String documentCreatedDateAsString = new SimpleDateFormat(DOCUMENT_CREATED_DATE_FORMAT).format(document.getCreated().getTime());
		variableValueByName.put(WorkflowVariableNames.DOCUMENT_CREATED_DATE, documentCreatedDateAsString);
		
		return variableValueByName;
	}

	public static void setSenderUserId(ProcessEngine processEngine, String processInstanceId, Long senderUserId) {
		String senderUserIdAsString = senderUserId.toString();
		processEngine.getExecutionService().setVariable(processInstanceId, WorkflowVariableNames.SENDER_USER_ID, senderUserIdAsString);
	}
	
	public static String getDocumentLocationRealName(OpenExecution execution) {
		String documentLocationRealNameAsString = (String) execution.getVariable(WorkflowVariableNames.DOCUMENT_LOCATION_REAL_NAME);
		String documentLocationRealName = getDocumentLocationRealNameFromVariableValue(documentLocationRealNameAsString);
		return documentLocationRealName;
	}
	
	public static String getDocumentId(OpenExecution execution) {
		String documentIdAsString = (String) execution.getVariable(WorkflowVariableNames.DOCUMENT_ID);
		String documentId = getDocumentIdFromVariableValue(documentIdAsString);
		return documentId;
	}
	
	public static DocumentIdentifier getDocumentIdentifier(ProcessEngine processEngine, Task task) {
		
		String executionId = task.getExecutionId();
		
		String documentLocationRealNameAsString = (String) processEngine.getExecutionService().getVariable(executionId, WorkflowVariableNames.DOCUMENT_LOCATION_REAL_NAME);
		String documentLocationRealName = getDocumentLocationRealNameFromVariableValue(documentLocationRealNameAsString);
		
		String documentIdAsString = (String) processEngine.getExecutionService().getVariable(executionId, WorkflowVariableNames.DOCUMENT_ID);
		String documentId = getDocumentIdFromVariableValue(documentIdAsString);
		
		return new DocumentIdentifier(documentLocationRealName, documentId);
	}
	
	private static String getDocumentLocationRealNameFromVariableValue(String variableValue) {
		return variableValue;
	}

	private static String getDocumentIdFromVariableValue(String variableValue) {
		return variableValue;
	}	
	
	public static Long getDocumentTypeId(OpenExecution execution) {
		String documentTypeIdAsString = (String) execution.getVariable(WorkflowVariableNames.DOCUMENT_TYPE_ID);
		Long documentTypeId = Long.valueOf(documentTypeIdAsString);
		return documentTypeId;
	}
	
	public static String getDocumentName(OpenExecution execution) {
		String documentNameAsString = (String) execution.getVariable(WorkflowVariableNames.DOCUMENT_NAME);
		String documentName = documentNameAsString;
		return documentName;
	}
	
	public static Long getDocumentAuthorUserId(OpenExecution execution) {
		String documentAuthorIdAsString = (String) execution.getVariable(WorkflowVariableNames.DOCUMENT_AUTHOR_ID);
		Long documentAuthorId = Long.valueOf(documentAuthorIdAsString);
		return documentAuthorId;
	}
	
	public static Date getDocumentCreatedDate(OpenExecution execution) {
		String documentCreatedDateAsString = (String) execution.getVariable(WorkflowVariableNames.DOCUMENT_CREATED_DATE);
		try {
			Date documentCreatedDate = new SimpleDateFormat(DOCUMENT_CREATED_DATE_FORMAT).parse(documentCreatedDateAsString);
			return documentCreatedDate;
		} catch (ParseException pe) {
			throw new IllegalStateException(pe);
		}
	}
	
	public static String getLastTransitionName(OpenExecution execution) {
		String lastTransitionNameAsString = (String) execution.getVariable(WorkflowVariableNames.LAST_TRANSITION_NAME);
		String lastTransitionName = lastTransitionNameAsString;
		return lastTransitionName;
	}
	
	public static String getLastTransitionName(ProcessEngine processEngine, String executionId) {
		Execution foundExecution = processEngine.getExecutionService().findExecutionById(executionId);
		if (!(foundExecution instanceof OpenExecution)) {
			String exceptionMessage = "Executia cu ID-ul [" + executionId + "] NU este de tipul necesar (" + OpenExecution.class.getName() + ").";
			throw new IllegalArgumentException(exceptionMessage);
		}
		return getLastTransitionName((OpenExecution) foundExecution);
	}
	
	public static Long getSenderUserId(OpenExecution execution) {
		String senderUserIdAsString = (String) execution.getVariable(WorkflowVariableNames.SENDER_USER_ID);
		Long senderUserId = Long.valueOf(senderUserIdAsString);
		return senderUserId;
	}
	
	public static Long getInitiatorUserId(OpenExecution execution) {
		String initiatorUserIdAsString = (String) execution.getVariable(WorkflowVariableNames.INITIATOR_ID);
		Long initiatorUserId = Long.valueOf(initiatorUserIdAsString);
		return initiatorUserId;
	}
}