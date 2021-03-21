package ro.taxiApp.docs.web.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.presentation.client.shared.exceptions.PresentationException;
import ro.taxiApp.docs.presentation.client.shared.model.auth.LoggedInUserModel;
import ro.taxiApp.docs.presentation.client.shared.model.auth.LoginRequestModel;
import ro.taxiApp.docs.presentation.client.shared.model.auth.LoginResponseModel;
import ro.taxiApp.docs.presentation.client.shared.model.organization.PasswordChangeModel;
import ro.taxiApp.docs.presentation.server.utils.PresentationExceptionUtils;
import ro.taxiApp.docs.services.security.AuthService;

@Component
@Path("/Auth")
public class AuthResource extends BaseResource{
	
	@Autowired
	private AuthService authService;
	
	@POST
	@Path("/login")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response login(LoginRequestModel loginRequest) throws PresentationException {
		try {
			LoginResponseModel loginResponse = authService.login(loginRequest);
        	return Response.ok(loginResponse).build();
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getLoggedInUser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response getLoggedInUser() {
		LoggedInUserModel loggedInUserModel = authService.getLoggedInUser();
        return Response.ok(loggedInUserModel).build();
	}
	
	@POST
	@Path("/changePassword")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response changePassword(PasswordChangeModel passwordChangeModel) 
			throws PresentationException {
		try {
			authService.changePassword(passwordChangeModel, getSecurity());
			return Response.ok().build();
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
}
