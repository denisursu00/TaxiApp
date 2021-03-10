package ro.cloudSoft.cloudDoc.services.nomenclator.customSelectionFilters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersResponseModel;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class InstitutieOfReprezentantiArbOrganismeInterneSiInternationale extends CustomNomenclatorSelectionFiltersExecution {
	
	public InstitutieOfReprezentantiArbOrganismeInterneSiInternationale() {
	}
	
	@Override
	protected CustomNomenclatorSelectionFiltersResponseModel doExecute(SecurityManager userSecurity, CustomNomenclatorSelectionFiltersRequestModel requestModel) throws AppException {
			
		CustomNomenclatorSelectionFiltersResponseModel responseModel = new CustomNomenclatorSelectionFiltersResponseModel();
		responseModel.setSelectable(false);
		
		String organismIdAsString = requestModel.getAttributeValueByKey().get(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_ORGANSIM);
		String comitetIdAsString = requestModel.getAttributeValueByKey().get(NomenclatorConstants.REPREZENTANTI_ARB_ORGANISME_INTERNE_SI_INTERNATIONALE_ATTRIBUTE_KEY_COMITET);
		
		if (StringUtils.isBlank(organismIdAsString) && StringUtils.isBlank(comitetIdAsString)) {
			responseModel.setSelectable(false); // explicit set as "false"
		} else {
			if (StringUtils.isNotBlank(organismIdAsString)) {
				
				NomenclatorValue organism = nomenclatorValueDao.find(Long.valueOf(organismIdAsString));
				
				Long tipOrganismId = NomenclatorValueUtils.getAttributeValueAsLong(organism, NomenclatorConstants.ORGANISME_ATTRIBUTE_KEY_TIP_ORGANISM);
				NomenclatorValue tipOrganism = nomenclatorValueDao.find(tipOrganismId);
				
				String codTipOrganism = NomenclatorValueUtils.getAttributeValueAsString(tipOrganism, NomenclatorConstants.TIP_ORGANISM_ATTRIBUTE_KEY_COD);
				if (NomenclatorConstants.TIP_ORGANISM_ATTRIBUTE_KEY_COD_VALUE_AS_ORGANISME_INTERNATIONALE.equals(codTipOrganism)) {
					if (StringUtils.isNotBlank(comitetIdAsString)) {
						prepareResponseByAttributeAndValue(responseModel, NomenclatorConstants.INSTITUTII_MEMBRE_ALE_ORGANISMELOR_SI_COMITETELOR_ATTRIBUTE_KEY_COMITET, comitetIdAsString);
					}
				} else if (NomenclatorConstants.TIP_ORGANISM_ATTRIBUTE_KEY_COD_VALUE_AS_ORGANISME_INTERNE.equals(codTipOrganism)) {		
					prepareResponseByAttributeAndValue(responseModel, NomenclatorConstants.INSTITUTII_MEMBRE_ALE_ORGANISMELOR_SI_COMITETELOR_ATTRIBUTE_KEY_ORGANISM, organismIdAsString);			
				} else {
					throw new AppException(AppExceptionCodes.NOMENCLATOR_VALUE_NOT_FOUND);
				}
			} else {
				responseModel.setSelectable(false); // explicit set as "false"
			}
		}
		
		return responseModel;
	}
	
	private void prepareResponseByAttributeAndValue(CustomNomenclatorSelectionFiltersResponseModel responseModel, String attributekey, String attributeValue) {
		List<NomenclatorValue> institutiiMembreOrganism = nomenclatorValueDao.findByNomenclatorCodeAndAttribute(NomenclatorConstants.INSTITUTII_MEMBRE_ALE_ORGANISMELOR_SI_COMITETELOR_NOMENCLATOR_CODE, attributekey, attributeValue);
		if (CollectionUtils.isNotEmpty(institutiiMembreOrganism)) {
			responseModel.setValueIds(getInstitutieIdsFromInstitutiiMembreOrganism(institutiiMembreOrganism));
			responseModel.setSelectable(true);
		}
	}
	
	private List<Long> getInstitutieIdsFromInstitutiiMembreOrganism(List<NomenclatorValue> institutiiMembreOrganism) {
		List<Long> institutieIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(institutiiMembreOrganism)) {
			for (NomenclatorValue institutieMembraOrganism : institutiiMembreOrganism) {
				Long institutieId = NomenclatorValueUtils.getAttributeValueAsLong(institutieMembraOrganism, NomenclatorConstants.INSTITUTII_MEMBRE_ALE_ORGANISMELOR_SI_COMITETELOR_ATTRIBUTE_KEY_INSTITUTIE);
				institutieIds.add(institutieId);
			}
		}
		return institutieIds;
	}
}
