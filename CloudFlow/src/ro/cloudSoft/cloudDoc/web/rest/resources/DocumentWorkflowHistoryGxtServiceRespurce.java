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
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentHistoryViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.bpm.DocumentWorkflowHistoryGxtService;

@Component
@Path("/DocumentWorkflowHistoryGxtService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class DocumentWorkflowHistoryGxtServiceRespurce {

	@Autowired
	private DocumentWorkflowHistoryGxtService documentWorkflowHistoryGxtService;

	@POST
	@Path("/getDocumentHistory/{documentId}")
	public List<DocumentHistoryViewModel> getDocumentHistory(@PathParam("documentId") String documentId) throws PresentationException {
		return documentWorkflowHistoryGxtService.getDocumentHistory(documentId);
	}
}