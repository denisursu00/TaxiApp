package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Frame;

public class PreviewAttachmentWindow extends Window {

	private Frame innerFrame;
	
	public PreviewAttachmentWindow() {

		setHeading(GwtLocaleProvider.getConstants().PREVIEW());
		setLayout(new FitLayout());
		setSize(800, 480);
		setClosable(true);
		setMaximizable(true);
		setMinimizable(false);
		
		String previewLoadingUrl = NavigationConstants.getPreviewAttachmentLoadingUrl();
		
		innerFrame = new Frame(previewLoadingUrl);
		innerFrame.getElement().setPropertyInt("frameBorder", 0);
		innerFrame.setSize("100%", "100%");
		
		add(innerFrame);
	}
	
	public void changeInnerFrameUrl(String url) {
		innerFrame.setUrl(url);
	}
	
	public static void showForPreviewUrl(final String previewUrl) {
		
		final PreviewAttachmentWindow previewWindow = new PreviewAttachmentWindow();
		previewWindow.show();
		
		/*
		 * Initial, in fereastra apare un mesaj de asteptare.
		 * Apoi se cere URL-ul preview-ului, care s-ar putea sa se incarce greu.
		 * Prin intermediul timer-ului, va fi afisat mesajul de asteptare pana se va termina de incarcat preview-ul.
		 */
		new Timer() {
			
			@Override
			public void run() {
				previewWindow.changeInnerFrameUrl(previewUrl);
			}
		}.schedule(500);
	}
}