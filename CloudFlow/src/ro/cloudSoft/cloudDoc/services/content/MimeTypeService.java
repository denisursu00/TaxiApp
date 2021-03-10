package ro.cloudSoft.cloudDoc.services.content;

import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.MimeType;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public interface MimeTypeService {
	
	List<MimeType> getAllMimeTypes();
	
	/**
	 * Daca nu se gaseste un tip de atasament cu ID-ul dat ca parametru, atunci returneaza null.
	 */
	MimeType getMimeTypeById(Long id);

	/**
	 * Daca nu se gaseste un tip de atasament cu ID-ul dat ca parametru, atunci returneaza null.
	 */
	MimeType getMimeTypeById(Long id, SecurityManager userSecurity); 
	
	void saveMimeTypes(Collection<MimeType> mimeTypes)  throws AppException;
	
	void saveMimeType(MimeType mimeType)  throws AppException;
	
	void saveMimeType(MimeType mimeType, SecurityManager userSecurity)  throws AppException;
	
	void deleteMimeType(Long mimeTypeId) throws AppException;
	
	void deleteMimeType(Long mimeTypeId, SecurityManager userSecurity) throws AppException;
}