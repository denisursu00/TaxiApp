package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiModel;
import ro.cloudSoft.cloudDoc.services.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiService;

@Component
@Path("/RegistruDocumenteJustificativePlati")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class RegistruDocumenteJustificativePlatiResource extends BaseResource{

	@Autowired
	private RegistruDocumenteJustificativePlatiService registruDocumenteJustificativePlatiService;
	
	@POST
	@Path("/getAllDocumenteJustificativePlati")
	public List<RegistruDocumenteJustificativePlatiModel> getAllDocumenteJustificativePlati() {
		return registruDocumenteJustificativePlatiService.getAllDocumenteJustificativePlati();
	}
	
	@POST
	@Path("/getAllByYear/{year}")
	public List<RegistruDocumenteJustificativePlatiModel> getAllByYear(@PathParam("year") Integer year) {
		return registruDocumenteJustificativePlatiService.getAllByYear(year);
	}
	
	@POST
	@Path("/getDocumentJustificativPlati/{documentJustificativPlatiId}")
	public RegistruDocumenteJustificativePlatiModel getDocumentJustificativPlati(@PathParam("documentJustificativPlatiId") Long documentJustificativPlatiId) {
		return registruDocumenteJustificativePlatiService.getDocumentJustificativPlati(documentJustificativPlatiId);
	}
	
	@POST
	@Path("/saveDocumentJustificativPlati")
	public void saveDocumentJustificativPlati(RegistruDocumenteJustificativePlatiModel documentJustificativPlatiModel) {
		registruDocumenteJustificativePlatiService.saveDocumentJustificativPlati(documentJustificativPlatiModel, getSecurity());
	}
	
	@POST
	@Path("/cancelDocumentJustificativPlati")
	public void cancelDocumentJustificativPlati(RegistruDocumenteJustificativePlatiModel documentJustificativPlatiModel) {
		registruDocumenteJustificativePlatiService.cancelDocumentJustificativPlati(documentJustificativPlatiModel);
	}
	
	@POST
	@Path("/getYearsWithInregistrariDocumenteJustificativePlati")
	public List<Integer> getYearsWithInregistrariDocumenteJustificativePlati() {
		return registruDocumenteJustificativePlatiService.getYearsWithInregistrariDocumenteJustificativePlati();
	}
	
	@GET
	@Path("/downloadAtasamentById/{atasamentId}")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response downloadAtasamentOfRegistruIesiriById(@PathParam("atasamentId") Long atasamentId) throws PresentationException {
		DownloadableFile downloadableFile = registruDocumenteJustificativePlatiService.downloadAtasamentOfRegistruDocumenteJustificativePlatiById(atasamentId);
		if (downloadableFile != null) {
			return buildDownloadableFileResponse(downloadableFile);	
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
}
