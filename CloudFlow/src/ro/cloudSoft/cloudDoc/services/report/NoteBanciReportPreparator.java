package ro.cloudSoft.cloudDoc.services.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.dao.bpm.WorkflowInstanceDao;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL.Calitate;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NoteBanciReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NoteBanciReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NoteBanciReportRowModel;
import ro.cloudSoft.cloudDoc.services.parameters.ParametersService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class NoteBanciReportPreparator {

	private NoteBanciReportFilterModel filter;
	private List<Document> reportDocuments;
	private Map<Long, NomenclatorValueModel> nomenclatorInstitutiiValuesById;
	private DocumentType prezentaComisiiGlDocumentType;
	private ArbConstants arbConstants;
	private WorkflowInstanceDao workflowInstanceDao;
	private ParametersService parametersService;
	private LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher;
	private List<Long> institutiiMembriiArbIds;

	public NoteBanciReportPreparator(WorkflowInstanceDao workflowInstanceDao, NoteBanciReportFilterModel filter, List<Document> reportDocuments,
			Map<Long, NomenclatorValueModel> nomenclatorInstitutiiValuesById, DocumentType prezentaComisiiGlDocumentType, ArbConstants arbConstants,
			ParametersService parametersService, LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher, List<Long> institutiiMembriiArbIds) {
		super();
		this.workflowInstanceDao = workflowInstanceDao;
		this.parametersService = parametersService;
		this.arbConstants = arbConstants;
		this.filter = filter;
		this.reportDocuments = reportDocuments;
		this.nomenclatorInstitutiiValuesById = nomenclatorInstitutiiValuesById;
		this.prezentaComisiiGlDocumentType = prezentaComisiiGlDocumentType;
		this.levelSearcher = levelSearcher;
		this.institutiiMembriiArbIds = institutiiMembriiArbIds;
	}

	public NoteBanciReportModel prepareReport() throws AppException {
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

		NoteBanciReportModel report = new NoteBanciReportModel();

		List<NoteBanciReportRowModel> rows = new ArrayList<>();
		report.setRows(rows);

		report.setLevel0Percentage(parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_0));
		report.setLevel1Percentage(parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_1));
		report.setLevel2Percentage(parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_2));
		report.setLevel3Percentage(parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_3));
		report.setLevel3PlusPercentage(parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_3_PLUS));
		report.setInAfaraNomPercentage(parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_IN_AFARA_NOMNINALIZARILOR));

		double coeficientPondereNotaPrezentaStructura = parametersService.getParamaterValueAsDoubleByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_PREZENTA_STRUCTURA) / 100;
		double coeficientPondereNotaPrezentaNumar = parametersService.getParamaterValueAsDoubleByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_PREZENTA_NUMAR) / 100;
		double coeficientPunctajMaxim = parametersService.getParamaterValueAsDoubleByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PUNCTAJ_MAXIM);
		double coeficientPondereNumarTotalSedinteArb = parametersService.getParamaterValueAsDoubleByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_SEDINTE_PONDERAT) / 100;

		for (Long bancaKey : infoParticipantiArbByInstitutieId.keySet()){  

			NoteBanciReportRowModel row = new NoteBanciReportRowModel();

			NomenclatorValueModel institutiteNomenclatorValueModel = nomenclatorInstitutiiValuesById.get(bancaKey);
			row.setBanca(NomenclatorValueUtils.getAttributeValueAsString(institutiteNomenclatorValueModel, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE));
			row.setCod(NomenclatorValueUtils.getAttributeValueAsString(institutiteNomenclatorValueModel, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_ABREVIERE));

			int prezentaPebanca = infoParticipantiArbByInstitutieId.get(bancaKey).size();
			for (Map<Long, String> infoPartByMtdDefId : infoParticipantiArbByInstitutieId.get(bancaKey)) {
				String functia = infoPartByMtdDefId.get(metadataDefinitionFunctie.getId());
				String calitateString = infoPartByMtdDefId.get(metadataDefinitionCalitate.getId());
				
				if (StringUtils.isBlank(calitateString)) {
					row.setLevel3(row.getLevel3() + 1);
				} else {
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

			apendToTotals(row, report);
			rows.add(row);
		}
		
		computeAverages(report);

		
		return report;
	}

	private void computeAverages(NoteBanciReportModel report) {
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
		
	}

	private void apendToTotals(NoteBanciReportRowModel row, NoteBanciReportModel report) {
		report.setLevel0Total(report.getLevel0Total() + row.getLevel0());
		report.setLevel1Total(report.getLevel1Total() + row.getLevel1());
		report.setLevel2Total(report.getLevel2Total() + row.getLevel2());
		report.setLevel3Total(report.getLevel3Total() + row.getLevel3());
		report.setLevel3PlusTotal(report.getLevel3PlusTotal() + row.getLevel3Plus());
		report.setInAfaraNomTotal(report.getInAfaraNomTotal() + row.getInAfaraNom());
		report.setTotalPrezentaTotal(report.getTotalPrezentaTotal() + row.getTotalPrezenta());
		report.setCoeficientStructuralTotal(report.getCoeficientStructuralTotal() + row.getCoeficientStructural());
		report.setNotaFinalaPrezentaTotal(report.getNotaFinalaPrezentaTotal() + row.getNotaFinalaPrezenta());
		
	}

}
