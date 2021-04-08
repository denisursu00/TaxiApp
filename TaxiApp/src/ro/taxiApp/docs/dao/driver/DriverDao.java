package ro.taxiApp.docs.dao.driver;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.taxiApp.docs.domain.drivers.Driver;


public class DriverDao extends HibernateDaoSupport {
	
	public Long save(Driver driver) {
		if (driver.getId() == null) {
			return (Long) getHibernateTemplate().save(driver);
		} else {
			getHibernateTemplate().saveOrUpdate(driver);
			return driver.getId();
		}
	}
	
	public Driver getById(Long id) {
		return (Driver) getHibernateTemplate().get(Driver.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Driver> getAll() {
		String query = "SELECT driver FROM Driver driver";
		return getHibernateTemplate().find(query);
	}
	
	public void deleteById(Long id) {
		Driver driver = getById(id);
		getHibernateTemplate().delete(driver);
	}

}
