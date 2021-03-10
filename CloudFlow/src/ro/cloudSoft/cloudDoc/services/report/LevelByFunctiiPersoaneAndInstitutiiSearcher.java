package ro.cloudSoft.cloudDoc.services.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class LevelByFunctiiPersoaneAndInstitutiiSearcher {

	private NomenclatorService nomenclatorService;

	private Map<String, String> levelNameByDenFunctieAndIdInstitutie;
	private Map<String, String> levelNameByDenumireFunctie;
	private Map<Long, String> levelNameById;
	private Map<Long, String> denumireFunctieById;

	public LevelByFunctiiPersoaneAndInstitutiiSearcher(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
		init();
	}

	private void init() {
		loadLevelNameByIdMap();
		loadLevelNameByFunctiiPersoaneAndDenumiriFunctiiById();
		loadFunctiiInstitutiiByLevel();

	}

	public String getLevelNameByFunctieAndInstitutieId(String denumireFunctie, Long institutieId) {
		if (StringUtils.isEmpty(denumireFunctie)) {
			return null;
		}
		String denumireFunctieFormated = denumireFunctie.trim().toUpperCase();
		String key = buildKeyFromIdFunctieAndIdInstitutie(denumireFunctieFormated, institutieId);
		if (levelNameByDenFunctieAndIdInstitutie.containsKey(key)) {
			return levelNameByDenFunctieAndIdInstitutie.get(key);
		} else if (levelNameByDenumireFunctie.containsKey(denumireFunctieFormated)){
			return levelNameByDenumireFunctie.get(denumireFunctieFormated);
		}
		
		return null;
		
	}

	private void loadLevelNameByIdMap() {
		
		levelNameById = new HashMap();
		List<NomenclatorValueModel> levelsFunctiiPersoane = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.LEVEL_FUNCTII_PERSOANE);
		
		for (NomenclatorValueModel level : levelsFunctiiPersoane) {
			String denumireLevel = NomenclatorValueUtils.getAttributeValueAsString(level, NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTR_KEY_DENUMIRE);
			levelNameById.put(level.getId(), denumireLevel);
		}
	}
	
	private void loadLevelNameByFunctiiPersoaneAndDenumiriFunctiiById() {
		
		levelNameByDenumireFunctie = new HashMap<>();
		denumireFunctieById = new HashMap<>();
		
		List<NomenclatorValueModel> functiiPersoane = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.FUNCTII_PERSOANE);
		
		for (NomenclatorValueModel functie : functiiPersoane) {
			String denumireFunctie = NomenclatorValueUtils.getAttributeValueAsString(functie, NomenclatorConstants.NOMENCLATOR_FUNCTII_PERSOANE_ATTR_KEY_DENUMIRE).trim()
					.toUpperCase();
			Long levelId = NomenclatorValueUtils.getAttributeValueAsLong(functie, NomenclatorConstants.NOMENCLATOR_FUNCTII_PERSOANE_ATTR_KEY_LEVEL);
			String levelName = levelNameById.get(levelId);
			levelNameByDenumireFunctie.put(denumireFunctie, levelName);
			denumireFunctieById.put(functie.getId(), denumireFunctie);
		}
	}
	
	private void loadFunctiiInstitutiiByLevel() {
		List<NomenclatorValueModel> levelFunctiiInstitutiiValues = nomenclatorService
				.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_PER_INSTITUTII_CODE);
		
		levelNameByDenFunctieAndIdInstitutie = new HashMap<>();
		levelFunctiiInstitutiiValues.forEach(value -> {
			
			Long idFunctie = NomenclatorValueUtils.getAttributeValueAsLong(value, NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_PER_INSTITUTII_ATTR_FUNCTIE);
			Long idLevel = NomenclatorValueUtils.getAttributeValueAsLong(value, NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_PER_INSTITUTII_ATTR_LEVEL);
			Long idInstitutie = NomenclatorValueUtils.getAttributeValueAsLong(value, NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_PER_INSTITUTII_ATTR_INSTITUTIE);
			String key = buildKeyFromIdFunctieAndIdInstitutie(denumireFunctieById.get(idFunctie), idInstitutie);
			levelNameByDenFunctieAndIdInstitutie.put(key, levelNameById.get(idLevel));
		});
	}

	private String buildKeyFromIdFunctieAndIdInstitutie(String denumireFunctie, Long idInstitutie) {
		return denumireFunctie + "-" + idInstitutie;
	}

	public Map<String, Long> getLevelIdByNameMap() {

		Map<String, Long> levelIdByName = new HashMap();
		List<NomenclatorValueModel> levelsFunctiiPersoane = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.LEVEL_FUNCTII_PERSOANE);

		for (NomenclatorValueModel level : levelsFunctiiPersoane) {
			String denumireLevel = NomenclatorValueUtils.getAttributeValueAsString(level, NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTR_KEY_DENUMIRE);
			if (denumireLevel.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_0)) {
				levelIdByName.put(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_0, level.getId());
			}
			if (denumireLevel.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_1)) {
				levelIdByName.put(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_1, level.getId());
			}
			if (denumireLevel.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_2)) {
				levelIdByName.put(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_2, level.getId());
			}
			if (denumireLevel.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_3)) {
				levelIdByName.put(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_3, level.getId());
			}
			if (denumireLevel.equals(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_3_PLUS)) {
				levelIdByName.put(NomenclatorConstants.NOMENCLATOR_LEVEL_FUNCTII_PERSOANE_ATTRIBUTE_DENUMIRE_VALUE_FOR_LEVEL_3_PLUS, level.getId());
			}
		}
		return levelIdByName;
	}


}
