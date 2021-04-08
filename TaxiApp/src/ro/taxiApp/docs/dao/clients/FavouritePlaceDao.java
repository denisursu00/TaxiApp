package ro.taxiApp.docs.dao.clients;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.taxiApp.docs.domain.clients.FavouritePlace;

public class FavouritePlaceDao extends HibernateDaoSupport {

	public Long save(FavouritePlace favouritePlace) {
		if (favouritePlace.getId() == null) {
			return (Long) getHibernateTemplate().save(favouritePlace);
		} else {
			getHibernateTemplate().saveOrUpdate(favouritePlace);
			return favouritePlace.getId();
		}
	}
	
	public FavouritePlace getById(Long id) {
		return (FavouritePlace) getHibernateTemplate().get(FavouritePlace.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<FavouritePlace> getAllByClientId(Long clientId) {
		String query = "SELECT favPlace FROM FavouritePlace favPlace "
				+ "		JOIN favPlace.client client "
				+ "		WHERE client.id = " + clientId.toString();
		return getHibernateTemplate().find(query);
	}
	
	public void deleteById(Long id) {
		FavouritePlace favouritePlace = getById(id);
		getHibernateTemplate().delete(favouritePlace);
	}
	
}
