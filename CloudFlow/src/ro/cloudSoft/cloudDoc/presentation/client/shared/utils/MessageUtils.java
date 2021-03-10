package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;

/**
 * Componenta pentru afisarea mesajelor de confirmare / eroare
 * 
 * 
 */
public class MessageUtils {
	
	private static final int DEFAULT_WIDTH = 320;
	private static final int DEFAULT_HEIGHT = 120;
	private static final int DEFAULT_TIMEOUT_IN_MILLISECONDS = 5000;
	
	private static void display(String title, String message, int width, int height, int timeoutInMilliseconds) {
		
		InfoConfig infoConfig = new InfoConfig(title, message);
		
		infoConfig.width = width;
		infoConfig.height = height;
		
		infoConfig.display = timeoutInMilliseconds;
		
		Info.display(infoConfig);
	}
	
	/**
	 * Afiseaza un mesaj.
	 * 
	 * @param title titlul mesajului
	 * @param message mesajul
	 */
	public static void display(String title, String message) {		
		display(title, message, GwtRegistryUtils.getGuiConstants().getMessageComponentWidth(),
			GwtRegistryUtils.getGuiConstants().getMessageComponentHeight(),
			GwtRegistryUtils.getGuiConstants().getMessageComponentTimeoutInMilliseconds());
	}

	/**
	 * Afiseaza un mesaj cu setarile implicite.
	 * Metoda este de folosit in cazul in care constantele nu sunt (inca) incarcate de pe server.
	 * 
	 * @param title titlul mesajului
	 * @param message mesajul
	 */
	public static void displayWithDefaultSettings(String title, String message) {
		display(title, message, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TIMEOUT_IN_MILLISECONDS);
	}
	
	/** Afiseaza un mesaj de eroare. */
	public static void displayError(String errorMessage) {
		display(GwtLocaleProvider.getConstants().ERROR(), errorMessage);
	}
	
	/**
	 * Afiseaza un mesaj de eroare.
	 * Metoda este de folosit in cazul in care constantele nu sunt (inca) incarcate de pe server.
	 */
	public static void displayErrorWithDefaultSettings(String errorMessage) {
		displayWithDefaultSettings(GwtLocaleProvider.getConstants().ERROR(), errorMessage);
	}

	/** Afiseaza un mesaj de eroare in functie de exceptia primita. */
	public static void displayError(Throwable exception) {
		displayError(GwtExceptionHelper.getErrorMessage(exception));
	}

	/**
	 * Afiseaza un mesaj de eroare in functie de exceptia primita.
	 * Metoda este de folosit in cazul in care constantele nu sunt (inca) incarcate de pe server.
	 */
	public static void displayErrorWithDefaultSettings(Throwable exception) {
		displayErrorWithDefaultSettings(GwtExceptionHelper.getErrorMessage(exception));
	}
}