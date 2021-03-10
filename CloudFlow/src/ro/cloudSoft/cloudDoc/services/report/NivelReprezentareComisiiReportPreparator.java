package ro.cloudSoft.cloudDoc.services.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.arb.ReprezentantiComisieSauGLDao;
import ro.cloudSoft.cloudDoc.domain.arb.ReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.GetNomenclatorValuesRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSimpleFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NivelReprezentareComisiiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NivelReprezentareComisiiReportModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.NivelReprezentareComisiiReportRowModel;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;

public class NivelReprezentareComisiiReportPreparator {

	private ReprezentantiComisieSauGLDao reprezentantiComisieSauGLDao;
	private NomenclatorService nomenclatorService;
	
	private NivelReprezentareComisiiReportFilterModel filter;
	
	private List<ReprezentantiComisieSauGL> reprezentanti;
	private Map<Long, NivelReprezentareComisiiReportRowModel> nivelReprezentareComisiiByInstitutieId;
	private List<NomenclatorValueModel> nomenclatorValuesForInstitutiiMembrii;
	private Set<Long> organismeInterneIds;
	private Map<Long, String> denumireInstitutieById;
	private Set<Long> reprezentatiOIOROIds;
	
	
	public NivelReprezentareComisiiReportPreparator(ReprezentantiComisieSauGLDao reprezentantiComisieSauGLDao, NomenclatorService nomenclatorService,
			NivelReprezentareComisiiReportFilterModel filter) throws AppException {
		super();
		this.reprezentantiComisieSauGLDao = reprezentantiComisieSauGLDao;
		this.nomenclatorService = nomenclatorService;
		this.filter = filter;
		
		init();
	}



	private void init() throws AppException {
		reprezentanti = reprezentantiComisieSauGLDao.getAllWithCategorieComisie();
		
		nomenclatorValuesForInstitutiiMembrii = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.INSTITUTII_MEMBRE_ALE_ORGANISMELOR_SI_COMITETELOR_NOMENCLATOR_CODE);
		
		GetNomenclatorValuesRequestModel requestModelForTipOrganism = new GetNomenclatorValuesRequestModel();
		requestModelForTipOrganism.setNomenclatorCode(NomenclatorConstants.TIP_ORGANISM_NOMENCLATOR_CODE);
		requestModelForTipOrganism.setFilters(Arrays.asList(new NomenclatorSimpleFilter(NomenclatorConstants.TIP_ORGANISM_ATTRIBUTE_KEY_COD, NomenclatorConstants.TIP_ORGANISM_ATTRIBUTE_KEY_COD_VALUE_AS_ORGANISME_INTERNE)));
		List<NomenclatorValueModel> tipOrganismInternIds = nomenclatorService.getNomenclatorValues(requestModelForTipOrganism );
		Long tipOrganismInternId = tipOrganismInternIds.get(0).getId();
		
		GetNomenclatorValuesRequestModel requestModel = new GetNomenclatorValuesRequestModel();
		requestModel.setNomenclatorCode(NomenclatorConstants.ORGANISME_NOMENCLATOR_CODE);
		requestModel.setFilters(Arrays.asList(new NomenclatorSimpleFilter(NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_TIP_ORGANISM, "" + tipOrganismInternId)));
		List<NomenclatorValueModel> organismeInterneNomValues = nomenclatorService.getNomenclatorValues(requestModel );
		
		organismeInterneIds = new HashSet<>();
		organismeInterneNomValues.forEach(organismIntern -> {
			organismeInterneIds.add(organismIntern.getId());
		});
		
		denumireInstitutieById = new HashMap<>();
		List<NomenclatorValueModel> nomenclatorValuesForInstitutii = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE);
		nomenclatorValuesForInstitutii.forEach(institutiteNomValues -> {
			String denumireInstitutie = NomenclatorValueUtils.getAttributeValueAsString(institutiteNomValues, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE_INSTITUTIE);
			denumireInstitutieById.put(institutiteNomValues.getId(), denumireInstitutie);
		});
		
		reprezentatiOIOROIds = new HashSet<>();
		List<NomenclatorValueModel> reprezentantiOIORO = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_CODE);
		reprezentantiOIORO.forEach(reprezentantORORO -> {
			reprezentatiOIOROIds.add(NomenclatorValueUtils.getAttributeValueAsLong(reprezentantORORO, NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_INSTITUTIE));
		});
		
		if (filter.getDataExpirareMandatDeLa() != null) {
			filter.setDataExpirareMandatDeLa(DateUtils.nullHourMinutesSeconds(filter.getDataExpirareMandatDeLa()));
		}
		if (filter.getDataExpirareMandatPanaLa() != null) {
			filter.setDataExpirareMandatPanaLa(DateUtils.maximizeHourMinutesSeconds(filter.getDataExpirareMandatPanaLa()));
		}
	}

	public NivelReprezentareComisiiReportModel prepareReport() {
		nivelReprezentareComisiiByInstitutieId = new HashMap<>();
		reprezentanti.forEach(reprezentant -> {
			NomenclatorValue presedinteNomValue = reprezentant.getPresedinte();
			Date dataExpirareMandatPresedinte = reprezentant.getDataExpirareMandatPresedinte();
			appendToRowData(filter, nivelReprezentareComisiiByInstitutieId, presedinteNomValue, dataExpirareMandatPresedinte, true);
			
			NomenclatorValue vicePresedinte1NomValue = reprezentant.getVicepresedinte1();
			Date dataExpirareMandatVicepresedinte1 = reprezentant.getDataExpirareMandatVicepresedinte1();
			appendToRowData(filter, nivelReprezentareComisiiByInstitutieId, vicePresedinte1NomValue, dataExpirareMandatVicepresedinte1, false);
			
			NomenclatorValue vicePresedinte2NomValue = reprezentant.getVicepresedinte2();
			Date dataExpirareMandatVicepresedinte2 = reprezentant.getDataExpirareMandatVicepresedinte2();
			appendToRowData(filter, nivelReprezentareComisiiByInstitutieId, vicePresedinte2NomValue, dataExpirareMandatVicepresedinte2, false);
			
			NomenclatorValue vicePresedinte3NomValue = reprezentant.getVicepresedinte3();
			Date dataExpirareMandatVicepresedinte3 = reprezentant.getDataExpirareMandatVicepresedinte3();
			appendToRowData(filter, nivelReprezentareComisiiByInstitutieId, vicePresedinte3NomValue, dataExpirareMandatVicepresedinte3, false);
		});
		
		Set<Long> organismeFromInstitutiiMembriiIds = new HashSet<>();
		nomenclatorValuesForInstitutiiMembrii.forEach(nomValue -> {
			Long institutieId = NomenclatorValueUtils.getAttributeValueAsLong(nomValue, NomenclatorConstants.INSTITUTII_MEMBRE_ALE_ORGANISMELOR_SI_COMITETELOR_ATTRIBUTE_KEY_INSTITUTIE);
			Long organismId = NomenclatorValueUtils.getAttributeValueAsLongOrNull(nomValue, NomenclatorConstants.INSTITUTII_MEMBRE_ALE_ORGANISMELOR_SI_COMITETELOR_ATTRIBUTE_KEY_ORGANISM);

			if (organismId != null) {
				organismeFromInstitutiiMembriiIds.add(organismId);
			}
			NivelReprezentareComisiiReportRowModel row = nivelReprezentareComisiiByInstitutieId.get(institutieId);
			if (row != null && organismId != null) {
				if ( organismeInterneIds.contains(organismId)) {
					row.setNrReprezOrganismeInterne(row.getNrReprezOrganismeInterne() + 1);
				} else {
					row.setNrReprezOrganismeInternationale(row.getNrReprezOrganismeInternationale() + 1);
				}
			}
		});
	
		
		NivelReprezentareComisiiReportModel report = new NivelReprezentareComisiiReportModel();
		report.setRows(new ArrayList<NivelReprezentareComisiiReportRowModel>(nivelReprezentareComisiiByInstitutieId.values()));
		report.getRows().forEach(row -> {
			int totalReprezentare = row.getNrReprezOrganismeInternationale() + row.getNrReprezOrganismeInterne();
			double procentReprezentare = ((double)totalReprezentare) * 100 / organismeFromInstitutiiMembriiIds.size();
			row.setProcentReprezentare(procentReprezentare);
			row.setTotalReprezentare(totalReprezentare );
			
			report.setTotalPresedintiComisii(report.getTotalPresedintiComisii() + row.getNrPresedintiComisie());
			report.setTotalVicepresedintiComisii(report.getTotalVicepresedintiComisii() + row.getNrVicepresedintiComisie());
			report.setTotalReprezentareOrganismeInternationale(report.getTotalReprezentareOrganismeInternationale() + row.getNrReprezOrganismeInternationale());
			report.setTotalReprezentareOrganismeInterne(report.getTotalReprezentareOrganismeInterne() + row.getNrReprezOrganismeInterne());
			report.setSumaTotalReprezentare(report.getSumaTotalReprezentare() + row.getTotalReprezentare());
			report.setTotalProcentReprezentare(report.getTotalProcentReprezentare() + row.getProcentReprezentare());
		});
		
		Collections.sort(report.getRows(), new Comparator<NivelReprezentareComisiiReportRowModel>() {

			@Override
			public int compare(NivelReprezentareComisiiReportRowModel arg0, NivelReprezentareComisiiReportRowModel arg1) {
				return arg0.getInstitutia().compareTo(arg1.getInstitutia());
			}
			
		});
		
		return report;
	}

	private void appendToRowData(NivelReprezentareComisiiReportFilterModel filter, Map<Long, NivelReprezentareComisiiReportRowModel> nivelReprezentareComisiiByInstitutieId,
			NomenclatorValue persoanaNomValue, Date dataExpirareMandatat, boolean isPresedinte) {
		if (persoanaNomValue != null 
				&& ((filter.getDataExpirareMandatDeLa() == null && filter.getDataExpirareMandatPanaLa() == null) 
						|| ((filter.getDataExpirareMandatDeLa() != null || filter.getDataExpirareMandatPanaLa() != null) && dataExpirareMandatat != null))
				&& (filter.getDataExpirareMandatDeLa() == null  || dataExpirareMandatat.getTime() >= filter.getDataExpirareMandatDeLa().getTime())
				&& (filter.getDataExpirareMandatPanaLa() == null || dataExpirareMandatat.getTime() <= filter.getDataExpirareMandatPanaLa().getTime())) {
			Long institutieId = NomenclatorValueUtils.getAttributeValueAsLong(persoanaNomValue, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_INSTITUTIE);
			if (!reprezentatiOIOROIds.contains(institutieId) || (filter.getInstitutieId() != null && !institutieId.equals(filter.getInstitutieId()))) {
				return;
			}
			if (!nivelReprezentareComisiiByInstitutieId.containsKey(institutieId)) {
				NivelReprezentareComisiiReportRowModel newRow = new NivelReprezentareComisiiReportRowModel();
				newRow.setInstitutia(denumireInstitutieById.get(institutieId));
				nivelReprezentareComisiiByInstitutieId.put(institutieId, newRow );
			}
			NivelReprezentareComisiiReportRowModel row = nivelReprezentareComisiiByInstitutieId.get(institutieId);
			
			if (isPresedinte) {
				row.setNrPresedintiComisie(row.getNrPresedintiComisie() + 1);
			} else {
				row.setNrVicepresedintiComisie(row.getNrVicepresedintiComisie() + 1);
			}
		}
	}

}
