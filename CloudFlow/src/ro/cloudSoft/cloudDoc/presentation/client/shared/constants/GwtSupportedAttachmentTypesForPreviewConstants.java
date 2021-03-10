package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtSupportedAttachmentTypesForPreviewConstants implements IsSerializable {
	
	private Map<String, String> previewUrlByFileExtensionInLowerCase = new HashMap<String, String>();
	
	protected GwtSupportedAttachmentTypesForPreviewConstants() {
		// Constructor implicit, necesar pentru serializare
	}

	public GwtSupportedAttachmentTypesForPreviewConstants(Map<String, String> previewUrlByFileExtensionInLowerCase) {
		this.previewUrlByFileExtensionInLowerCase = previewUrlByFileExtensionInLowerCase;
	}
	
	public boolean isFileExtensionSupportedForPreview(String extension) {
		String extensionInLowerCase = extension.toLowerCase();
		return previewUrlByFileExtensionInLowerCase.containsKey(extensionInLowerCase);
	}
	
	public String getPreviewUrlForFileExtension(String extension) {
		
		String extensionInLowerCase = extension.toLowerCase();
		
		String previewUrl = previewUrlByFileExtensionInLowerCase.get(extensionInLowerCase);
		if (previewUrl == null) {
			throw new IllegalArgumentException("Fisierele cu extensia [" + extension + "] NU suporta preview.");
		}
		
		String normalizedUrl = NavigationConstants.addBasePrefixToUrl(previewUrl);
		return normalizedUrl;
	}
}