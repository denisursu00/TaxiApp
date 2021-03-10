package ro.cloudSoft.cloudDoc.utils.placeholderValueContexts;


/**
 * Context de valori pentru marcaje
 * 
 * 
 */
public interface PlaceholderValueContext {
	
	String VALUE_WHEN_NOT_FOUND = "";
	
	/**
	 * Returneaza valoarea corespunzatoare marcajului cu numele dat.
	 */
	String getValue(String placeholderName);
}