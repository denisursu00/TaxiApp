package ro.cloudSoft.cloudDoc.services.nomenclator.customSelectionFilters;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttribute;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersResponseModel;
import ro.cloudSoft.common.utils.spring.SpringUtils;

public abstract class CustomNomenclatorSelectionFiltersExecution {
	
	protected NomenclatorDao nomenclatorDao;
	protected NomenclatorValueDao nomenclatorValueDao;
	
	protected Nomenclator nomenclator;
	
	public CustomNomenclatorSelectionFiltersResponseModel execute(SecurityManager userSecurity, CustomNomenclatorSelectionFiltersRequestModel requestModel) throws AppException {
		resolveDependencies();
		nomenclator = this.nomenclatorDao.find(requestModel.getNomenclatorId());
		return doExecute(userSecurity, requestModel);
	}
	
	protected abstract CustomNomenclatorSelectionFiltersResponseModel doExecute(SecurityManager userSecurity, CustomNomenclatorSelectionFiltersRequestModel requestModel) throws AppException;
	
	private void resolveDependencies() {
		nomenclatorDao = SpringUtils.getBean(NomenclatorDao.class);
		nomenclatorValueDao = SpringUtils.getBean(NomenclatorValueDao.class);
	}
	
	protected String getAttributeNameByKey(String attributeKey) {
		for (NomenclatorAttribute attribute : nomenclator.getAttributes()) {
			if (attribute.getKey().equalsIgnoreCase(attributeKey)) {
				return attribute.getName();
			}
		}
		throw new RuntimeException("atribute name not found by key [" + attributeKey + "]");
	}
}
