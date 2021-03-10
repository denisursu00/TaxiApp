package ro.cloudSoft.cloudDoc.presentation.server.converters.deplasariDeconturi;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.deplasariDeconturi.DeplasariDeconturiDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb.ValutaForCheltuieliArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb.ValutaForCheltuieliReprezentantArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont.ApelativReprezentantArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont.ModalitatePlataForDecontEnum;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.CheltuialaArbModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.CheltuialaReprezentantArbModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.DeplasareDecontModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi.DeplasareDecontViewModel;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class DeplasariDeconturiConverter {

	private DeplasariDeconturiDao deplasariDeconturiDao;
	private NomenclatorValueDao nomenclatorValueDao;
	private CheltuieliArbConverter cheltuieliArbConverter;
	private CheltuieliReprezentantArbConverter cheltuieliReprezentantArbConverter;
	
	public void setDeplasariDeconturiDao(DeplasariDeconturiDao deplasariDeconturiDao) {
		this.deplasariDeconturiDao = deplasariDeconturiDao;
	}
	
	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
	
	public void setCheltuieliArbConverter(CheltuieliArbConverter cheltuieliArbConverter) {
		this.cheltuieliArbConverter = cheltuieliArbConverter;
	}
	
	public void setCheltuieliReprezentantArbConverter(
			CheltuieliReprezentantArbConverter cheltuieliReprezentantArbConverter) {
		this.cheltuieliReprezentantArbConverter = cheltuieliReprezentantArbConverter;
	}

	public DeplasareDecont getEntityFromModel(DeplasareDecontModel model) {
		
		DeplasareDecont entity = null;
		
		if (model.getId() != null) {
			entity = deplasariDeconturiDao.findById(model.getId());
		} else {
			entity = new DeplasareDecont();
		}
		
		entity.setApelativ(ApelativReprezentantArbEnum.valueOf(model.getApelativ()));
		entity.setReprezentantArb(nomenclatorValueDao.find(model.getReprezentantArbId()));
		entity.setNumarDecizie(model.getNumarDecizie());
		entity.setDocumentId(model.getDocumentId());
		entity.setDocumentLocationRealName(model.getDocumentLocationRealName());
		entity.setDenumireInstitutie(model.getDenumireInstitutie());
		entity.setDataDecizie(model.getDataDecizie());
		entity.setOrganismId(model.getOrganismId());
		entity.setDenumireComitet(model.getDenumireComitet());
		entity.setNumarDeplasariEfectuate(model.getNumarDeplasariEfectuate());
		entity.setNumarDeplasariBugetateRamase(model.getNumarDeplasariBugetateRamase());
		entity.setEveniment(model.getEveniment());
		entity.setTara(model.getTara());
		entity.setOras(model.getOras());
		entity.setDataPlecare(model.getDataPlecare());
		entity.setDataSosire(model.getDataSosire());
		entity.setDataConferintaInceput(model.getDataConferintaInceput());
		entity.setDataConferintaSfarsit(model.getDataConferintaSfarsit());
		entity.setNumarNopti(model.getNumarNopti());
		entity.setMinutaIntalnireTransmisa(model.isMinutaIntalnireTransmisa());
		entity.setObservatii(model.getObservatii());
		entity.setDetaliiNumarDeplasariBugetateNomenclatorValueId(model.getDetaliiNumarDeplasariBugetateNomenclatorValueId());
		
		entity.setCheltuieliArbTitularDecont(model.getCheltuieliArbTitularDecont());
		entity.setCheltuieliArbTipDecont(model.getCheltuieliArbTipDecont());
		entity.setCheltuieliArbDataDecont(model.getCheltuieliArbDataDecont());
		
		List<CheltuialaArb> cheltuieliArb = new ArrayList<>();
		if (!CollectionUtils.isEmpty(model.getCheltuieliArb())) {
			for (CheltuialaArbModel cheltuialaArbModel : model.getCheltuieliArb()) {
				CheltuialaArb cheltuialaArb = cheltuieliArbConverter.getEntityFromModel(cheltuialaArbModel);
				cheltuialaArb.setDeplasareDecont(entity);
				cheltuieliArb.add(cheltuialaArb);
			}
		}
		entity.setCheltuieliArb(cheltuieliArb);
		
		entity.setCheltuieliReprezentantArbDiurnaZilnica(model.getCheltuieliReprezentantArbDiurnaZilnica());
		entity.setCheltuieliReprezentantArbDiurnaZilnicaValuta(model.getCheltuieliReprezentantArbDiurnaZilnicaValuta());
		entity.setCheltuieliReprezentantArbDiurnaZilnicaCursValutar(model.getCheltuieliReprezentantArbDiurnaZilnicaCursValutar());
		if (model.getCheltuieliReprezentantArbDiurnaZilnicaModalitatePlata() != null) {
			entity.setCheltuieliReprezentantArbDiurnaZilnicaModalitatePlata(ModalitatePlataForDecontEnum.valueOf(model.getCheltuieliReprezentantArbDiurnaZilnicaModalitatePlata()));
		}
		entity.setCheltuieliReprezentantArbNumarZile(model.getCheltuieliReprezentantArbNumarZile());
		entity.setCheltuieliReprezentantArbTotalDiurna(calculateRonValueForTotalDiurna(model.getCheltuieliReprezentantArbDiurnaZilnica(), model.getCheltuieliReprezentantArbDiurnaZilnicaValuta(), model.getCheltuieliReprezentantArbDiurnaZilnicaCursValutar(), model.getCheltuieliReprezentantArbNumarZile()));
		entity.setCheltuieliReprezentantArbAvansPrimitSuma(model.getCheltuieliReprezentantArbAvansPrimitSuma());
		entity.setCheltuieliReprezentantArbAvansPrimitSumaValuta(model.getCheltuieliReprezentantArbAvansPrimitSumaValuta());
		entity.setCheltuieliReprezentantArbAvansPrimitSumaCursValutar(model.getCheltuieliReprezentantArbAvansPrimitSumaCursValutar());
		if (model.getCheltuieliReprezentantArbAvansPrimitCardSauNumerar() != null) {
			entity.setCheltuieliReprezentantArbAvansPrimitCardSauNumerar(ModalitatePlataForDecontEnum.valueOf(model.getCheltuieliReprezentantArbAvansPrimitCardSauNumerar()));
		}
		
		List<CheltuialaReprezentantArb> cheltuieliReprezentantArb = new ArrayList<>();
		if (!CollectionUtils.isEmpty(model.getCheltuieliReprezentantArb())) {
			for (CheltuialaReprezentantArbModel cheltuialaReprezentantArbModel : model.getCheltuieliReprezentantArb()) {
				CheltuialaReprezentantArb cheltuialaReprezentantArb = cheltuieliReprezentantArbConverter.getEntityFromModel(cheltuialaReprezentantArbModel);
				cheltuialaReprezentantArb.setDeplasareDecont(entity);
				cheltuieliReprezentantArb.add(cheltuialaReprezentantArb);
			}
		}
		entity.setCheltuieliReprezentantArb(cheltuieliReprezentantArb);
		
		return entity;
	}

	public List<DeplasareDecontViewModel> getDeplasariDeconturiViewModelsFromEntities(
			List<DeplasareDecont> entities) {
		List<DeplasareDecontViewModel> deplasariDeconturiViewModels = new ArrayList<DeplasareDecontViewModel>();
		for (DeplasareDecont deplasareDecont : entities) {
			DeplasareDecontViewModel deplasareDecontViewModel = getDeplasareDecontViewModelFromEntity(deplasareDecont);
			deplasariDeconturiViewModels.add(deplasareDecontViewModel);
		}
		return deplasariDeconturiViewModels;
	}

	private DeplasareDecontViewModel getDeplasareDecontViewModelFromEntity(DeplasareDecont entity) {

		DeplasareDecontViewModel model = new DeplasareDecontViewModel();
		
		model.setId(entity.getId());
		
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMMMM");
		model.setMonth(simpleDateformat.format(entity.getDataDecizie()).toUpperCase());
		model.setNumarInregistrare(entity.getNumarInregistrare());
		model.setApelativ(entity.getApelativ().toString());
		
		String nomenclatorValuePersoanaId = NomenclatorValueUtils.getAttributeValueAsString(entity.getReprezentantArb(), NomenclatorConstants.REPREZENTANT_ARB_ATTRIBUTE_KEY_PERSOANA_ID);
		String numeReprezentantArb = NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(Long.valueOf(nomenclatorValuePersoanaId)), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME);
		String prenumeReprezentantArb = NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(Long.valueOf(nomenclatorValuePersoanaId)), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME);
		model.setDenumireReprezentantArb(numeReprezentantArb + " " + prenumeReprezentantArb);
		
		model.setDenumireInstitutie(entity.getDenumireInstitutie());
		model.setFunctie(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(Long.valueOf(nomenclatorValuePersoanaId)), NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_FUNCTIA));
		model.setNumarDecizie(entity.getNumarDecizie());
		model.setDataDecizie(entity.getDataDecizie());
		model.setDenumireOrganism(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(entity.getOrganismId()), NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_DENUMIRE));
		model.setAbreviereOrganism(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueDao.find(entity.getOrganismId()), NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_ABREVIERE));
		model.setDenumireComitet(entity.getDenumireComitet());
		model.setNumarDeplasariEfectuate(entity.getNumarDeplasariEfectuate());
		model.setNumarDeplasariBugetateRamase(entity.getNumarDeplasariBugetateRamase());
		model.setEveniment(entity.getEveniment());
		model.setTara(entity.getTara());
		model.setOras(entity.getOras());
		model.setDataPlecare(entity.getDataPlecare());
		model.setDataSosire(entity.getDataSosire());
		model.setDataConferintaInceput(entity.getDataConferintaInceput());
		model.setDataConferintaSfarsit(entity.getDataConferintaSfarsit());
		model.setNumarNopti(entity.getNumarNopti());
		model.setObservatii(entity.getObservatii());
		model.setDetaliiNumarDeplasariBugetateNomenclatorValueId(entity.getDetaliiNumarDeplasariBugetateNomenclatorValueId());
		
		model.setCheltuieliArbTitularDecont(entity.getCheltuieliArbTitularDecont());
		model.setCheltuieliArbTipDecont(entity.getCheltuieliArbTipDecont());
		model.setCheltuieliArbDataDecont(entity.getCheltuieliArbDataDecont());
		
		BigDecimal totalCheltuieliArbValutaEur = BigDecimal.ZERO;
		BigDecimal totalCheltuieliArbValutaUsd = BigDecimal.ZERO;
		BigDecimal totalCheltuieliArbValutaRon = BigDecimal.ZERO;
		BigDecimal totalCheltuieliArbRon = BigDecimal.ZERO;
		
		List<CheltuialaArbModel> cheltuialaArbModels = new ArrayList<>();
		if (!CollectionUtils.isEmpty(entity.getCheltuieliArb())) {
			for (CheltuialaArb cheltuialaArb : entity.getCheltuieliArb()) {
				CheltuialaArbModel cheltuialaArbModel = cheltuieliArbConverter.getModelFromEntity(cheltuialaArb);
				cheltuialaArbModels.add(cheltuialaArbModel);
				
				if (cheltuialaArb.getValuta().equals(ValutaForCheltuieliArbEnum.EUR)) {
					
					totalCheltuieliArbValutaEur = totalCheltuieliArbValutaEur.add(cheltuialaArb.getValoareCheltuiala());
					
					BigDecimal totalCheltuieliArbValutaEurConvertedInRon = cheltuialaArb.getValoareCheltuiala().multiply(cheltuialaArb.getCursValutar());
					totalCheltuieliArbRon = totalCheltuieliArbRon.add(totalCheltuieliArbValutaEurConvertedInRon);
					
				} else if (cheltuialaArb.getValuta().equals(ValutaForCheltuieliArbEnum.USD)) {
					
					totalCheltuieliArbValutaUsd = totalCheltuieliArbValutaUsd.add(cheltuialaArb.getValoareCheltuiala());
					
					BigDecimal totalCheltuieliArbValutaUsdConvertedInRon = cheltuialaArb.getValoareCheltuiala().multiply(cheltuialaArb.getCursValutar());
					totalCheltuieliArbRon = totalCheltuieliArbRon.add(totalCheltuieliArbValutaUsdConvertedInRon);
					
				} else if (cheltuialaArb.getValuta().equals(ValutaForCheltuieliArbEnum.RON)) {
					
					totalCheltuieliArbValutaRon = totalCheltuieliArbValutaRon.add(cheltuialaArb.getValoareCheltuiala());
					
					totalCheltuieliArbRon = totalCheltuieliArbRon.add(cheltuialaArb.getValoareCheltuiala());
				}
			}
		}
		model.setCheltuieliArb(cheltuialaArbModels);
		
		model.setTotalCheltuieliArbValutaEur(totalCheltuieliArbValutaEur);
		model.setTotalCheltuieliArbValutaUsd(totalCheltuieliArbValutaUsd);
		model.setTotalCheltuieliArbValutaRon(totalCheltuieliArbValutaRon);
		model.setTotalCheltuieliArbRon(totalCheltuieliArbRon);
		
		BigDecimal totalCheltuieliReprezentantArbValutaEur = BigDecimal.ZERO;
		BigDecimal totalCheltuieliReprezentantArbValutaUsd = BigDecimal.ZERO;
		BigDecimal totalCheltuieliReprezentantArbValutaRon = BigDecimal.ZERO;
		BigDecimal totalCheltuieliReprezentantArbRon = BigDecimal.ZERO;
		
		List<CheltuialaReprezentantArbModel> cheltuieliReprezentantArbModels = new ArrayList<>();
		if (!CollectionUtils.isEmpty(entity.getCheltuieliReprezentantArb())) {
			for (CheltuialaReprezentantArb cheltuialaReprezentantArb : entity.getCheltuieliReprezentantArb()) {
				CheltuialaReprezentantArbModel cheltuialaReprezentantArbModel = cheltuieliReprezentantArbConverter.getModelFromEntity(cheltuialaReprezentantArb);
				cheltuieliReprezentantArbModels.add(cheltuialaReprezentantArbModel);
				
				if (cheltuialaReprezentantArb.getValuta().equals(ValutaForCheltuieliReprezentantArbEnum.EUR)) {
					
					totalCheltuieliReprezentantArbValutaEur = totalCheltuieliReprezentantArbValutaEur.add(cheltuialaReprezentantArb.getValoareCheltuiala());
					
					BigDecimal totalCheltuieliReprezentantArbValutaEurConvertedInRon = cheltuialaReprezentantArb.getValoareCheltuiala().multiply(cheltuialaReprezentantArb.getCursValutar());
					totalCheltuieliReprezentantArbRon = totalCheltuieliReprezentantArbRon.add(totalCheltuieliReprezentantArbValutaEurConvertedInRon);
					
				} else if (cheltuialaReprezentantArb.getValuta().equals(ValutaForCheltuieliReprezentantArbEnum.USD)) {
					
					totalCheltuieliReprezentantArbValutaUsd = totalCheltuieliReprezentantArbValutaUsd.add(cheltuialaReprezentantArb.getValoareCheltuiala());
					
					BigDecimal totalCheltuieliReprezentantArbValutaUsdConvertedInRon = cheltuialaReprezentantArb.getValoareCheltuiala().multiply(cheltuialaReprezentantArb.getCursValutar());
					totalCheltuieliReprezentantArbRon = totalCheltuieliReprezentantArbRon.add(totalCheltuieliReprezentantArbValutaUsdConvertedInRon);
					
				} else if (cheltuialaReprezentantArb.getValuta().equals(ValutaForCheltuieliReprezentantArbEnum.RON)) {
					
					totalCheltuieliReprezentantArbValutaRon = totalCheltuieliReprezentantArbValutaRon.add(cheltuialaReprezentantArb.getValoareCheltuiala());
					
					totalCheltuieliReprezentantArbRon = totalCheltuieliReprezentantArbRon.add(cheltuialaReprezentantArb.getValoareCheltuiala());
				}
			}
		}
		
		model.setCheltuieliReprezentantArb(cheltuieliReprezentantArbModels);
		
		model.setTotalCheltuieliReprezentantArbValutaEur(totalCheltuieliReprezentantArbValutaEur);
		model.setTotalCheltuieliReprezentantArbValutaUsd(totalCheltuieliReprezentantArbValutaUsd);
		model.setTotalCheltuieliReprezentantArbValutaRon(totalCheltuieliReprezentantArbValutaRon);
		
		
		model.setTotalDiurnaRon(entity.getCheltuieliReprezentantArbTotalDiurna());
		
		BigDecimal totalDiurna = model.getTotalDiurnaRon();
		if (entity.getCheltuieliReprezentantArbTotalDiurna() != null) {
			totalCheltuieliReprezentantArbRon = totalCheltuieliReprezentantArbRon.add(totalDiurna);			
		}
		
		if (StringUtils.isNotBlank(entity.getCheltuieliReprezentantArbAvansPrimitSumaValuta())) {
			if (entity.getCheltuieliReprezentantArbAvansPrimitSumaValuta().equals(ValutaForCheltuieliReprezentantArbEnum.RON.toString())) {
				model.setAvansPrimitRon(entity.getCheltuieliReprezentantArbAvansPrimitSuma());
			} else {
				BigDecimal avansPrimitSumaInRon = entity.getCheltuieliReprezentantArbAvansPrimitSuma().multiply(entity.getCheltuieliReprezentantArbAvansPrimitSumaCursValutar());
				model.setAvansPrimitRon(avansPrimitSumaInRon);
			}
			model.setTotalDeIncasat(totalCheltuieliReprezentantArbRon.subtract(model.getAvansPrimitRon()));
		}
		
		model.setTotalCheltuieliReprezentantArbRon(totalCheltuieliReprezentantArbRon);
		
		model.setAnulat(entity.isAnulat());
		model.setMotivAnulare(entity.getMotivAnulare());
		model.setFinalizat(entity.isFinalizat());
		
		return model;
	}

	public DeplasareDecontModel getModelFromEntity(DeplasareDecont entity) {
		DeplasareDecontModel model = new DeplasareDecontModel();
		model.setId(entity.getId());
		model.setApelativ(entity.getApelativ().toString());
		model.setReprezentantArbId(entity.getReprezentantArb().getId());
		model.setNumarDecizie(entity.getNumarDecizie());
		model.setDocumentId(entity.getDocumentId());
		model.setDocumentLocationRealName(entity.getDocumentLocationRealName());
		model.setDenumireInstitutie(entity.getDenumireInstitutie());
		

		NomenclatorValue organismNomenclatorValue = nomenclatorValueDao.find(entity.getOrganismId());
		String denumireOrganism = NomenclatorValueUtils.getAttributeValueAsString(organismNomenclatorValue, NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_DENUMIRE);
		model.setDenumireOrganism(denumireOrganism);
		String abreviereOrganism = NomenclatorValueUtils.getAttributeValueAsString(organismNomenclatorValue, NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_ABREVIERE);
		model.setAbreviereOrganism(abreviereOrganism);
		
		model.setDataDecizie(entity.getDataDecizie());
		model.setOrganismId(entity.getOrganismId());
		model.setDenumireComitet(entity.getDenumireComitet());
		model.setNumarDeplasariEfectuate(entity.getNumarDeplasariEfectuate());
		model.setNumarDeplasariBugetateRamase(entity.getNumarDeplasariBugetateRamase());
		model.setEveniment(entity.getEveniment());
		model.setTara(entity.getTara());
		model.setOras(entity.getOras());
		model.setDataPlecare(entity.getDataPlecare());
		model.setDataSosire(entity.getDataSosire());
		model.setDataConferintaInceput(entity.getDataConferintaInceput());
		model.setDataConferintaSfarsit(entity.getDataConferintaSfarsit());
		model.setNumarNopti(entity.getNumarNopti());
		model.setMinutaIntalnireTransmisa(entity.isMinutaIntalnireTransmisa());
		model.setObservatii(entity.getObservatii());
		
		model.setCheltuieliArbTitularDecont(entity.getCheltuieliArbTitularDecont());
		model.setCheltuieliArbTipDecont(entity.getCheltuieliArbTipDecont());
		model.setCheltuieliArbDataDecont(entity.getCheltuieliArbDataDecont());
		model.setDetaliiNumarDeplasariBugetateNomenclatorValueId(entity.getDetaliiNumarDeplasariBugetateNomenclatorValueId());
		
		List<CheltuialaArbModel> cheltuialaArbModels = new ArrayList<>();
		if (!CollectionUtils.isEmpty(entity.getCheltuieliArb())) {
			for (CheltuialaArb cheltuialaArb : entity.getCheltuieliArb()) {
				CheltuialaArbModel cheltuialaArbModel = cheltuieliArbConverter.getModelFromEntity(cheltuialaArb);
				cheltuialaArbModels.add(cheltuialaArbModel);
			}
		}
		model.setCheltuieliArb(cheltuialaArbModels);
		
		model.setCheltuieliReprezentantArbDiurnaZilnica(entity.getCheltuieliReprezentantArbDiurnaZilnica());
		model.setCheltuieliReprezentantArbDiurnaZilnicaValuta(entity.getCheltuieliReprezentantArbDiurnaZilnicaValuta());
		model.setCheltuieliReprezentantArbDiurnaZilnicaCursValutar(entity.getCheltuieliReprezentantArbDiurnaZilnicaCursValutar());
		if (entity.getCheltuieliReprezentantArbDiurnaZilnicaModalitatePlata() != null) {
			model.setCheltuieliReprezentantArbDiurnaZilnicaModalitatePlata(entity.getCheltuieliReprezentantArbDiurnaZilnicaModalitatePlata().toString());
		}
		model.setCheltuieliReprezentantArbNumarZile(entity.getCheltuieliReprezentantArbNumarZile());
		model.setCheltuieliReprezentantArbTotalDiurna(entity.getCheltuieliReprezentantArbTotalDiurna());
		model.setCheltuieliReprezentantArbAvansPrimitSuma(entity.getCheltuieliReprezentantArbAvansPrimitSuma());
		model.setCheltuieliReprezentantArbAvansPrimitSumaValuta(entity.getCheltuieliReprezentantArbAvansPrimitSumaValuta());
		model.setCheltuieliReprezentantArbAvansPrimitSumaCursValutar(entity.getCheltuieliReprezentantArbAvansPrimitSumaCursValutar());
		if (entity.getCheltuieliReprezentantArbAvansPrimitCardSauNumerar() != null) {
			model.setCheltuieliReprezentantArbAvansPrimitCardSauNumerar(entity.getCheltuieliReprezentantArbAvansPrimitCardSauNumerar().toString());
		}
		
		List<CheltuialaReprezentantArbModel> cheltuieliReprezentantArbModels = new ArrayList<>();
		if (!CollectionUtils.isEmpty(entity.getCheltuieliReprezentantArb())) {
			for (CheltuialaReprezentantArb cheltuialaReprezentantArb : entity.getCheltuieliReprezentantArb()) {
				CheltuialaReprezentantArbModel cheltuialaReprezentantArbModel = cheltuieliReprezentantArbConverter.getModelFromEntity(cheltuialaReprezentantArb);
				cheltuieliReprezentantArbModels.add(cheltuialaReprezentantArbModel);
			}
		}
		model.setCheltuieliReprezentantArb(cheltuieliReprezentantArbModels);
		
		return model;
	}

	private BigDecimal calculateRonValueForTotalDiurna(BigDecimal cheltuieliReprezentantArbDiurnaZilnica,
			String cheltuieliReprezentantArbDiurnaZilnicaValuta,
			BigDecimal cheltuieliReprezentantArbDiurnaZilnicaCursValutar, Integer cheltuieliReprezentantArbNumarZile) {
		
		BigDecimal totalDiurnaRon = null;
		if (StringUtils.isNotBlank(cheltuieliReprezentantArbDiurnaZilnicaValuta)) {
			if (cheltuieliReprezentantArbDiurnaZilnicaValuta.equals(ValutaForCheltuieliReprezentantArbEnum.RON.toString())) {
				totalDiurnaRon = cheltuieliReprezentantArbDiurnaZilnica.multiply(new BigDecimal(cheltuieliReprezentantArbNumarZile));
			} else {
				totalDiurnaRon = cheltuieliReprezentantArbDiurnaZilnica.multiply(cheltuieliReprezentantArbDiurnaZilnicaCursValutar).multiply(new BigDecimal(cheltuieliReprezentantArbNumarZile));
			}
		}	
		
		return totalDiurnaRon;
	}
}
