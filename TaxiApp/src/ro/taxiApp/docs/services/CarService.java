package ro.taxiApp.docs.services;

import java.util.List;

import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.presentation.client.shared.model.cars.CarModel;

public interface CarService {
	
	public Long save(CarModel carModel) throws AppException;
	
	public CarModel getById(Long id);
	
	public List<CarModel> getAll();
	
	public void deleteById(Long id);

}
