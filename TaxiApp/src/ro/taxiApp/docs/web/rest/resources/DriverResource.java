package ro.taxiApp.docs.web.rest.resources;

import java.util.List;

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
import ro.taxiApp.docs.presentation.client.shared.model.drivers.DriverModel;
import ro.taxiApp.docs.presentation.server.utils.PresentationExceptionUtils;
import ro.taxiApp.docs.services.drivers.DriverService;

@Component
@Path("/Drivers")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class DriverResource extends BaseResource {

	@Autowired
	private DriverService driverService;
	
	@POST
	@Path("/saveDriver")
	public Long save(DriverModel driverModel) throws PresentationException {
		try {
			return driverService.save(driverModel);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getAllDrivers")
	public List<DriverModel> getAll() throws PresentationException {
		return driverService.getAll();
	}
	
	@POST
	@Path("/getDriverById/{id}")
	public DriverModel getDriverById(@PathParam("id") Long id) throws PresentationException {
		return driverService.getById(id);
	}
	
	@POST
	@Path("/getDriverByUserId/{id}")
	public DriverModel getDriverByUserId(@PathParam("id") Long id) throws PresentationException {
		return driverService.getDriverByUserId(id);
	}
	
	@POST
	@Path("/deleteDriverById/{id}")
	public void deleteDriverById(@PathParam("id") Long id) throws PresentationException {
		driverService.deleteById(id);
	}
	
}
