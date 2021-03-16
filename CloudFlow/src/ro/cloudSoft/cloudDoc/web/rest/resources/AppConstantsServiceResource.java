package ro.cloudSoft.cloudDoc.web.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ApplicationInfoModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AppGxtService;

@Component
@Path("/AppConstantsService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class AppConstantsServiceResource {

	@Autowired
	AppGxtService appGxtService;
	
	@POST
	@Path("/getApplicationInfo")
	public ApplicationInfoModel getApplicationInfo() {
		return appGxtService.getApplicationInfo();
	}
}