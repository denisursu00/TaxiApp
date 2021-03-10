package ro.cloudSoft.cloudDoc.services.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.dao.parameters.ParametersDao;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL.Calitate;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaReprezentivitateReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaReprezentivitateReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaReprezentivitateReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportRowModel;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.MapUtils;

public class PrezentaReprezentativitateReportPreparator {

	private PrezentaReprezentivitateReportFilterModel filter;
	private List<Document> reportDocuments;
	private Map<Long, NomenclatorValueModel> nomenclatorInstitutiiValuesById;
	private DocumentType prezentaComisiiGlDocumentType;
	private Map<String, RaspunsuriBanciReportRowModel> raspunsuriBanciRowByDenBanca;
	private ArbConstants arbConstants;
	private WorkflowInstanceDao workflowInstanceDao;
	private ParametersDao parametersDao;
	private LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher;
	private List<Long> institutiiMembriiArbIds;
	private Map<String, Long> nrInterogariByBanca;
	private Map<Long, Long> nrRaspunsuriByBanca;

	public PrezentaReprezentativitateReportPreparator(PrezentaReprezentivitateReportFilterModel filter, List<Document> reportDocuments,
			Map<Long, NomenclatorValueModel> nomenclatorInstitutiiValuesById, DocumentType prezentaComisiiGlDocumentType,
			Map<String, RaspunsuriBanciReportRowModel> raspunsuriBanciRowByDenBanca, ArbConstants arbConstants, WorkflowInstanceDao workflowInstanceDao,
			ParametersDao parametersDao, LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher, List<Long> institutiiMembriiArbIds, Map<String, Long> nrInterogariByBanca,
			Map<Long, Long> nrRaspunsuriByBanca) {
		super();
		this.filter = filter;
		this.reportDocuments = reportDocuments;
		this.nomenclatorInstitutiiValuesById = nomenclatorInstitutiiValuesById;
		this.prezentaComisiiGlDocumentType = prezentaComisiiGlDocumentType;
		this.raspunsuriBanciRowByDenBanca = raspunsuriBanciRowByDenBanca;
		this.arbConstants = arbConstants;
		this.workflowInstanceDao = workflowInstanceDao;
		this.parametersDao = parametersDao;
		this.levelSearcher = levelSearcher;
		this.institutiiMembriiArbIds = institutiiMembriiArbIds;
		this.nrInterogariByBanca = nrInterogariByBanca;
		this.nrRaspunsuriByBanca = nrRaspunsuriByBanca;
	}

	public PrezentaReprezentivitateReportModel prepareReport() {
		Map<Long, List<Map<Long, String>>> infoParticipantiArbByInstitutieId = new HashMap<>();

		String infoInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName();

		MetadataDefinition metadataDefinitionInstitutie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
				infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName());
		MetadataDefinition metadataDefinitionFunctie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
				infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getFunctieMembruOfInformatiiParticipantiMetadataName());
		MetadataDefinition metadataDefinitionCalitate = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
				infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getCalitateParticipantOfInformatiiParticipantiMetadataName());

		int numarTotalDeSedinteArb = 0;
		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {
				boolean documentHasArbMembers = false;
				WorkflowInstance workflowInstance = workflowInstanceDao.getForDocument(reportDocument.getId());

				if (workflowInstance == null || workflowInstance.getStatus().equals(WorkflowInstance.STATUS_FINNISHED)) {
					List<CollectionInstance> metadataCollectionInfoParticipanti = DocumentUtils.getMetadataCollectionInstance(reportDocument, prezentaComisiiGlDocumentType,
							arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName());
					
					if (CollectionUtils.isNotEmpty(metadataCollectionInfoParticipanti)) {
						
						for (CollectionInstance metadataInfoMembrii : metadataCollectionInfoParticipanti) {
							Map<Long, String> infoParticipantiByMetadataDefId = new HashMap<>();
							boolean areFilterConditionRespected = true;
							Long institutieId = null;
							for (MetadataInstance metadataInstance : metadataInfoMembrii.getMetadataInstanceList()) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInstitutie.getId())) {
									institutieId = Long.parseLong(metadataInstance.getValue());
									if (filter.getInstitutieId() != null && !institutieId.equals(filter.getInstitutieId())) {
										areFilterConditionRespected = false;
									}
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionFunctie.getId())) {
									infoParticipantiByMetadataDefId.put(metadataDefinitionFunctie.getId(), metadataInstance.getValue());
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionCalitate.getId())) {
									infoParticipantiByMetadataDefId.put(metadataDefinitionCalitate.getId(), metadataInstance.getValue());
								}

							}
							
							
							if (areFilterConditionRespected && institutieId != null && institutiiMembriiArbIds.contains(institutieId)) {
								documentHasArbMembers = true;
								List<Map<Long, String>> infoParticipanti = new ArrayList<>();
								
								if (infoParticipantiArbByInstitutieId.containsKey(institutieId)) {
									infoParticipanti = infoParticipantiArbByInstitutieId.get(institutieId);
								} 
								
								infoParticipanti.add(infoParticipantiByMetadataDefId);
								infoParticipantiArbByInstitutieId.put(institutieId, infoParticipanti);
							}
						}
					}
				}
				if (documentHasArbMembers) {
					numarTotalDeSedinteArb++;
				}
			}
		}

		PrezentaReprezentivitateReportModel report = new PrezentaReprezentivitateReportModel();

		List<PrezentaReprezentivitateReportRowModel> rows = new ArrayList<>();
		report.setRows(rows);

		report.setLevel0Percentage(Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_0).getValue()));
		report.setLevel1Percentage(Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_1).getValue()));
		report.setLevel2Percentage(Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_2).getValue()));
		report.setLevel3Percentage(Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_3).getValue()));
		report.setLevel3PlusPercentage(Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_3_PLUS).getValue()));
		report.setInAfaraNomPercentage(Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_IN_AFARA_NOMNINALIZARILOR).getValue()));
		report.setRaspFaraPropPercentage(Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_RASPUNSURI_FARA_PROPUNERI_PE_BANCA_RESPECTIVA).getValue()));
		report.setRaspCuPropPercentage(Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_RASPUNSURI_CU_PROPUNERI_PE_BANCA_RESPECTIVA).getValue()));
		report.setNrRaspCuIntarzPercentage(Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_RASPUNSURI_CU_INTARZIERI_PE_BANCA_RESPECTIVA).getValue()));
		report.setNrRaspFaraIntarzPercentage(Integer.parseInt(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_RASPUNSURI_FARA_INTARZIERI_PE_BANCA_RESPECTIVA).getValue()));

		double coeficientPondereNotaPrezentaStructura = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_PREZENTA_STRUCTURA).getValue()) / 100;
		double coeficientPondereNotaPrezentaNumar = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_PREZENTA_NUMAR).getValue()) / 100;
		double coeficientPunctajMaxim = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PUNCTAJ_MAXIM).getValue());
		double coeficientPondereNumarTotalSedinteArb = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_SEDINTE_PONDERAT).getValue()) / 100;
		double coeficientPondereNotaFinalaPrezenta = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_FINALA_PREZENTA).getValue()) / 100;
		double coeficientPondereNotaTotalaRaspunsuriBanci = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_TOTALA_RASPUNSURI_BANCI).getValue()) / 100;

		double coeficientPondereNotaRaspunsuriAjustataCuCalitatateaRasp = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_RASPUNSURI_AJUSTATA_CALITATE_RASPUNS).getValue()) / 100;
		double coeficientPondereNotaRaspunsuriAjustataCuVitezaRasp = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_RASPUNSURI_AJUSTATA_CU_VITEZA_RASPUNS).getValue()) / 100;
		double coeficientPondereNotaNrRaspunsuri = Double.parseDouble(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_NUMAR_RASPUNSURI).getValue()) / 100;

		for (Long bancaKey : infoParticipantiArbByInstitutieId.keySet()){  
			PrezentaReprezentivitateReportRowModel row = new PrezentaReprezentivitateReportRowModel();

			NomenclatorValueModel institutiteNomenclatorValueModel = nomenclatorInstitutiiValuesById.get(bancaKey);
			row.setBanca(NomenclatorValueUtils.getAttributeValueAsString(institutiteNomenclatorValueModel, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE));
			row.setCod(NomenclatorValueUtils.getAttributeValueAsString(institutiteNomenclatorValueModel, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_ABREVIERE));

			int prezentaPebanca = infoParticipantiArbByInstitutieId.get(bancaKey).size();
					
			for (Map<Long, String> infoPartByMtdDefId : infoParticipantiArbByInstitutieId.get(bancaKey)) {
				String functia = infoPartByMtdDefId.get(metadataDefinitionFunctie.getId());
				String calitateString = infoPartByMtdDefId.get(metadataDefinitionCalitate.getId());
				if (StringUtils.isBlank(calitateString)) {
					row.setLevel3(row.getLevel3() + 1);
					continue;
				}
				Calitate calitate = Calitate.valueOf(calitateString.toUpperCase());
				if (calitate == null) {
					row.setLevel3(row.getLevel3() + 1);
				} else if (calitate.equals(Calitate.INLOCUITOR)) {
					row.setInAfaraNom(row.getInAfaraNom() + 1);
				} else if (calitate.equals(Calitate.TITULAR) || calitate.equals(Calitate.SUPLEANT)) {
					String levelName = levelSearcher.getLevelNameByFunctieAndInstitutieId(functia, bancaKey);
					if (StringUtils.isBlank(levelName)) {
						row.setLevel3(row.getLevel3() + 1);
					} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_0)) {
						row.setLevel0(row.getLevel0() + 1);
					} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_1)) {
						row.setLevel1(row.getLevel1() + 1);
					} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_2)) {
						row.setLevel2(row.getLevel2() + 1);
					} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_3)) {
						row.setLevel3(row.getLevel3() + 1);
					} else {
						row.setLevel3Plus(row.getLevel3Plus() + 1);
					}
				}
			}

			row.setTotalPrezenta(row.getLevel0() + row.getLevel1() + row.getLevel2() + row.getLevel3() + row.getLevel3Plus() + row.getInAfaraNom());

			double coeficientStructural = 0;
			coeficientStructural += (double)row.getLevel0() * report.getLevel0Percentage() / 100;
			coeficientStructural += (double)row.getLevel1() * report.getLevel1Percentage() / 100;
			coeficientStructural += (double)row.getLevel2() * report.getLevel2Percentage() / 100;
			coeficientStructural += (double)row.getLevel3() * report.getLevel3Percentage() / 100;
			coeficientStructural += (double)row.getLevel3Plus() * report.getLevel3PlusPercentage() / 100;
			coeficientStructural += (double)row.getInAfaraNom() * report.getInAfaraNomPercentage() / 100;
			if (row.getTotalPrezenta() == 0) {
				coeficientStructural = 0;
			} else {
				coeficientStructural /= row.getTotalPrezenta();
			}
			row.setCoeficientStructural(coeficientStructural);

			double coeficientStructuralStructura = coeficientStructural;
			double notaPrezentaStructura = Double.min(coeficientPunctajMaxim, coeficientStructuralStructura * coeficientPunctajMaxim);
			double numarSedintePonderat = numarTotalDeSedinteArb * coeficientPondereNumarTotalSedinteArb;
			double coeficientStructuralNumar = coeficientStructural;
			double notaPrezentaNumar = Double.min(coeficientPunctajMaxim,(coeficientStructuralNumar * prezentaPebanca / numarSedintePonderat * coeficientPunctajMaxim));
			double notaFinalaPrezenta = notaPrezentaNumar * coeficientPondereNotaPrezentaNumar + notaPrezentaStructura * coeficientPondereNotaPrezentaStructura;
			row.setNotaFinalaPrezenta(notaFinalaPrezenta);

			RaspunsuriBanciReportRowModel raspunsuriBanci = raspunsuriBanciRowByDenBanca.get(row.getBanca());
			if (raspunsuriBanci == null) {
				raspunsuriBanci = new RaspunsuriBanciReportRowModel();
			}
			row.setRaspunsuriBanci(raspunsuriBanci);
			
			double notaFinalaRaspunsuriBanci = raspunsuriBanci.getNotaRaspunsuriAjustataCuCalitateaRaspunsurilor() * coeficientPondereNotaRaspunsuriAjustataCuCalitatateaRasp;
			notaFinalaRaspunsuriBanci += raspunsuriBanci.getNotaRaspunsuriAjustataCuVitezaRaspunsurilor() * coeficientPondereNotaRaspunsuriAjustataCuVitezaRasp;
			long numarTotalInterogariPeBanca = MapUtils.getAndInitIfNull(nrInterogariByBanca, row.getBanca(), new Long(0));
			long nrTotalRaspunsuriPeBanca = MapUtils.getAndInitIfNull(nrRaspunsuriByBanca, bancaKey, new Long(0));
			double notaNrRaspunsuri = 0;
			if (numarTotalInterogariPeBanca != 0) {
				notaNrRaspunsuri = nrTotalRaspunsuriPeBanca / numarTotalInterogariPeBanca;
			}
			notaNrRaspunsuri = Double.min(coeficientPunctajMaxim, notaNrRaspunsuri);
			notaFinalaRaspunsuriBanci += notaNrRaspunsuri * coeficientPondereNotaNrRaspunsuri;
			row.setNotaFinalaRaspunsuriBanci(notaFinalaRaspunsuriBanci);

			double notaFinalaBanca = notaFinalaPrezenta * coeficientPondereNotaFinalaPrezenta
					+ raspunsuriBanci.getNotaTotalaRaspunsuriBanci() * coeficientPondereNotaTotalaRaspunsuriBanci;
			row.setNotaFinalaBanca(notaFinalaBanca);

			apendToTotals(row, report);
			rows.add(row);
		}
		
		computeAverages(report);

		appendRankNotaBancaToReportRows(report.getRows());
		
		return report;
	}

	private void computeAverages(PrezentaReprezentivitateReportModel report) {
		int rowsSize = report.getRows().size();
		if (rowsSize == 0) {
			return;
		}
		
		report.setLevel0Avg(report.getLevel0Total() / rowsSize);
		report.setLevel1Avg(report.getLevel1Total() / rowsSize);
		report.setLevel2Avg(report.getLevel2Total() / rowsSize);
		report.setLevel3Avg(report.getLevel3Total() / rowsSize);
		report.setLevel3PlusAvg(report.getLevel3PlusTotal() / rowsSize);
		report.setInAfaraNomAvg(report.getInAfaraNomTotal() / rowsSize);
		report.setTotalPrezentaAvg(report.getTotalPrezentaTotal() / rowsSize);
		report.setCoeficientStructuralAvg(report.getCoeficientStructuralTotal() / rowsSize);
		report.setNotaFinalaPrezentaAvg(report.getNotaFinalaPrezentaTotal() / rowsSize);
		
		report.setNrRaspunsuriCuPropuneriAvg(report.getNrRaspunsuriCuPropuneriTotal() / rowsSize);
		report.setNrRaspunsuriCuIntarzierePesteOZiAvg(report.getNrRaspunsuriCuIntarzierePesteOZiTotal() / rowsSize);
		report.setNrTotalRaspunsuriPropuneriAvg(report.getNrTotalRaspunsuriPropuneriTotal() / rowsSize);
		report.setCoeficientStructuralCalitateRaspunsuriAvg(report.getCoeficientStructuralCalitateRaspunsuriTotal() / rowsSize);
		report.setNotaRaspunsuriAjustataCuCalitateaRaspunsurilorAvg(report.getNotaRaspunsuriAjustataCuCalitateaRaspunsurilorTotal() / rowsSize);
		report.setNrRaspunsuriFaraIntarziereAvg(report.getNrRaspunsuriFaraIntarziereTotal() / rowsSize);
		report.setNrRaspunsuriCuIntarzierePesteOZiAvg(report.getNrRaspunsuriCuIntarzierePesteOZiTotal() / rowsSize);
		report.setNrTotalRaspunsuriTermenAvg(report.getNrTotalRaspunsuriTermenTotal() / rowsSize);
		report.setCoeficientStructuralVitezaRaspunsuriAvg(report.getCoeficientStructuralVitezaRaspunsuriTotal() / rowsSize);
		report.setNotaRaspunsuriAjustataCuVitezaRaspunsurilorAvg(report.getNotaRaspunsuriAjustataCuVitezaRaspunsurilorTotal() / rowsSize);
		report.setNotaTotalaRaspunsuriBanciAvg(report.getNotaTotalaRaspunsuriBanciTotal() / rowsSize);
		report.setNotaFinalaRaspunsuriBanciAvg(report.getNotaFinalaRaspunsuriBanciTotal() / rowsSize);

		report.setNotaFinalaBancaAvg(report.getNotaFinalaBancaTotal() / rowsSize);
		report.setRankNotaBancaAvg(report.getRankNotaBancaTotal() / rowsSize);
		
	}

	private void apendToTotals(PrezentaReprezentivitateReportRowModel row, PrezentaReprezentivitateReportModel report) {
		
		report.setLevel0Total(report.getLevel0Total() + row.getLevel0());
		report.setLevel1Total(report.getLevel1Total() + row.getLevel1());
		report.setLevel2Total(report.getLevel2Total() + row.getLevel2());
		report.setLevel3Total(report.getLevel3Total() + row.getLevel3());
		report.setLevel3PlusTotal(report.getLevel3PlusTotal() + row.getLevel3Plus());
		report.setInAfaraNomTotal(report.getInAfaraNomTotal() + row.getInAfaraNom());
		report.setTotalPrezentaTotal(report.getTotalPrezentaTotal() + row.getTotalPrezenta());
		report.setCoeficientStructuralTotal(report.getCoeficientStructuralTotal() + row.getCoeficientStructural());
		report.setNotaFinalaPrezentaTotal(report.getNotaFinalaPrezentaTotal() + row.getNotaFinalaPrezenta());
		
		report.setNrRaspunsuriCuPropuneriTotal(report.getNrRaspunsuriCuPropuneriTotal() + row.getRaspunsuriBanci().getNrRaspunsuriCuPropuneri());
		report.setNrRaspunsuriCuIntarzierePesteOZiTotal(report.getNrRaspunsuriCuIntarzierePesteOZiTotal() + row.getRaspunsuriBanci().getNrRaspunsuriCuIntarzierePesteOZi());
		report.setNrTotalRaspunsuriPropuneriTotal(report.getNrTotalRaspunsuriPropuneriTotal() + row.getRaspunsuriBanci().getNrTotalRaspunsuriPropuneri());
		report.setCoeficientStructuralCalitateRaspunsuriTotal(report.getCoeficientStructuralCalitateRaspunsuriTotal() + row.getRaspunsuriBanci().getCoeficientStructuralCalitateRaspunsuri());
		report.setNotaRaspunsuriAjustataCuCalitateaRaspunsurilorTotal(report.getNotaRaspunsuriAjustataCuCalitateaRaspunsurilorTotal() + row.getRaspunsuriBanci().getNotaRaspunsuriAjustataCuCalitateaRaspunsurilor());
		report.setNrRaspunsuriFaraIntarziereTotal(report.getNrRaspunsuriFaraIntarziereTotal() + row.getRaspunsuriBanci().getNrRaspunsuriFaraIntarziere());
		report.setNrRaspunsuriCuIntarzierePesteOZiTotal(report.getNrRaspunsuriCuIntarzierePesteOZiTotal() + row.getRaspunsuriBanci().getNrRaspunsuriCuIntarzierePesteOZi());
		report.setNrTotalRaspunsuriTermenTotal(report.getNrTotalRaspunsuriTermenTotal() + row.getRaspunsuriBanci().getNrTotalRaspunsuriTermen());
		report.setCoeficientStructuralVitezaRaspunsuriTotal(report.getCoeficientStructuralVitezaRaspunsuriTotal() + row.getRaspunsuriBanci().getCoeficientStructuralVitezaRaspunsuri());
		report.setNotaRaspunsuriAjustataCuVitezaRaspunsurilorTotal(report.getNotaRaspunsuriAjustataCuVitezaRaspunsurilorTotal() + row.getRaspunsuriBanci().getNotaRaspunsuriAjustataCuVitezaRaspunsurilor());
		report.setNotaTotalaRaspunsuriBanciTotal(report.getNotaTotalaRaspunsuriBanciTotal() + row.getRaspunsuriBanci().getNotaTotalaRaspunsuriBanci());
		report.setNotaFinalaRaspunsuriBanciTotal(report.getNotaFinalaRaspunsuriBanciTotal() + row.getNotaFinalaRaspunsuriBanci());
	
		report.setNotaFinalaBancaTotal(report.getNotaFinalaBancaTotal() + row.getNotaFinalaBanca());
		report.setRankNotaBancaTotal(report.getRankNotaBancaTotal() + row.getRankNotaBanca());
		
	}
	
	public void appendRankNotaBancaToReportRows(List<PrezentaReprezentivitateReportRowModel> rows) {
		Set<Double> noteFinaleSet = new HashSet<Double>();

		for (PrezentaReprezentivitateReportRowModel row : rows) {
			noteFinaleSet.add(row.getNotaFinalaBanca());
		}

		List<Double> noteFinaleAsSortedList  = new ArrayList<>();
		noteFinaleAsSortedList.addAll(noteFinaleSet);
		
		Collections.sort(noteFinaleAsSortedList, Collections.reverseOrder());
		for (PrezentaReprezentivitateReportRowModel row : rows) {
			row.setRankNotaBanca(noteFinaleAsSortedList.indexOf(row.getNotaFinalaBanca()) + 1);
		}
		
	}

}
