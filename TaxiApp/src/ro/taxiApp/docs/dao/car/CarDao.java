package ro.taxiApp.docs.dao.car;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.taxiApp.docs.domain.cars.Car;

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
	
	public void deleteById(Long id) {
		Car car = getById(id);
		getHibernateTemplate().delete(car);
	}
	
}
