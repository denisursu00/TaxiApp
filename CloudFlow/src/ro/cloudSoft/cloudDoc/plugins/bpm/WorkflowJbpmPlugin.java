package ro.cloudSoft.cloudDoc.plugins.bpm;

import java.util.List;

import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowDao;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class WorkflowJbpmPlugin implements WorkflowPlugin, InitializingBean {

	private WorkflowDao workflowDao;
	private WorkflowJbpmProcessDefinitionXmlGenerator workflowJbpmProcessDefinitionXmlGenerator;
	private ProcessEngine processEngine;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			workflowDao,
			workflowJbpmProcessDefinitionXmlGenerator,
			processEngine
		);
	}
	
	@Override
	public String createOrUpdateProcess(Workflow workflow, SecurityManager userSecurity) throws AppException {
		
		NewDeployment deployment = processEngine.getRepositoryService().createDeployment();
		String xml = new String(workflowJbpmProcessDefinitionXmlGenerator.createProcessDefinition(workflow, userSecurity));
		String newDeploymentId = deployment.addResourceFromString(workflow.getName() + ".jpdl.xml", xml).deploy();

		List<ProcessDefinition> processDefinitions = processEngine.getRepositoryService()
			.createProcessDefinitionQuery().processDefinitionName(workflow.getName()).list();

		for (ProcessDefinition processDefinition : processDefinitions) {
			// Trebuie returnat ID-ul ULTIMEI versiuni de proces.
			if (processDefinition.getDeploymentId().equals(newDeploymentId)) {
				return processDefinition.getId();
			}
		}
		
		String errorMessage = "S-a adaugat / modificat fluxul cu ID-ul [%d] si numele [%s], " +
			"dar nu s-a gasit ultima versiune a procesului jBPM asociat.";
		throw new IllegalStateException(String.format(errorMessage, workflow.getId(), workflow.getName()));
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void removeProcess(String processDefinitionId,
			SecurityManager userSecurity) 
	{
		List<ProcessDefinition> list = processEngine.getRepositoryService()
				.createProcessDefinitionQuery().processDefinitionId(
						processDefinitionId + "").list();
		if (list != null && list.size() > 0) {
			processEngine.getRepositoryService().deleteDeploymentCascade(list.get(0)
					.getDeploymentId());
		}
	}

	@Override
	public WorkflowState getStartState(Workflow workflow) {
		
		if (workflow == null) {
			return null;
		}
		
		List<String> startStateCodes = this.processEngine.getRepositoryService().getStartActivityNames(workflow.getProcessDefinitionId());
		if (startStateCodes.isEmpty()) {
			return null;
		}
		String startStateCode = startStateCodes.get(0);
		return this.workflowDao.getWorkflowState(workflow.getId(), startStateCode);
	}

	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
	}
	public void setWorkflowJbpmProcessDefinitionXmlGenerator(WorkflowJbpmProcessDefinitionXmlGenerator workflowJbpmProcessDefinitionXmlGenerator) {
		this.workflowJbpmProcessDefinitionXmlGenerator = workflowJbpmProcessDefinitionXmlGenerator;
	}
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}
}