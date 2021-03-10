package ro.cloudSoft.cloudDoc.utils;

import java.util.List;

import javax.servlet.http.HttpSession;

import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.common.utils.HttpSessionEntityManager;

/**
 * @see SessionAttachmentManager
 * 
 * 
 */
public class SessionTemplateManager {

	private static final String SESSION_ATTRIBUTE_TEMPLATE_MAP = "templateMap";
	
	private static HttpSessionEntityManager<String, DocumentTypeTemplate> manager =
		new HttpSessionEntityManager<String, DocumentTypeTemplate>(SESSION_ATTRIBUTE_TEMPLATE_MAP);
	
	public static void addTemplate(HttpSession session, DocumentTypeTemplate template) {
		manager.addEntity(session, template.getName(), template);
	}

	public static void removeTemplates(HttpSession session, List<String> templateNames) {
		manager.removeEntities(session, templateNames);
	}

	public static List<DocumentTypeTemplate> getTemplates(HttpSession session) {
		return manager.getEntities(session);
	}

	public static DocumentTypeTemplate getTemplate(HttpSession session, String templateName) {
		return manager.getEntity(session, templateName);
	}
	
	/** Elimina toate template-urile temporare asociate utilizatorului curent. */
	public static void clear(HttpSession session) {
		manager.removeAllEntities(session);
	}
}