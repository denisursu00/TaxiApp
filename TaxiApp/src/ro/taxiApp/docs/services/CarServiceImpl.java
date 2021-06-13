package ro.taxiApp.docs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.dao.car.CarDao;
import ro.taxiApp.docs.domain.cars.Car;
import ro.taxiApp.docs.domain.cars.CarCategory;
import ro.taxiApp.docs.presentation.client.shared.model.cars.CarCategoryModel;
import ro.taxiApp.docs.presentation.client.shared.model.cars.CarModel;
import ro.taxiApp.docs.presentation.server.converters.cars.CarConverter;

public class CarServiceImpl implements CarService, InitializingBean {

	private CarDao carDao;
	private CarConverter carConverter;

	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			carDao,
			carConverter
		);
	}
	
	@Transactional
	public Long save(CarModel carModel) throws AppException {
		Car carEntity = carConverter.toEntity(carModel);
		return carDao.save(carEntity);
	}
	
	@Override
	public CarModel getById(Long id) {
		Car car = carDao.getById(id);
		return carConverter.toModel(car);
	}
	
	@Override
	public List<CarModel> getAll() {
		
		List<CarModel> carModels = new ArrayList<CarModel>();
		List<Car> carEntities = new ArrayList<Car>();
		
		carEntities = carDao.getAll();
		
		for (Car car : carEntities) {
			carModels.add(carConverter.toModel(car));
		}
		
		return carModels;
	}
	
	@Override
	public void deleteById(Long id) {
		carDao.deleteById(id);
	}
	
	@Override
	public CarCategoryModel getCarCategoryById(Long id) {
		
		CarCategoryModel carCategoryModel = new CarCategoryModel();
		CarCategory carCategoryEntity = new CarCategory();
		carCategoryEntity = carDao.getCarCategoryById(id);
		
		carCategoryModel = carConverter.toModel(carCategoryEntity);
		
		return carCategoryModel;
	}
	
	@Override
	public List<CarCategoryModel> getAllCarCategories() {
		
		List<CarCategoryModel> carCategoryModels = new ArrayList<CarCategoryModel>();
		List<CarCategory> carCategoryEntities = new ArrayList<CarCategory>();
		
		carCategoryEntities = carDao.getAllCarCategories();
		
		for (CarCategory carCategory : carCategoryEntities) {
			carCategoryModels.add(carConverter.toModel(carCategory));
		}
		
		return carCategoryModels;
	}

	//Setters
	public void setCarDao(CarDao carDao) {
		this.carDao = carDao;
	}

	public void setCarConverter(CarConverter carConverter) {
		this.carConverter = carConverter;
	}
	
}
