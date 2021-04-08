package ro.taxiApp.docs.presentation.server.converters.drivers;

import org.springframework.beans.factory.InitializingBean;
import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.dao.driver.DriverDao;
import ro.taxiApp.docs.domain.drivers.Driver;
import ro.taxiApp.docs.domain.organization.User;
import ro.taxiApp.docs.plugins.organization.UserPersistencePlugin;
import ro.taxiApp.docs.presentation.client.shared.model.drivers.DriverModel;
import ro.taxiApp.docs.presentation.server.converters.organization.UserConverter;

public class DriverConverter implements InitializingBean {
	
	private DriverDao driverDao;
	private UserPersistencePlugin userPersistencePlugin;

	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			driverDao,
			userPersistencePlugin
		);
	}
	
	public DriverModel toModel(Driver driver) {
		DriverModel model = new DriverModel();
		model.setId(driver.getId());
		model.setBirthDate(driver.getBirthDate());
		model.setLicenceNumber(driver.getLicenceNumber());
		model.setExpiryDate(driver.getExpiryDate());
		model.setAvailable(driver.getAvailable());
		model.setLastMedExam(driver.getLastMedExam());
		
		model.setUser(UserConverter.getModelFromUser(driver.getUser()));
		
		return model;
	}
	
	public Driver toEntity(DriverModel model) {
		
		Driver entity = null;
		if (model.getId() != null) {
			entity = driverDao.getById(model.getId());
		} else {
			entity = new Driver();
		}
		
		entity.setBirthDate(model.getBirthDate());
		entity.setLicenceNumber(model.getLicenceNumber());
		entity.setExpiryDate(model.getExpiryDate());
		entity.setAvailable(model.getAvailable());
		entity.setLastMedExam(model.getLastMedExam());
		
		User user = null;
		user = userPersistencePlugin.getUserById(model.getUser().getId());
		entity.setUser(user);
		
		return entity;
	}

	public void setDriverDao(DriverDao driverDao) {
		this.driverDao = driverDao;
	}

	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
	
}
