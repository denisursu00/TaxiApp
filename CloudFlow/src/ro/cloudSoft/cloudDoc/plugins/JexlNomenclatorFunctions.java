package ro.cloudSoft.cloudDoc.plugins;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class JexlNomenclatorFunctions {
	
	private NomenclatorValueDao nomenclatorValueDao;
	
	public JexlNomenclatorFunctions() {
		resolveDependencies();
	}
	
	private void resolveDependencies() {
		nomenclatorValueDao = SpringUtils.getBean(NomenclatorValueDao.class);
	}
	
	public boolean isTipInstitutieMembruARB(Integer nomenclatorValueId) {
		return isTipInstitutieEqualsWith(nomenclatorValueId, NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_ARB);
	}
	
	public boolean isTipInstitutieMembruAfiliat(Integer nomenclatorValueId) {
		return isTipInstitutieEqualsWith(nomenclatorValueId, NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_AFILIAT);
	}
	
	public boolean isTipInstitutieAlteInstitutii(Integer nomenclatorValueId) {
		return isTipInstitutieEqualsWith(nomenclatorValueId, NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_ALTE_INSTITUTII);
	}
	
	public boolean existsDenumireFunctiePersoana(String denumireFunctiePersoana) {
		List<NomenclatorValue> nomenclatorValues = nomenclatorValueDao.findByNomenclatorCodeAndAttribute(NomenclatorConstants.FUNCTII_PERSOANE, NomenclatorConstants.NOMENCLATOR_FUNCTII_PERSOANE_ATTR_KEY_DENUMIRE, denumireFunctiePersoana);
		return CollectionUtils.isNotEmpty(nomenclatorValues);
	}
	
	private boolean isTipInstitutieEqualsWith(Integer nomenclatorValueId, String denumireTipInstitutieArg) {
		if (nomenclatorValueId == null) {
			return false;
		}
		NomenclatorValue tipInstitutieNomValue = nomenclatorValueDao.find(Long.valueOf(nomenclatorValueId));
		String denumireTipInstitutie = NomenclatorValueUtils.getAttributeValueAsString(tipInstitutieNomValue, NomenclatorConstants.TIP_INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE);
		return denumireTipInstitutieArg.equals(denumireTipInstitutie);
	}
}
