package ro.cloudSoft.cloudDoc.utils;

import java.util.List;

import javax.servlet.http.HttpSession;

import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.common.utils.HttpSessionEntityManager;

/**
 * Clasa se ocupa cu operatii asupra atasamentelor stocate intr-un spatiu
 * temporar, in acest caz fiind sesiunea utilizatorului pe server.
 * 
 * 
 */
public class SessionAttachmentManager {

	/** numele atributului de pe sesiune unde vor fi stocate atasamentele temporar */
	private static final String SESSION_ATTRIBUTE_ATTACHMENT_MAP = "attachmentMap";
	
	private static HttpSessionEntityManager<String, Attachment> manager =
		new HttpSessionEntityManager<String, Attachment>(SESSION_ATTRIBUTE_ATTACHMENT_MAP);
	
	/**
	 * Adauga un atasament in spatiul temporar.
	 * @param session sesiunea utilizatorului
	 * @param attachment atasamentul
	 */
	public static void addAttachment(HttpSession session, Attachment attachment) {
		manager.addEntity(session, attachment.getName(), attachment);
	}

	/**
	 * Inlatura atasamentele specificate prin nume din spatiul temporar.
	 * @param session sesiunea utilizatorului
	 * @param attachmentNames numele atasamentelor de inlaturat
	 */
	public static void removeAttachments(HttpSession session, List<String> attachmentNames) {
		manager.removeEntities(session, attachmentNames);
	}

	/**
	 * Returneaza atasamentele aflate in spatiul temporar, inlaturandu-le apoi
	 * din acesta.
	 * @param session sesiunea utilizatorului
	 * @return atasamentele aflate in spatiul temporar
	 */
	public static List<Attachment> getAttachments(HttpSession session) {
		return manager.getEntities(session);
	}

	/**
	 * Returneaza atasamentul specificat prin nume, aflat in spatiul temporar.
	 * @param session sesiunea utilizatorului
	 * @param attachmentName numele atasamentului
	 * @return atasamentul specificat prin nume
	 */
	public static Attachment getAttachment(HttpSession session, String attachmentName) {
		return manager.getEntity(session, attachmentName);
	}
	
	/** Elimina toate atasamentele temporare asociate utilizatorului curent. */
	public static void clear(HttpSession session) {
		manager.removeAllEntities(session);
	}
}