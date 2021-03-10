package ro.cloudSoft.cloudDoc.dao.registruDocumenteJustificativePlati;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlati;
import ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiAtasament;

public interface RegistruDocumenteJustificativePlatiDao {

	public List<RegistruDocumenteJustificativePlati> getAll();
	
	public RegistruDocumenteJustificativePlati find(Long id);

	public Long save(RegistruDocumenteJustificativePlati documentJustificativPlati);

	public void delete(RegistruDocumenteJustificativePlati documentJustificativPlati);

	public List<Integer> getYearsWithInregistrariDocumenteJustificativePlati();

	public List<RegistruDocumenteJustificativePlati> getAllByYear(Integer year);

	public RegistruDocumenteJustificativePlatiAtasament findAtasamentOfRegistruIesiriById(Long atasamentId);

	public void deleteAtasamenteOfRegistruDocumenteJustificativePlatiByIds(List<Long> atasamenteIdsToDelete);

	public Long saveAtasamentOfRegistruDocumentJustificativPlati(RegistruDocumenteJustificativePlatiAtasament atasament);
	
	public List<Long> getAttachmentIds(Long registruDocumenteJustificativeId);

	RegistruDocumenteJustificativePlati getByNrInregistrare(String nrInregistrare);
}
