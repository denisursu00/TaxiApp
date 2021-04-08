package ro.taxiApp.docs.presentation.server.converters.ride;

import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.dao.clients.ClientDao;
import ro.taxiApp.docs.dao.driver.DriverDao;
import ro.taxiApp.docs.dao.ride.RideDao;
import ro.taxiApp.docs.domain.rides.PaymentType;
import ro.taxiApp.docs.domain.rides.Ride;
import ro.taxiApp.docs.presentation.client.shared.model.rides.RideModel;

public class RideConverter implements InitializingBean {
	
	private RideDao rideDao;
	private DriverDao driverDao;
	private ClientDao clientDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			rideDao,
			driverDao,
			clientDao
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
		model.setClientId(entity.getClient().getId());
		model.setDriverId(entity.getDriver().getId());
		model.setPaymentType(entity.getPaymentType().toString());
		
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
		
		entity.setClient(clientDao.getById(model.getClientId()));
		entity.setDriver(driverDao.getById(model.getDriverId()));
		
		entity.setPaymentType(PaymentType.valueOf(model.getPaymentType()));
		
		
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

}
