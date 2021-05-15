package ro.taxiApp.docs.presentation.server.converters.ride;

import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.dao.clients.ClientDao;
import ro.taxiApp.docs.dao.driver.DriverDao;
import ro.taxiApp.docs.dao.ride.RideDao;
import ro.taxiApp.docs.domain.rides.PaymentType;
import ro.taxiApp.docs.domain.rides.Ride;
import ro.taxiApp.docs.plugins.organization.UserPersistencePlugin;
import ro.taxiApp.docs.presentation.client.shared.model.rides.RideModel;

public class RideConverter implements InitializingBean {
	
	private RideDao rideDao;
	private DriverDao driverDao;
	private ClientDao clientDao;
	private UserPersistencePlugin userPersistencePlugin;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			rideDao,
			driverDao,
			clientDao,
			userPersistencePlugin
		);
	}
	
	public RideModel toModel(Ride entity) {
		RideModel model = new RideModel();
		
		model.setId(entity.getId());
		model.setStartTime(entity.getStartTime());
		model.setEndTime(entity.getEndTime());
		model.setStartLocation(entity.getStartLocation());
		model.setEndLocation(entity.getEndLocation());
		model.setStartAdress(entity.getStartAdress());
		model.setEndAdress(entity.getEndAdress());
		model.setPrice(entity.getPrice());
		model.setCanceled(entity.getCanceled());
		model.setFinished(entity.getFinished());
		if (entity.getClient()!=null) {
			model.setClientId(entity.getClient().getId());
		}
		if (entity.getDriver()!=null) {
			model.setDriverId(entity.getDriver().getId());
		}
		if (entity.getDispatcher()!=null) {
			model.setDispatcherId(entity.getDispatcher().getId());
		}
		model.setPaymentType(entity.getPaymentType().toString());
		model.setObservations(entity.getObservations());
		return model;
	}
	
	public Ride toEntity(RideModel model) {
		Ride entity = null;
		
		if (model.getId() != null) {
			entity = rideDao.getById(model.getId());
		} else {
			entity = new Ride();
		}
		
		entity.setId(model.getId());
		entity.setStartTime(model.getStartTime());
		entity.setEndTime(model.getEndTime());
		entity.setStartLocation(model.getStartLocation());
		entity.setEndLocation(model.getEndLocation());
		entity.setStartAdress(model.getStartAdress());
		entity.setEndAdress(model.getEndAdress());
		entity.setPrice(model.getPrice());
		entity.setCanceled(model.getCanceled());
		entity.setFinished(model.getFinished());
		
		if (model.getClientId()!=null) {
			entity.setClient(clientDao.getById(model.getClientId()));
		}
		if (model.getDriverId()!=null) {
			entity.setDriver(driverDao.getById(model.getDriverId()));
		}
		if (model.getDispatcherId()!=null) {
			entity.setDispatcher(userPersistencePlugin.getUserById(model.getDispatcherId()));
		}
		
		entity.setPaymentType(PaymentType.valueOf(model.getPaymentType()));
		entity.setObservations(model.getObservations());
		
		return entity;
	}

	public void setRideDao(RideDao rideDao) {
		this.rideDao = rideDao;
	}

	public void setDriverDao(DriverDao driverDao) {
		this.driverDao = driverDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}

}
