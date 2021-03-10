package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import com.google.gwt.user.client.Window;

/**
 * 
 */
public class ErrorUtils {
	
	/**
	 * "Arunca" o exceptie intr-un mod vizibil din interfata grafica GWT.
	 * <br><br>
	 * NOTA: Aceasta metoda trebuie apelata pentru aruncarea unei exceptii din partea de interfata grafica GWT.
	 * 
	 * @param throwerClass clasa care a aruncat exceptia
	 * @param exception exceptia ce se doreste a fi aruncata
	 */
	public static void throwException(Class<?> throwerClass, Exception exception) {
		
		String exceptionType = exception.getClass().getName();
		String exceptionMessage = exception.getMessage();
		
		String message = (exceptionMessage != null) ? (exceptionType + ": " + exceptionMessage) : exceptionType;
		message = "[" + throwerClass.getName() + "] " + message;
		
		Window.alert(message);
	}
}