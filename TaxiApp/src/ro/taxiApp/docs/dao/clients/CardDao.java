package ro.taxiApp.docs.dao.clients;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.taxiApp.docs.domain.clients.Card;

public class CardDao extends HibernateDaoSupport {

	public Long save(Card card) {
		if (card.getId() == null) {
			return (Long) getHibernateTemplate().save(card);
		} else {
			getHibernateTemplate().saveOrUpdate(card);
			return card.getId();
		}
	}
	
	public Card getById(Long id) {
		return (Card) getHibernateTemplate().get(Card.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Card> getAllByClientId(Long clientId) {
		String query = "SELECT card FROM Card card "
				+ "		JOIN card.client client "
				+ "		WHERE client.id = " + clientId.toString();
		return getHibernateTemplate().find(query);
	}
	
	public void deleteById(Long id) {
		Card card = getById(id);
		getHibernateTemplate().delete(card);
	}
	
}
