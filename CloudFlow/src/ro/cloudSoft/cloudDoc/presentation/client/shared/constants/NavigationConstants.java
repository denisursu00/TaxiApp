package ro.cloudSoft.cloudDoc.presentation.client.shared.constants;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;

import com.google.gwt.core.client.GWT;

public class NavigationConstants {
	
	private static final String SLASH = "/";
	
	private static String getBaseUrlWithoutEndingSlash() {
		String baseUrl = GWT.getHostPageBaseURL();
		if (baseUrl.endsWith(SLASH)) {
			return baseUrl.substring(0, baseUrl.length() - SLASH.length());
		} else {
			return baseUrl;
		}
	}
	
	public static String addBasePrefixToUrl(String url) {
		return (getBaseUrlWithoutEndingSlash() + url);
	}
    
    public static String getLogoutLink() {
    	return addBasePrefixToUrl(GwtRegistryUtils.getWebConstants().getPageLogout());
    }
    
    public static String getAdminLink() {
    	return addBasePrefixToUrl(GwtRegistryUtils.getWebConstants().getPageModuleAdmin());
    }
    
    public static String getClientLink() {
    	return addBasePrefixToUrl(GwtRegistryUtils.getWebConstants().getPageModuleClient());
    }
    
    public static String getArchiveLink() {
    	return addBasePrefixToUrl(GwtRegistryUtils.getWebConstants().getPageModuleArchive());
    }
    
    public static String getExportDocumentLink() {
    	return addBasePrefixToUrl(GwtRegistryUtils.getWebConstants().getPageExportDocument());
    }
    
    public static String getDocumentSearchReportLink() {
    	return addBasePrefixToUrl(GwtRegistryUtils.getWebConstants().getPageDocumentSearchReport());
    }
    
    public static String getDownloadTemplateLink() {
    	return GWT.getHostPageBaseURL() + "downloadTemplate";
    }
    
    public static String getUploadTemplateLink() {
    	return GWT.getHostPageBaseURL() + "uploadTemplate";
    }
    
    public static String getUploadAttachmentLink() {
    	return GWT.getHostPageBaseURL() + "uploadAttachment";
    }
    
    public static String getDownloadAttachmentLink() {
    	return GWT.getHostPageBaseURL() + "downloadAttachment";
    }
    
    public static String getViewWorkflowGraphLink() {
    	return GWT.getHostPageBaseURL() + "viewWorkflowGraph.do";
    }
    
    public static String getPreviewAttachmentLoadingUrl() {
    	return GWT.getHostPageBaseURL() + "previewAttachmentLoading.jsp";
    }
}