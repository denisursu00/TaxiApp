package ro.cloudSoft.cloudDoc.services.alteDeconturi;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi.AlteDeconturiViewModel;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public interface AlteDeconturiService {

	List<Integer> getYearsOfExistingDeconturi();

	List<AlteDeconturiViewModel> getAllAlteDeconturiViewModelsByYear(Integer year);

	AlteDeconturiModel getDecontById(Long decontId);

	boolean isDecontCanceled(Long decontId);

	void saveAlteDeconturi(AlteDeconturiModel alteDeconturiModel, SecurityManager userSecurity);

	void deleteDecontById(Long decontId);

	void cancelDecont(Long decontId, String motivAnulare);

	void finalizeDecontById(Long decontId);

	boolean isDecontFinalized(Long decontId);
	
	DownloadableFile downloadAtasamentOfCheltuialaById(Long attachmentId);
	
	 List<String> getAllDistinctTitulari();
	
}
