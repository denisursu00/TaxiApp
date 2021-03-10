package ro.cloudSoft.cloudDoc.dao.content;

import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;

/**
 * 
 */
public interface DocumentTypeTemplateDao {
	
	void saveAll(Long documentTypeId, Collection<DocumentTypeTemplate> templates);
	
	/** Sterge template-urile tipului de document cu ID-ul dat care au numele in lista precizata. */
	void delete(Long documentTypeId, Collection<String> templateNames);

	/** Sterge toate template-urile unui tip de document. */
	void deleteAll(Long documentTypeId);
	
	List<DocumentTypeTemplate> getAll();

	DocumentTypeTemplate getByTemplateNameAndDocumentTypeId(String templateName, Long documentTypeId);

	List<DocumentTypeTemplate> getAllJasperTemplates();
}