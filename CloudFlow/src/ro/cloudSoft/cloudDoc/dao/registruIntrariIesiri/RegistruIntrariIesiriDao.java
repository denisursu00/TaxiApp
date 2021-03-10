package ro.cloudSoft.cloudDoc.dao.registruIntrariIesiri;

import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiri;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriAtasament;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriDestinatar;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIntrari;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIntrariAtasament;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.documenteTrimiseDeArb.DocumenteTrimiseDeArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIesiriFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectRegistruIntrariIesiriReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaPeMembriiArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportFilterModel;

public interface RegistruIntrariIesiriDao {

	List<RegistruIntrari> getAllIntrari();

	List<Integer> getYearsOfExistingIntrari();
	
	List<Integer> getYearsOfExistingIesiri();

	List<RegistruIntrari> getAllIntrariByYear(Integer year);
	
	List<RegistruIesiri> getAllIesiriByYear(Integer year);
	
	List<RegistruIesiri> getIesiriByFilter(RegistruIesiriFilterModel filter, String docTypeCodEchivalent);
	
	List<RegistruIntrari> getAllRegistruIntrariByFilter(RegistruIntrariFilter filter, String docTypeCodEchivalent);

	RegistruIntrari findIntrare(Long registruIntrariId);

	RegistruIesiri findIesire(Long registruIesiriId);

	Long saveRegistruIesiri(RegistruIesiri registruIesiri);
	
	Long saveRegistruIntrari(RegistruIntrari registruIntrari);
	
	List<RegistruIesiri> getAllRegistruIesiri();

	RegistruIntrari getById(Long registruId);

	RegistruIesiriDestinatar findRegistruIesireDestinatar(Long destinatarId);
	
	public RegistruIesiriDestinatar getRegistruIesireDestinatarByIntrareId(Long intrareId);
	
	void deleteRegistruIesiriDestinatari(List<Long> destinatariIds);

	void deleteRegistruIntrariAtasamente(List<Long> atasamenteIdsToDelete);
	
	void deleteRegistruIesiriAtasamente(List<Long> atasamenteIdsToDelete);

	Long saveRegistruIesiriAtasamente(RegistruIesiriAtasament regIesiriAtasament);

	Long saveRegistruIntrariAtasamente(RegistruIntrariAtasament regIntrariAtasament);

	RegistruIntrariAtasament findRegistruIntrariAtasamentById(Long id);
	
	RegistruIesiriAtasament findRegistruIesiriAtasamentById(Long id);
	
	List<RegistruIesiri> getIesiriByDocumenteTrimiseDeArbReportFilterModel(DocumenteTrimiseDeArbReportFilterModel filter);
	
	Long saveRegistruIesiriDestinatari(RegistruIesiriDestinatar regIesiriDestinatar);

	List<ActiuniPeProiectRegistruIntrariIesiriReportModel> getAllRegistruIntrariByActiuniPeProiectReportFilterModel(ActiuniPeProiectReportFilterModel filter);
	
	List<ActiuniPeProiectRegistruIntrariIesiriReportModel> getAllRegistruIesiriByActiuniPeProiectReportFilterModel(ActiuniPeProiectReportFilterModel filter);
	
	Map<String, Long> getNumarInterogariMembriiArbByBancaAndComisie(List<Long> institutiiMembriiArbIds);

	List<RegistruIesiri> getAllRegistruIesiriByRaspunsuriBanciReportFilterModel( RaspunsuriBanciReportFilterModel filter);
	
	List<SimpleListItemModel> getAllDenumiriBanciFromRegistruIesiriDestinatariAsSelectItems();
	
	List<SimpleListItemModel> getAllProjectsFromRegistruIesiriAsSelectItems();

	Map<String, Long> getNrInterogariByBancaAsMap();
	
	Map<Long, Long> getNrRaspunsuriByBancaAsMap();

	Map<String, Long> getNrInterogariByBancaAsMapByRaspunsuriBanciFilter(RaspunsuriBanciReportFilterModel filter);

	public Map<String, List<RegistruIesiriDestinatar>> getAllRegistruIesiriDestinatariByNotaGeneralaMembriiArbFilter(List<Long> institutiiMembriiArbIds) ;
	
	RegistruIntrari getIntrareByNrInregistrare(String nrInregistrare);
	
	RegistruIesiri getIesireByNrInregistrare(String nrInregistrare);
	
	List<RegistruIntrari> getMappedIntrariByIesireId(Long registruIesiriId);
	
	List<RegistruIesiri> getMappedIesiriByIntrareId(Long registruIntrariId);

	List<RegistruIesiri> getAllIesiriBySubactivityId(Long subactivityId);
	
	List<RegistruIntrari> getAllIntrariBySubactivityId(Long subactivityId);

}
