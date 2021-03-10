package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class RegistruIesiriImportValidator {

	public void validate(RegistruIesiriAllXlsModel data, String rootDirectoryAttachsPath) throws AppUtilitiesException {
		
		List<String> errors = new ArrayList<>();
		
		List<RegistruIesiriAtasamentXlsModel> atasamente = data.getAtasamente();
		if (CollectionUtils.isNotEmpty(atasamente)) {
			validateAtasamente(atasamente, rootDirectoryAttachsPath, errors);
		}
		
		if (!errors.isEmpty()) {
			throw new AppUtilitiesException(errors);
		}
	}
	
	
	private void validateAtasament(RegistruIesiriAtasamentXlsModel atasament, String rootDirectoryAttachsPath, List<String> errors) {
		
		String errorPrefix = "[" + atasament.getNrRegistruIesiri() + "] Atasamentul [" + atasament.getNumeFisier() + "]";

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
	
	private void validateAtasamente(List<RegistruIesiriAtasamentXlsModel> atasamente, String rootDirectoryAttachsPath, List<String> errors) {
		for (RegistruIesiriAtasamentXlsModel atasament : atasamente) {
			validateAtasament(atasament, rootDirectoryAttachsPath, errors);
		}
	}

}
