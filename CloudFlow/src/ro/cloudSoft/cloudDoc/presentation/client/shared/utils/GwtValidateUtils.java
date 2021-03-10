package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.Collection;

public class GwtValidateUtils {
	
	public static boolean hasElements(Collection<?> collection) {
        return (collection != null) && (collection.size() > 0);
    }

	public static boolean isCompleted(Object value) {
        return (value != null) && (value.toString().trim().length() > 0);
    }
	
	/**
	 * Verifica daca un obiect de tip <code>Boolean</code> indica valoarea
	 * <code>true</code>.
	 * @param booleanObject obiectul de tip <code>Boolean</code>
	 * @return <code>true</code> daca obiectul indica valoarea <code>true</code>,
	 * <code>false</code> altfel
	 */
	public static boolean isTrue(Boolean booleanObject) {
		return (booleanObject != null) && (booleanObject.booleanValue());
	}
}