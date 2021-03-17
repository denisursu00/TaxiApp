package ro.taxiApp.docs.web.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.taxiApp.docs.presentation.client.shared.model.SecurityManagerModel;
import ro.taxiApp.docs.presentation.client.shared.services.AclGxtService;

@Component
@Path("/AclGxtService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class AclGxtServiceResource extends BaseResource {

	@Autowired
	private AclGxtService aclGxtService;
	
	@POST
	@Path("/getSecurityManager")
	public SecurityManagerModel getSecurityManager() {
		return aclGxtService.getSecurityManager();
	}
}
