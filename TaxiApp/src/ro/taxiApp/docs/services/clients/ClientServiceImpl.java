package ro.taxiApp.docs.services.clients;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.dao.clients.ClientDao;
import ro.taxiApp.docs.domain.clients.Client;
import ro.taxiApp.docs.presentation.client.shared.model.clients.ClientModel;
import ro.taxiApp.docs.presentation.server.converters.clients.ClientConverter;

public class ClientServiceImpl implements ClientService, InitializingBean {

	private ClientDao clientDao;
	private ClientConverter clientConverter;

	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			clientDao,
			clientConverter
		);
	}
	
	@Transactional
	public Long save(ClientModel client) {
		Client entity = clientConverter.toEntity(client);
		return clientDao.save(entity);
	}
	
	public ClientModel getClientById(Long id) {
		Client client = clientDao.getById(id);
		return clientConverter.toModel(client);
	}
	
	public ClientModel getClientByUserId(Long userId) {
		Client client = clientDao.getByUserId(userId);
		return clientConverter.toModel(client);
	}
	
	public void deleteById(Long id) {
		clientDao.deleteById(id);
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	public void setClientConverter(ClientConverter clientConverter) {
		this.clientConverter = clientConverter;
	}
	
}
