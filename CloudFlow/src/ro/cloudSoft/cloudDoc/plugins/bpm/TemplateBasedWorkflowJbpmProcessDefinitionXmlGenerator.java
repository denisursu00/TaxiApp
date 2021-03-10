package ro.cloudSoft.cloudDoc.plugins.bpm;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowPathWithManualAssignment;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.bpm.assignment.IncomingTransitionAwareAssignmentHandler;
import ro.cloudSoft.cloudDoc.plugins.bpm.assignment.ApplicationAssignmentHandler;
import ro.cloudSoft.cloudDoc.plugins.bpm.events.EndStateStartEventListener;
import ro.cloudSoft.cloudDoc.plugins.bpm.events.StartEventListener;
import ro.cloudSoft.cloudDoc.plugins.bpm.events.StateStartEventListener;
import ro.cloudSoft.cloudDoc.plugins.bpm.events.TransitionEventListener;
import ro.cloudSoft.cloudDoc.plugins.bpm.jpdl.WorkflowJbpmProcess;
import ro.cloudSoft.cloudDoc.plugins.bpm.jpdl.WorkflowJbpmProcessBuilder;
import ro.cloudSoft.cloudDoc.plugins.bpm.variables.WorkflowVariableNames;
import ro.cloudSoft.cloudDoc.templating.TemplateEngine;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.ImmutableMap;

/**
 * 
 */
public class TemplateBasedWorkflowJbpmProcessDefinitionXmlGenerator implements WorkflowJbpmProcessDefinitionXmlGenerator, InitializingBean {

	private TemplateEngine templateEngine;
	private String templateName;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			templateEngine,
			templateName
		);
	}
	
	@Override
	public byte[] createProcessDefinition(Workflow workflow, SecurityManager securityManager) throws AppException {
		
		WorkflowJbpmProcessBuilder workflowJbpmProcessBuilder = new WorkflowJbpmProcessBuilder(workflow);
		WorkflowJbpmProcess workflowJbpmProcess = workflowJbpmProcessBuilder.build();
		
		Map<String, Object> contextMap = ImmutableMap.<String, Object> builder()
			.put("process", workflowJbpmProcess)
			.put("EVENT_LISTENER_CLASS_PROCESS_START", StartEventListener.class.getName())
			.put("EVENT_LISTENER_CLASS_NODE_START", StateStartEventListener.class.getName())
			.put("EVENT_LISTENER_CLASS_END_NODE_START", EndStateStartEventListener.class.getName())
			.put("EVENT_LISTENER_CLASS_TRANSITION", TransitionEventListener.class.getName())
			.put("ASSIGNMENT_HANDLER_CLASS", IncomingTransitionAwareAssignmentHandler.class.getName())
			.put("APPLICATION_ASSIGNMENT_HANDLER_CLASS", ApplicationAssignmentHandler.class.getName())
			.put("CLASS_WORKFLOW_PATH_WITH_MANUAL_ASSIGNMENT", WorkflowPathWithManualAssignment.class.getName())
			.put("VARIABLE_NAME_LAST_TRANSITION_NAME", WorkflowVariableNames.LAST_TRANSITION_NAME)
			.build();
		
		return templateEngine.processAndReturnAsBytes(templateName, contextMap);
	}
	
	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
}