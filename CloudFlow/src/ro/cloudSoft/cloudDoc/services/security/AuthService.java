package ro.cloudSoft.cloudDoc.services.security;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.auth.LoggedInUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.auth.LoginRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.auth.LoginResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.PasswordChangeModel;

public interface AuthService {

	LoginResponseModel login(LoginRequestModel loginRequest) throws AppException;
	
	LoggedInUserModel getLoggedInUser();

	void changePassword(PasswordChangeModel passwordChangeModel, SecurityManager securityManager) throws AppException;
}
