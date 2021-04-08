package ro.taxiApp.docs.dao.ride;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.taxiApp.docs.domain.rides.Ride;

public class RideDao extends HibernateDaoSupport {
	
	public Long save(Ride ride) {
		if (ride.getId() == null) {
			return (Long) getHibernateTemplate().save(ride);
		} else {
			getHibernateTemplate().saveOrUpdate(ride);
			return ride.getId();
		}
	}
	
	public Ride getById(Long id) {
		return (Ride) getHibernateTemplate().get(Ride.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Ride> getAll() {
		String query = "SELECT ride FROM Ride ride";
		return getHibernateTemplate().find(query);
	}
	
}
