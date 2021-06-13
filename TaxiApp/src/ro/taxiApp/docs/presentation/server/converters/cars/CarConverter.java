package ro.taxiApp.docs.presentation.server.converters.cars;

import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.dao.car.CarDao;
import ro.taxiApp.docs.dao.driver.DriverDao;
import ro.taxiApp.docs.domain.cars.Car;
import ro.taxiApp.docs.domain.cars.CarCategory;
import ro.taxiApp.docs.presentation.client.shared.model.cars.CarCategoryModel;
import ro.taxiApp.docs.presentation.client.shared.model.cars.CarModel;
import ro.taxiApp.docs.presentation.server.converters.drivers.DriverConverter;

public class CarConverter implements InitializingBean {
	
	private CarDao carDao;
	private DriverConverter driverConverter;
	private DriverDao driverDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			carDao,
			driverConverter,
			driverDao
		);
	}

	public CarModel toModel(Car entity) {
		CarModel model = new CarModel();
		
		model.setId(entity.getId());
		model.setModel(entity.getModel());
		model.setRegistrationNumber(entity.getRegistrationNumber());
		model.setLastTechControl(entity.getLastTechControl());
		model.setDriverId(entity.getDriver().getId());
		model.setCarCategoryId(entity.getCarCategory().getId());
		
		return model;
	}
	
	public Car toEntity(CarModel model) throws AppException {
		Car entity = null;
		
		if (model.getId() != null) {
			entity = carDao.getById(model.getId());
		} else {
			entity = new Car();
		}
		
		entity.setId(model.getId());
		entity.setModel(model.getModel());
		entity.setRegistrationNumber(model.getRegistrationNumber());
		entity.setLastTechControl(model.getLastTechControl());
		
		entity.setDriver(driverDao.getById(model.getDriverId()));
		
		entity.setCarCategory(carDao.getCarCategoryById(model.getCarCategoryId()));
		
		return entity;
	}
	
	public CarCategoryModel toModel(CarCategory entity) {
		CarCategoryModel model = new CarCategoryModel();
		model.setId(entity.getId());
		model.setCode(entity.getCode());
		model.setName(entity.getName());
		return model;
	}

	public void setCarDao(CarDao carDao) {
		this.carDao = carDao;
	}

	public void setDriverConverter(DriverConverter driverConverter) {
		this.driverConverter = driverConverter;
	}

	public void setDriverDao(DriverDao driverDao) {
		this.driverDao = driverDao;
	}
	
}
