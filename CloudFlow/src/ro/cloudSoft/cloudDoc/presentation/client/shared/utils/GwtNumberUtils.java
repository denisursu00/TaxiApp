package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

/**
 * 
 */
public class GwtNumberUtils {

	/**
	 * Returneaza valoarea intreaga a numarului dat.
	 * Daca numarul este null, atunci va returna null.
	 */
	public static Integer integerValue(Number number) {
		return (number != null) ? number.intValue() : null;
	}
}