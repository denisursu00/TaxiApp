package ro.taxiApp.docs.services.drivers;

import java.util.List;

import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.presentation.client.shared.model.drivers.DriverModel;

public interface DriverService {

	public Long save(DriverModel driverModel) throws AppException;
	
	public DriverModel getById(Long id);
	
	public DriverModel getDriverByUserId(Long userId);
	
	public List<DriverModel> getAll();
	
	public void deleteById(Long id);
	
}
