package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

public class GwtXmlUtils {

	/**
	 * Elimina toate tag-urile dintr-un text.
	 */
	public static String stripAllTags(String text) {
		/*
		 * Caut orice incepe cu < si se termina cu >.
		 * Intre paranteze poate fi orice caracter, de oricate ori, cu exceptia caracterului >.
		 */
		return text.replaceAll("<[^>]+>", "");
	}
}