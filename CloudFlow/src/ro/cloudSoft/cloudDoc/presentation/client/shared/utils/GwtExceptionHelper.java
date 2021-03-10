package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.MissingResourceException;

import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationExceptionCodes;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.google.gwt.user.client.rpc.InvocationException;

/**
 * Reprezinta o clasa ajutatoare pentru exceptiile de interfata.
 * 
 * 
 */
public class GwtExceptionHelper {
	
	/**
	 * Returneaza mesajul de eroare corespunzator exceptiei precizate.
	 * @param exception exceptia
	 */
	public static String getErrorMessage(Throwable exception) {
		if (exception instanceof PresentationException) {
			return getLocalizedMessage(((PresentationException) exception).getCode());
		} else if (exception instanceof InvocationException) {
			return GwtLocaleProvider.getExceptionMessages().INVOCATION_EXCEPTION();
		} else {
			return GwtLocaleProvider.getExceptionMessages().APPLICATION_ERROR();
		}
	}
	
	private static String getLocalizedMessage(String code) {
		try {
			return GwtLocaleProvider.getExceptionMessages().getString(code);
		} catch (MissingResourceException mre) {
			return GwtLocaleProvider.getExceptionMessages().APPLICATION_ERROR();
		}
	}
	
	/**
	 * Verifica daca utilizatorul nu este autentificat pe baza exceptiei primite de la server.
	 * <br><br>
	 * NOTA: In clasa serviciului GWT RPC apelat trebuie sa existe cel putin o declaratie de <code>throws</code> cu
	 * exceptia care ar putea fi aruncata. Altfel, exceptia primita de partea client nu va fi cea aruncata, ci una
	 * generica GWT.
	 */
	public static boolean isUnauthenticated(Throwable exception) {
		
		if (!(exception instanceof PresentationException)) {
			return false;
		}
		
		PresentationException presentationException = (PresentationException) exception;
		String presentationExceptionCode = presentationException.getCode();
		
		return ((presentationExceptionCode != null) &&
			presentationExceptionCode.equals(PresentationExceptionCodes.UNAUTHENTICATED));
	}
}