package ro.cloudSoft.cloudDoc.web.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtAppComponentsAvailabilityConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtBusinessConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtGuiConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtReplacementProfilesOutOfOfficeConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtSupportedAttachmentTypesForPreviewConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtWebConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ApplicationInfoModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AppGxtService;

@Component
@Path("/AppConstantsService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class AppConstantsServiceResource {

	@Autowired
	AppGxtService appGxtService;

	@POST
	@Path("/getGuiConstants")
	public GwtGuiConstants getGuiConstants() {
		return appGxtService.getConstants().getGuiConstants();
	}

	@POST
	@Path("/getBusinessConstants")
	public GwtBusinessConstants getBusinessConstants() {
		return appGxtService.getConstants().getBusinessConstants();
	}

	@POST
	@Path("/getWebConstants")
	public GwtWebConstants getWebConstants() {
		return appGxtService.getConstants().getWebConstants();
	}

	@POST
	@Path("/getAppComponentsAvailabilityConstants")
	public GwtAppComponentsAvailabilityConstants getAppComponentsAvailabilityConstants() {
		return appGxtService.getConstants().getAppComponentsAvailabilityConstants();
	}

	@POST
	@Path("/getReplacementProfilesOutOfOfficeConstants")
	public GwtReplacementProfilesOutOfOfficeConstants getReplacementProfilesOutOfOfficeConstants() {
		return appGxtService.getConstants().getReplacementProfilesOutOfOfficeConstants();
	}

	@POST
	@Path("/getSupportedAttachmentTypesForPreviewConstants")
	public GwtSupportedAttachmentTypesForPreviewConstants getSupportedAttachmentTypesForPreviewConstants() {
		return appGxtService.getConstants().getSupportedAttachmentTypesForPreviewConstants();
	}
	
	@POST
	@Path("/getApplicationInfo")
	public ApplicationInfoModel getApplicationInfo() {
		return appGxtService.getApplicationInfo();
	}
}