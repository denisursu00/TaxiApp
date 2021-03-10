package ro.cloudSoft.cloudDoc.plugins.bpm.events;

import java.util.List;

import org.jbpm.api.JbpmException;
import org.jbpm.api.listener.EventListener;
import org.jbpm.api.listener.EventListenerExecution;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowPathWithManualAssignment;
import ro.cloudSoft.cloudDoc.plugins.bpm.SpringContextUtils;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowManualAssignmentVariableHelper;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableHelper;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.utils.log.LogConstants;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;

public class StartEventListener implements EventListener {
	
	private static final long serialVersionUID = 1L;
	
	private final static LogHelper logger = LogHelper.getInstance(StartEventListener.class);
	
	private List<WorkflowPathWithManualAssignment> pathsWithManualAssignment;
	
	@Override
	public void notify(EventListenerExecution execution) throws Exception {
		try {
			// Iau identificatorii documentului de pe flux.
			String documentLocationRealName = WorkflowVariableHelper.getDocumentLocationRealName(execution);
			String documentId = WorkflowVariableHelper.getDocumentId(execution);
			// Obtin serviciul pentru documente.
			DocumentService documentService = SpringContextUtils.getBean("documentService");
			// Schimb toate permisiunile documentului in reader.
			documentService.setReadOnly(documentLocationRealName, documentId);
			
			WorkflowManualAssignmentVariableHelper.initializeManualAssignmentVariables(execution, pathsWithManualAssignment);
		}catch (JbpmException jbpmException) {
			logger.error("Error creating variables for manual assignment", jbpmException, LogConstants.MODULE_BPM, "StartEventListener.notify");
		}
	}
	
	public void setPathsWithManualAssignment(List<WorkflowPathWithManualAssignment> pathsWithManualAssignment) {
		this.pathsWithManualAssignment = pathsWithManualAssignment;
	}
}