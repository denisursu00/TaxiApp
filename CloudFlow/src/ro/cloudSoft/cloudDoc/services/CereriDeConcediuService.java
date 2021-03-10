package ro.cloudSoft.cloudDoc.services;

import java.util.Collection;
import java.util.Date;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.Concediu;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

/**
 * , Dan Cirja
 */
public interface CereriDeConcediuService {

	void anuleaza(String documentLocationRealName, String documentId,
		SecurityManager userSecurity) throws AppException;
	
	void saveOrUpdateConcediuForCerere(Document document, String documentId, SecurityManager userSecurity) throws AppException;
	
	boolean isDocumentCerereDeConcediu(Long documentTypeId);
	
	/**
	 * Sterge concediul asociat cererii, DACA documentul cu ID-ul dat este cerere de concediu.
	 */
	void deleteConcediuForCerere(String documentId);
	
	boolean isCerereRespinsa(Document document);
	
	boolean isCerereAnulata(Document document);

	/**
	 * Returneaza concediul pentru documentul dat.
	 * Daca nu se gaseste concediul, atunci va returna null.
	 */
	Concediu getConcediuForCerere(Document document);
	
	/**
	 * Returneaza ID-urile utilizatorilor ce au perioada de concediu 
	 * inclusa in intervalul inchis [dataInceput, dataSfarsit].
	 */
	Collection<Long> getIdsForUsersInConcediu(Date dataInceput, Date dataSfarsit);
	
	/**
	 * Actualizeaza cererile de concediu care nu contin valoarea metadatei pentru anulare, adaugand valoarea negativa.
	 */
	void updateCereriWithMissingMetadataValueForAnulare() throws AppException;
	
	/**
	 * Extrage datele necesare pentru concedii din cereri si actualizeaza lista de concedii.
	 * Se vor lua in considerare doar cererile aprobate sau in curs de aprobare, cele respinse sau anulate vor fi ignorate.
	 * Cererile care nu au completate toate metadatele necesare vor fi ignorate.
	 */
	void extractAndUpdateConcediiFromCereri() throws AppException;
}