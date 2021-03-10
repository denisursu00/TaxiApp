package ro.cloudSoft.cloudDoc.services.deplasariDeconturi;

import java.text.ParseException;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.deciziiDeplasari.DocumentDecizieDeplasareModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb.deciziiDeplasari.NumarDecizieDeplasareModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.DeplasareDecontModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.DeplasareDecontViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiReprezentantArbReportDateFilterModel;

public interface DeplasariDeconturiService {

	void saveDeplasareDecont(DeplasareDecontModel deplasareDecontModel);

	List<NumarDecizieDeplasareModel> getDeciziiAprobateNealocateForReprezentantForDeplasareDecont(Long reprezentantArbId,
			Long deplasareDecontId, SecurityManager userSecurity);

	DocumentDecizieDeplasareModel getDocumentDecizieDeplasare(String documentId, String documentLocationRealName, 
			SecurityManager userSecurity) throws PresentationException, AppException, ParseException;

	List<Integer> getYearsOfExistingDeplasariDeconturi();

	List<DeplasareDecontViewModel> getAllDeplasariDeconturiViewModelsByYear(Integer year);

	boolean isDeplasareDecontCanceled(Long deplasareDecontId);

	DeplasareDecontModel getDeplasareDecontById(Long deplasareDecontId);
	
	void cancelDeplasareDecont(Long deplasareDecontId, String motiv);
	
	void removeDeplasareDecont(Long deplasareDecontId) throws AppException;
	
	void finalizeDeplasareDecont(Long deplasareDecontId) throws AppException;
	
	 List<String> getAllDistinctTitulari();
	 
	 List<String> getAllNumarDeciziiByFilter(CheltuieliArbSiReprezentantArbReportDateFilterModel filter);
}
