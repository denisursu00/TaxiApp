export class ApiPathConstants {

	// AclGxtService
	public static readonly GET_SECURITY_MANAGER = "/AclGxtService/getSecurityManager";

	// AppConstantsService
	public static readonly GET_APPLICATION_INFO = "/AppConstantsService/getApplicationInfo";

	// Parameters
	public static readonly GET_ALL_PARAMETERS = "/Parameters/getAllParameters";
	public static readonly SAVE_PARAMETER = "/Parameters/saveParameter";
	public static readonly GET_PARAMETER_BY_ID = "/Parameters/getParameterById";
	public static readonly DELETE_PARAMETER_BY_ID = "/Parameters/deleteParameterById";

	// Auth
	public static readonly AUTH_LOGIN = "/Auth/login";
	public static readonly AUTH_GET_LOGGED_IN_USER = "/Auth/getLoggedInUser";
	public static readonly AUTH_CHANGE_PASSWORD = "/Auth/changePassword";
}