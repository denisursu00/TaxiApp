package ro.cloudSoft.cloudDoc.dao.alteDeconturi;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturi;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturiCheltuiala;
import ro.cloudSoft.cloudDoc.domain.alteDeconturi.AlteDeconturiCheltuialaAtasament;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DecontCheltuieliAlteDeconturiReportFilterModel;

public interface AlteDeconturiDao {

	List<Integer> getYearsOfExistingDeconturi();

	List<AlteDeconturi> getAllAlteDeconturiByYear(Integer year);

	AlteDeconturi findDecontById(Long decontId);

	Long saveAlteDeconturi(AlteDeconturi alteDeconturi);

	AlteDeconturiCheltuiala findCheltuialaById(Long cheltuieliId);

	void deleteCheltuieli(List<Long> cheltuieliIds);
	
	void deleteDecontById(Long decontId);
	
	AlteDeconturiCheltuialaAtasament findAtasamentOfCheltuialaById(Long atasamentId);
	
	void deleteAtasamenteOfCheltuialaByIds(List<Long> atasamenteIds);
	
	List<AlteDeconturi> getByDecontCheltuieliAlteDeconturiReportFilterModel(DecontCheltuieliAlteDeconturiReportFilterModel filter);

	List<String> getAllDistinctTitulari();
}
