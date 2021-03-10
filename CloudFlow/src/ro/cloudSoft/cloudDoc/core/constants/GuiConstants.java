package ro.cloudSoft.cloudDoc.core.constants;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

/**
 * Contine constante legate de interfata grafica.
 * Aceasta clasa va fi folosita pe server.
 * 
 * 
 */
public class GuiConstants implements InitializingBean {

	private final int messageComponentWidth;
	private final int messageComponentHeight;
	private final int messageComponentTimeoutInMilliseconds;
	
	private final int workspacePanelWindow_documentsPageSize;

	public GuiConstants(int messageComponentWidth, int messageComponentHeight,
			int messageComponentTimeoutInMilliseconds, int workspacePanelWindow_documentsPageSize) {
		
		this.messageComponentWidth = messageComponentWidth;
		this.messageComponentHeight = messageComponentHeight;
		this.messageComponentTimeoutInMilliseconds = messageComponentTimeoutInMilliseconds;
		
		this.workspacePanelWindow_documentsPageSize = workspacePanelWindow_documentsPageSize;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			messageComponentWidth,
			messageComponentHeight,
			messageComponentTimeoutInMilliseconds,
			
			workspacePanelWindow_documentsPageSize
		);
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