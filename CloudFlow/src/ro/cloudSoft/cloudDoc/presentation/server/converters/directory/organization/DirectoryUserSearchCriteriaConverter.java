package ro.cloudSoft.cloudDoc.presentation.server.converters.directory.organization;

import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUserSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.GwtDirectoryUserSearchCriteria;

public class DirectoryUserSearchCriteriaConverter {
	
	public static DirectoryUserSearchCriteria getFromGwt(GwtDirectoryUserSearchCriteria gwtDirectoryUserSearchCriteria) {
		
		DirectoryUserSearchCriteria directoryUserSearchCriteria = new DirectoryUserSearchCriteria();
		
		directoryUserSearchCriteria.setUsername(gwtDirectoryUserSearchCriteria.getUsername());
		directoryUserSearchCriteria.setFirstName(gwtDirectoryUserSearchCriteria.getFirstName());
		directoryUserSearchCriteria.setLastName(gwtDirectoryUserSearchCriteria.getLastName());
		
		return directoryUserSearchCriteria;
	}
}