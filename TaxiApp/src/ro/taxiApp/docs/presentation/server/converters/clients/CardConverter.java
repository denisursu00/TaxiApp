package ro.taxiApp.docs.presentation.server.converters.clients;

import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.dao.clients.CardDao;
import ro.taxiApp.docs.dao.clients.ClientDao;
import ro.taxiApp.docs.domain.clients.Card;
import ro.taxiApp.docs.presentation.client.shared.model.clients.CardModel;

public class CardConverter implements InitializingBean {
	
	private CardDao cardDao;
	private ClientDao clientDao;

	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			cardDao,
			clientDao
		);
	}
	
	public CardModel toModel(Card entity) {
		CardModel model = new CardModel();
		
		model.setId(entity.getId());
		model.setCardNumber(entity.getCardNumber());
		model.setExpDate(entity.getExpDate());
		model.setCvv(entity.getCvv());
		model.setClientId(entity.getClient().getId());
		
		return model;
	}
	
	public Card toEntity(CardModel model) {
		Card entity = null;
		
		if (model.getId() != null) {
			entity = cardDao.getById(model.getId());
		} else {
			entity = new Card();
		}
		
		entity.setCardNumber(model.getCardNumber());
		entity.setExpDate(model.getExpDate());
		entity.setCvv(model.getCvv());
		
		entity.setClient(clientDao.getById(model.getClientId()));
		
		return entity;
	}

	public void setCardDao(CardDao cardDao) {
		this.cardDao = cardDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	
}
