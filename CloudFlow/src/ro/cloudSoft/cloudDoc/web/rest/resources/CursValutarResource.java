package ro.cloudSoft.cloudDoc.web.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.cursValutar.CursValutarModel;
import ro.cloudSoft.cloudDoc.services.cursValutar.CursValutarService;

@Component
@Path("/CursValutar")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class CursValutarResource extends BaseResource  {
	
	@Autowired
	private CursValutarService cursValutarService;
	
	@POST
	@Path("/getCursValutarCurent")
	public CursValutarModel getCursValutarCurent() {
		return cursValutarService.getCursValutarCurent();
	}
}
