package ro.cloudSoft.cloudDoc.plugins.bpm;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 * 
 */
public interface WorkflowJbpmProcessDefinitionXmlGenerator {

	byte[] createProcessDefinition(Workflow workflow, SecurityManager securityManager) throws AppException;
}