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

import com.extjs.gxt.ui.client.data.ModelData;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.DocumentLocationGxtService;

@Component
@Path("/DocumentLocationGxtService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class DocumentLocationGxtServiceResource {
	
	@Autowired
	private DocumentLocationGxtService documentLocationGxtService;

	@POST
	@Path("/getAllDocumentLocations")
	public List<DocumentLocationModel> getAllDocumentLocations() throws PresentationException {
		return documentLocationGxtService.getAllDocumentLocations();
	}

	@POST
	@Path("/getAllDocumentLocationsAsModelData")
	public List<ModelData> getAllDocumentLocationsAsModelData() throws PresentationException {
		return documentLocationGxtService.getAllDocumentLocationsAsModelData();
	}
	
	@POST
	@Path("/getDocumentLocation")
	public List<ModelData> getDocumentLocation(DocumentLocationModel documentLocation) {
		return documentLocationGxtService.getDocumentLocation(documentLocation);
	}

	@POST
	@Path("/getFoldersFromDocumentLocation/{documentLocationRealName}")
	public List<ModelData> getFoldersFromDocumentLocation(@PathParam("documentLocationRealName") String documentLocationRealName) throws PresentationException {
		return documentLocationGxtService.getFoldersFromDocumentLocation(documentLocationRealName);
	}

	@POST
	@Path("/saveDocumentLocation")
	public String saveDocumentLocation(DocumentLocationModel documentLocationModel) throws PresentationException {    	
		return documentLocationGxtService.saveDocumentLocation(documentLocationModel);
	}
	
	@POST
	@Path("/deleteDocumentLocation/{realName}")
 	public void deleteDocumentLocation(@PathParam("realName") String realName) throws PresentationException {
		documentLocationGxtService.deleteDocumentLocation(realName);
	}

	@POST
	@Path("/getDocumentLocationByRealName/{realName}")
	public DocumentLocationModel getDocumentLocationByRealName(@PathParam("realName") String realName) throws PresentationException {
		return documentLocationGxtService.getDocumentLocationByRealName(realName);
	}
}
