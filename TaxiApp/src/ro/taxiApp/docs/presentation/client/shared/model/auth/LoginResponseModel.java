package ro.taxiApp.docs.presentation.client.shared.model.auth;

public class LoginResponseModel {

	private String authToken;
	private LoggedInUserModel loggedInUser;
	
	public String getAuthToken() {
		return authToken;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public LoggedInUserModel getLoggedInUser() {
		return loggedInUser;
	}
	
	public void setLoggedInUser(LoggedInUserModel loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
}
