package ro.taxiApp.docs.services.ride;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.dao.car.CarDao;
import ro.taxiApp.docs.dao.ride.RideDao;
import ro.taxiApp.docs.domain.cars.Car;
import ro.taxiApp.docs.domain.rides.Ride;
import ro.taxiApp.docs.presentation.client.shared.model.rides.RideModel;
import ro.taxiApp.docs.presentation.server.converters.ride.RideConverter;

public class RideServiceImpl implements RideService, InitializingBean {

	private RideDao rideDao;
	private RideConverter rideConverter;
	private CarDao carDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			rideDao,
			rideConverter,
			carDao
		);
	}
	
	@Transactional
	public Long save(RideModel ride) {
		Ride entity = rideConverter.toEntity(ride);
		return rideDao.save(entity);
	}
	
	@Override
	public RideModel getById(Long id) {
		Ride ride = rideDao.getById(id);
		return rideConverter.toModel(ride);
	}
	
	@Override
	public List<RideModel> getAll() {
		List<Ride> rideEntities = new ArrayList<Ride>();
		rideEntities = rideDao.getAll();
		List<RideModel> rideModels = new ArrayList<RideModel>();
		if (rideEntities != null) {
			for (Ride entity : rideEntities) {
				rideModels.add(rideConverter.toModel(entity));
			}
		}
		return rideModels;
	}
	
	@Override
	public List<RideModel> getRidesForDriver(Long driverId) {
		List<Car> cars = new ArrayList<Car>();
		List<Long> carCategoriesIds = new ArrayList<Long>();
		List<Ride> rideEntities = new ArrayList<Ride>();
		List<RideModel> rideModels = new ArrayList<RideModel>();
		cars = carDao.getByDriverId(driverId);
		if (cars.isEmpty()) {
			return null;
		} else {
			for (Car car: cars) {
				carCategoriesIds.add(car.getCarCategory().getId());
			}
		}
		rideEntities = rideDao.getActiveRidesForDriver(carCategoriesIds);
		if (rideEntities != null) {
			for (Ride entity : rideEntities) {
				rideModels.add(rideConverter.toModel(entity));
			}
		}
		return rideModels;
	}
	
	@Override
	public RideModel getActiveRideByClientId(Long clientId) {
		Ride ride;
		ride = rideDao.getActiveRideByClientId(clientId);
		if (ride == null) {
			return null;
		} else {
			return rideConverter.toModel(ride);
		}
	}
	
	public void setRideDao(RideDao rideDao) {
		this.rideDao = rideDao;
	}
	public void setRideConverter(RideConverter rideConverter) {
		this.rideConverter = rideConverter;
	}

	public void setCarDao(CarDao carDao) {
		this.carDao = carDao;
	}
	
	
}
