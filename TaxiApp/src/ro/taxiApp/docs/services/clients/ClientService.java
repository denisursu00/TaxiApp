package ro.taxiApp.docs.services.clients;

import ro.taxiApp.docs.presentation.client.shared.model.clients.ClientModel;

public interface ClientService {
	
	public Long save(ClientModel client);
	
	public ClientModel getClientById(Long id);
	
	public ClientModel getClientByUserId(Long userId);
	
	public void deleteById(Long id);
	
}
