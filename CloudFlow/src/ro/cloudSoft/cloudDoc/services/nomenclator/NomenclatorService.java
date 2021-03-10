package ro.cloudSoft.cloudDoc.services.nomenclator;

import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorValueAsViewSearchRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorValueSearchRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.GetNomenclatorValuesRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.JoinedNomenclatorUiAttributesValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorAttributeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorRunExpressionRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorRunExpressionResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.SaveNomenclatorValueResponseModel;
import ro.cloudSoft.common.utils.PagingList;

public interface NomenclatorService {
	
	public Long saveNomenclator(NomenclatorModel nomenclator) throws AppException;
	public NomenclatorModel getNomenclator(Long id);
	public NomenclatorModel getNomenclatorByCode(String code);
	public List<NomenclatorModel> getAllNomenclators();
	public List<NomenclatorModel> getVisibleNomenclators();
	public List<NomenclatorModel> getAvailableNomenclatorsForProcessingValuesFromUI();
	public List<NomenclatorModel> getAvailableNomenclatorsForProcessingStructureFromUI();
	
	public SaveNomenclatorValueResponseModel saveNomenclatorValue(NomenclatorValueModel nomenclatorValueModel);
	public NomenclatorValueModel getNomenclatorValue(Long id);
	public PagingList<NomenclatorValueModel> searchNomenclatorValues(NomenclatorValueSearchRequestModel requestModel);
	public PagingList<NomenclatorValueViewModel> searchNomenclatorValueViewModels(NomenclatorValueAsViewSearchRequestModel requestModel) throws AppException;
	public List<NomenclatorValueViewModel> searchNomenclatorValueViewModelsWithoutPaging(NomenclatorValueAsViewSearchRequestModel requestModel) throws AppException;
	public List<JoinedNomenclatorUiAttributesValueModel> getJoinedNomenclatorUiAtributesValueByNomenclatorId(Long nomenclatorId) throws AppException;
	public List<JoinedNomenclatorUiAttributesValueModel> getJoinedNomenclatorUiAtributesValueByNomenclatorCode(String nomenclatorCode) throws AppException;
	public Map<Long, String> getUiAttributeValues(List<Long> nomenclatorValuesIds);
	public String getNomenclatorUiValue(Long nomenclatorValueIds);
	
	public Long saveNomenclatorAttribute(NomenclatorAttributeModel nomenclatorAttributeModel);
	public NomenclatorAttributeModel getNomenclatorAttribute(Long id);
	public List<NomenclatorAttributeModel> getNomenclatorAttributesByNomenclatorId(Long nomenclatorId);
	public List<NomenclatorAttributeModel> getNomenclatorAttributesByNomenclatorCode(String nomenclatorCode);
	public void saveNomenclatorAttributes(List<NomenclatorAttributeModel> nomenclatorAttributeModel);
	
	public boolean nomenclatorHasValue(Long nomenclatorId);
	public boolean nomenclatorHasValueByNomenclatorCode(String nomenclatorCode);
	
	public void deleteNomenclator(Long id);
	public void deleteNomenclatorValue(Long valueId);
	
	public Map<String, Long> getNomenclatorIdByCodeMapByNomenclatorCodes(List<String> codes);
	public List<NomenclatorValueModel> getNomenclatorValuesByNomenclatorId(Long nomenclatorId);
	public List<NomenclatorValueModel> getNomenclatorValuesByNomenclatorCode(String nomenclatorCode);
	public NomenclatorValue getInstitutieArb();
	public Map<Long,String> getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(String nomenclatorCode) throws AppException;
	
	public List<NomenclatorValueModel> getNomenclatorValues(GetNomenclatorValuesRequestModel requestModel) throws AppException;

	public List<Integer> getYearsFromNomenclatorValuesByNomenclatorCodeAndAttributeKey(String nomenclatorCode, String attributeKey);
	
	public CustomNomenclatorSelectionFiltersResponseModel getCustomNomenclatorSelectionFilters(SecurityManager userSecurity, CustomNomenclatorSelectionFiltersRequestModel requestModel) throws AppException;
	public NomenclatorRunExpressionResponseModel runExpressions(SecurityManager userSecurity, NomenclatorRunExpressionRequestModel requestModel) throws AppException;
	
	public List<JoinedNomenclatorUiAttributesValueModel> getNomenclatorUIValuesAsFilterForNomenclatorAttributeThatUseIt(Long nomenclatorId, Long nomenclatorAttributeIdThatUseIt) throws AppException;
	
	public boolean existsPersonAndInstitutionInNomPersoane(NomenclatorValueModel nomenclatorValueModel);
}
