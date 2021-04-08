package ro.taxiApp.docs.presentation.server.converters.clients;

import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.dao.clients.ClientDao;
import ro.taxiApp.docs.domain.clients.Client;
import ro.taxiApp.docs.domain.organization.User;
import ro.taxiApp.docs.plugins.organization.UserPersistencePlugin;
import ro.taxiApp.docs.presentation.client.shared.model.clients.ClientModel;

public class ClientConverter implements InitializingBean {

	private ClientDao clientDao;
	private UserPersistencePlugin userPersistencePlugin;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			clientDao,
			userPersistencePlugin
		);
	}
	
	public ClientModel toModel(Client entity) {
		ClientModel model = new ClientModel();
		model.setId(entity.getId());
		model.setUserId(entity.getUser().getId());
		return model;
	}
	
	public Client toEntity(ClientModel model) {
		Client entity = null;
		if (model.getId() != null) {
			entity = clientDao.getById(model.getId());
		} else {
			entity = new Client();
		}
		
		User user = null;
		user = userPersistencePlugin.getUserById(model.getUserId());
		entity.setUser(user);
		
		return entity;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
	
}
