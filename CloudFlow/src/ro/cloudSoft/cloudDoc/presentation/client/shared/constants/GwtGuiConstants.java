package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Contine constante legate de interfata grafica.
 * Aceasta clasa va fi folosita in interfata grafica.
 * 
 * 
 */
public class GwtGuiConstants implements IsSerializable {

	private int messageComponentWidth;
	private int messageComponentHeight;
	private int messageComponentTimeoutInMilliseconds;
	
	private int workspacePanelWindow_documentsPageSize;
	
	@SuppressWarnings("unused")
	private GwtGuiConstants() {}

	public GwtGuiConstants(int messageComponentWidth, int messageComponentHeight,
			int messageComponentTimeoutInMilliseconds, int workspacePanelWindow_documentsPageSize) {
		
		this.messageComponentWidth = messageComponentWidth;
		this.messageComponentHeight = messageComponentHeight;
		this.messageComponentTimeoutInMilliseconds = messageComponentTimeoutInMilliseconds;
		
		this.workspacePanelWindow_documentsPageSize = workspacePanelWindow_documentsPageSize;
	}
	
	public int getMessageComponentWidth() {
		return messageComponentWidth;
	}
	public int getMessageComponentHeight() {
		return messageComponentHeight;
	}
	public int getMessageComponentTimeoutInMilliseconds() {
		return messageComponentTimeoutInMilliseconds;
	}
	public int getWorkspacePanelWindow_documentsPageSize() {
		return workspacePanelWindow_documentsPageSize;
	}
}