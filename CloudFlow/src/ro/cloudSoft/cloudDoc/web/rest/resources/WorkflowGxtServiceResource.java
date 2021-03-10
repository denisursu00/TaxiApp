package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CheckSendingRightsRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeIdsRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.GetCurrentStateRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm.WorkflowGxtService;

@Component
@Path("/WorkflowGxtServiceResource")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class WorkflowGxtServiceResource {
	
	@Autowired
	private WorkflowGxtService workflowGxtService;
	
	@POST
	@Path("/getAllWorkflows")
	public List<WorkflowModel> getAllWorkflows() throws PresentationException {
		return workflowGxtService.getAllWorkflows();
	}

	@POST
	@Path("/getWorkflowById/{id}")
	public WorkflowModel getWorkflowById(@PathParam("id") Long id) throws PresentationException {
		return workflowGxtService.getWorkflowById(id);
	}

	@POST
	@Path("/deleteWorkflow/{id}")
	public void deleteWorkflow(@PathParam("id") Long id) throws PresentationException {
		workflowGxtService.deleteWorkflow(id);
	}

	@POST
	@Path("/saveWorkflow")
	public void saveWorkflow(WorkflowModel workflowModel) throws PresentationException {
		workflowGxtService.saveWorkflow(workflowModel);
	}

	@POST
	@Path("/createNewVersion/{workflowId}")
	public Long createNewVersion(@PathParam("workflowId") Long workflowId) throws PresentationException {
		return workflowGxtService.createNewVersion(workflowId);
	}

	@POST
	@Path("/getStatesByDocumentTypeIds")
	public List<WorkflowStateModel> getStatesByDocumentTypeIds(DocumentTypeIdsRequestModel documentTypeIdsRequestModel)  throws PresentationException {
		return workflowGxtService.getStatesByDocumentTypeIds(documentTypeIdsRequestModel.getIds());
	}

	@POST
	@Path("/getWorkflowByDocumentType/{documentTypeId}")
	public WorkflowModel getWorkflowByDocumentType(@PathParam("documentTypeId") Long documentTypeId) {
		return workflowGxtService.getWorkflowByDocumentType(documentTypeId);
	}

	@POST
	@Path("/getWorkflowStatesByDocumentType/{documentTypeId}")
	public List<WorkflowStateModel> getWorkflowStatesByDocumentType(@PathParam("documentTypeId") Long documentTypeId) {
		return workflowGxtService.getWorkflowStatesByDocumentType(documentTypeId);
	}

	@POST
	@Path("/getWorkflowForDocument/{documentLocationRealName}/{documentId}/{documentTypeId}")
	public WorkflowModel getWorkflowForDocument(@PathParam("documentTypeId") String documentLocationRealName, @PathParam("documentTypeId") String documentId, @PathParam("documentTypeId") Long documentTypeId) {
		return workflowGxtService.getWorkflowForDocument(documentLocationRealName, documentId, documentTypeId);
	}

	@POST
	@Path("/getCurrentState")
	public WorkflowStateModel getCurrentState(GetCurrentStateRequestModel getCurrentStateRequestModel) throws PresentationException {
		return workflowGxtService.getCurrentState(getCurrentStateRequestModel.getWorkflowModel(), getCurrentStateRequestModel.getDocumentModel());
	}

	@POST
	@Path("/checkSendingRights")
	public Boolean checkSendingRights(CheckSendingRightsRequestModel checkSendingRightsRequestModel) throws PresentationException {
		return workflowGxtService.checkSendingRights(checkSendingRightsRequestModel.getWorkflowModel(), checkSendingRightsRequestModel.getDocumentModel());
	}

	@POST
	@Path("/hasInstances/{workflowId}")
	public boolean hasInstances(@PathParam("workflowId") Long workflowId) {
		return workflowGxtService.hasInstances(workflowId);
	}
}