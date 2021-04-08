package ro.taxiApp.docs.presentation.server.converters.clients;

import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.dao.clients.ClientDao;
import ro.taxiApp.docs.dao.clients.FavouritePlaceDao;
import ro.taxiApp.docs.domain.clients.FavouritePlace;
import ro.taxiApp.docs.presentation.client.shared.model.clients.FavouritePlaceModel;

public class FavouritePlaceConverter implements InitializingBean {

	private FavouritePlaceDao favouritePlaceDao;
	private ClientDao clientDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			favouritePlaceDao,
			clientDao
		);
	}
	
	public FavouritePlaceModel toModel(FavouritePlace entity) {
		FavouritePlaceModel model = new FavouritePlaceModel();
		
		model.setId(entity.getId());
		model.setLocation(entity.getLocation());
		model.setName(entity.getName());
		model.setClientId(entity.getClient().getId());
		
		return model;
	}
	
	public FavouritePlace toEntity(FavouritePlaceModel model) {
		FavouritePlace entity = null;
		
		if (model.getId() != null) {
			entity = favouritePlaceDao.getById(model.getId());
		} else {
			entity = new FavouritePlace();
		}
		
		entity.setLocation(model.getLocation());
		entity.setName(model.getName());
		
		entity.setClient(clientDao.getById(model.getClientId()));
		
		return entity;
	}

	public void setFavouritePlaceDao(FavouritePlaceDao favouritePlaceDao) {
		this.favouritePlaceDao = favouritePlaceDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	
}
