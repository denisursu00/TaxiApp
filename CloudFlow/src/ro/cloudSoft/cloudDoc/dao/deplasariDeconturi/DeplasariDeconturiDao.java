package ro.cloudSoft.cloudDoc.dao.deplasariDeconturi;

import java.util.Date;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DeplasariDeconturiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuialaReprezentantArbAvansPrimit;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuialaReprezentantArbDiurna;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiRePrezentantArbRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb.CheltuieliArbSiReprezentantArbReportFilterModel;

public interface DeplasariDeconturiDao {

	DeplasareDecont findById(Long deplasareDecontId);

	Long saveDeplasareDecont(DeplasareDecont deplasareDecont);

	List<Integer> getYearsOfExistingDeconturi();

	List<DeplasareDecont> getAllDeplasariDeconturiByYear(Integer year);
	
	void deleteById(Long id);
	
	List<CheltuialaArb> getCheltuieliArbByDeplsareDecontId(Long deplasareDecontId);
	
	CheltuialaArb findCheltuialaArbById(Long id);
	
	void removeCheltuielaArb(List<Long> cheltuialiArbIds);
	
	List<CheltuialaReprezentantArb> getCheltuieliReprezentantArbByDeplsareDecontId(Long deplasareDecontId);
	
	CheltuialaReprezentantArb findCheltuialaReprezentantArbById(Long id);
	
	void removeCheltuielaReprezentantArb(List<Long> cheltuialiReprezentantArbIds);

	List<String> getListaNrDeciziiAlocateByReprezentantId(Long reprezentantArbId);
	
	List<String> getAllDistinctTitulariWithDecont();
	
	Set<String> getAllDistinctOrase();
	
	Set<String> getAllDistinctDenumiriInstitutii();
	
	Set<String> getAllDistinctDenumiriComitete();
	
	List<CheltuieliArbSiRePrezentantArbRowModel> getCheltuieliArbByCheltuieliArbSiReprezentantArbReportFilterModel(CheltuieliArbSiReprezentantArbReportFilterModel filter);
	
	List<CheltuieliArbSiRePrezentantArbRowModel> getCheltuieliReprezentantArbByCheltuieliArbSiReprezentantArbReportFilterModel(CheltuieliArbSiReprezentantArbReportFilterModel filter);
	
	CheltuialaReprezentantArbDiurna getDiurnaReprezentantArbByCheltuieliArbSiReprezentantArbReportFilterModel(CheltuieliArbSiReprezentantArbReportFilterModel filter);
	
	CheltuialaReprezentantArbAvansPrimit getAvansReprezentantArbByCheltuieliArbSiReprezentantArbReportFilterModel(CheltuieliArbSiReprezentantArbReportFilterModel filter);
	
	List<String> getAllNumarDecizieByDate(Date startDate, Date endDate);

	List<DeplasareDecont> getDeplasariDeconturiByTitularAndDataDecont(String titular, Date dataDecont, Date dataDecontDeLa, Date dataDecontPanaLa);

	List<DeplasareDecont> getAllByDeplasariDeconturiReportFilterModel(DeplasariDeconturiReportFilterModel filter);

}
