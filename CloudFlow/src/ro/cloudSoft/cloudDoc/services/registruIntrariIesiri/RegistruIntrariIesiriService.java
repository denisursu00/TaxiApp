package ro.cloudSoft.cloudDoc.services.registruIntrariIesiri;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariViewModel;
import ro.cloudSoft.common.utils.PagingList;

public interface RegistruIntrariIesiriService {
	
	List<RegistruIntrariModel> getAllIntrari();

	List<RegistruIntrariViewModel> getAllRegistruIntrariViewModels();

	List<Integer> getYearsOfExistingIntrari();

	List<Integer> getYearsOfExistingIesiri();

	List<RegistruIntrariViewModel> getAllRegistruIntrariViewModelsByYear(Integer year);

	void saveRegistruIntrari(RegistruIntrariModel registruIntrariModel, String userName);

	PagingList<RegistruIesiriViewModel> getRegistruIesiriViewModelByFilter(RegistruIesiriFilterModel filter);
		
	PagingList<RegistruIntrariViewModel> getRegistruIntrariByFilter(RegistruIntrariFilter registruIntrariFilter);
	
	List<String> getNrInregistrareOfMappedRegistriIntrariByIesireId(Long registruIesiriId);
	
	List<String> getNrInregistrareOfMappedRegistriIesiriByIntrareId(Long registruIntrariId);
	
	void saveRegistruIesiri(RegistruIesiriModel registruIesiriModel, String userName);
	
	RegistruIesiriModel getRegistruIesiri(Long registruIesiriId);

	List<RegistruIesiriViewModel> getAllRegistruIesiriViewModels();

	RegistruIntrariModel getRegistruIntrariById(Long registruId);

	void cancelRegistruIntrari(Long registruIntrariId, String motivAnulare);

	void cancelRegistruIesiri(Long registruIesiriId, String motivAnulare);

	boolean isRegistruIesiriCanceled(Long registruIesiriId);
	
	boolean isRegistruIesiriFinalized(Long registruIesiriId);

	boolean isRegistruIntrariCanceled(Long registruIntrariId);
	
	boolean isRegistruIntrariFinalized(Long registruIntrariId);

	void finalizareRegistruIntrari(Long registruIntrariId);

	void finalizareRegistruIesiri(Long registruIesiriId);
	
	DownloadableFile downloadAtasamentOfRegistruIntrariById(Long atasamentId);

	DownloadableFile downloadAtasamentOfRegistruIesiriById(Long atasamentId);
	
	public Long getLastNumarInregistrareByTipRegistruAndYear(String tipRegistru, Integer year);
	
	boolean isSubactivityUsed(Long subactivityId);
}
