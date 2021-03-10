package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtSupportedAttachmentTypesForPreviewConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtFileUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Frame;

public class AttachmentPreviewTab extends TabItem {
	
	private GwtSupportedAttachmentTypesForPreviewConstants supportedAttachmentTypesForPreviewConstants =
		GwtRegistryUtils.getSupportedAttachmentTypesForPreviewConstants();
	
	private String previewLoadingUrl = NavigationConstants.getPreviewAttachmentLoadingUrl();
	private String previewUrl;

	private Frame innerFrame;

	public AttachmentPreviewTab(DocumentWindowRelatedAttachmentInfo attachmentInfo) {
		
		previewUrl = getPreviewUrl(attachmentInfo);
		
		setText(attachmentInfo.getAttachmentName());
		setLayout(new FitLayout());
		
		innerFrame = new Frame(previewLoadingUrl);
		innerFrame.getElement().setPropertyInt("frameBorder", 0);
		innerFrame.setSize("100%", "100%");
		
		add(innerFrame, new FitData(10));
		
		addListener(Events.Select, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				reloadPreview();
			}
		});
	}
	
	private String getPreviewUrl(DocumentWindowRelatedAttachmentInfo attachmentInfo) {

		String attachmentName = attachmentInfo.getAttachmentName();
		String attachmentExtension = GwtFileUtils.getExtension(attachmentName);
		
		String documentLocationRealName = attachmentInfo.getDocumentLocationRealName();
		String documentId = attachmentInfo.getDocumentId();
		Integer versionNumber = attachmentInfo.getVersionNumber();
		
		String previewUrlWithoutParameters = supportedAttachmentTypesForPreviewConstants.getPreviewUrlForFileExtension(attachmentExtension);
		String previewUrlWithParameters = AttachmentUrlHelper.getUrlWithAttachmentParameters(previewUrlWithoutParameters, documentLocationRealName, documentId, versionNumber, attachmentName);
		return previewUrlWithParameters;
	}
	
	private void reloadPreview() {
		
		innerFrame.setUrl(previewLoadingUrl);
		
		new Timer() {
			
			@Override
			public void run() {
				innerFrame.setUrl(previewUrl);
			}
		}.schedule(500);
	}
}