package ro.cloudSoft.cloudDoc.services.registruDocumenteJustificativePlati;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiModel;

public interface RegistruDocumenteJustificativePlatiService {

	List<RegistruDocumenteJustificativePlatiModel> getAllDocumenteJustificativePlati();

	RegistruDocumenteJustificativePlatiModel getDocumentJustificativPlati(Long documentJustificativPlatiId);

	void saveDocumentJustificativPlati(RegistruDocumenteJustificativePlatiModel documentJustificativPlatiModel, SecurityManager securityManager);

	void cancelDocumentJustificativPlati(RegistruDocumenteJustificativePlatiModel documentJustificativPlatiModel);

	List<RegistruDocumenteJustificativePlatiModel> getAllByYear(Integer year);

	List<Integer> getYearsWithInregistrariDocumenteJustificativePlati();

	DownloadableFile downloadAtasamentOfRegistruDocumenteJustificativePlatiById(Long atasamentId);

}
