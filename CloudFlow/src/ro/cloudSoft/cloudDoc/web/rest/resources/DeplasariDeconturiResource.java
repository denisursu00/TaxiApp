package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.text.ParseException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.deciziiDeplasari.DocumentDecizieDeplasareModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.deciziiDeplasari.NumarDecizieDeplasareModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.DeplasareDecontModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.DeplasareDecontViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiReprezentantArbReportDateFilterModel;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.deplasariDeconturi.DeplasariDeconturiService;

@Component
@Path("/DeplasariDeconturi")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class DeplasariDeconturiResource extends BaseResource {

	@Autowired
	private DeplasariDeconturiService deplasariDeconturiService;

	@POST
	@Path("/saveDeplasareDecont")
	public void saveDeplasareDecont(DeplasareDecontModel deplasareDecontModel) {
		deplasariDeconturiService.saveDeplasareDecont(deplasareDecontModel);
	}

	@POST
	@Path("/getDeciziiAprobateNealocateForReprezentantForDeplasareDecont/{reprezentantArbId}")
	public List<NumarDecizieDeplasareModel> getDeciziiAprobateNealocateForReprezentantForDeplasareDecont(@PathParam("reprezentantArbId") Long reprezentantArbId, 
			@QueryParam(value = "deplasareDecontId") Long deplasareDecontId) {
		return deplasariDeconturiService.getDeciziiAprobateNealocateForReprezentantForDeplasareDecont(reprezentantArbId, deplasareDecontId, getSecurity());
	}

	@POST
	@Path("/getDocumentDecizieDeplasare/{documentId}/{documentLocationRealName}")
	public DocumentDecizieDeplasareModel getDocumentDecizieDeplasare(@PathParam("documentId") String documentId, 
			@PathParam("documentLocationRealName") String documentLocationRealName) throws PresentationException, AppException, ParseException {
		return deplasariDeconturiService.getDocumentDecizieDeplasare(documentId, documentLocationRealName, getSecurity());
	}
	
	@POST
	@Path("/getYearsOfExistingDeplasariDeconturi")
	public List<Integer> getYearsOfExistingDeplasariDeconturi() {
		return deplasariDeconturiService.getYearsOfExistingDeplasariDeconturi();
	}

	@POST
	@Path("/getAllDeplasariDeconturiViewModelsByYear/{year}")
	public List<DeplasareDecontViewModel> getAllDeplasariDeconturiViewModelsByYear(@PathParam("year") Integer year) {
		return deplasariDeconturiService.getAllDeplasariDeconturiViewModelsByYear(year);
	}
	
	@POST
	@Path("/isDeplasareDecontCanceled/{deplasareDecontId}")
	public boolean isDeplasareDecontCanceled(@PathParam("deplasareDecontId") Long deplasareDecontId) {
		return deplasariDeconturiService.isDeplasareDecontCanceled(deplasareDecontId);
	}

	@POST
	@Path("/getDeplasareDecontById/{deplasareDecontId}")
	public DeplasareDecontModel getDeplasareDecontById(@PathParam("deplasareDecontId") Long deplasareDecontId) {
		return deplasariDeconturiService.getDeplasareDecontById(deplasareDecontId);
	}

	@POST
	@Path("/cancelDeplasareDecont/{deplasareDecontId}/{motiv}")
	public void cancelDeplasareDecont(@PathParam("deplasareDecontId") Long deplasareDecontId, @PathParam("motiv") String motiv) {
		deplasariDeconturiService.cancelDeplasareDecont(deplasareDecontId, motiv);
	}

	@POST
	@Path("/removeDeplasareDecont/{deplasareDecontId}")
	public void removeDeplasareDecont(@PathParam("deplasareDecontId") Long deplasareDecontId) throws PresentationException {
		try {
			deplasariDeconturiService.removeDeplasareDecont(deplasareDecontId);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/finalizeDeplasareDecont/{deplasareDecontId}")
	public void finalizeDeplasareDecont(@PathParam("deplasareDecontId") Long deplasareDecontId) throws PresentationException {
		try {
			deplasariDeconturiService.finalizeDeplasareDecont(deplasareDecontId);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getAllDistinctTitulari")
	public List<String> getAllDistinctTitulari() {
		return deplasariDeconturiService.getAllDistinctTitulari();
	}
	
	@POST
	@Path("/getAllNumarDeciziiByFilter")
	public List<String> getAllNumarDeciziiByFilter(CheltuieliArbSiReprezentantArbReportDateFilterModel filter) {
		return deplasariDeconturiService.getAllNumarDeciziiByFilter(filter);
	}
}
