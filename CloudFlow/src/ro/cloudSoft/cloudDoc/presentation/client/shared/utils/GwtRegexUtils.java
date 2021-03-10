package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.Arrays;

public class GwtRegexUtils {

	/**
	 * Contine meta-caracterele interpretate intr-o expresie regulata.
	 * Caracterele sunt ordonate crescator pentru cautare binara rapida in sir.
	 */
	private static final char[] META_CHARS = {'$', '(', ')', '*', '+', '-', '?', '[', '\\', ']', '^', '{', '|', '}'};

	/**
	 * Inlocuieste meta-caracterele folosite in expresii regulata cu
	 * caracterele ne-interpretate in sirul de caractere precizat.
	 * 
	 * @param string sirul de caractere
	 * 
	 * @return sirul de caractere avand meta-caracterele inlocuite
	 */
	public static String escapeMetaChars(String string) {
		StringBuilder escapedString = new StringBuilder();

		for (int i = 0; i < string.length(); i++) {
			char character = string.charAt(i);

			if (isMetaChar(character)) {
				escapedString.append('\\');
			}

			escapedString.append(character);
		}

		return escapedString.toString();
	}

	/**
	 * Verifica daca un caracter este un meta-caracter (caracter interpretat
	 * intr-o expresie regulata).
	 * 
	 * @param character caracterul
	 * 
	 * @return <code>true</code> daca caracterul precizat este un meta-caracter,
	 * <code>false</code> altfel
	 */
	public static boolean isMetaChar(char character) {
		return (Arrays.binarySearch(META_CHARS, character) >= 0);
	}
}