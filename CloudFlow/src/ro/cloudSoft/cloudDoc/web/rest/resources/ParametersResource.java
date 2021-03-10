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

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.parameters.ParameterModel;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.parameters.ParametersService;

@Component
@Path("/Parameters")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ParametersResource extends BaseResource {
	
	@Autowired
	private ParametersService parametersService;
	
	@POST
	@Path("/saveParameter")
	public void save(ParameterModel parameterModel) throws PresentationException {
		try {
			parametersService.save(parameterModel);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getAllParameters")
	public List<ParameterModel> getAllParameters() {
		return parametersService.getAllParameters();
	}

	@POST
	@Path("/getParameterByName/{name}")
	public ParameterModel getParameterByName(@PathParam("name") String name) throws PresentationException {
		try {
			return parametersService.getParameterByName(name);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getParameterById/{id}")
	public ParameterModel getParameterByName(@PathParam("id") Long id) {
		return parametersService.getParameterById(id);
	}

	@POST
	@Path("/deleteParameterById/{id}")
	public void deleteParameterById(@PathParam("id") Long id) {
		parametersService.deleteById(id);
	}
}
