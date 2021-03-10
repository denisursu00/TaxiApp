package ro.cloudSoft.cloudDoc.presentation.server.services.bpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.bpm.Workflow;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowState;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm.WorkflowGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.WorkflowConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.bpm.WorkflowStateConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.content.DocumentConverter;
import ro.cloudSoft.cloudDoc.presentation.server.services.GxtServiceImplBase;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.Lists;

public class WorkflowGxtServiceImpl extends GxtServiceImplBase implements WorkflowGxtService, InitializingBean {
	
	private WorkflowService workflowService;
	private WorkflowExecutionService workflowExecutionService;
	
	private WorkflowConverter workflowConverter;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			workflowService,
			workflowExecutionService,
			
			workflowConverter
		);
	}

	@Override
	public List<WorkflowModel> getAllWorkflows() throws PresentationException
	{
		List<WorkflowModel> workflowModels = null;
		try
		{
			List<Workflow> workflows = workflowService.getAllWorkflowsForDisplay(getSecurity());			
			workflowModels = workflowConverter.getModelsFromWorkflows(workflows);
		} 
		catch (AppException ae)
		{
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		return workflowModels;
	}

	@Override
	public WorkflowModel getWorkflowById(Long id) throws PresentationException {
		Workflow workflow = workflowService.getWorkflowById(id, getSecurity());
		WorkflowModel workflowModel = workflowConverter.getModelFromWorkflow(workflow);
		return workflowModel;
	}

	@Override
	public void deleteWorkflow(Long id) throws PresentationException
	{
		try {
			workflowService.deleteWorkflow(id, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@Override
	public void saveWorkflow(WorkflowModel workflowModel) throws PresentationException
	{
		try {
			Workflow workflow = workflowConverter.getWorkflowFromModel(workflowModel);
			validateWorkflowBeforeSave(workflow);
			workflowService.saveWorkflow(workflow, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public Long createNewVersion(Long workflowId) throws PresentationException {
		try {
			return workflowService.createNewVersion(workflowId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	/**
	 * Valideaza fluxul inainte sa-l salveze, deoarece exista posibilitatea
	 * de adaugare in baza de date a unei stari de mai multe ori daca ea se afla
	 * in mai multe tranzitii.
	 */
	private void validateWorkflowBeforeSave(Workflow workflow){
		Set<WorkflowTransition> transitions = workflow.getTransitions();
		List<WorkflowState> states = new ArrayList<WorkflowState>();
		for (WorkflowTransition wt : transitions){
			states.add(wt.getStartState());
			states.add(wt.getFinalState());
		}	
		Map<String, WorkflowState> tempStates = new HashMap<String, WorkflowState>();
		for (WorkflowState state : states){
			if (!tempStates.containsKey(state.getCode()))
				tempStates.put(state.getCode(), state);
		}
		for (WorkflowTransition t : transitions){
			String codsi = t.getStartState().getCode();
			String codsf = t.getFinalState().getCode();
			t.setStartState(tempStates.get(codsi));
			t.setFinalState(tempStates.get(codsf));
		}
	}
	
	@Override
	public List<WorkflowStateModel> getStatesByDocumentTypeIds(List<Long> ids)  throws PresentationException {
		List<WorkflowStateModel> result = new ArrayList<WorkflowStateModel>();
		try {
			List<WorkflowState> list = workflowService.getStatesByDocumentTypeIds(ids, getSecurity());
			if (list != null){
				for(WorkflowState ws : list){
					WorkflowStateModel wsm = WorkflowStateConverter.getModelFromWorkflowState(ws);
					result.add(wsm);
				}
			}
		}catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		return result;
	}

	@Override
	public WorkflowModel getWorkflowByDocumentType(Long documentTypeId) {
		Workflow workflow = workflowService.getWorkflowByDocumentType(documentTypeId);
		if (workflow != null)
			return workflowConverter.getModelFromWorkflow(workflow);
		return null;
	}
	
	@Override
	public List<WorkflowStateModel> getWorkflowStatesByDocumentType(Long documentTypeId) {
		List<WorkflowState> workflowStates = workflowService.getWorkflowStatesByDocumentType(documentTypeId);
		List<WorkflowStateModel> workflowStateModels = Lists.newArrayListWithCapacity(workflowStates.size());
		for (WorkflowState workflowState : workflowStates) {
			WorkflowStateModel workflowStateModel = WorkflowStateConverter.getModelFromWorkflowState(workflowState);
			workflowStateModels.add(workflowStateModel);
		}
		return workflowStateModels;
	}
	
	@Override
	public WorkflowModel getWorkflowForDocument(String documentLocationRealName, String documentId, Long documentTypeId) {
		Workflow workflow = workflowService.getWorkflowForDocument(documentLocationRealName, documentId, documentTypeId);
		if (workflow == null) {
			return null;
		}
		return workflowConverter.getModelFromWorkflow(workflow);
	}
	
	@Override
	public WorkflowStateModel getCurrentState(WorkflowModel workflowModel, DocumentModel documentModel) throws PresentationException {
		if (workflowModel == null) {
			return null;
		}
		
		Workflow workflow = workflowConverter.getWorkflowFromModel(workflowModel);
		Document document = (documentModel != null) ? DocumentConverter.getDocumentFromModel(documentModel) : null;
		
		WorkflowState state = null;
		try {
			state = this.workflowExecutionService.getCurrentState(workflow, document, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
		
		if (state == null) {
			return null;
		}
		
		WorkflowStateModel stateModel = WorkflowStateConverter.getModelFromWorkflowState(state);
		
		return stateModel;
	}

	@Override
	public Boolean checkSendingRights(WorkflowModel workflowModel, DocumentModel documentModel) throws PresentationException {
		Workflow workflow = (workflowModel != null) ? workflowConverter.getWorkflowFromModel(workflowModel) : null;
		Document document = (documentModel != null) ? DocumentConverter.getDocumentFromModel(documentModel) : null;
		
		try {
			boolean canSend = this.workflowExecutionService.checkSendingRights(document, workflow, getSecurity());
			return new Boolean(canSend);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public boolean hasInstances(Long workflowId) {
		return workflowService.hasInstances(workflowId);
	}
		
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	public void setWorkflowExecutionService(WorkflowExecutionService workflowExecutionService) {
		this.workflowExecutionService = workflowExecutionService;
	}
	public void setWorkflowConverter(WorkflowConverter workflowConverter) {
		this.workflowConverter = workflowConverter;
	}
}