package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.dao.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiDao;
import ro.cloudSoft.cloudDoc.dao.registruIntrariIesiri.RegistruIntrariIesiriDao;
import ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlati;
import ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiAtasament;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.security.SecurityManagerFactory;

public class RegistruDocumenteJustificativePlatiImporter {
	private SecurityManagerFactory securityManagerFactory;
	private BusinessConstants businessConstants;
	private UserPersistencePlugin userPersistencePlugin;
	private NomenclatorValueDao nomenclatorValueDao;
	private ProjectDao projectDao;
	private RegistruIntrariIesiriDao registruIntrariIesiriDao;
	private RegistruDocumenteJustificativePlatiDao registruDocumenteJustificativePlatiDao;

	public void doImport(String excelFilePath) throws AppUtilitiesException {
		validateInputs(excelFilePath);
		File excelFile = new File(excelFilePath);
		if (!excelFile.exists()) {
			throw new AppUtilitiesException("Fisierul excel nu exista");
		}

		RegistruDocumenteJustificativePlatiImportParser parser = new RegistruDocumenteJustificativePlatiImportParser(
				excelFilePath);
		RegistruDocumenteJustificativePlatiAllXlsModel data = parser.parse();

		RegistruDocumenteJustificativePlatiImportValidator validator = new RegistruDocumenteJustificativePlatiImportValidator();
		String atasamenteRootDir = excelFile.getParentFile().getAbsolutePath() + File.separator + "atasamente";
		validator.validate(data, atasamenteRootDir);

		saveData(data, atasamenteRootDir);
	}

	@Transactional
	private void saveData(RegistruDocumenteJustificativePlatiAllXlsModel data, String attachemntsRootDirPath)
			throws AppUtilitiesException {

		List<RegistruDocumenteJustificativePlatiAtasamentXlsModel> atasamnte = data.getAtasamente();

		if (CollectionUtils.isNotEmpty(atasamnte)) {
			for (RegistruDocumenteJustificativePlatiAtasamentXlsModel atasament : atasamnte) {

				RegistruDocumenteJustificativePlati registruDocumenteJustificativePlati = registruDocumenteJustificativePlatiDao.getByNrInregistrare(atasament.getNrDocumenteJustificativePlati());

				if (registruDocumenteJustificativePlati != null) {
					File attachFile = new File(attachemntsRootDirPath + File.separator + atasament.getNumeFisier());
	
					RegistruDocumenteJustificativePlatiAtasament atasamentToSave = new RegistruDocumenteJustificativePlatiAtasament();
					atasamentToSave.setFileName(atasament.getNumeFisier());
					atasamentToSave.setRegistruDocumenteJustificativePlati(registruDocumenteJustificativePlati);
					try {
						atasamentToSave.setFilecontent(FileUtils.readFileToByteArray(attachFile));
					} catch (IOException e) {
						throw new AppUtilitiesException("Registru ducumente justificative plati [" + atasament.getNrDocumenteJustificativePlati() + "] -> eroare la citire atasament [" + atasament.getNumeFisier() + "]");
					}
	
					registruDocumenteJustificativePlatiDao.saveAtasamentOfRegistruDocumentJustificativPlati(atasamentToSave);
				} else {
					throw new AppUtilitiesException("Registru ducumente justificative plati [" + atasament.getNrDocumenteJustificativePlati() + "] -> NU EXISTA ");
				}
			}
		}
	}

	private void validateInputs(String excelFilePath) throws AppUtilitiesException {
		List<String> errorMessages = new ArrayList<>();
		if (StringUtils.isBlank(excelFilePath)) {
			errorMessages.add("Calea fisierului excel nu poate fi null");
		}
		File f = new File(excelFilePath);
		if (!f.exists()) {
			errorMessages.add("Fisierul excel nu exista");
		}
		if (CollectionUtils.isNotEmpty(errorMessages)) {
			throw new AppUtilitiesException(errorMessages);
		}
	}

	public void setSecurityManagerFactory(SecurityManagerFactory securityManagerFactory) {
		this.securityManagerFactory = securityManagerFactory;
	}

	public void setBusinessConstants(BusinessConstants businessConstants) {
		this.businessConstants = businessConstants;
	}

	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}

	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public void setRegistruIntrariIesiriDao(RegistruIntrariIesiriDao registruIntrariIesiriDao) {
		this.registruIntrariIesiriDao = registruIntrariIesiriDao;
	}

	public void setRegistruDocumenteJustificativePlatiDao(
			RegistruDocumenteJustificativePlatiDao registruDocumenteJustificativePlatiDao) {
		this.registruDocumenteJustificativePlatiDao = registruDocumenteJustificativePlatiDao;
	}

}
