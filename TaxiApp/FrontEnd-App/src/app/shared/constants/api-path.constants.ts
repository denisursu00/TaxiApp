export class ApiPathConstants {

	// AclGxtService
	public static readonly GET_SECURITY_MANAGER = "/AclGxtService/getSecurityManager";

	// AppConstantsService

	// Parameters
	public static readonly GET_ALL_PARAMETERS = "/Parameters/getAllParameters";
	public static readonly SAVE_PARAMETER = "/Parameters/saveParameter";
	public static readonly GET_PARAMETER_BY_ID = "/Parameters/getParameterById";
	public static readonly DELETE_PARAMETER_BY_ID = "/Parameters/deleteParameterById";

	// Auth
	public static readonly AUTH_LOGIN = "/Auth/login";
	public static readonly AUTH_REGISTER = "/Auth/register";
	public static readonly AUTH_GET_LOGGED_IN_USER = "/Auth/getLoggedInUser";

	// Drivers
	public static readonly DRIVER_SAVE = "/Drivers/saveDriver";
	public static readonly DRIVER_GET_ALL = "/Drivers/getAllDrivers";
	public static readonly DRIVER_GET_BY_ID = "/Drivers/getDriverById";
	public static readonly DRIVER_GET_BY_USER_ID = "/Drivers/getDriverByUserId";
	public static readonly DRIVER_DELETE_BY_ID = "/Drivers/deleteDriverById";

	// Clients
	public static readonly CLIENT_SAVE = "/Client/saveClient";
	public static readonly CLIENT_GET_BY_ID = "/Client/getClientById";
	public static readonly CLIENT_GET_BY_USER_ID = "/Client/getClientByUserId";
	public static readonly CLIENT_DELETE_BY_ID = "/Client/deleteClientById";

	// Cars
	public static readonly CAR_SAVE = "/Car/saveCar";
	public static readonly CAR_GET_ALL = "/Car/getAllCars";
	public static readonly CAR_GET_BY_ID = "/Car/getCarById";
	public static readonly CAR_DELETE_BY_ID = "/Car/deleteCarById";
	public static readonly CAR_GET_ALL_CAR_CATEGORIES = "/Car/getAllCarCategories";
	public static readonly CAR_GET_CATEGORY_BY_ID = "/Car/getCarCategoryById";

	// Rides
	public static readonly RIDE_SAVE = "/Ride/saveRide";
	public static readonly RIDE_GET_BY_ID = "/Ride/getRideById";
	public static readonly RIDE_GET_ACTIVE_RIDE = "/Ride/getActiveRide";
	public static readonly RIDE_GET_ALL = "/Ride/getAllRides";

	// Organization
	public static readonly ORGANIZATION_SAVE_USER_AS_DRIVER = "/Organization/saveUserAsDriver";
	public static readonly ORGANIZATION_SAVE_USER_AS_CLIENT = "/Organization/saveUserAsClient";
	public static readonly ORGANIZATION_SAVE_USER_AS_DISPATCHER = "/Organization/saveUserAsDispatcher";
	public static readonly ORGANIZATION_GET_USER_BY_ID = "/Organization/getUserById";
	public static readonly ORGANIZATION_DELETE_USER_BY_ID = "/Organization/deleteUserById";
	public static readonly ORGANIZATION_GET_DISPATCHERS = "/Organization/getDispatchers";
	public static readonly ORGANIZATION_GET_CLIENTS = "/Organization/getClients";

}