package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Inglobeaza constantele ce trebuie trimise interfetei grafice.
 * 
 * 
 */
public class GwtConstantsPayload implements IsSerializable {

	private GwtGuiConstants guiConstants;
	private GwtBusinessConstants businessConstants;
	private GwtCereriDeConcediuConstants cereriDeConcediuConstants;
	private GwtWebConstants webConstants;
	private GwtAppComponentsAvailabilityConstants appComponentsAvailabilityConstants;
	private GwtReplacementProfilesOutOfOfficeConstants replacementProfilesOutOfOfficeConstants;
	private GwtSupportedAttachmentTypesForPreviewConstants supportedAttachmentTypesForPreviewConstants;
	
	@SuppressWarnings("unused")
	private GwtConstantsPayload() {}
	
	public GwtConstantsPayload(GwtGuiConstants guiConstants, GwtBusinessConstants businessConstants,
			GwtCereriDeConcediuConstants cereriDeConcediuConstants, GwtWebConstants webConstants,
			GwtAppComponentsAvailabilityConstants appComponentsAvailabilityConstants,
			GwtReplacementProfilesOutOfOfficeConstants replacementProfilesOutOfOfficeConstants,
			GwtSupportedAttachmentTypesForPreviewConstants supportedAttachmentTypesForPreviewConstants) {
		
		this.guiConstants = guiConstants;
		this.businessConstants = businessConstants;
		this.cereriDeConcediuConstants = cereriDeConcediuConstants;
		this.webConstants = webConstants;
		this.appComponentsAvailabilityConstants = appComponentsAvailabilityConstants;
		this.replacementProfilesOutOfOfficeConstants = replacementProfilesOutOfOfficeConstants;
		this.supportedAttachmentTypesForPreviewConstants = supportedAttachmentTypesForPreviewConstants;
	}
	
	public GwtGuiConstants getGuiConstants() {
		return guiConstants;
	}
	public GwtBusinessConstants getBusinessConstants() {
		return businessConstants;
	}
	public GwtCereriDeConcediuConstants getCereriDeConcediuConstants() {
		return cereriDeConcediuConstants;
	}
	public GwtWebConstants getWebConstants() {
		return webConstants;
	}
	public GwtAppComponentsAvailabilityConstants getAppComponentsAvailabilityConstants() {
		return appComponentsAvailabilityConstants;
	}
	public GwtReplacementProfilesOutOfOfficeConstants getReplacementProfilesOutOfOfficeConstants() {
		return replacementProfilesOutOfOfficeConstants;
	}
	public GwtSupportedAttachmentTypesForPreviewConstants getSupportedAttachmentTypesForPreviewConstants() {
		return supportedAttachmentTypesForPreviewConstants;
	}
}