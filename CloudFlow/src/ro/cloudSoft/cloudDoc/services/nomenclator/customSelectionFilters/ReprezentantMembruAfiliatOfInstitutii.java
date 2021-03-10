package ro.cloudSoft.cloudDoc.services.nomenclator.customSelectionFilters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersResponseModel;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class ReprezentantMembruAfiliatOfInstitutii extends CustomNomenclatorSelectionFiltersExecution {

	@Override
	protected CustomNomenclatorSelectionFiltersResponseModel doExecute(SecurityManager userSecurity, CustomNomenclatorSelectionFiltersRequestModel requestModel) throws AppException {
		
		CustomNomenclatorSelectionFiltersResponseModel responseModel = new CustomNomenclatorSelectionFiltersResponseModel();
		responseModel.setSelectable(false);
		
		String tipInstitutieIdAsString = requestModel.getAttributeValueByKey().get(NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE);
		
		if (StringUtils.isBlank(tipInstitutieIdAsString)) {
			responseModel.setSelectable(false); // explicit set as "false"
		} else {
			
			NomenclatorValue tipInstitutieNomenclatorValue = nomenclatorValueDao.find(Long.valueOf(tipInstitutieIdAsString));
			String tipInstitutie = NomenclatorValueUtils.getAttributeValueAsString(tipInstitutieNomenclatorValue, NomenclatorConstants.TIP_INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE);
			if (NomenclatorConstants.TIP_INSTITUTII_VALUE_FOR_MEMBRU_AFILIAT.equals(tipInstitutie)) {
				List<NomenclatorValue> institutii = nomenclatorValueDao.findByNomenclatorCodeAndAttribute(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_TIP_INSTITUTIE, tipInstitutieIdAsString);
				if (CollectionUtils.isEmpty(institutii)) {
					responseModel.setSelectable(false);
				} else {
					responseModel.setSelectable(true);
					responseModel.setAttributeValuesByKey(new HashMap<>());
					List<String> values = new ArrayList<>();
					for (NomenclatorValue institutie: institutii) {
						values.add(institutie.getId().toString());
					}
					responseModel.getAttributeValuesByKey().put(NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_INSTITUTIE, values);
				}
			} else {
				responseModel.setSelectable(false);
			}			
		}
		
		return responseModel;
	}

}
