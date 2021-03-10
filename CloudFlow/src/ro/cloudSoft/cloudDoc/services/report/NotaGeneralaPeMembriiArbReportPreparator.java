package ro.cloudSoft.cloudDoc.services.report;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.dao.parameters.ParametersDao;
import ro.cloudSoft.cloudDoc.dao.registruIntrariIesiri.RegistruIntrariIesiriDao;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL.Calitate;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiri;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIesiriDestinatar;
import ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri.RegistruIntrari;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.GetNomenclatorValuesRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSimpleFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariModel.RaspunsuriBanciCuPropuneriEnum;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaPeMembriiArbReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaPeMembriiArbReportMapModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaPeMembriiArbReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaPeMembriiArbReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.PrezentaReprezentivitateReportRowModel;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;

public class NotaGeneralaPeMembriiArbReportPreparator {

	private ParametersDao parametersDao;
	Map<Long, String> nomenclatorComisiiUiAttrValuesAsMap;
	private Map<Long, String> nomenclatorInstitutiiUiAttrValuesAsMap;
	private Map<String, Long> nrRaspByBancaComisie;
	private Map<String, Long> nrRaspCuIntarziereByBancaComisie;
	private Map<String, Long> nrRaspFaraIntarziereByBancaComisie;
	private Map<String, Long> nrRaspCuPropuneriByBancaComisie;
	private Map<String, Long> nrRaspFaraPropuneriByBancaComisie;
	private Map<String, Long> numarInterogariByBancaAndComisieAsMap;
	private  List<Document> reportDocuments;
	private LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher;
	private DocumentType prezentaComisiiGlDocumentType;
	private WorkflowInstanceDao workflowInstanceDao;
	private List<Long> institutiiMembriiArbIds;
	private ArbConstants arbConstants;
	private NotaGeneralaPeMembriiArbReportFilterModel filter;
	private  NomenclatorService nomenclatorService;
	private RegistruIntrariIesiriDao registruIntrariIesiriDao;

	public NotaGeneralaPeMembriiArbReportPreparator(ParametersDao parametersDao, NomenclatorService nomenclatorService, RegistruIntrariIesiriDao registruIntrariIesiriDao,
			List<Long> institutiiMembriiArbIds, List<Document> reportDocuments, DocumentType prezentaComisiiGlDocumentType, WorkflowInstanceDao workflowInstanceDao, ArbConstants arbConstants, NotaGeneralaPeMembriiArbReportFilterModel filter, LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher) throws AppException {

		this.parametersDao = parametersDao;
		this.nomenclatorService = nomenclatorService;
		this.registruIntrariIesiriDao = registruIntrariIesiriDao;
		this.institutiiMembriiArbIds = institutiiMembriiArbIds;
		this.reportDocuments = reportDocuments;
		this.prezentaComisiiGlDocumentType = prezentaComisiiGlDocumentType;
		this.workflowInstanceDao = workflowInstanceDao;
		this.arbConstants = arbConstants;
		this.filter = filter;
		this.levelSearcher = levelSearcher;

		init();
	}

	private void init() throws AppException {
		this.nomenclatorComisiiUiAttrValuesAsMap = nomenclatorService.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.COMISII_SAU_GL_NOMENCLATOR_CODE);
		this.nomenclatorInstitutiiUiAttrValuesAsMap = nomenclatorService.getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);
		this.numarInterogariByBancaAndComisieAsMap = registruIntrariIesiriDao.getNumarInterogariMembriiArbByBancaAndComisie(institutiiMembriiArbIds);
		this.nrRaspByBancaComisie = new HashMap<>();
		this.nrRaspCuIntarziereByBancaComisie = new HashMap<>();
		this.nrRaspFaraIntarziereByBancaComisie = new HashMap<>();
		this.nrRaspCuPropuneriByBancaComisie = new HashMap<>();
		this.nrRaspFaraPropuneriByBancaComisie = new HashMap<>();
		Map<String, List<RegistruIesiriDestinatar>> registreIntrareByBancaComisie = this.registruIntrariIesiriDao.getAllRegistruIesiriDestinatariByNotaGeneralaMembriiArbFilter(institutiiMembriiArbIds);
		registreIntrareByBancaComisie.forEach((bancaComisie, registreIesiriDestinatari) -> {
			if (!nrRaspCuIntarziereByBancaComisie.containsKey(bancaComisie)) {
				nrRaspCuIntarziereByBancaComisie.put(bancaComisie, 0l);
			}
			if (!nrRaspFaraIntarziereByBancaComisie.containsKey(bancaComisie)) {
				nrRaspFaraIntarziereByBancaComisie.put(bancaComisie, 0l);
			}
			if (!nrRaspCuPropuneriByBancaComisie.containsKey(bancaComisie)) {
				nrRaspCuPropuneriByBancaComisie.put(bancaComisie, 0l);
			}
			if (!nrRaspFaraPropuneriByBancaComisie.containsKey(bancaComisie)) {
				nrRaspFaraPropuneriByBancaComisie.put(bancaComisie, 0l);
			}
			long nrRaspunsuri = 0;
			for (RegistruIesiriDestinatar destinatar : registreIesiriDestinatari) {
				nrRaspunsuri ++;
				RegistruIesiri regIesire = destinatar.getRegistruIesiri();
				RegistruIntrari regIntrare = destinatar.getRegistruIntrari();
				if (regIntrare.getRaspunsuriBanciCuPropuneri().equals(RaspunsuriBanciCuPropuneriEnum.DA)) {
					Long nrRasp = nrRaspCuPropuneriByBancaComisie.get(bancaComisie);
					nrRasp ++;
					nrRaspCuPropuneriByBancaComisie.put(bancaComisie, nrRasp);
				} else {
					Long nrRasp = nrRaspFaraPropuneriByBancaComisie.get(bancaComisie);
					nrRasp ++;
					nrRaspFaraPropuneriByBancaComisie.put(bancaComisie, nrRasp);
				}
				if (regIesire.getTermenRaspuns() != null) {
					long numberDaysBetween = DateUtils.numberDaysBetween(regIntrare.getDataInregistrare(), regIesire.getTermenRaspuns());
					if (numberDaysBetween < -1) {
						Long nrRasp = nrRaspFaraIntarziereByBancaComisie.get(bancaComisie);
						nrRasp ++;
						nrRaspFaraIntarziereByBancaComisie.put(bancaComisie, nrRasp);
					} else {
						Long nrRasp = nrRaspCuIntarziereByBancaComisie.get(bancaComisie);
						nrRasp ++;
						nrRaspCuIntarziereByBancaComisie.put(bancaComisie, nrRasp);
					}
				}
			}
			nrRaspByBancaComisie.put(bancaComisie, nrRaspunsuri);
		});
	}

	public NotaGeneralaPeMembriiArbReportModel prepareReport() throws AppException {
		Map<Long, List<NotaGeneralaPeMembriiArbReportMapModel>> rowsBancaByIdAsMap = new HashMap<>();
		Map<Long, Map<Long, Integer>> nrSedinteByComisieIdByInstitutieId = new HashMap();

		String infoInformatiiParticipantiMetadataName = arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName();
		MetadataDefinition metadataDefinitionInstitutie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
				infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getNumeInstitutieOfInformatiiParticipantiMetadataName());
		MetadataDefinition metadataDefinitionFunctie = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
				infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getFunctieMembruOfInformatiiParticipantiMetadataName());
		MetadataDefinition metadataDefinitionCalitate = DocumentTypeUtils.getMetadataDefinitionFromCollectionByName(prezentaComisiiGlDocumentType,
				infoInformatiiParticipantiMetadataName, arbConstants.getDocumentPrezentaComisieGlConstants().getCalitateParticipantOfInformatiiParticipantiMetadataName());

		if (CollectionUtils.isNotEmpty(reportDocuments)) {
			for (Document reportDocument : reportDocuments) {
				if (workflowInstanceDao.hasFinishedStatusWorkflowInstanceByDocumentId(reportDocument.getDocumentLocationRealName(), reportDocument.getId())) {
					Long comisieId = DocumentUtils.getMetadataNomenclatorValue(reportDocument, prezentaComisiiGlDocumentType,
							arbConstants.getDocumentPrezentaComisieGlConstants().getDenumireComisieGlMetadataName());

					List<CollectionInstance> metadataCollectionInfoParticipanti = DocumentUtils.getMetadataCollectionInstance(reportDocument, prezentaComisiiGlDocumentType,
							arbConstants.getDocumentPrezentaComisieGlConstants().getInformatiiParticipantiMetadataName());

					boolean isMembruArbOnDocument = false;

					if (CollectionUtils.isNotEmpty(metadataCollectionInfoParticipanti)) {

						for (CollectionInstance metadataInfoMembrii : metadataCollectionInfoParticipanti) {
							NotaGeneralaPeMembriiArbReportMapModel row = new NotaGeneralaPeMembriiArbReportMapModel();
							row.setComisieId(comisieId);


							boolean areFilterConditionRespected = true;
							Long institutieId = null;
							for (MetadataInstance metadataInstance : metadataInfoMembrii.getMetadataInstanceList()) {
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionInstitutie.getId())) {
									institutieId = Long.parseLong(metadataInstance.getValue());
									if (filter.getBancaId() != null && !institutieId.equals(filter.getBancaId())) {
										areFilterConditionRespected = false;
									}
									row.setBancaId(institutieId);
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionFunctie.getId())) {
									row.setFunctia(metadataInstance.getValue());
								}
								if (metadataInstance.getMetadataDefinitionId().equals(metadataDefinitionCalitate.getId())) {
									row.setCalitatea(metadataInstance.getValue());
								}

							}

							if (areFilterConditionRespected && institutieId != null && institutiiMembriiArbIds.contains(institutieId)) {
								isMembruArbOnDocument = true;
								if (!nrSedinteByComisieIdByInstitutieId.containsKey(institutieId)) {
									Map<Long, Integer> nrSedinteByComisieId = new HashMap<>();
									nrSedinteByComisieId.put(comisieId, 1);
									nrSedinteByComisieIdByInstitutieId.put(institutieId, nrSedinteByComisieId);
								} else {
									Map<Long, Integer> nrSedinteByComisieId = nrSedinteByComisieIdByInstitutieId.get(institutieId);
									if (!nrSedinteByComisieId.containsKey(comisieId)) {
										nrSedinteByComisieId.put(comisieId, 1);
									} else {
										Integer nrSedinte = nrSedinteByComisieId.get(comisieId);
										nrSedinte ++;
										nrSedinteByComisieId.replace(comisieId, nrSedinte);
									}
								}
								if (!rowsBancaByIdAsMap.containsKey(institutieId)) {
									List<NotaGeneralaPeMembriiArbReportMapModel> items = new ArrayList<NotaGeneralaPeMembriiArbReportMapModel>();

									items.add(row);
									rowsBancaByIdAsMap.put(institutieId, items);
								} else {
									List<NotaGeneralaPeMembriiArbReportMapModel> items = rowsBancaByIdAsMap.get(institutieId);
									items.add(row);
								}
							}
						}
					}
				}
			}
		}

		Map<Long, Map<Long, List<NotaGeneralaPeMembriiArbReportMapModel>>> rowsBancaComisieByIdReportAsMap = prepareRowsGroupByBancaAndComisieAsMap(rowsBancaByIdAsMap);
		
		NotaGeneralaPeMembriiArbReportModel report = new NotaGeneralaPeMembriiArbReportModel();
		List<NotaGeneralaPeMembriiArbReportRowModel> rows = prepareReportRowsAndCalculateNotaFinala(rowsBancaComisieByIdReportAsMap, nrSedinteByComisieIdByInstitutieId);
		report.setRows(rows);

		return report;
	}

	private Map<Long, Map<Long, List<NotaGeneralaPeMembriiArbReportMapModel>>> prepareRowsGroupByBancaAndComisieAsMap(
			Map<Long, List<NotaGeneralaPeMembriiArbReportMapModel>> rowsBancaByIdAsMap) {

		Map<Long, Map<Long, List<NotaGeneralaPeMembriiArbReportMapModel>>> rowsBancaComisieByIdReportAsMap = new HashMap<>();

		for (List<NotaGeneralaPeMembriiArbReportMapModel> rows : rowsBancaByIdAsMap.values()) {
			Map<Long, List<NotaGeneralaPeMembriiArbReportMapModel>> rowsComisieByIdReportAsMap = new HashMap<>();

			for (NotaGeneralaPeMembriiArbReportMapModel row : rows) {
				Long comisieKey = row.getComisieId();

				if (!rowsComisieByIdReportAsMap.containsKey(comisieKey)) {
					List<NotaGeneralaPeMembriiArbReportMapModel> items = new ArrayList<NotaGeneralaPeMembriiArbReportMapModel>();

					items.add(row);
					rowsComisieByIdReportAsMap.put(comisieKey, items);
				} else {
					List<NotaGeneralaPeMembriiArbReportMapModel> items = rowsComisieByIdReportAsMap.get(comisieKey);

					items.add(row);
				}
			}
			rowsBancaComisieByIdReportAsMap.put(rows.get(0).getBancaId(), rowsComisieByIdReportAsMap);

		}

		return rowsBancaComisieByIdReportAsMap;
	}

	public List<NotaGeneralaPeMembriiArbReportRowModel> prepareReportRowsAndCalculateNotaFinala(
			Map<Long, Map<Long, List<NotaGeneralaPeMembriiArbReportMapModel>>> rowsBancaComisieByIdReportAsMap, Map<Long, Map<Long, Integer>> nrSedinteByComisieIdByInstitutieId) throws AppException {

		List<NotaGeneralaPeMembriiArbReportRowModel> rows = new ArrayList<NotaGeneralaPeMembriiArbReportRowModel>();
		
		BigDecimal procent = new BigDecimal(100);

		BigDecimal coeficientProcentualLevel0 = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_0).getValue());
		BigDecimal coeficientProcentualLevel1 = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_1).getValue());
		BigDecimal coeficientProcentualLevel2 = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_2).getValue());
		BigDecimal coeficientProcentualLevel3 = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_3).getValue());
		BigDecimal coeficientProcentualLevel3Plus = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_3_PLUS).getValue());
		BigDecimal coeficientProcentualInAfaraNomninalizarilor = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_IN_AFARA_NOMNINALIZARILOR).getValue());
		BigDecimal coeficientPunctajMaxim = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PUNCTAJ_MAXIM).getValue());
		BigDecimal coeficientProcentualNotaPrezentaNumar = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_PREZENTA_NUMAR).getValue());
		BigDecimal coeficientProcentualNotaPrezentaStructura = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_PREZENTA_STRUCTURA).getValue());
		BigDecimal coeficientNumarSedintePonderat = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_SEDINTE_PONDERAT).getValue()).divide(procent);
		BigDecimal coeficientProcentualNumarRaspunsuriFaraPropuneri = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_RASPUNSURI_FARA_PROPUNERI_PE_BANCA_RESPECTIVA).getValue());
		BigDecimal coeficientProcentualNumarRaspunsuriCuPropuneri = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_RASPUNSURI_CU_PROPUNERI_PE_BANCA_RESPECTIVA).getValue());
		BigDecimal coeficientProcentualNumarRaspunsuriFaraIntarzieri = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_RASPUNSURI_FARA_INTARZIERI_PE_BANCA_RESPECTIVA).getValue());
		BigDecimal coeficientProcentulNumarRaspunsuriCuIntarzieri = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_RASPUNSURI_CU_INTARZIERI_PE_BANCA_RESPECTIVA).getValue());
		BigDecimal coeficientProcentualPrezente = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_PREZENTE).getValue());
		BigDecimal coeficientProcentualRaspunsuri = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_RASPUNSURI).getValue());
		BigDecimal coeficientPondereNotaRaspunsuriAjustataCuCalitatateaRasp = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_RASPUNSURI_AJUSTATA_CALITATE_RASPUNS).getValue()).divide(procent);
		BigDecimal coeficientPondereNotaRaspunsuriAjustataCuVitezaRasp = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_RASPUNSURI_AJUSTATA_CU_VITEZA_RASPUNS).getValue()).divide(procent);
		BigDecimal coeficientPondereNotaNrRaspunsuri = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_NUMAR_RASPUNSURI).getValue()).divide(procent);
		BigDecimal coeficientPonderatNrInterogari = new BigDecimal(parametersDao.getByName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_GRAD_NR_RASPUNSURI).getValue()).divide(procent);
		
		MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);
		for (Long bancaKey : rowsBancaComisieByIdReportAsMap.keySet()) {
			Map<Long, List<NotaGeneralaPeMembriiArbReportMapModel>> banci = rowsBancaComisieByIdReportAsMap.get(bancaKey);
			for (Long comisieKey : banci.keySet()) {
				List<NotaGeneralaPeMembriiArbReportMapModel> comisii = banci.get(comisieKey);

				Integer totalLevel0 = 0;
				Integer totalLevel1 = 0;
				Integer totalLevel2 = 0;
				Integer totalLevel3 = 0;
				Integer totalLevel3Plus = 0;
				Integer totalInAfaraNominalizarilor = 0;
				Integer totalPrezenta = 0;

				for (NotaGeneralaPeMembriiArbReportMapModel comisie : comisii) {
					if (StringUtils.isBlank(comisie.getCalitatea())) {
						totalLevel3++;
						continue;
					}
					String calitate = comisie.getCalitatea().trim().toUpperCase();
					String functia = comisie.getFunctia();
					Long institutieId = comisie.getBancaId();
					
					if (calitate == null) {
						totalLevel3++;
					} else	if (calitate.equals(Calitate.TITULAR.toString()) || calitate.equals(Calitate.SUPLEANT.toString())) {
						
						String levelName = levelSearcher.getLevelNameByFunctieAndInstitutieId(functia, institutieId);
						
						if (StringUtils.isBlank(levelName)) {
							totalLevel3++;
						} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_0)) {
							totalLevel0++;
						} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_1)) {
							totalLevel1++;
						} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_2)) {
							totalLevel2++;
						} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_3)) {
							totalLevel3++;
						} else {
							totalLevel3Plus++;
						}
					} else if (calitate.equals(Calitate.INLOCUITOR.toString())) {
						totalInAfaraNominalizarilor++;
					}
				}

				totalPrezenta += totalLevel0 + totalLevel1 + totalLevel2 + totalLevel3 + totalLevel3Plus + totalInAfaraNominalizarilor;

				int prezentaPeBanca = totalPrezenta;

				double numarSedintePonderate = ((double) nrSedinteByComisieIdByInstitutieId.get(bancaKey).get(comisieKey)) * coeficientNumarSedintePonderat.doubleValue() ;

				BigDecimal level0Ponderat = ((new BigDecimal(totalLevel0)).multiply(coeficientProcentualLevel0)).divide(procent);
				BigDecimal level1Ponderat = ((new BigDecimal(totalLevel1)).multiply(coeficientProcentualLevel1)).divide(procent);
				BigDecimal level2Ponderat = ((new BigDecimal(totalLevel2)).multiply(coeficientProcentualLevel2)).divide(procent);
				BigDecimal level3Ponderat = ((new BigDecimal(totalLevel3)).multiply(coeficientProcentualLevel3)).divide(procent);
				BigDecimal level3PlusPonderat = ((new BigDecimal(totalLevel3Plus)).multiply(coeficientProcentualLevel3Plus)).divide(procent);
				BigDecimal inAfaraNominalizarilorPonderat = ((new BigDecimal(totalInAfaraNominalizarilor)).multiply(coeficientProcentualInAfaraNomninalizarilor)).divide(procent);

				BigDecimal coeficientStructuralNumar = BigDecimal.ZERO;

				if (totalPrezenta != 0) {
					
					coeficientStructuralNumar = (level0Ponderat.add(level1Ponderat).add(level2Ponderat).add(level3Ponderat).add(level3PlusPonderat)
							.add(inAfaraNominalizarilorPonderat)).divide(new BigDecimal(totalPrezenta),mathContext );

				}

				BigDecimal notaTotalaPrezentaNumar = BigDecimal.ZERO;

				if (numarSedintePonderate != 0) {
					notaTotalaPrezentaNumar = coeficientPunctajMaxim.min(new BigDecimal(coeficientStructuralNumar.doubleValue() * prezentaPeBanca / numarSedintePonderate * coeficientPunctajMaxim.doubleValue()));
				}

				BigDecimal notaTotalaPrezentaStructura = coeficientPunctajMaxim.min(coeficientStructuralNumar.multiply(coeficientPunctajMaxim));

				BigDecimal notaTotalaPrezenta = ((notaTotalaPrezentaNumar.multiply(coeficientProcentualNotaPrezentaNumar)).divide(procent)).add((notaTotalaPrezentaStructura
												.multiply(coeficientProcentualNotaPrezentaStructura)
												.divide(procent)));

				Long nrRaspunsuri = nrRaspByBancaComisie.get(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaKey) + comisieKey);
				BigDecimal numarTotalRaspunsuriPeBancaRespectiva = BigDecimal.ZERO;
				if (nrRaspunsuri != null) {
					numarTotalRaspunsuriPeBancaRespectiva = new BigDecimal(nrRaspunsuri);
				}

				BigDecimal coeficientStructuralCalitateRaspunsuri = BigDecimal.ZERO;

				if (!numarTotalRaspunsuriPeBancaRespectiva.equals(BigDecimal.ZERO)) {
					Long nrRaspunsuriFaraPropuneri = nrRaspFaraPropuneriByBancaComisie.get(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaKey) + comisieKey);
					BigDecimal numarRaspunsuriFaraPropuneriPeBancaRespectiva = BigDecimal.ZERO;
					if (nrRaspunsuriFaraPropuneri != null) {
						numarRaspunsuriFaraPropuneriPeBancaRespectiva = new BigDecimal(nrRaspunsuriFaraPropuneri);
					}

					Long numarRaspunsuriCuPropuneri = nrRaspCuPropuneriByBancaComisie.get(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaKey) + comisieKey);
					BigDecimal numarRaspunsuriCuPropuneriPeBancaRespectiva = BigDecimal.ZERO;
					if (numarRaspunsuriCuPropuneri != null) {
						numarRaspunsuriCuPropuneriPeBancaRespectiva = new BigDecimal(numarRaspunsuriCuPropuneri);
					}

					coeficientStructuralCalitateRaspunsuri = (((numarRaspunsuriFaraPropuneriPeBancaRespectiva.multiply(coeficientProcentualNumarRaspunsuriFaraPropuneri )).divide(procent))
											.add(((numarRaspunsuriCuPropuneriPeBancaRespectiva.multiply(coeficientProcentualNumarRaspunsuriCuPropuneri )).divide(procent)))).divide(numarTotalRaspunsuriPeBancaRespectiva);
				}

				BigDecimal notaRaspunsuriAjustataCuCalitateaRaspunsurilor = coeficientPunctajMaxim.min((coeficientStructuralCalitateRaspunsuri.multiply(coeficientPunctajMaxim)));

				BigDecimal coeficientStructuralVitezaRaspunsuri = BigDecimal.ZERO;

				if (!numarTotalRaspunsuriPeBancaRespectiva.equals(BigDecimal.ZERO)) {
					Long numarRaspunsuriFaraIntarziere = nrRaspFaraIntarziereByBancaComisie.get(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaKey) + comisieKey);
					BigDecimal numarRaspunsuriFaraIntarzierePeBancaRespectiva = BigDecimal.ZERO;
					if (numarRaspunsuriFaraIntarziere != null) {
						numarRaspunsuriFaraIntarzierePeBancaRespectiva = new BigDecimal(numarRaspunsuriFaraIntarziere);
					}
					Long numarRaspunsuriCuIntarziere = nrRaspCuIntarziereByBancaComisie.get(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaKey) + comisieKey);
					BigDecimal numarRaspunsuriCuIntarzierePeBancaRespectiva = BigDecimal.ZERO;
					if (numarRaspunsuriCuIntarziere != null) {
						numarRaspunsuriCuIntarzierePeBancaRespectiva = new BigDecimal(numarRaspunsuriCuIntarziere);
					}

					coeficientStructuralVitezaRaspunsuri = (((numarRaspunsuriFaraIntarzierePeBancaRespectiva.multiply(coeficientProcentualNumarRaspunsuriFaraIntarzieri)).divide(procent))
							.add(((numarRaspunsuriCuIntarzierePeBancaRespectiva.multiply(coeficientProcentulNumarRaspunsuriCuIntarzieri )).divide(procent))))
							.divide(numarTotalRaspunsuriPeBancaRespectiva, mathContext);
				}

				BigDecimal notaRaspunsuriAjustataCuVitezaRaspunsurilor = coeficientPunctajMaxim.min(coeficientStructuralVitezaRaspunsuri.multiply(coeficientPunctajMaxim));

				Long numarInterogari = numarInterogariByBancaAndComisieAsMap.get(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaKey) + comisieKey);
				double numarTotalInterogariPonderat = 0;
				double notaNumarRaspunsuri = 0;
				if (numarInterogari != null) {
					numarTotalInterogariPonderat = ((double)numarInterogari) * coeficientPonderatNrInterogari.doubleValue();
					notaNumarRaspunsuri = numarTotalRaspunsuriPeBancaRespectiva.doubleValue() / numarTotalInterogariPonderat * coeficientPunctajMaxim.doubleValue();
					if (notaNumarRaspunsuri > coeficientPunctajMaxim.doubleValue()) {
						notaNumarRaspunsuri = coeficientPunctajMaxim.doubleValue();
					}
				}

				BigDecimal notaTotalaRaspunsuriBanci = (notaRaspunsuriAjustataCuCalitateaRaspunsurilor.multiply(coeficientPondereNotaRaspunsuriAjustataCuCalitatateaRasp))
										.add(notaRaspunsuriAjustataCuVitezaRaspunsurilor.multiply(coeficientPondereNotaRaspunsuriAjustataCuVitezaRasp ))
										.add(new BigDecimal(notaNumarRaspunsuri).multiply(coeficientPondereNotaNrRaspunsuri));

				BigDecimal notaFinalaBanaca = ((notaTotalaPrezenta.multiply(coeficientProcentualPrezente)).divide(procent))
						.add((notaTotalaRaspunsuriBanci.multiply(coeficientProcentualRaspunsuri).divide(procent)));

				NotaGeneralaPeMembriiArbReportRowModel row = new NotaGeneralaPeMembriiArbReportRowModel();

				row.setBanca(nomenclatorInstitutiiUiAttrValuesAsMap.get(bancaKey));
				row.setComisie(nomenclatorComisiiUiAttrValuesAsMap.get(comisieKey));

				row.setNotaFinalaComisie(notaFinalaBanaca);
				rows.add(row);
			}
		}
		
		appendRankNotaComiseToReportRows(rows);
		
		return rows;
	}

	public void appendRankNotaComiseToReportRows(List<NotaGeneralaPeMembriiArbReportRowModel> rows) {
		Set<BigDecimal> noteFinaleSet = new HashSet<BigDecimal>();

		for (NotaGeneralaPeMembriiArbReportRowModel row : rows) {
			noteFinaleSet.add(row.getNotaFinalaComisie());
		}

		List<BigDecimal> noteFinaleAsSortedList  = new ArrayList<>();
		noteFinaleAsSortedList.addAll(noteFinaleSet);
		Collections.sort(noteFinaleAsSortedList, Collections.reverseOrder());

		for (NotaGeneralaPeMembriiArbReportRowModel row : rows) {
			row.setRankNotaComisie(noteFinaleAsSortedList.indexOf(row.getNotaFinalaComisie()) + 1);
		}
		
	}

}
