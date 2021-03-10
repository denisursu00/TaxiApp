package ro.cloudSoft.cloudDoc.presentation.server.converters.bpm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowTransitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentTypeConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.OrganizationEntityConverter;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class WorkflowConverter implements InitializingBean {
	
	private WorkflowService workflowService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			workflowService
		);
	}

	public WorkflowModel getModelFromWorkflow(Workflow workflow) {
		
		WorkflowModel model = new WorkflowModel();
		
		model.setId(workflow.getId());
		model.setName(workflow.getName());
		model.setDescription(workflow.getDescription());
		
		if (workflow.getBaseVersion() != null) {
			Long baseVersionWorkflowId = workflow.getBaseVersion().getId();
			model.setBaseVersionWorkflowId(baseVersionWorkflowId);
		}
		if (workflow.getSourceVersion() != null) {
			Long sourceVersionWorkflowId = workflow.getSourceVersion().getId();
			model.setSourceVersionWorkflowId(sourceVersionWorkflowId);
		}
		model.setVersionNumber(workflow.getVersionNumber());
		
		List<DocumentTypeModel> listDocs = new ArrayList<DocumentTypeModel>();
		if (workflow.getDocumentTypes() != null){
			for (DocumentType dt : workflow.getDocumentTypes()){
				DocumentTypeModel dtModel = DocumentTypeConverter.getModelFromDocumentType(dt);
				listDocs.add(dtModel);
			}
		}
		model.setDocumentTypes(listDocs);
		
		List<OrganizationEntityModel> listSupervisors = new ArrayList<OrganizationEntityModel>();
		if (workflow.getSupervisors() != null){
			for (OrganizationEntity oe : workflow.getSupervisors()){
				OrganizationEntityModel oeModel = OrganizationEntityConverter.getModelFromOrganizationEntity(oe);
				listSupervisors.add(oeModel);
			}
		}
		model.setSupervisors(listSupervisors);
		
		List<WorkflowTransitionModel> listTransitions = new ArrayList<WorkflowTransitionModel>();
		for (WorkflowTransition wt : workflow.getTransitions()){
			WorkflowTransitionModel wtModel = WorkflowTransitionConverter.getModelFromWorkflowTransition(wt);
			listTransitions.add(wtModel);
		}
		model.setTransitions(listTransitions);
		model.setProcessDefinitionId(workflow.getProcessDefinitionId());
		
		return model;
	}
	
	public Workflow getWorkflowFromModel(WorkflowModel model) {
		
		Workflow workflow = new Workflow();
		
		workflow.setId(model.getId());
		workflow.setName(model.getName());
		workflow.setDescription(model.getDescription());
		
		if (model.getBaseVersionWorkflowId() != null) {
			Workflow baseVersion = workflowService.getWorkflowById(model.getBaseVersionWorkflowId());
			workflow.setBaseVersion(baseVersion);
		}
		if (model.getSourceVersionWorkflowId() != null) {
			Workflow sourceVersion = workflowService.getWorkflowById(model.getSourceVersionWorkflowId());
			workflow.setSourceVersion(sourceVersion);
		}
		workflow.setVersionNumber(model.getVersionNumber());
		
		Set<DocumentType> listDocs = new HashSet<DocumentType>();
		if (model.getDocumentTypes() != null){
			for (DocumentTypeModel dtModel : model.getDocumentTypes()){
				// TODO aici ar trebui sa folosesc DocumentTypeConvertor dar acolo da 
				// erori pentru valori null, asa ca am facut eu conversia minima din
				// model in DocumentType
				DocumentType dt = new DocumentType();
				dt.setId(dtModel.getId());
				dt.setName(dtModel.getName());
				listDocs.add(dt);
			}
		}
		workflow.setDocumentTypes(listDocs);
	
		Set<OrganizationEntity> listSupervisors = new HashSet<OrganizationEntity>();
		if (model.getSupervisors() != null){
			for (OrganizationEntityModel oeModel : model.getSupervisors()){
				OrganizationEntity oe = OrganizationEntityConverter.getOrganizationEntityFromModel(oeModel);
				listSupervisors.add(oe);
			}
		}
		workflow.setSupervisors(listSupervisors);
		
		Set<WorkflowTransition> listTransitions = new HashSet<WorkflowTransition>();
		if (model.getTransitions() != null){
			for (WorkflowTransitionModel wtModel : model.getTransitions()){
				WorkflowTransition wt = WorkflowTransitionConverter.getWorkflowTransitionFromModel(wtModel);
				listTransitions.add(wt);
			}
		}
		workflow.setTransitions(listTransitions);
		workflow.setProcessDefinitionId(model.getProcessDefinitionId());
		
		return workflow;
	}
	
	public List<WorkflowModel> getModelsFromWorkflows(List<Workflow> workflows){
		List<WorkflowModel> models = new ArrayList<WorkflowModel>();
		if (workflows != null){
			for (Workflow w : workflows){
				WorkflowModel wModel = getModelFromWorkflowForDisplay(w);
				models.add(wModel);
			}
		}
		return models;
	}
	
	private WorkflowModel getModelFromWorkflowForDisplay(Workflow workflow)
	{
		WorkflowModel model = new WorkflowModel();
		model.setId(workflow.getId());
		model.setName(workflow.getName());
		model.setDescription(workflow.getDescription());
		model.setVersionNumber(workflow.getVersionNumber());
		List<DocumentTypeModel> listDocs = new ArrayList<DocumentTypeModel>();
		if (workflow.getDocumentTypes() != null){
			for (DocumentType dt : workflow.getDocumentTypes()){
				DocumentTypeModel dtModel = DocumentTypeConverter.getModelFromDocumentType(dt);
				listDocs.add(dtModel);
			}
		}
		model.setDocumentTypes(listDocs);
		return model;
	}

	public List<Workflow> getWorkflowsFromModels(List<WorkflowModel> models) {
		List<Workflow> workflows = new ArrayList<Workflow>();
		if (models != null){
			for (WorkflowModel wModel : models){
				Workflow w = getWorkflowFromModel(wModel);
				workflows.add(w);
			}
		}
		return workflows;
	}
	
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
}