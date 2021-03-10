package ro.cloudSoft.cloudDoc.presentation.server.services;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.config.environment.AppEnvironment;
import ro.cloudSoft.cloudDoc.config.environment.AppEnvironmentConfig;
import ro.cloudSoft.cloudDoc.core.constants.AppComponentsAvailabilityConstants;
import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.core.constants.CereriDeConcediuConstants;
import ro.cloudSoft.cloudDoc.core.constants.GuiConstants;
import ro.cloudSoft.cloudDoc.core.constants.ReplacementProfilesOutOfOfficeConstants;
import ro.cloudSoft.cloudDoc.core.constants.SupportedAttachmentTypesForPreviewConstants;
import ro.cloudSoft.cloudDoc.core.constants.WebConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtAppComponentsAvailabilityConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtBusinessConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtConstantsPayload;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtGuiConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtSupportedAttachmentTypesForPreviewConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtWebConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ApplicationInfoModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AppGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.constants.CereriDeConcediuConstantsConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.constants.ReplacementProfilesOutOfOfficeConstantsConverter;
import ro.cloudSoft.cloudDoc.presentation.server.utils.LocaleUtils;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class AppGxtServiceImpl extends GxtServiceImplBase implements AppGxtService, InitializingBean {

	private final GuiConstants guiConstants;
	private final BusinessConstants businessConstants;
	private final CereriDeConcediuConstants cereriDeConcediuConstants;
	private final WebConstants webConstants;
	private final AppComponentsAvailabilityConstants appComponentsAvailabilityConstants;
	private final ReplacementProfilesOutOfOfficeConstants replacementProfilesOutOfOfficeConstants;
	private final SupportedAttachmentTypesForPreviewConstants supportedAttachmentTypesForPreviewConstants;
	
	public AppGxtServiceImpl(GuiConstants guiConstants, BusinessConstants businessConstants,
			CereriDeConcediuConstants cereriDeConcediuConstants, WebConstants webConstants,
			AppComponentsAvailabilityConstants appComponentsAvailabilityConstants,
			ReplacementProfilesOutOfOfficeConstants replacementProfilesOutOfOfficeConstants,
			SupportedAttachmentTypesForPreviewConstants supportedAttachmentTypesForPreviewConstants) {
		
		this.guiConstants = guiConstants;
		this.businessConstants = businessConstants;
		this.cereriDeConcediuConstants = cereriDeConcediuConstants;
		this.webConstants = webConstants;
		this.appComponentsAvailabilityConstants = appComponentsAvailabilityConstants;
		this.replacementProfilesOutOfOfficeConstants = replacementProfilesOutOfOfficeConstants;
		this.supportedAttachmentTypesForPreviewConstants = supportedAttachmentTypesForPreviewConstants;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			guiConstants,
			businessConstants,
			cereriDeConcediuConstants,
			webConstants,
			replacementProfilesOutOfOfficeConstants
		);
	}

	@Override
	public void setLocale(String locale) {
		HttpSession session = getSession();
		LocaleUtils.setLocaleByCode(locale, session);
	}
	
	@Override
	public GwtConstantsPayload getConstants() {
		return new GwtConstantsPayload(
			new GwtGuiConstants(
				guiConstants.getMessageComponentWidth(),
				guiConstants.getMessageComponentHeight(),
				guiConstants.getMessageComponentTimeoutInMilliseconds(),
				guiConstants.getWorkspacePanelWindow_documentsPageSize()
			),
			new GwtBusinessConstants(
				businessConstants.getGroupNameAdmins()
			),
			CereriDeConcediuConstantsConverter.getForGwt(cereriDeConcediuConstants),
			new GwtWebConstants(
				
				webConstants.getRequestParameterOrAttributeNameRequestedModule(),
				
				webConstants.getModuleAdmin(),
				webConstants.getModuleClient(),
				
				webConstants.getPageLogout(),
				
				webConstants.getPageModuleAdmin(),
				webConstants.getPageModuleClient(),
				webConstants.getPageModuleArchive(),
				
				webConstants.getPageExportDocument(),
				webConstants.getPageDocumentSearchReport()
			),
			new GwtAppComponentsAvailabilityConstants(
				appComponentsAvailabilityConstants.isWorkflowGraphViewGeneratorEnabled()
			),
			ReplacementProfilesOutOfOfficeConstantsConverter.getForGwt(replacementProfilesOutOfOfficeConstants),
			new GwtSupportedAttachmentTypesForPreviewConstants(
				supportedAttachmentTypesForPreviewConstants.getPreviewUrlByFileExtensionInLowerCase()
			)
		);
	}
	
	@Override
	public Integer getSessionTimeoutInSeconds() {
		return getSession().getMaxInactiveInterval();
	}
	
	@Override
	public void keepSessionAlive() {
		getSession().getAttribute("dummy");
	}
	
	@Override
	public ApplicationInfoModel getApplicationInfo() {
		ApplicationInfoModel appInfo = new ApplicationInfoModel();
		appInfo.setEnvironmentName(AppEnvironmentConfig.getCurrentEnvironment().name());
		appInfo.setProduction(AppEnvironmentConfig.getCurrentEnvironment().equals(AppEnvironment.PRODUCTION));
		return appInfo;
	}
}