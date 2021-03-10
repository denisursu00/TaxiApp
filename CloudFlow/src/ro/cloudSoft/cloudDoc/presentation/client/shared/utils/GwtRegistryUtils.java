package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtAppComponentsAvailabilityConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtBusinessConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtCereriDeConcediuConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtGuiConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtReplacementProfilesOutOfOfficeConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtSupportedAttachmentTypesForPreviewConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtWebConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;

public class GwtRegistryUtils {
	
	private static boolean initialized = false;

    private static SecurityManagerModel userSecurity;
    
    private static GwtGuiConstants guiConstants;
    private static GwtBusinessConstants businessConstants;
    private static GwtCereriDeConcediuConstants cereriDeConcediuConstants;
    private static GwtWebConstants webConstants;
    private static GwtAppComponentsAvailabilityConstants appComponentsAvailabilityConstants;
    private static GwtReplacementProfilesOutOfOfficeConstants replacementProfilesOutOfOfficeConstants;
	private static GwtSupportedAttachmentTypesForPreviewConstants supportedAttachmentTypesForPreviewConstants;
    
    private static void checkInitialized() {
    	if (!initialized) {
    		ErrorUtils.throwException(GwtRegistryUtils.class, new IllegalStateException("GwtRegistryUtils nu a fost initializata."));
    	}
    }
    
    public static void init(SecurityManagerModel userSecurity, GwtGuiConstants guiConstants,
    		GwtBusinessConstants businessConstants, GwtCereriDeConcediuConstants cereriDeConcediuConstants,
    		GwtWebConstants webConstants, GwtAppComponentsAvailabilityConstants appComponentsAvailabilityConstants,
    		GwtReplacementProfilesOutOfOfficeConstants replacementProfilesOutOfOfficeConstants,
    		GwtSupportedAttachmentTypesForPreviewConstants supportedAttachmentTypesForPreviewConstants) {
    	
    	GwtRegistryUtils.userSecurity = userSecurity;
    	
    	GwtRegistryUtils.guiConstants = guiConstants;
    	GwtRegistryUtils.businessConstants = businessConstants;
    	GwtRegistryUtils.cereriDeConcediuConstants = cereriDeConcediuConstants;
    	GwtRegistryUtils.webConstants = webConstants;
    	GwtRegistryUtils.appComponentsAvailabilityConstants = appComponentsAvailabilityConstants;
    	GwtRegistryUtils.replacementProfilesOutOfOfficeConstants = replacementProfilesOutOfOfficeConstants;
    	GwtRegistryUtils.supportedAttachmentTypesForPreviewConstants = supportedAttachmentTypesForPreviewConstants;
    	
    	initialized = true;
    }
    
    public static SecurityManagerModel getUserSecurity() {
    	checkInitialized();
		return userSecurity;
	}
    
    public static GwtGuiConstants getGuiConstants() {
    	checkInitialized();
		return guiConstants;
	}
    
    public static GwtBusinessConstants getBusinessConstants() {
    	checkInitialized();
		return businessConstants;
	}
    
    public static GwtCereriDeConcediuConstants getCereriDeConcediuConstants() {
    	checkInitialized();
		return cereriDeConcediuConstants;
	}
    
    public static GwtWebConstants getWebConstants() {
    	checkInitialized();
		return webConstants;
	}
    
    public static GwtAppComponentsAvailabilityConstants getAppComponentsAvailabilityConstants() {
    	checkInitialized();
		return appComponentsAvailabilityConstants;
	}
    
    public static GwtReplacementProfilesOutOfOfficeConstants getReplacementProfilesOutOfOfficeConstants() {
    	checkInitialized();
		return replacementProfilesOutOfOfficeConstants;
	}
    
    public static GwtSupportedAttachmentTypesForPreviewConstants getSupportedAttachmentTypesForPreviewConstants() {
    	checkInitialized();
		return supportedAttachmentTypesForPreviewConstants;
	}
}