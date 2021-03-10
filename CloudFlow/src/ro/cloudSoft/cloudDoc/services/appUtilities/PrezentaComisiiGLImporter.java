package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.security.SecurityManagerFactory;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.importer.PrezentaComisiiGLImporterService;

public class PrezentaComisiiGLImporter {
	
	private SecurityManagerFactory securityManagerFactory;
	private BusinessConstants businessConstants;
	private PrezentaComisiiGLImporterService prezentaComisiiGLImporterService;
	
	public void doImport(String excelFilePath, String workspaceName, final String folderName) throws AppUtilitiesException {
		SecurityManager securityManager =  securityManagerFactory.getSecurityManager(businessConstants.getApplicationUserName());
		SecurityManager securityManagerAdmin =  securityManagerFactory.getSecurityManager("vlad.barabas");
		SecurityManagerHolder.setSecurityManager(securityManagerAdmin);
		
		validateInputs(excelFilePath, workspaceName, folderName);
		
		prezentaComisiiGLImporterService.importData(excelFilePath, workspaceName, folderName);
	}
	
	private void validateInputs(String excelFilePath, String workspaceName, final String folderName) throws AppUtilitiesException {		
		List<String> errorMessages = new ArrayList<>();
		if (StringUtils.isBlank(excelFilePath)) {
			errorMessages.add("Calea fisierului excel nu poate fi null");
		}
		if (StringUtils.isBlank(workspaceName)) {
			errorMessages.add("Workspace name nu poate fi null");
		}
		if (StringUtils.isBlank(folderName)) {
			errorMessages.add("Folder name nu poate fi null");
		}
		if (CollectionUtils.isNotEmpty(errorMessages)) {
			throw new AppUtilitiesException(errorMessages);
		}
	}
	
	public void setSecurityManagerFactory(SecurityManagerFactory securityManagerFactory) {
		this.securityManagerFactory = securityManagerFactory;
	}
	
	public void setBusinessConstants(BusinessConstants businessConstants) {
		this.businessConstants = businessConstants;
	}

	public void setPrezentaComisiiGLImporterService(PrezentaComisiiGLImporterService prezentaComisiiGLImporterService) {
		this.prezentaComisiiGLImporterService = prezentaComisiiGLImporterService;
	}
	
}
