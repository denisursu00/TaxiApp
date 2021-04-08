package ro.taxiApp.docs.dao.clients;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import ro.taxiApp.docs.domain.clients.Client;

public class ClientDao extends HibernateDaoSupport {

	public Long save(Client client) {
		if (client.getId() == null) {
			return (Long) getHibernateTemplate().save(client);
		} else {
			getHibernateTemplate().saveOrUpdate(client);
			return client.getId();
		}
	}
	
	public Client getById(Long id) {
		return (Client) getHibernateTemplate().get(Client.class, id);
	}
	
	public Client getByUserId(Long id) {
		String query = " SELECT cl FROM Client cl "
					+ "  JOIN cl.user us "
					+ "  WHERE us.id = " + id.toString();
		return (Client) getHibernateTemplate().find(query);
	}
	
	public void deleteById(Long id) {
		Client client = getById(id);
		getHibernateTemplate().delete(client);
	}
	
}
