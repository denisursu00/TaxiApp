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
import ro.cloudSoft.cloudDoc.dao.parameters.ParametersDao;
import ro.cloudSoft.cloudDoc.domain.arb.MembruReprezentantiComisieSauGL.Calitate;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NotaGeneralaReportRowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.RaspunsuriBanciReportRowModel;
import ro.cloudSoft.cloudDoc.services.parameters.ParametersService;
import ro.cloudSoft.cloudDoc.utils.DocumentTypeUtils;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.MapUtils;

public class NotaGeneralaReportPreparator {

	private NotaGeneralaReportFilterModel filter;
	private List<Document> reportDocuments;
	private Map<Long, NomenclatorValueModel> nomenclatorInstitutiiValuesById;
	private DocumentType prezentaComisiiGlDocumentType;
	private Map<String, RaspunsuriBanciReportRowModel> raspunsuriBanciRowByDenBanca;
	private ArbConstants arbConstants;
	private WorkflowInstanceDao workflowInstanceDao;
	private ParametersService parametersService;
	private LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher;
	private List<Long> institutiiMembriiArbIds;
	private Map<String, Long> nrInterogariByBanca;
	private Map<Long, Long> nrRaspunsuriByBanca;

	public NotaGeneralaReportPreparator(NotaGeneralaReportFilterModel filter, List<Document> reportDocuments,
			Map<Long, NomenclatorValueModel> nomenclatorInstitutiiValuesById, DocumentType prezentaComisiiGlDocumentType,
			Map<String, RaspunsuriBanciReportRowModel> raspunsuriBanciRowByDenBanca, ArbConstants arbConstants, WorkflowInstanceDao workflowInstanceDao,
			ParametersService parametersService, LevelByFunctiiPersoaneAndInstitutiiSearcher levelSearcher, List<Long> institutiiMembriiArbIds, Map<String, Long> nrInterogariByBanca,
			Map<Long, Long> nrRaspunsuriByBanca) {
		super();
		this.filter = filter;
		this.reportDocuments = reportDocuments;
		this.nomenclatorInstitutiiValuesById = nomenclatorInstitutiiValuesById;
		this.prezentaComisiiGlDocumentType = prezentaComisiiGlDocumentType;
		this.raspunsuriBanciRowByDenBanca = raspunsuriBanciRowByDenBanca;
		this.arbConstants = arbConstants;
		this.workflowInstanceDao = workflowInstanceDao;
		this.parametersService = parametersService;
		this.levelSearcher = levelSearcher;
		this.institutiiMembriiArbIds = institutiiMembriiArbIds;
		this.nrInterogariByBanca = nrInterogariByBanca;
		this.nrRaspunsuriByBanca = nrRaspunsuriByBanca;
	}

	public List<NotaGeneralaReportRowModel> prepareReport() throws AppException {
		SetMultimap<Long, Map<Long, String>> infoParticipantiArbByInstitutii = HashMultimap.create();

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
				if (workflowInstanceDao.hasFinishedStatusWorkflowInstanceByDocumentId(reportDocument.getDocumentLocationRealName(), reportDocument.getId())) {

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
								infoParticipantiArbByInstitutii.put(institutieId, infoParticipantiByMetadataDefId);
							}
						}
					}
				}
				if (documentHasArbMembers) {
					numarTotalDeSedinteArb++;
				}
			}
		}

		List<NotaGeneralaReportRowModel> rows = new ArrayList<>();

		int level0Percentage = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_0);
		int level1Percentage = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_1);
		int level2Percentage = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_2);
		int level3Percentage = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_3);
		int level3PlusPercentage = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_LEVEL_3_PLUS);
		int inAfaraNomPercentage = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_IN_AFARA_NOMNINALIZARILOR);

		double coeficientPondereNotaPrezentaStructura = parametersService.getParamaterValueAsDoubleByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_PREZENTA_STRUCTURA) / 100;
		double coeficientPondereNotaPrezentaNumar = parametersService.getParamaterValueAsDoubleByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_PREZENTA_NUMAR) / 100;
		double coeficientPunctajMaxim = parametersService.getParamaterValueAsDoubleByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PUNCTAJ_MAXIM);
		double coeficientPondereNumarTotalSedinteArb = parametersService.getParamaterValueAsDoubleByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NUMAR_SEDINTE_PONDERAT) / 100;
		double coeficientPondereNotaFinalaPrezenta = parametersService.getParamaterValueAsDoubleByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_FINALA_PREZENTA) / 100;
		double coeficientPondereNotaTotalaRaspunsuriBanci = parametersService.getParamaterValueAsDoubleByParameterName(ParameterConstants.PARAM_NAME_RAPORT_COEFICIENT_PROCENTUAL_NOTA_TOTALA_RASPUNSURI_BANCI) / 100;


		for (Long bancaKey : infoParticipantiArbByInstitutii.keySet()) {

			NotaGeneralaReportRowModel row = new NotaGeneralaReportRowModel();

			NomenclatorValueModel institutiteNomenclatorValueModel = nomenclatorInstitutiiValuesById.get(bancaKey);
			row.setBanca(NomenclatorValueUtils.getAttributeValueAsString(institutiteNomenclatorValueModel, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE));

			double inAfaraNom = 0;
			double level0 = 0;
			double level1 = 0;
			double level2 = 0;
			double level3 = 0;
			double level3Plus = 0;
			int prezentaPebanca = infoParticipantiArbByInstitutii.get(bancaKey).size();
			for (Map<Long, String> infoPartByMtdDefId : infoParticipantiArbByInstitutii.get(bancaKey)) {
				String functia = infoPartByMtdDefId.get(metadataDefinitionFunctie.getId());
				String calitateString = infoPartByMtdDefId.get(metadataDefinitionCalitate.getId());
				if (StringUtils.isBlank(calitateString)) {
					level3 = level3 + 1;
					continue;
				}
				Calitate calitate = Calitate.valueOf(calitateString.toUpperCase());
				if (calitate == null) {
					level3 = level3 + 1;
				} else	if (calitate.equals(Calitate.INLOCUITOR)) {
					inAfaraNom = inAfaraNom + 1; 
				} else if (calitate.equals(Calitate.TITULAR) || calitate.equals(Calitate.SUPLEANT)) {
					String levelName = levelSearcher.getLevelNameByFunctieAndInstitutieId(functia, bancaKey);
					if (StringUtils.isBlank(levelName)) {
						level3 = level3 + 1;
					} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_0)) {
						level0 = level0 + 1;
					} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_1)) {
						level1 = level1 + 1;
					} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_2)) {
						level2 = level2 +1;
					} else if (levelName.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_3)) {
						level3 = level3 + 1;
					} else {
						level3Plus = level3Plus + 1;
					}
				}
			}

			double totalPrezenta = level0 + level1 + level2 + level3 + level3Plus + inAfaraNom;

			double coeficientStructural = 0;
			coeficientStructural += level0 * level0Percentage / 100;
			coeficientStructural += level1 * level1Percentage / 100;
			coeficientStructural += level2 * level2Percentage / 100;
			coeficientStructural += level3 * level3Percentage / 100;
			coeficientStructural += level3Plus * level3PlusPercentage / 100;
			coeficientStructural += inAfaraNom * inAfaraNomPercentage / 100;
			if (totalPrezenta == 0) {
				coeficientStructural = 0;
			} else {
				coeficientStructural /= totalPrezenta;
			}

			double coeficientStructuralStructura = coeficientStructural;
			double notaPrezentaStructura = Double.min(coeficientPunctajMaxim, coeficientStructuralStructura * coeficientPunctajMaxim);
			double numarSedintePonderat = numarTotalDeSedinteArb * coeficientPondereNumarTotalSedinteArb;
			double coeficientStructuralNumar = coeficientStructural;
			double notaPrezentaNumar = Double.min(coeficientPunctajMaxim,(coeficientStructuralNumar * prezentaPebanca / numarSedintePonderat * coeficientPunctajMaxim));
			double notaFinalaPrezenta = notaPrezentaNumar * coeficientPondereNotaPrezentaNumar + notaPrezentaStructura * coeficientPondereNotaPrezentaStructura;

			RaspunsuriBanciReportRowModel raspunsuriBanci = raspunsuriBanciRowByDenBanca.get(row.getBanca());
			if (raspunsuriBanci == null) {
				raspunsuriBanci = new RaspunsuriBanciReportRowModel();
			}
			
			long numarTotalInterogariPeBanca = MapUtils.getAndInitIfNull(nrInterogariByBanca, row.getBanca(), new Long(0));
			long nrTotalRaspunsuriPeBanca = MapUtils.getAndInitIfNull(nrRaspunsuriByBanca, bancaKey, new Long(0));
			double notaNrRaspunsuri = 0;
			if (numarTotalInterogariPeBanca != 0) {
				notaNrRaspunsuri = nrTotalRaspunsuriPeBanca / numarTotalInterogariPeBanca;
			}
			notaNrRaspunsuri = Double.min(coeficientPunctajMaxim, notaNrRaspunsuri);

			double notaFinalaBanca = notaFinalaPrezenta * coeficientPondereNotaFinalaPrezenta
					+ raspunsuriBanci.getNotaTotalaRaspunsuriBanci() * coeficientPondereNotaTotalaRaspunsuriBanci;
			row.setNotaFinalaBanca(notaFinalaBanca);

			rows.add(row);
		}
		

		
		return rows;
	}


}
