package ro.cloudSoft.cloudDoc.dao.content;

import java.util.Collection;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.MimeType;

public interface MimeTypeDao {
	
	List<MimeType> getAllMimeTypes();
	
	void saveMimeType(MimeType mimeType);
	
	void saveAll(Collection<MimeType> mimeTypes);
	
	MimeType getMimeType(String extension);
	
	/* Daca nu se gaseste un tip cu extensia data ca parametru, se returneaza null.
	 * Cautarea nu este case-sensitive.
	 **/
	Long getMimeTypeIdForExtension(String extension);
	
	/* Daca nu se gaseste un tip cu id-ul dat ca parametru, se returneaza null. */
	MimeType find(Long id);
	
	void deleteMimeType(Long mimeType);
	
}