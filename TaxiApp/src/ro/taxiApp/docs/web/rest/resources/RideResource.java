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

import ro.taxiApp.docs.presentation.client.shared.exceptions.PresentationException;
import ro.taxiApp.docs.presentation.client.shared.model.rides.RideModel;
import ro.taxiApp.docs.services.ride.RideService;

@Component
@Path("/Ride")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class RideResource extends BaseResource {
	
	@Autowired
	private RideService rideService;
	
	@POST
	@Path("/saveRide")
	public Long save(RideModel ride) throws PresentationException {
		return rideService.save(ride);
	}
	
	@POST
	@Path("/getRideById/{id}")
	public RideModel getRideById(@PathParam("id") Long id) throws PresentationException {
		return rideService.getById(id);
	}
	
	@POST
	@Path("/getAllRides")
	public List<RideModel> getAll() throws PresentationException {
		return rideService.getAll();
	}
	
	@POST
	@Path("/getRidesForDriver/{driverId}")
	public List<RideModel> getRidesForDriver(@PathParam("driverId") Long driverId) throws PresentationException {
		return rideService.getRidesForDriver(driverId);
	}
	
	@POST
	@Path("/getActiveRide/{clientId}")
	public RideModel getActiveRide(@PathParam("clientId") Long clientId) throws PresentationException {
		RideModel ride = rideService.getActiveRideByClientId(clientId);
		return ride;
	}
	
}
