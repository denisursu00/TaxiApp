package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.appUtilities.DSPProiectXlsModel.DSPGradDeImportanta;
import ro.cloudSoft.cloudDoc.services.appUtilities.DSPTaskXlsModel.StatusTask;

public class DSPImportValidator {
	
	private UserPersistencePlugin userPersistencePlugin;
	private NomenclatorValueDao nomenclatorValueDao;
	
	public DSPImportValidator(UserPersistencePlugin userPersistencePlugin, NomenclatorValueDao nomenclatorValueDao) {
		this.userPersistencePlugin = userPersistencePlugin;
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
	
	public void validate(DSPAllXlsModel data, String rootDirectoryAttachsPath) throws AppUtilitiesException {
		
		List<String> errors = new ArrayList<>();
		
		List<DSPProiectXlsModel> proiecte = data.getProiecte();
		if (CollectionUtils.isNotEmpty(proiecte)) {
			for (DSPProiectXlsModel proiect : proiecte) {
				validateProiect(proiect, errors);
				if (StringUtils.isNotBlank(proiect.getAbreviereProiect())) {
					validateComisiiGLproiect(data.getComisiiGLImplicateOfProiect(proiect.getAbreviereProiect()), errors);
					validateParticipantiProiect(data.getParticipantiOfProiect(proiect.getAbreviereProiect()), errors);
					validateTaskuri(data.getTaskuriOfProiect(proiect.getAbreviereProiect()), rootDirectoryAttachsPath, errors);
				}				
			}
		}
		
		if (!errors.isEmpty()) {
			throw new AppUtilitiesException(errors);
		}
	}
	
	private void validateProiect(DSPProiectXlsModel proiect, List<String> errors) {
		
		String errorPrefix = "[" + proiect.getAbreviereProiect() + "]";
		
		String domeniuBancar = proiect.getDomeniuBancar();
		checkNomenclatorValue(NomenclatorConstants.DOMENIU_BANCAR_NOMENCLATOR_CODE, NomenclatorConstants.DOMENIU_BANCAR_ATTRIBUTE_KEY_DENUMIRE, domeniuBancar, 
				errors, errorPrefix + " Nomenclator Domeniu bancar: [" + domeniuBancar+ "] -> ");
		
		String incadrareProiect = proiect.getIncadrareProiect();
		checkNomenclatorValue(NomenclatorConstants.INCADRARI_PROIECTE_NOMENCLATOR_CODE, NomenclatorConstants.INCADRARI_PROIECTE_ATTRIBUTE_KEY_INCADRARE_PROIECT, incadrareProiect, 
				errors, errorPrefix + " Nomenclator Incadrare proiect: [" + incadrareProiect + "] -> ");
		
		DSPGradDeImportanta gradImportanta = proiect.getGradImportanta();
		checkNomenclatorValue(NomenclatorConstants.NOMENCLATOR_CODE_IMPORTANTA_PROIECTE, 
				NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_GRAD_IMPORTANTA, gradImportanta.getGrad(), 
				NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA, gradImportanta.getValoare(), 
				errors, errorPrefix + " Nomenclator Grad importanta: grad[" + gradImportanta.getGrad() +"] si valoare[" + gradImportanta.getValoare() + "] -> ");
		
		checkUser(proiect.getResponsabilProiect(), errors, errorPrefix + " Responsabil [" + proiect.getResponsabilProiect() + "] -> ");
		
		if (!proiect.getDataSfarsit().after(proiect.getDataInceput())) {
			errors.add(errorPrefix + "'Data sfarsit proiect' trebuie sa fie mai mare decat 'Data inceput proiect'");
		}
	}
	
	private void checkNomenclatorValue(String nomenclatorCode, String attributeKey, String attributeValue, List<String> errors, String errorMessagePrefix) {
		List<NomenclatorValue> nvs = nomenclatorValueDao.findByNomenclatorCodeAndAttribute(nomenclatorCode, attributeKey, attributeValue);
		if (CollectionUtils.isEmpty(nvs)) {
			errors.add(errorMessagePrefix + "NU EXISTA VALOARE");
		} else {
			if (nvs.size() > 1) {
				errors.add(errorMessagePrefix + "EXISTA MAI MULT DE O VALOARE");
			}
		}		
	}
	
	private void checkUser(String username, List<String> errors, String errorMessagePrefix) {
		User user = userPersistencePlugin.getUserByUsername(username);
		if (user == null) {
			errors.add(errorMessagePrefix + "NU EXISTA");
		}
	}
	
	
	private void checkNomenclatorValue(String nomenclatorCode, String attributeKey1, String attributeValue1, String attributeKey2, String attributeValue2, List<String> errors, String errorMessagePrefix) {
		List<NomenclatorValue> nvs = nomenclatorValueDao.findByNomenclatorCodeAndAttributes(nomenclatorCode, attributeKey1, attributeValue1, attributeKey2, attributeValue2);
		if (CollectionUtils.isEmpty(nvs)) {
			errors.add(errorMessagePrefix + "NU EXISTA VALOARE");
		} else {
			if (nvs.size() > 1) {
				errors.add(errorMessagePrefix + "EXISTA MAI MULT DE O VALOARE");
			}
		}		
	}
	
	private void validateParticipantiProiect(List<DSPParticipantXlsModel> participantiProiect, List<String> errors) {
		for (DSPParticipantXlsModel participant : participantiProiect) {
			validateParticipant(participant, errors);
		}
	}

	private void validateParticipant(DSPParticipantXlsModel participant, List<String> errors) {
		checkUser(participant.getParticipant(), errors, "[" + participant.getAbreviereProiect() + "] Participant [" +participant.getParticipant()+ "] -> ");
	} 
	
	private void validateComisiiGLproiect(List<DSPComisieGLXlsModel> comisiiGLProiect, List<String> errors) {
		for (DSPComisieGLXlsModel comisieGL : comisiiGLProiect) {
			validateComisieGL(comisieGL, errors);
		}
	}
	
	private void validateComisieGL(DSPComisieGLXlsModel comisieGL, List<String> errors) {
		String comisia = comisieGL.getDenumireComisieGL();
		checkNomenclatorValue(NomenclatorConstants.COMISII_SAU_GL_NOMENCLATOR_CODE, NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE, comisia, 
				errors, "[" + comisieGL.getAbreviereProiect() + "] Comisia[" + comisia+ "] -> ");
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
	
	private void validateTaskuri(List<DSPTaskXlsModel> comisieGLProiect, String rootDirectoryAttachsPath, List<String> errors) {
		for (DSPTaskXlsModel comisieGL : comisieGLProiect) {
			validateTask(comisieGL, rootDirectoryAttachsPath, errors);
		}
	}
}
