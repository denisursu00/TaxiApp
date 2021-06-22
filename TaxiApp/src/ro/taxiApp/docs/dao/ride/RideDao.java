package ro.taxiApp.docs.dao.ride;

import java.util.List;

import javax.persistence.Query;

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
	
	@SuppressWarnings("unchecked")
	public List<Ride> getActiveRidesForDriver(List<Long> carCategoriesIds) {
		String query = " SELECT ride FROM Ride ride "
				 + " JOIN ride.carCategory carCategory "
				 + " WHERE carCategory.id IN (:carCategoriesIds) "
				 + " AND ride.driver IS NULL "
				 + " AND (ride.canceled = false OR ride.canceled IS NULL) "
				 + " AND (ride.finished = false OR ride.finished IS NULL) "
				 + " AND ride.endTime IS NULL "
				 + " AND ride.startTime IS NULL ";
		try {
			return getHibernateTemplate().findByNamedParam(query, "carCategoriesIds", carCategoriesIds);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Ride getActiveRideByClientId(Long clientId) {
		String query = " SELECT ride FROM Ride ride "
					 + " JOIN ride.client client "
					 + " WHERE client.id = " + clientId.toString()
					 + " AND (ride.canceled = false OR ride.canceled IS NULL) "
					 + " AND (ride.finished = false OR ride.finished IS NULL) "
					 + " AND ride.endTime IS NULL ";
		try {
			return (Ride) getHibernateTemplate().find(query).get(0);
		} catch (Exception e) {
			return null;
		}
	}
	
}
