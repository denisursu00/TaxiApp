package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtWebConstants implements IsSerializable {
	
	private String requestParameterOrAttributeNameRequestedModule;
	
	private String moduleAdmin;
	private String moduleClient;

	private String pageLogout;
	
	private String pageModuleAdmin;
	private String pageModuleClient;
	private String pageModuleArchive;
	
	private String pageExportDocument;
	private String pageDocumentSearchReport;
	
	@SuppressWarnings("unused")
	private GwtWebConstants() {}
	
	public GwtWebConstants(String requestParameterOrAttributeNameRequestedModule, String moduleAdmin,
			String moduleClient, String pageLogout, String pageModuleAdmin, String pageModuleClient,
			String pageModuleArchive, String pageExportDocument, String pageDocumentSearchReport) {
		
		this.requestParameterOrAttributeNameRequestedModule = requestParameterOrAttributeNameRequestedModule;
		
		this.moduleAdmin = moduleAdmin;
		this.moduleClient = moduleClient;
		
		this.pageLogout = pageLogout;
		
		this.pageModuleAdmin = pageModuleAdmin;
		this.pageModuleClient = pageModuleClient;
		this.pageModuleArchive = pageModuleArchive;
		
		this.pageExportDocument = pageExportDocument;
		this.pageDocumentSearchReport = pageDocumentSearchReport;
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