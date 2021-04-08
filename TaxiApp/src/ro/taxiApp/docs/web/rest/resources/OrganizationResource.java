package ro.taxiApp.docs.web.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.presentation.client.shared.exceptions.PresentationException;
import ro.taxiApp.docs.presentation.client.shared.model.organization.UserModel;
import ro.taxiApp.docs.presentation.server.utils.PresentationExceptionUtils;
import ro.taxiApp.docs.services.organization.UserService;

@Component
@Path("/Organization")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class OrganizationResource extends BaseResource {

	private static final String driver = "SOFER";
	private static final String client = "CLIENT";
	private static final String dispatcher = "DISPECER";
	
	@Autowired
	private UserService userService;
	
	@POST
	@Path("/saveUserAsDriver")
	public Long saveUserAsDriver(UserModel user) throws PresentationException {
		try {
			return userService.saveUserWithRole(user, driver, getSecurity());
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
	
	@POST
	@Path("/saveUserAsClient")
	public Long saveUserAsClient(UserModel user) throws PresentationException {
		try {
			return userService.saveUserWithRole(user, client, getSecurity());
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
	
	@POST
	@Path("/saveUserAsDispatcher")
	public Long saveUserAsDispatcher(UserModel user) throws PresentationException {
		try {
			return userService.saveUserWithRole(user, dispatcher, getSecurity());
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
	
	@POST
	@Path("/getUserById/{id}")
	public UserModel getUserById(@PathParam("id") Long id) {
		return userService.getUserByIdAsModel(id);
	}
	
}
