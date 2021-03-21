package ro.taxiApp.docs.core;

public class AppExceptionUtils {

	/**
	 * Metoda cauta prin cauzele exceptiei o exceptie de tip ApplicationException. Daca
	 * o gaseste o va returna, altfel returneaza null.
	 */
	public static AppException getAppExceptionFromExceptionCause(Throwable exception) {
		Throwable throwable = exception.getCause();
		while (throwable != null) {
			if (throwable instanceof AppException) {
				return (AppException)throwable;
			}
			throwable = throwable.getCause();				
		}
		return null;
	}	

}