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
import ro.cloudSoft.cloudDoc.dao.registruIntrariIesiri.RegistruIntrariIesiriDao;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiri;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriAtasament;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.security.SecurityManagerFactory;

public class RegistruIesiriImporter {
	private SecurityManagerFactory securityManagerFactory;
	private BusinessConstants businessConstants;
	private UserPersistencePlugin userPersistencePlugin;
	private NomenclatorValueDao nomenclatorValueDao;
	private ProjectDao projectDao;
	private RegistruIntrariIesiriDao registruIntrariIesiriDao;
	
	public void doImport(String excelFilePath) throws AppUtilitiesException {
		validateInputs(excelFilePath);
		File excelFile = new File(excelFilePath);
		if (!excelFile.exists()) {
			throw new AppUtilitiesException("Fisierul excel nu exista");
		}
		
		RegistruIesiriImportParser parser = new RegistruIesiriImportParser(excelFilePath);
		RegistruIesiriAllXlsModel data = parser.parse();
		
		RegistruIesiriImportValidator validator = new RegistruIesiriImportValidator();
		String atasamenteRootDir = excelFile.getParentFile().getAbsolutePath() + File.separator + "atasamente"; 
		validator.validate(data, atasamenteRootDir);
				
		saveData(data, atasamenteRootDir);
	}
	
	@Transactional
	private void saveData(RegistruIesiriAllXlsModel data, String attachemntsRootDirPath) throws AppUtilitiesException {
		
		List<RegistruIesiriAtasamentXlsModel> atasamnte = data.getAtasamente();
		
		if (CollectionUtils.isNotEmpty(atasamnte)) {
			for (RegistruIesiriAtasamentXlsModel atasament : atasamnte) {
				
				RegistruIesiri registruIesiri = registruIntrariIesiriDao.getIesireByNrInregistrare(atasament.getNrRegistruIesiri());
				
				if (registruIesiri != null) {
					File attachFile = new File(attachemntsRootDirPath + File.separator + atasament.getNumeFisier());
					
					RegistruIesiriAtasament atasamentToSave = new RegistruIesiriAtasament();
					atasamentToSave.setFileName(atasament.getNumeFisier());
					atasamentToSave.setRegistruIesiri(registruIesiri);
					try {
						atasamentToSave.setFilecontent(FileUtils.readFileToByteArray(attachFile));
					} catch (IOException e) {
						throw new AppUtilitiesException("Registru iesiri [" + atasament.getNrRegistruIesiri() + "] -> eroare la citire atasament [" + atasament.getNumeFisier() + "]");
					}
					
					registruIntrariIesiriDao.saveRegistruIesiriAtasamente(atasamentToSave);
				} else {
					throw new AppUtilitiesException("Registru iesiri [" + atasament.getNrRegistruIesiri() + "] -> NU EXISTA ");
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
}
