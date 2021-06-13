package ro.taxiApp.docs.services.drivers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.dao.driver.DriverDao;
import ro.taxiApp.docs.domain.drivers.Driver;
import ro.taxiApp.docs.presentation.client.shared.model.drivers.DriverModel;
import ro.taxiApp.docs.presentation.server.converters.drivers.DriverConverter;

public class DriverServiceImpl implements DriverService, InitializingBean {

	private DriverDao driverDao;
	private DriverConverter driverConverter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			driverDao,
			driverConverter
		);
	}

	@Transactional
	@Override
	public Long save(DriverModel driverModel) throws AppException {
		Driver driverEntity = driverConverter.toEntity(driverModel);
		return driverDao.save(driverEntity);
	}
	
	@Override
	public DriverModel getById(Long id) {
		Driver driver = driverDao.getById(id);
		return driverConverter.toModel(driver);
	}
	
	@Override
	public DriverModel getDriverByUserId(Long userId) {
		Driver driver = driverDao.getDriverByUserId(userId);
		return driverConverter.toModel(driver);
	}
	
	@Override
	public List<DriverModel> getAll() {
		
		List<DriverModel> driversModels = new ArrayList<DriverModel>();
		List<Driver> driversEntities = new ArrayList<Driver>();
		
		driversEntities = driverDao.getAll();
		
		if (driversEntities != null) {
			for (Driver driver : driversEntities) {
				driversModels.add(driverConverter.toModel(driver));
			}
		}
		
		return driversModels;
	}
	
	@Override
	public void deleteById(Long id) {
		driverDao.deleteById(id);
	}
	
	//Setters
	
	public void setDriverDao(DriverDao driverDao) {
		this.driverDao = driverDao;
	}

	public void setDriverConverter(DriverConverter driverConverter) {
		this.driverConverter = driverConverter;
	}

}
