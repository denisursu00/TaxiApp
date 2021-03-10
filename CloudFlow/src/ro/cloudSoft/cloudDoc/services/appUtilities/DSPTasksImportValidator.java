package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.appUtilities.DSPTaskXlsModel.StatusTask;

public class DSPTasksImportValidator {
	
	private UserPersistencePlugin userPersistencePlugin;
	
	public DSPTasksImportValidator(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
	
	public void validate(List<DSPTaskXlsModel> data, String rootDirectoryAttachsPath) throws AppUtilitiesException {
		
		List<String> errors = new ArrayList<>();
		
		if (CollectionUtils.isNotEmpty(data)) {
			for (DSPTaskXlsModel task : data) {
				validateTask(task, rootDirectoryAttachsPath, errors);
			}
		}
		
		if (!errors.isEmpty()) {
			throw new AppUtilitiesException(errors);
		}
	}
	
	private void checkUser(String username, List<String> errors, String errorMessagePrefix) {
		User user = userPersistencePlugin.getUserByUsername(username);
		if (user == null) {
			errors.add(errorMessagePrefix + "NU EXISTA");
		}
	}

	private void validateTask(DSPTaskXlsModel task, String rootDirectoryAttachsPath, List<String> errors) {
		
		String errorPrefix = "[" + task.getAbreviereProiect() + "] Task[" + task.getNumeTask() + "]";
		checkUser(task.getResponsabilActivitate(), errors, errorPrefix + " Responsabil [" + task.getResponsabilActivitate() + "] -> ");
		
		if (StringUtils.isNotBlank(task.getParticipareLa()) && task.getParticipareLa().equals("Altele")) {
			if (StringUtils.isBlank(task.getExplicatii())) {
				errors.add(errorPrefix + " -> 'Explicatii' trebuie completat cand 'Participare la' este completat");
			}
		}
		
		if (task.getStatus().equals(StatusTask.FINALIZED)) {
			if (task.getDataFinalizare() == null) {
				errors.add(errorPrefix + " -> 'Data finalizare task' trebuie completata daca status este " + StatusTask.FINALIZED);
			} else {
				if (task.getDataFinalizare().before(task.getDataInceput())) {
					errors.add(errorPrefix + " -> 'Data finalizare task' trebuie sa fie mai mare sau egala decat 'Data inceput task'");
				}
			}
		} else {
			if (task.getDataFinalizare() != null) {
				errors.add(errorPrefix + " -> 'Data finalizare task' NU trebuie completata daca status este diferit de " + StatusTask.FINALIZED);
			}
			if (CollectionUtils.isNotEmpty(task.getNumeAtasamente())) {
				errors.add(errorPrefix + " -> Atasamentele nu sunt permise cand status este diferit de " + StatusTask.FINALIZED);
			}
		}
		
		if (CollectionUtils.isNotEmpty(task.getNumeAtasamente())) {
			for (String attachmentName : task.getNumeAtasamente()) {
				try {
					File attach = new File(rootDirectoryAttachsPath + File.separator + task.getAbreviereProiect() + File.separator + attachmentName);
					if (!attach.exists()) {
						errors.add(errorPrefix + " Atasament[" +attachmentName+ "] -> NU EXISTA");
					}
				} catch (Exception e) {
					errors.add(errorPrefix + " Atasament[" +attachmentName+ "] -> EROARE VALIDARE - [" + e.getMessage()+"]");
				}
			}
		}
		
		if (task.getDataSfarsit().before(task.getDataInceput())) {
			errors.add(errorPrefix + " -> 'Data sfarsit task' trebuie sa fie mai mare sau egala decat 'Data inceput task'");
		}
		
	}
}
