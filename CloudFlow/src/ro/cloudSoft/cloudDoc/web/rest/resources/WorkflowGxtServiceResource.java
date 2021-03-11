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
	
	
}