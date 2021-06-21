package ro.taxiApp.docs.services.ride;

import java.util.List;

import ro.taxiApp.docs.presentation.client.shared.model.rides.RideModel;

public interface RideService {
	
	public Long save(RideModel ride);
	
	public RideModel getById(Long id);
	
	public List<RideModel> getAll();
	
	public RideModel getActiveRideByClientId(Long clientId);

}
