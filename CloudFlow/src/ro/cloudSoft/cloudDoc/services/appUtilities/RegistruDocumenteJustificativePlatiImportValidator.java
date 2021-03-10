package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class RegistruDocumenteJustificativePlatiImportValidator {
	
	public void validate(RegistruDocumenteJustificativePlatiAllXlsModel data, String rootDirectoryAttachsPath) throws AppUtilitiesException {
		
		List<String> errors = new ArrayList<>();
		
		List<RegistruDocumenteJustificativePlatiAtasamentXlsModel> atasamente = data.getAtasamente();
		if (CollectionUtils.isNotEmpty(atasamente)) {
			validateAtasamente(atasamente, rootDirectoryAttachsPath, errors);
		}
		
		if (!errors.isEmpty()) {
			throw new AppUtilitiesException(errors);
		}
	}
	
	
	private void validateAtasament(RegistruDocumenteJustificativePlatiAtasamentXlsModel atasament, String rootDirectoryAttachsPath, List<String> errors) {
		
		String errorPrefix = "[" + atasament.getNrDocumenteJustificativePlati() + "] Atasamentul [" + atasament.getNumeFisier() + "]";

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
	
	private void validateAtasamente(List<RegistruDocumenteJustificativePlatiAtasamentXlsModel> atasamente, String rootDirectoryAttachsPath, List<String> errors) {
		for (RegistruDocumenteJustificativePlatiAtasamentXlsModel atasament : atasamente) {
			validateAtasament(atasament, rootDirectoryAttachsPath, errors);
		}
	}

}
