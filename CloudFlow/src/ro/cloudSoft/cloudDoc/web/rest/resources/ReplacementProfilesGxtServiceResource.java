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
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.GetAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.SaveReplacementProfileRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.replacementProfiles.ReplacementProfilesGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.replacementProfiles.ReplacementProfileConverter;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;

@Component
@Path("/ReplacementProfilesGxtService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ReplacementProfilesGxtServiceResource extends BaseResource {

	@Autowired
	private ReplacementProfilesGxtService replacementProfilesGxtService;
	
	@Autowired
	private ReplacementProfileConverter replacementProfileConverter;
	
	@POST
	@Path("/getVisibleReplacementProfiles")
	public List<ReplacementProfileModel> getVisibleReplacementProfiles() throws PresentationException {
		return replacementProfilesGxtService.getVisibleReplacementProfiles();
	}
	
	@POST
	@Path("/deleteReplacementProfileById/{replacementProfileId}")
	public void deleteReplacementProfileById(@PathParam("replacementProfileId") Long replacementProfileId) throws PresentationException {
		replacementProfilesGxtService.deleteReplacementProfileById(replacementProfileId);
	}
	
	@POST
	@Path("/getReplacementProfileById/{replacementProfileId}")
	public ReplacementProfileModel getReplacementProfileById(@PathParam("replacementProfileId") Long replacementProfileId) throws PresentationException {
		return replacementProfilesGxtService.getReplacementProfileById(replacementProfileId);
	}
	
	@POST
	@Path("/saveReplacementProfile")
	public void saveReplacementProfile(SaveReplacementProfileRequestModel saveReplacementProfileRequest) throws PresentationException {
		ReplacementProfileModel replacementProfile = replacementProfileConverter.getModel(saveReplacementProfileRequest);
		try {
			replacementProfilesGxtService.saveReplacementProfile(replacementProfile);
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
	
	@POST
	@Path("/returned/{replacementProfileId}")
	public void returned(@PathParam("replacementProfileId") Long replacementProfileId) throws PresentationException {
		replacementProfilesGxtService.returned(replacementProfileId);
	}
	
	@POST
	@Path("/getAvailableReplacementProfilesInWhichRequesterIsReplacement")
	public List<ReplacementProfileModel> getAvailableReplacementProfilesInWhichRequesterIsReplacement(
			GetAvailableReplacementProfilesInWhichRequesterIsReplacementRequestModel requestModel) throws PresentationException {
		return replacementProfilesGxtService.getAvailableReplacementProfilesInWhichRequesterIsReplacement(requestModel.getIdForRequestingReplacementProfile(), 
				requestModel.getIdsForRequesterUserProfiles(), requestModel.getStartDate(), requestModel.getEndDate());
	}
}
