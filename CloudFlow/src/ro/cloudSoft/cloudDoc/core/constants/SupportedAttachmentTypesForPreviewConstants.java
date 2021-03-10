package ro.cloudSoft.cloudDoc.core.constants;

import java.util.Map;

import com.google.common.collect.Maps;

public class SupportedAttachmentTypesForPreviewConstants {
	
	private Map<String, String> previewUrlByFileExtensionInLowerCase = Maps.newHashMap();
	
	public Map<String, String> getPreviewUrlByFileExtensionInLowerCase() {
		return previewUrlByFileExtensionInLowerCase;
	}
	
	public void setPreviewUrlByFileExtensionInLowerCase(Map<String, String> previewUrlByFileExtensionInLowerCase) {
		this.previewUrlByFileExtensionInLowerCase = previewUrlByFileExtensionInLowerCase;
	}
}