package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import com.google.gwt.user.client.Window;

public class GwtRequestUtils {
	
	/**
	 * Returneaza valoarea unui parametru de pe request cu numele dat.
	 * Daca parametrul nu exista, va returna null.
	 */
	public static String getRequestParameterValue(String name) {
		String parameterValue = Window.Location.getParameter(name);
		if (parameterValue != null) {
			return parameterValue.replace('+', ' ');
		} else {
			return null;
		}
	}
}