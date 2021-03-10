package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;;

public class RegistruIntrariImportValidator {

	private UserPersistencePlugin userPersistencePlugin;
	private NomenclatorValueDao nomenclatorValueDao;
	private ProjectDao projectDao;
	
	public RegistruIntrariImportValidator(UserPersistencePlugin userPersistencePlugin, NomenclatorValueDao nomenclatorValueDao, ProjectDao projectDao) {
		this.userPersistencePlugin = userPersistencePlugin;
		this.nomenclatorValueDao = nomenclatorValueDao;
		this.projectDao =projectDao;
	}
	
	public void validate(RegistruIntrariAllXlsModel data, String rootDirectoryAttachsPath) throws AppUtilitiesException {
		
		List<String> errors = new ArrayList<>();
		
		List<RegistruIntrariAtasamentXlsModel> atasamente = data.getAtasamente();
		if (CollectionUtils.isNotEmpty(atasamente)) {
			validateAtasamente(atasamente, rootDirectoryAttachsPath, errors);
		}
		
		if (!errors.isEmpty()) {
			throw new AppUtilitiesException(errors);
		}
	}

	private void validateAtasament(RegistruIntrariAtasamentXlsModel atasament, String rootDirectoryAttachsPath, List<String> errors) {
		
		String errorPrefix = "[" + atasament.getNrRegistruIntrari() + "] Atasamentul [" + atasament.getNumeFisier() + "]";

		if (StringUtils.isNoneBlank(atasament.getNumeFisier())) {
			try {
				File attach = new File(rootDirectoryAttachsPath + File.separator + atasament.getNumeFisier());
				if (!attach.exists()) {
					errors.add(errorPrefix + " Atasament[" + atasament.getNumeFisier() + "] -> NU EXISTA");
				}
			} catch (Exception e) {
				errors.add(errorPrefix + " Atasament[" + atasament.getNumeFisier() + "] -> EROARE VALIDARE - [" + e.getMessage()+"]");
			}
			
		}
		
	}
	
	private void validateAtasamente(List<RegistruIntrariAtasamentXlsModel> atasamente, String rootDirectoryAttachsPath, List<String> errors) {
		for (RegistruIntrariAtasamentXlsModel atasament : atasamente) {
			validateAtasament(atasament, rootDirectoryAttachsPath, errors);
		}
	}

}
