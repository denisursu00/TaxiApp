package ro.cloudSoft.cloudDoc.core.constants;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class WebConstants implements InitializingBean {
	
	private final String requestParameterOrAttributeNameRequestedModule;
	
	private final String moduleAdmin;
	private final String moduleClient;
	
	private final String pageLogin;
	private final String pageLogout;
	
	private final String pageModuleAdmin;
	private final String pageModuleClient;
	private final String pageModuleArchive;
	
	private final String pageExportDocument;
	private final String pageDocumentSearchReport;
	
	public WebConstants(String requestParameterOrAttributeNameRequestedModule, String moduleAdmin, String moduleClient,
			String pageLogin, String pageLogout, String pageModuleAdmin, String pageModuleClient,
			String pageModuleArchive, String pageExportDocument, String pageDocumentSearchReport) {
		
		this.requestParameterOrAttributeNameRequestedModule = requestParameterOrAttributeNameRequestedModule;
				
		this.moduleAdmin = moduleAdmin;
		this.moduleClient = moduleClient;
		
		this.pageLogin = pageLogin;
		this.pageLogout = pageLogout;
		
		this.pageModuleAdmin = pageModuleAdmin;
		this.pageModuleClient = pageModuleClient;
		this.pageModuleArchive = pageModuleArchive;
		
		this.pageExportDocument = pageExportDocument;
		this.pageDocumentSearchReport = pageDocumentSearchReport;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			requestParameterOrAttributeNameRequestedModule,
			
			moduleAdmin,
			moduleClient,
			
			pageLogin,
			pageLogout,
			
			pageModuleAdmin,
			pageModuleClient,
			pageModuleArchive,
			
			pageExportDocument,
			pageDocumentSearchReport
		);
	}

	public String getRequestParameterOrAttributeNameRequestedModule() {
		return requestParameterOrAttributeNameRequestedModule;
	}
	public String getModuleAdmin() {
		return moduleAdmin;
	}
	public String getModuleClient() {
		return moduleClient;
	}
	public String getPageLogin() {
		return pageLogin;
	}
	public String getPageLogout() {
		return pageLogout;
	}
	public String getPageModuleAdmin() {
		return pageModuleAdmin;
	}
	public String getPageModuleClient() {
		return pageModuleClient;
	}
	public String getPageModuleArchive() {
		return pageModuleArchive;
	}
	public String getPageExportDocument() {
		return pageExportDocument;
	}
	public String getPageDocumentSearchReport() {
		return pageDocumentSearchReport;
	}
}