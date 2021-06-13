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
import ro.taxiApp.docs.presentation.client.shared.model.cars.CarCategoryModel;
import ro.taxiApp.docs.presentation.client.shared.model.cars.CarModel;
import ro.taxiApp.docs.presentation.server.utils.PresentationExceptionUtils;
import ro.taxiApp.docs.services.CarService;

@Component
@Path("/Car")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class CarResource extends BaseResource {
	
	@Autowired
	private CarService carService;
	
	@POST
	@Path("/saveCar")
	public Long save(CarModel carModel) throws PresentationException {
		try {
			return carService.save(carModel);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getAllCars")
	public List<CarModel> getAll() throws PresentationException {
		return carService.getAll();
	}
	
	@POST
	@Path("/getCarById/{id}")
	public CarModel getCarById(@PathParam("id") Long id) throws PresentationException {
		return carService.getById(id);
	}
	
	@POST
	@Path("/deleteCarById/{id}")
	public void deleteCarById(@PathParam("id") Long id) throws PresentationException {
		carService.deleteById(id);
	}
	
	@POST
	@Path("/getCarCategoryById/{id}")
	public CarCategoryModel getCarCategoryById(@PathParam("id") Long id) throws PresentationException {
		return carService.getCarCategoryById(id);
	}
	
	@POST
	@Path("/getAllCarCategories")
	public List<CarCategoryModel> getAllCarCategories() throws PresentationException {
		return carService.getAllCarCategories();
	}
	
}
