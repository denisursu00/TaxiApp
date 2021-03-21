package ro.taxiApp.docs.services.security;

import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.domain.security.SecurityManager;
import ro.taxiApp.docs.presentation.client.shared.model.auth.LoggedInUserModel;
import ro.taxiApp.docs.presentation.client.shared.model.auth.LoginRequestModel;
import ro.taxiApp.docs.presentation.client.shared.model.auth.LoginResponseModel;
import ro.taxiApp.docs.presentation.client.shared.model.organization.PasswordChangeModel;

public interface AuthService {

	LoginResponseModel login(LoginRequestModel loginRequest) throws AppException;
	
	LoggedInUserModel getLoggedInUser();

	void changePassword(PasswordChangeModel passwordChangeModel, SecurityManager securityManager) throws AppException;
}
