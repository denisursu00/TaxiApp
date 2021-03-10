package ro.cloudSoft.cloudDoc.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.Concediu;



/**
 */
public interface ConcediuDao {
	
	void save(Concediu concediu);
	
	/**
	 * Sterge concediul asociat cererii, DACA documentul cu ID-ul dat este cerere de concediu.
	 */
	void deleteByDocumentId(String documentId);

	/**
	 * Returneaza lista id-urilor de utilizatori ce au perioada de concediu 
	 * inclusa in intervalul inchis [dataInceput, dataSfarsit].
	 */
	List<Long> getSolicitantsIdsByPeriod(Date dataInceput, Date dataSfarsit);

	void saveAll(Collection<Concediu> concedii);
	
	/**
	 * Returneaza concediul pentru documentul cu ID-ul dat.
	 * Daca nu se gaseste concediul, atunci va returna null.
	 */
	Concediu getForDocument(String documentId);
	
	Collection<Long> getSolicitantIdsForConcediiWithCerereAprobataThatContainDay(Date day);
}