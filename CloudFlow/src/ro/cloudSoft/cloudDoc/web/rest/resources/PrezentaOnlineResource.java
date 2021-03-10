package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.DocumentPrezentaOnlineModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.ParticipantiModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline.PrezentaMembriiReprezentantiComisieGl;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.prezentaOnline.PrezentaOnlineService;

@Component
@Path("/PrezentaOnline")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class PrezentaOnlineResource extends BaseResource{
	
	@Autowired
	private PrezentaOnlineService prezentaOnlineService;
	
	@POST
	@Path("/getAllDocumentsPrezenta")
	public List<DocumentPrezentaOnlineModel> getAllDocumentsPrezenta() {
		return prezentaOnlineService.getAllDocumentsPrezenta(getSecurity());
	}
	
	@POST
	@Path("/getAllMembriiReprezentantiByComisieGlId/{comisieGlId}")
	public List<PrezentaMembriiReprezentantiComisieGl> getAllMembriiReprezentantiByComisieGlId(@PathParam("comisieGlId") Long comisieGlId) throws PresentationException {
		try {
			return prezentaOnlineService.getAllMembriiReprezentantiByComisieGlId(comisieGlId);
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
	
	@POST
	@Path("/saveParticipant")
	public void saveParticipant(PrezentaMembriiReprezentantiComisieGl prezentaMembriiReprezentantiComisieGl) {
		prezentaOnlineService.saveParticipant(prezentaMembriiReprezentantiComisieGl);
	}
	
	@POST
	@Path("/getAllParticipantiByDocument/{documentId}/{documentLocationRealName}")
	public ParticipantiModel getAllParticipantiByDocument(@PathParam("documentId") String documentId, @PathParam("documentLocationRealName") String documentLocationRealName) throws PresentationException {
		try {
			return prezentaOnlineService.getAllParticipantiByDocument(documentId, documentLocationRealName);
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
	
	@POST
	@Path("/deleteParticipant/{id}")
	public void deleteParticipant(@PathParam("id") Long id) {
		prezentaOnlineService.deleteParticipant(id);
	}
	
	@POST
	@Path("/finalizarePrezentaByDocument/{documentId}/{documentLocationRealName}")
	public void finalizarePrezentaByDocument(@PathParam("documentId") String documentId, @PathParam("documentLocationRealName") String documentLocationRealName) {
		prezentaOnlineService.finalizarePrezentaByDocument(documentId, documentLocationRealName);
	}
	
	@POST
	@Path("/isPrezentaFinalizataByDocument/{documentId}/{documentLocationRealName}")
	public boolean isPrezentaFinalizataByDocument(@PathParam("documentId") String documentId, @PathParam("documentLocationRealName") String documentLocationRealName){
		return prezentaOnlineService.isPrezentaFinalizataByDocument(documentId, documentLocationRealName);
	}
	
	@POST
	@Path("/importaPrezentaOnlineByDocument/{documentId}/{documentLocationRealName}")
	public void importaPrezentaOnlineByDocument(@PathParam("documentId") String documentId, @PathParam("documentLocationRealName") String documentLocationRealName) throws PresentationException{
		try {
			prezentaOnlineService.importaPrezentaOnlineByDocument(documentId, documentLocationRealName, getSecurity());
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}

}
