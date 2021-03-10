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

import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariViewModel;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.registruIntrariIesiri.RegistruIntrariIesiriService;
import ro.cloudSoft.common.utils.PagingList;

@Component
@Path("/RegistruIntrariIesiri")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class RegistruIntrariIesiriResource extends BaseResource{

	@Autowired
	private RegistruIntrariIesiriService registruIntrariIesiriService;
	
	@Autowired
	private UserService userService;

	@POST
	@Path("/getAllIntrari")
	public List<RegistruIntrariModel> getAllIntrari() {
		return registruIntrariIesiriService.getAllIntrari();
	}

	@POST
	@Path("/getAllRegistruIntrariViewModels")
	public List<RegistruIntrariViewModel> getAllRegistruIntrariViewModels() {
		return registruIntrariIesiriService.getAllRegistruIntrariViewModels();
	}
	
	@POST
	@Path("/getYearsOfExistingIntrari")
	public List<Integer> getYearsOfExistingIntrari() {
		return registruIntrariIesiriService.getYearsOfExistingIntrari();
	}
	
	@POST
	@Path("/getYearsOfExistingIesiri")
	public List<Integer> getYearsOfExistingIesiri() {
		return registruIntrariIesiriService.getYearsOfExistingIesiri();
	}

	@POST
	@Path("/getAllRegistruIntrariViewModelsByYear/{year}")
	public List<RegistruIntrariViewModel> getAllRegistruIntrariViewModelsByYear(@PathParam("year") Integer year) {
		return registruIntrariIesiriService.getAllRegistruIntrariViewModelsByYear(year);
	}

	@POST
	@Path("/getRegistruIesiriViewModelsByFilter")
	public PagingList<RegistruIesiriViewModel> getRegistruIesiriViewModelsByFilter(RegistruIesiriFilterModel filter) {
		return registruIntrariIesiriService.getRegistruIesiriViewModelByFilter(filter);
	}
	
	@POST
	@Path("/getRegistruIntrariByFilter")
	public PagingList<RegistruIntrariViewModel> getRegistruIntrariByFilter(RegistruIntrariFilter registruIntrariFilter) {
		return registruIntrariIesiriService.getRegistruIntrariByFilter(registruIntrariFilter);
	}
	
	@POST
	@Path("/getNrInregistrareOfMappedRegistriByRegistruId/{registruType}/{registruId}")
	public List<String> getNrInregistrareOfMappedRegistriByRegistruId(@PathParam("registruType") String registruType, @PathParam("registruId") Long registruId) throws PresentationException{
		if (registruType.equals("iesire")) {
			return registruIntrariIesiriService.getNrInregistrareOfMappedRegistriIntrariByIesireId(registruId);
		}else if (registruType.equals("intrare")){
			return registruIntrariIesiriService.getNrInregistrareOfMappedRegistriIesiriByIntrareId(registruId);
		}else {
			throw new PresentationException(AppExceptionCodes.APPLICATION_ERROR.toString());
		}
	}
	
	@POST
	@Path("/saveRegistruIntrari")
	public void saveRegistruIntrari(RegistruIntrariModel registruIntrariModel) throws PresentationException {
		Boolean isCurrentUserAdmin = userService.getUserRoleNames(getSecurity().getUserId()).contains("ADMIN");
		if (registruIntrariModel.getId() != null
				&& registruIntrariIesiriService.isRegistruIntrariFinalized(registruIntrariModel.getId())
				&& !isCurrentUserAdmin) {
			throw new PresentationException(AppExceptionCodes.INSUFFICIENT_RIGHTS.toString());
		}
		registruIntrariIesiriService.saveRegistruIntrari(registruIntrariModel, getSecurity().getUserUsername());
	}

	@POST
	@Path("/saveRegistruIesiri")
	public void saveRegistruIesiri(RegistruIesiriModel registruIesiriModel) throws PresentationException {
		Boolean isCurrentUserAdmin = userService.getUserRoleNames(getSecurity().getUserId()).contains("ADMIN");
		if (registruIesiriModel.getId() != null
				&& registruIntrariIesiriService.isRegistruIesiriFinalized(registruIesiriModel.getId())
				&& !isCurrentUserAdmin) {
			throw new PresentationException(AppExceptionCodes.INSUFFICIENT_RIGHTS.toString());
		}
		registruIntrariIesiriService.saveRegistruIesiri(registruIesiriModel, getSecurity().getUserUsername());
	}

	@POST
	@Path("/getRegistruIesiri/{registruIesiriId}")
	public RegistruIesiriModel getRegistruIesiri(@PathParam("registruIesiriId") Long registruIesiriId) {
		return registruIntrariIesiriService.getRegistruIesiri(registruIesiriId);
	}

	@POST
	@Path("/getAllRegistruIesiriViewModels")
	public List<RegistruIesiriViewModel> getAllRegistruIesiriViewModels() {
		return registruIntrariIesiriService.getAllRegistruIesiriViewModels();
	}

	@POST
	@Path("/getRegistruIntrariById/{registruId}")
	public RegistruIntrariModel getRegistruIntrariById(@PathParam("registruId") Long registruId) {
		return registruIntrariIesiriService.getRegistruIntrariById(registruId);
	}
	
	@POST
	@Path("/cancelRegistruIntrari/{registruIntrariId}/{motivAnulare}")
	public void cancelRegistruIntrari(@PathParam("registruIntrariId") Long registruIntrariId, @PathParam("motivAnulare") String motivAnulare) {
		registruIntrariIesiriService.cancelRegistruIntrari(registruIntrariId, motivAnulare);
	}
	
	@POST
	@Path("/cancelRegistruIesiri/{registruIesiriId}/{motivAnulare}")
	public void cancelRegistruIesiri(@PathParam("registruIesiriId") Long registruIesiriId, @PathParam("motivAnulare") String motivAnulare) {
		registruIntrariIesiriService.cancelRegistruIesiri(registruIesiriId, motivAnulare);
	}
	
	@POST
	@Path("/isRegistruIesiriCanceled/{registruIesiriId}")
	public boolean isRegistruIesiriCanceled(@PathParam("registruIesiriId") Long registruIesiriId) {
		return registruIntrariIesiriService.isRegistruIesiriCanceled(registruIesiriId);
	}
	
	@POST
	@Path("/isRegistruIntrariCanceled/{registruIntrariId}")
	public boolean isRegistruIntrariCanceled(@PathParam("registruIntrariId") Long registruIntrariId) {
		return registruIntrariIesiriService.isRegistruIntrariCanceled(registruIntrariId);
	}
	
	@POST
	@Path("/isRegistruIntrariFinalized/{registruIntrariId}")
	public boolean isRegistruIntrariFinalized(@PathParam("registruIntrariId") Long registruIntrariId) {
		return registruIntrariIesiriService.isRegistruIntrariFinalized(registruIntrariId);
	}
	
	@POST
	@Path("/finalizareRegistruIntrari/{registruIntrariId}") 
	public void finalizareRegistruIntrari(@PathParam("registruIntrariId") Long registruIntrariId) {
		registruIntrariIesiriService.finalizareRegistruIntrari(registruIntrariId);
	}
	
	@POST
	@Path("/finalizareRegistruIesiri/{registruIesiriId}") 
	public void finalizareRegistruIesiri(@PathParam("registruIesiriId") Long registruIesiriId) {
		registruIntrariIesiriService.finalizareRegistruIesiri(registruIesiriId);
	}
	
	@GET
	@Path("/downloadAtasamentOfRegistruIntrariById/{atasamentId}")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response downloadAtasamentOfRegistruIntrariById(@PathParam("atasamentId") Long atasamentId) throws PresentationException {
		DownloadableFile downloadableFile = registruIntrariIesiriService.downloadAtasamentOfRegistruIntrariById(atasamentId);
		if (downloadableFile != null) {
			return buildDownloadableFileResponse(downloadableFile);	
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/downloadAtasamentOfRegistruIesiriById/{atasamentId}")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response downloadAtasamentOfRegistruIesiriById(@PathParam("atasamentId") Long atasamentId) throws PresentationException {
		DownloadableFile downloadableFile = registruIntrariIesiriService.downloadAtasamentOfRegistruIesiriById(atasamentId);
		if (downloadableFile != null) {
			return buildDownloadableFileResponse(downloadableFile);	
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@POST
	@Path("/getLastNumarInregistrareByTipRegistruAndYear/{tipRegistru}/{year}")
	public Long getLastNumarInregistrareByTipRegistruAndYear(@PathParam("tipRegistru") String tipRegistru, @PathParam("year") Integer year) {
		Long lastNrInregistrare = registruIntrariIesiriService.getLastNumarInregistrareByTipRegistruAndYear(tipRegistru, year);
		return lastNrInregistrare;
	}
	
	@POST
	@Path("/isSubactivityUsedInAnyRegisterEntry/{subactivityId}")
	public boolean isSubactivityUsedInAnyRegisterEntry(@PathParam("subactivityId") Long subactivityId) {
		return registruIntrariIesiriService.isSubactivityUsed(subactivityId);
	}
}
