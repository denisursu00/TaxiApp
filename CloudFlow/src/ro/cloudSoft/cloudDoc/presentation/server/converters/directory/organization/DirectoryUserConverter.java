package ro.cloudSoft.cloudDoc.presentation.server.converters.directory.organization;

import ro.cloudSoft.cloudDoc.domain.directory.organization.DirectoryUser;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.DirectoryUserModel;

public class DirectoryUserConverter {
	
	public static DirectoryUserModel getModelFromDirectoryUser(DirectoryUser directoryUser) {
		
		DirectoryUserModel directoryUserModel = new DirectoryUserModel();
		
		directoryUserModel.setUsername(directoryUser.getUsername());
		directoryUserModel.setFirstName(directoryUser.getFirstName());
		directoryUserModel.setLastName(directoryUser.getLastName());
		directoryUserModel.setPassword(directoryUser.getPassword());
		directoryUserModel.setTitle(directoryUser.getTitle());
		directoryUserModel.setEmail(directoryUser.getEmail());
		directoryUserModel.setEmployeeNumber(directoryUser.getEmployeeNumber());
		
		return directoryUserModel;
	}
}