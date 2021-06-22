package ro.taxiApp.docs.dao.car;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.taxiApp.docs.domain.cars.Car;
import ro.taxiApp.docs.domain.cars.CarCategory;

public class CarDao extends HibernateDaoSupport {

	public Long save(Car car) {
		if (car.getId() == null) {
			return (Long) getHibernateTemplate().save(car);
		} else {
			getHibernateTemplate().saveOrUpdate(car);
			return car.getId();
		}
	}
	
	public Car getById(Long id) {
		return (Car) getHibernateTemplate().get(Car.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Car> getAll() {
		String query = "SELECT car FROM Car car";
		return getHibernateTemplate().find(query);
	}
	
	public List<Car> getByDriverId(Long driverId) {
		String query = "SELECT car FROM Car car"
					+ " JOIN car.driver driver "
					+ " WHERE driver.id = " + driverId.toString();
		return getHibernateTemplate().find(query);
	}
	
	public void deleteById(Long id) {
		Car car = getById(id);
		getHibernateTemplate().delete(car);
	}
	
	@SuppressWarnings("unchecked")
	public List<CarCategory> getAllCarCategories() {
		String query = "SELECT carCategory FROM CarCategory carCategory";
		return getHibernateTemplate().find(query);
	}
	
	public CarCategory getCarCategoryById(Long id) {
		return (CarCategory) getHibernateTemplate().get(CarCategory.class, id);
	}
	
}
