package ro.cloudSoft.cloudDoc.plugins;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.UserAndNomenclatorPersonRelation;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.common.utils.DateUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public class JexlUserFunctions {

	private NomenclatorValueDao getNomenclatorValueDao() {
		return SpringUtils.getBean(NomenclatorValueDao.class);
	}

	private UserPersistencePlugin getUserPersistencePlugin() {
		return SpringUtils.getBean(UserPersistencePlugin.class);
	}
	
	public boolean hasRecordInConcediiPersonalArbForCurrentYear(Integer userId) {
		String nomenclatorCode = NomenclatorConstants.CONCEDII_PERSONAL_ARB;
		UserAndNomenclatorPersonRelation userAndNomenclatorPersonRelationByUserId = getUserPersistencePlugin().getUserAndNomenclatorPersonRelationByUserId(Long.parseLong(userId.toString()));
		String attributeNumeKey = NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_NUME;
		String attributeNumeValue = userAndNomenclatorPersonRelationByUserId.getNomenclatorPerson().getId().toString();
		String attributeAnKey= NomenclatorConstants.CONCEDII_PERSONAL_ARB_ATTRIBUTE_KEY_AN;
		String attributeAnValue = DateUtils.getCurrentYear() + "";
		List<NomenclatorValue> result = getNomenclatorValueDao().findByNomenclatorCodeAndAttributes(nomenclatorCode, attributeNumeKey, attributeNumeValue, attributeAnKey, attributeAnValue );
		if (CollectionUtils.isNotEmpty(result)) {
			return true;
		}
		return false;
	}

}
