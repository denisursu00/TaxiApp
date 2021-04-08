package ro.taxiApp.docs.web.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.taxiApp.docs.presentation.client.shared.exceptions.PresentationException;
import ro.taxiApp.docs.presentation.client.shared.model.clients.ClientModel;
import ro.taxiApp.docs.services.clients.ClientService;

@Component
@Path("/Client")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ClientResource extends BaseResource {
	
	@Autowired
	private ClientService clientService;
	
	@POST
	@Path("/saveClient")
	public Long save(ClientModel clientModel) throws PresentationException {
		return clientService.save(clientModel);
	}
	
	@POST
	@Path("/getClientById/{id}")
	public ClientModel getClientById(@PathParam("id") Long id) throws PresentationException {
		return clientService.getClientById(id);
	}
	
	@POST
	@Path("/getClientByUserId/{userId}")
	public ClientModel getClientByUserId(@PathParam("id") Long userId) throws PresentationException {
		return clientService.getClientByUserId(userId);
	}
	
	@POST
	@Path("/deleteClientById/{id}")
	public void deleteClientById(@PathParam("id") Long id) throws PresentationException {
		clientService.deleteById(id);
	}
	
}
