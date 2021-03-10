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
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiViewModel;
import ro.cloudSoft.cloudDoc.services.alteDeconturi.AlteDeconturiService;

@Component
@Path("/AlteDeconturi")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class AlteDeconturiResource extends BaseResource{

	@Autowired
	private AlteDeconturiService alteDeconturiService;
	
	
	@POST
	@Path("/getYearsOfExistingDeconturi")
	public List<Integer> getYearsOfExistingDeconturi() {
		return alteDeconturiService.getYearsOfExistingDeconturi();
	}

	@POST
	@Path("/getAllAlteDeconturiViewModelsByYear/{year}")
	public List<AlteDeconturiViewModel> getAllAlteDeconturiViewModelsByYear(@PathParam("year") Integer year) {
		return alteDeconturiService.getAllAlteDeconturiViewModelsByYear(year);
	}

	@POST
	@Path("/getDecontById/{decontId}")
	public AlteDeconturiModel getDecontById(@PathParam("decontId") Long decontId) {
		return alteDeconturiService.getDecontById(decontId);
	}
	
	@POST
	@Path("/isDecontCanceled/{decontId}")
	public boolean isDecontCanceled(@PathParam("decontId") Long decontId) {
		return alteDeconturiService.isDecontCanceled(decontId);
	}

	@POST
	@Path("/saveAlteDeconturi")
	public void saveAlteDeconturi(AlteDeconturiModel alteDeconturiModel) {
		alteDeconturiService.saveAlteDeconturi(alteDeconturiModel, getSecurity());
	}
	
	@POST
	@Path("/deleteDecontById/{decontId}")
	public void deleteDecontById(@PathParam("decontId") Long decontId) {
		alteDeconturiService.deleteDecontById(decontId);
	}
	
	@POST
	@Path("/cancelDecont/{decontId}/{motivAnulare}")
	public void cancelDecont(@PathParam("decontId") Long decontId, @PathParam("motivAnulare") String motivAnulare) {
		alteDeconturiService.cancelDecont(decontId, motivAnulare);
	}
	
	@POST
	@Path("/finalizeDecontById/{decontId}")
	public void finalizeDecontById(@PathParam("decontId") Long decontId) {
		alteDeconturiService.finalizeDecontById(decontId);
	}
	
	@POST
	@Path("/isDecontFinalized/{decontId}")
	public boolean isDecontFinalized(@PathParam("decontId") Long decontId) {
		return alteDeconturiService.isDecontFinalized(decontId);
	}
	
	@GET
	@Path("/downloadAtasamentOfCheltuiala/{atasamentId}")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response downloadAtasamentOfCheltuiala(@PathParam("atasamentId") Long atasamentId) throws PresentationException {
		DownloadableFile downloadableFile = alteDeconturiService.downloadAtasamentOfCheltuialaById(atasamentId);
		if (downloadableFile != null) {
			return buildDownloadableFileResponse(downloadableFile);	
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
			
	}
	
	@POST
	@Path("/getAllDistinctTitulari")
	public List<String> getAllDistinctTitulari() {
		return alteDeconturiService.getAllDistinctTitulari();
	}
}
