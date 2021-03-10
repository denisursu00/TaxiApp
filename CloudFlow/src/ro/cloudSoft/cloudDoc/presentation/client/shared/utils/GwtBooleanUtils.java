package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

/**
 * Utilitati pentru valori de tip boolean pentru partea de client (GWT)
 * 
 * 
 */
public class GwtBooleanUtils {

	public static boolean isTrue(Boolean value) {
		return ((value != null) && value.equals(Boolean.TRUE));
	}
	
	public static boolean isNotTrue(Boolean value) {
		return (!isTrue(value));
	}
}