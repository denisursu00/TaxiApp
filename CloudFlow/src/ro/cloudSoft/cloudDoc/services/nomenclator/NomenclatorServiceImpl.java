package ro.cloudSoft.cloudDoc.services.nomenclator;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.MatchMode;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.ArbConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorAttributeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorUiAttributeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttribute;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttributeSelectionFilter;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttributeTypeEnum;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorUiAttribute;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValidation;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValueSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.plugins.ExpressionEvaluator;
import ro.cloudSoft.cloudDoc.plugins.NomenclatorAttributeEvaluationWrapper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorValueAsViewSearchRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorValueSearchRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.CustomNomenclatorSelectionFiltersResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.GetNomenclatorValuesRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.JoinedNomenclatorUiAttributesValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorAttributeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorMultipleFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorRunExpressionRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorRunExpressionResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSimpleFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSortType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSortedAttribute;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.SaveNomenclatorValueResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.SaveNomenclatorValueResponseModel.SaveNomenclatorValueResponseStatus;
import ro.cloudSoft.cloudDoc.presentation.server.converters.nomenclator.NomenclatorAttributeConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.nomenclator.NomenclatorConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.nomenclator.NomenclatorValueConverter;
import ro.cloudSoft.cloudDoc.services.nomenclator.customSelectionFilters.CustomNomenclatorSelectionFiltersExecution;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.PagingList;
import ro.cloudSoft.common.utils.StringUtils2;

public class NomenclatorServiceImpl implements NomenclatorService, InitializingBean {
	
	private static final LogHelper logger = LogHelper.getInstance(NomenclatorServiceImpl.class);
	
	private NomenclatorDao nomenclatorDao;
	private NomenclatorValueDao nomenclatorValueDao;
	private NomenclatorAttributeDao nomenclatorAttributeDao;
	private NomenclatorUiAttributeDao nomenclatorUiAttributeDao;
	
	private NomenclatorConverter nomenclatorConverter;
	private NomenclatorValueConverter nomenclatorValueConverter;
	private NomenclatorAttributeConverter nomenclatorAttributeConverter;
	
	private ArbConstants arbConstants;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		DependencyInjectionUtils.checkRequiredDependencies(
			nomenclatorDao,
			nomenclatorValueDao,
			nomenclatorAttributeDao,
			nomenclatorConverter,
			nomenclatorValueConverter,
			nomenclatorAttributeConverter,
			arbConstants
		);
	}
	
	@Transactional
	@Override
	public Long saveNomenclator(NomenclatorModel nomenclatorModel) throws AppException {
		
		List<Long> persistedAttributeIds = new ArrayList<>();
		List<Long> persistedNomAttrSelFilterIds = new ArrayList<>();
		List<NomenclatorUiAttribute> uiAttributeToRemove = new ArrayList<>();
		boolean nomenclatorHasData = false;
		boolean attributeTypeChanged = false;
		boolean attributeKeyChanged = false;
		Map<Long, Long> attributeTypeNomenclatorIdByAttributeId = new HashMap<>();
		
		if (nomenclatorModel.getId() != null) {
			
			nomenclatorHasData = nomenclatorValueDao.nomenclatorHasValuesByNomenclatorId(nomenclatorModel.getId());
			
			Nomenclator existingNomenclator = nomenclatorDao.find(nomenclatorModel.getId());
			for (NomenclatorAttribute attribute : existingNomenclator.getAttributes()) {
				persistedAttributeIds.add(attribute.getId());
				if (nomenclatorHasData) {
					for (NomenclatorAttributeModel attributeModel : nomenclatorModel.getAttributes()) {
						if (attributeModel.getId() != null) {
							if (attributeModel.getId().equals(attribute.getId()) && !attributeModel.getType().equals(attribute.getType().name())) {
								attributeTypeChanged = true;
							}
							if (attributeModel.getId().equals(attribute.getId()) && !attributeModel.getKey().equals(attribute.getKey())) {
								attributeKeyChanged = true;
							}
						}
					}
				}
				if (attribute.getType().equals(NomenclatorAttributeTypeEnum.NOMENCLATOR)) {
					if (CollectionUtils.isNotEmpty(attribute.getTypeNomenclatorSelectionFilters())) {
						for (NomenclatorAttributeSelectionFilter f : attribute.getTypeNomenclatorSelectionFilters()) {
							persistedNomAttrSelFilterIds.add(f.getId());
						}
					}
					attributeTypeNomenclatorIdByAttributeId.put(attribute.getId(), attribute.getTypeNomenclator().getId());
				}
			}
			for (NomenclatorUiAttribute uiAttribute : existingNomenclator.getUiAttributes()) {
				boolean foundInModel = false;
				for (String uiAttributeName : nomenclatorModel.getUiAttributeNames()) {
					if (uiAttribute.getAttribute().getName().equals(uiAttributeName)) {
						foundInModel = true;
					}
				}
				if (!foundInModel) {
					uiAttributeToRemove.add(uiAttribute);
				}
			}
		}
		
		boolean attributeTypeNomenclatorChanged = false;
		List<Long> remainedAttributeIds = new ArrayList<>();
		for (NomenclatorAttributeModel attributeModel : nomenclatorModel.getAttributes()) {
			if (attributeModel.getId() != null) {
				remainedAttributeIds.add(attributeModel.getId());				
				Long existingTypeNomenclatorId = attributeTypeNomenclatorIdByAttributeId.get(attributeModel.getId());
				if (existingTypeNomenclatorId != null && existingTypeNomenclatorId.longValue() != attributeModel.getTypeNomenclatorId().longValue()) {
					attributeTypeNomenclatorChanged = true;
				}
			}
		}
		
		persistedAttributeIds.removeAll(remainedAttributeIds);
		if (nomenclatorHasData) {
			if (CollectionUtils.isNotEmpty(persistedAttributeIds)) {
				throw new AppException(AppExceptionCodes.NOMENCLATOR_ATTRIBUTES_CANNOT_BE_DELETED_BECAUSE_DATA_EXIST);
			}
			if (attributeTypeChanged) {
				throw new AppException(AppExceptionCodes.NOMENCLATOR_ATTRIBUTE_TYPE_CANNOT_BE_CHANGED_BECAUSE_DATA_EXIST);
			}
			if (attributeKeyChanged) {
				throw new AppException(AppExceptionCodes.NOMENCLATOR_ATTRIBUTE_KEY_CANNOT_BE_CHANGED_BECAUSE_DATA_EXIST);
			}
			if (attributeTypeNomenclatorChanged) {
				throw new AppException(AppExceptionCodes.NOMENCLATOR_OF_NOMENCLATOR_ATTRIBUTE_CANNOT_BE_CHANGED_BECAUSE_DATA_EXIST);
			}
		}
		
		Nomenclator nomenclator = nomenclatorConverter.getFromModel(nomenclatorModel);
		
		int lastNumberForAttributeKeyOrColumn = 0;
		if (nomenclatorHasData) {
			lastNumberForAttributeKeyOrColumn = getLastNumberForAttributeColumn(nomenclator.getAttributes());
		}
		for (NomenclatorAttribute attribute : nomenclator.getAttributes()) {
			if (nomenclatorHasData) {
				if (attribute.getId() == null) {
					lastNumberForAttributeKeyOrColumn++;
					attribute.setColumnName(NomenclatorConstants.ATTRIBUTE_COLUMN_NAME_PREFIX + lastNumberForAttributeKeyOrColumn);
				}
			} else {
				lastNumberForAttributeKeyOrColumn++;
				attribute.setColumnName(NomenclatorConstants.ATTRIBUTE_COLUMN_NAME_PREFIX + lastNumberForAttributeKeyOrColumn);
			}
		}
		
		// Remove nomenclator attribute selection filters
		if (CollectionUtils.isNotEmpty(persistedNomAttrSelFilterIds)) {
			List<Long> remainedNomAttrSelFilterIds = new ArrayList<>();
			for (NomenclatorAttribute attribute : nomenclator.getAttributes()) {
				if (attribute.getType().equals(NomenclatorAttributeTypeEnum.NOMENCLATOR)) {
					if (CollectionUtils.isNotEmpty(attribute.getTypeNomenclatorSelectionFilters())) {
						for (NomenclatorAttributeSelectionFilter f : attribute.getTypeNomenclatorSelectionFilters()) {
							remainedNomAttrSelFilterIds.add(f.getId());
						}
					}
				}
			}
			if (!remainedNomAttrSelFilterIds.isEmpty()) {
				persistedNomAttrSelFilterIds.removeAll(remainedNomAttrSelFilterIds);
			}
			if (!persistedNomAttrSelFilterIds.isEmpty()) {
				nomenclatorAttributeDao.deleteNomenclatorAttributeSelectionFiltersByIds(persistedNomAttrSelFilterIds);
			}
		}	
		
		// Remove UI attributes
		if (CollectionUtils.isNotEmpty(uiAttributeToRemove)) {
			nomenclatorUiAttributeDao.deleteAll(uiAttributeToRemove);			
		}
				
		// Remove attributes
		if (CollectionUtils.isNotEmpty(persistedAttributeIds)) {
			nomenclatorAttributeDao.deleteByIds(persistedAttributeIds);
		}
		
		validateAttributeKeyDuplicated(nomenclator);
		validateAttributeColumnDuplicated(nomenclator);
		
		if (nomenclatorModel.getId() == null) {
			nomenclator.setHidden(false);
			nomenclator.setAllowProcessingValuesFromUI(true);
			nomenclator.setAllowProcessingStructureFromUI(true);
		}		
		
		return nomenclatorDao.save(nomenclator);
	}
	
	private void validateAttributeKeyDuplicated(Nomenclator nomenclator) throws AppException {
		
		List<NomenclatorAttribute> attributes = nomenclator.getAttributes();
		if (CollectionUtils.isEmpty(attributes)) {
			return;
		}
		List<String> attributeKeys = new ArrayList<>();
		for (NomenclatorAttribute attribute : attributes) {
			if (attributeKeys.contains(attribute.getKey())) {
				throw new AppException(AppExceptionCodes.NOMENCLATOR_DUPLICATED_ATTRIBUTE_KEYS);
			}
			attributeKeys.add(attribute.getKey());
		}
	}
	
	private void validateAttributeColumnDuplicated(Nomenclator nomenclator) throws AppException {
		
		List<NomenclatorAttribute> attributes = nomenclator.getAttributes();
		if (CollectionUtils.isEmpty(attributes)) {
			return;
		}
		List<String> attributeColumns = new ArrayList<>();
		for (NomenclatorAttribute attribute : attributes) {
			if (attributeColumns.contains(attribute.getColumnName())) {
				throw new AppException(AppExceptionCodes.NOMENCLATOR_DUPLICATED_ATTRIBUTE_COLUMNS);
			}
			attributeColumns.add(attribute.getColumnName());
		}
	}

	private int getLastNumberForAttributeColumn(List<NomenclatorAttribute> attributes) {
		int counter = 0;
		for (NomenclatorAttribute attribute : attributes) {
			if (attribute.getId() != null) {
				counter++;
			}
		}
		return counter;
	}

	@Override
	public NomenclatorModel getNomenclator(Long id) {
		
		Nomenclator nomenclator = nomenclatorDao.find(id);
		NomenclatorModel nomenclatorModel = nomenclatorConverter.getModelFromNomenclator(nomenclator);
		return nomenclatorModel;
	}
	
	@Override
	public NomenclatorModel getNomenclatorByCode(String code) {
		
		Nomenclator nomenclator = nomenclatorDao.findByCode(code);
		NomenclatorModel nomenclatorModel = nomenclatorConverter.getModelFromNomenclator(nomenclator);
		return nomenclatorModel;
	}

	@Override
	public List<NomenclatorModel> getAllNomenclators() {		
		List<Nomenclator> nomenclators = nomenclatorDao.getAll();
		return convertNomenclatorsToModel(nomenclators);
	}
	
	@Override
	public List<NomenclatorModel> getVisibleNomenclators() {
		List<Nomenclator> nomenclators = nomenclatorDao.getVisibleNomenclators();
		return convertNomenclatorsToModel(nomenclators);
	}
	
	@Override
	public List<NomenclatorModel> getAvailableNomenclatorsForProcessingValuesFromUI() {
		List<Nomenclator> nomenclators = nomenclatorDao.getAllThatAllowProcessingValuesFromUI();
		return convertNomenclatorsToModel(nomenclators);
	}
	
	@Override
	public List<NomenclatorModel> getAvailableNomenclatorsForProcessingStructureFromUI() {
		List<Nomenclator> nomenclators = nomenclatorDao.getAllThatAllowProcessingStructureFromUI();
		return convertNomenclatorsToModel(nomenclators);
	}
	
	private List<NomenclatorModel> convertNomenclatorsToModel(List<Nomenclator> nomenclators) {
		List<NomenclatorModel> nomenclatorsModels = new ArrayList<NomenclatorModel>();
		for (Nomenclator nomenclator : nomenclators) {
			NomenclatorModel nomenclatorModel = nomenclatorConverter.getModelFromNomenclator(nomenclator);
			nomenclatorsModels.add(nomenclatorModel);
		}
		return nomenclatorsModels;
	}
	
	public SaveNomenclatorValueResponseModel saveNomenclatorValue(NomenclatorValueModel nomenclatorValueModel) {		
		SaveNomenclatorValueResponseModel response = new SaveNomenclatorValueResponseModel();
		try {
			List<String> validationMessages = validateNomenclatorValue(nomenclatorValueModel);
			if (CollectionUtils.isEmpty(validationMessages)) {
				NomenclatorValue nomenclatorValue = nomenclatorValueConverter.getEntity(nomenclatorValueModel);
				checkNomenclatorValueSaveConstraints(nomenclatorValue);
				nomenclatorValueDao.save(nomenclatorValue);
				response.setStatus(SaveNomenclatorValueResponseStatus.SUCCESS);
			} else {
				response.setStatus(SaveNomenclatorValueResponseStatus.ERROR);
				response.setMessages(validationMessages);
			}
		} catch (Exception e) {
			response.setStatus(SaveNomenclatorValueResponseStatus.ERROR);
			List<String> messages = new LinkedList<>();
			String message = StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "An unexpected error occurred.";
			messages.add(message);
			response.setMessages(messages);
		}
		return response;
	}
	
	private List<String> validateNomenclatorValue(NomenclatorValueModel nomenclatorValue) {
		
		List<String> validationMessages = new LinkedList<>();
		
		Nomenclator nomenclator = nomenclatorDao.find(nomenclatorValue.getNomenclatorId());
		List<NomenclatorValidation> validations = nomenclator.getValidations();
		
		if (CollectionUtils.isNotEmpty(validations)) {
			
			Map<String, NomenclatorAttributeEvaluationWrapper> attributeWrapperByAttributeKey = new HashMap<>();
			for (NomenclatorAttribute attribute : nomenclator.getAttributes()) {
				NomenclatorAttributeEvaluationWrapper wrapper = new NomenclatorAttributeEvaluationWrapper(attribute.getType(), NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, attribute.getKey()));
				attributeWrapperByAttributeKey.put(attribute.getKey(), wrapper);
			}
			
			for (NomenclatorValidation validation : validations) {
				boolean validationResult = ExpressionEvaluator.evaluateNomenclatorExpression(validation.getConditionExpression(), attributeWrapperByAttributeKey);
				if (!validationResult) {
					validationMessages.add(validation.getMessage());
				}
			}
		}
		
		return validationMessages;
	}
	
	private void checkNomenclatorValueSaveConstraints(NomenclatorValue nomenclatorValue) {
		if (StringUtils.isBlank(nomenclatorValue.getNomenclator().getCode())) {
			return;
		}
		if (nomenclatorValue.getNomenclator().getCode().equals(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE)) {
			String newInstitutieDenumire = NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE_INSTITUTIE);
			if (StringUtils.isNotBlank(newInstitutieDenumire)) {
				newInstitutieDenumire = newInstitutieDenumire.trim();
				if (nomenclatorValue.getId() == null && newInstitutieDenumire.equalsIgnoreCase(arbConstants.getDenumireInstitutieArb())) {
					throw new RuntimeException("o institutie cu acest nume nu poate fi salvata.");
				}
			}
			NomenclatorValue institutieArb = getInstitutieArb();
			if (institutieArb.getId().equals(nomenclatorValue.getId())) {
				if (!arbConstants.getDenumireInstitutieArb().equals(newInstitutieDenumire)) {
					throw new RuntimeException("denumirea institutiei [" + arbConstants.getDenumireInstitutieArb() + "] nu poate fi modificata");
				}
			}
		} else if (nomenclatorValue.getNomenclator().getCode().equals(NomenclatorConstants.PERSOANE_NOMENCLATOR_CODE)) {
			checkNomenclatorValueIsPersoanaOfInstitutieArb(nomenclatorValue);
			if (nomenclatorValue.getId() != null) {
				NomenclatorValue dbNomenclatorValue = nomenclatorValueDao.find(nomenclatorValue.getId());
				checkNomenclatorValueIsPersoanaOfInstitutieArb(dbNomenclatorValue);
			}
		}
	}
	
	private void checkNomenclatorValueIsPersoanaOfInstitutieArb(NomenclatorValue nomenclatorValue) {
		Long institutieId = NomenclatorValueUtils.getAttributeValueAsLong(nomenclatorValue, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_INSTITUTIE);
		NomenclatorValue institutieArb = getInstitutieArb();
		if (institutieArb.getId().equals(institutieId)) {
			throw new RuntimeException("o persoana din institutia [" + arbConstants.getDenumireInstitutieArb() + "] nu poate fi salvata");
		}
	}
	
	public NomenclatorValueModel getNomenclatorValue(Long id) {
		
		NomenclatorValue nomenclatorValue = nomenclatorValueDao.find(id);
		NomenclatorValueModel nomenclatorValueModel = nomenclatorValueConverter.getModel(nomenclatorValue);
		return nomenclatorValueModel;
	}
	
	public Map<Long, String> getUiAttributeValues(List<Long> nomenclatorValueIds) {
		
		LinkedHashMap<Long, String> uiAttributeValues = new LinkedHashMap<Long, String>();
		
		List<NomenclatorValue> values = nomenclatorValueDao.getAllByIds(nomenclatorValueIds);
		for (NomenclatorValue value : values) {	
			String concatenatedValues = "";
			Nomenclator nomenclator = nomenclatorDao.find(value.getNomenclator().getId());
			for (NomenclatorUiAttribute uiAttribute : nomenclator.getUiAttributes()) {
				String attributeValue = getUiAttributeConcatenatedValue(value, uiAttribute.getAttribute());
				concatenatedValues = StringUtils2.appendToStringWithSeparator(concatenatedValues, attributeValue, " ");
				
			}
			uiAttributeValues.put(value.getId(), concatenatedValues);
		}
		return uiAttributeValues;
	}
	
	@Override
	public String getNomenclatorUiValue(Long nomenclatorValueId) {
		List<Long> nomenclatorValueIds = new LinkedList<Long>();
		nomenclatorValueIds.add(nomenclatorValueId);
		Map<Long, String> uiValues = this.getUiAttributeValues(nomenclatorValueIds);
		return uiValues.get(nomenclatorValueId);
	}
	
	private String getUiAttributeConcatenatedValue(NomenclatorValue value, NomenclatorAttribute attribute) {
		String concatenatedValues = "";
		if (attribute.getType().equals(NomenclatorAttributeTypeEnum.NOMENCLATOR)) {
			String uiValueIdAsString = getNomenclatorAttributeValueByNomenclatorTypeAsAttribute(attribute, value);
			if (StringUtils.isNotBlank(uiValueIdAsString)) {
				Long uiValueId = Long.valueOf(uiValueIdAsString);
				NomenclatorValue uiNomenclatorValue = nomenclatorValueDao.find(uiValueId);
				Nomenclator nomenclator = nomenclatorDao.find(uiNomenclatorValue.getNomenclator().getId());
			
				for (NomenclatorUiAttribute uiAttribute : nomenclator.getUiAttributes()) {
					String attributeValue = getUiAttributeConcatenatedValue(uiNomenclatorValue, uiAttribute.getAttribute());
					concatenatedValues = StringUtils2.appendToStringWithSeparator(concatenatedValues, attributeValue, " ");
				}
			}
		} else {
			String uiAttributeValue = getNomenclatorAttributeValueByNomenclatorTypeAsAttribute(attribute, value);
			if (StringUtils.isNotBlank(uiAttributeValue)) {
				concatenatedValues = StringUtils2.appendToStringWithSeparator(concatenatedValues, uiAttributeValue, " ");
			}
		}
		return concatenatedValues;
	}
	
	public PagingList<NomenclatorValueModel> searchNomenclatorValues(NomenclatorValueSearchRequestModel requestModel) {
		
		NomenclatorValueSearchCriteria searchCriteria = getSearchCriteria(requestModel);
		updateSearchFilterMatchModeByNomenclatorAttributeValueType(searchCriteria);
		PagingList<NomenclatorValue> nomenclatorValuesPagingList = nomenclatorValueDao.searchValues(requestModel.getOffset(), requestModel.getPageSize(), searchCriteria);
		
		List<NomenclatorValueModel> nomenclatorValueModels = nomenclatorValueConverter.getModels(nomenclatorValuesPagingList.getElements());
		PagingList<NomenclatorValueModel> nomenclatorValueModelsPagingResults = 
				new PagingList<NomenclatorValueModel>(nomenclatorValuesPagingList.getTotalCount(), nomenclatorValuesPagingList.getOffset(), nomenclatorValueModels);
		
		return nomenclatorValueModelsPagingResults;
	}

	private NomenclatorValueSearchCriteria getSearchCriteria(NomenclatorValueSearchRequestModel requestModel) {
		NomenclatorValueSearchCriteria searchCriteria = new NomenclatorValueSearchCriteria();
		searchCriteria.setNomenclatorId(requestModel.getNomenclatorId());
		searchCriteria.setFilterValues(requestModel.getFilters());
		searchCriteria.setSortedAttributes(requestModel.getSortedAttributes());
		return searchCriteria;
	}
	
	public PagingList<NomenclatorValueViewModel> searchNomenclatorValueViewModels(NomenclatorValueAsViewSearchRequestModel requestModel) throws AppException {
		
		NomenclatorValueSearchCriteria searchCriteria = prepareSearchCriteria(requestModel);
		updateSearchFilterMatchModeByNomenclatorAttributeValueType(searchCriteria);
		PagingList<NomenclatorValue> nomenclatorValuesPagingList = nomenclatorValueDao.searchValues(requestModel.getOffset(), 
				requestModel.getPageSize(), searchCriteria);
		
		List<NomenclatorAttribute> nomenclatorAttributes = nomenclatorAttributeDao.getAllByNomenclatorId(requestModel.getNomenclatorId());
		List<NomenclatorValueViewModel> nomenclatorValueViewModels = new ArrayList<NomenclatorValueViewModel>();
		
		for (NomenclatorValue nomenclatorValue : nomenclatorValuesPagingList.getElements()) {
			
			NomenclatorValueViewModel nomenclatorValueViewModel = new NomenclatorValueViewModel();
			nomenclatorValueViewModel.setId(nomenclatorValue.getId());
			
			for (NomenclatorAttribute nomenclatorAttribute : nomenclatorAttributes) {
				String attributeValue = getNomenclatorAttributeValueByNomenclatorTypeAsAttribute(nomenclatorAttribute, nomenclatorValue);
				
				if (nomenclatorAttribute.getType().equals(NomenclatorAttributeTypeEnum.NOMENCLATOR)) {
					String nomenclatorUiAttributeValue = getUiAttributeConcatenatedValue(nomenclatorValue, nomenclatorAttribute);
					nomenclatorValueViewModel.getAttributes().put(nomenclatorAttribute.getKey(), nomenclatorUiAttributeValue);
				} else {
					nomenclatorValueViewModel.getAttributes().put(nomenclatorAttribute.getKey(), attributeValue);
				}
			}
			nomenclatorValueViewModels.add(nomenclatorValueViewModel);
		}

		if (CollectionUtils.isNotEmpty(requestModel.getSortedAttributes())) {
			String attributeKey = requestModel.getSortedAttributes().get(0).getAttributeKey();
			Optional<NomenclatorAttribute> nomenclatorAttribute = nomenclatorAttributes.stream().
				    filter(a -> a.getKey().equals(attributeKey)).
				    findFirst();
			if (nomenclatorAttribute != null) {
				NomenclatorAttributeTypeEnum attributeType = nomenclatorAttribute.get().getType();
				Comparator<NomenclatorValueViewModel> comparator = null;
				
				if (attributeType.equals(NomenclatorAttributeTypeEnum.NUMERIC)) {
					comparator = new Comparator<NomenclatorValueViewModel>() {
						@Override
						public int compare(NomenclatorValueViewModel n1, NomenclatorValueViewModel n2) {
							String valoareN1 = n1.getAttributes().get(attributeKey); 
							String valoareN2 = n2.getAttributes().get(attributeKey); 
							if ( valoareN1 != null  && valoareN2 != null) {
								return (new BigDecimal(valoareN1)).compareTo(new BigDecimal(valoareN2));
							}
							return 1;
						}
					};
				} else if (attributeType.equals(NomenclatorAttributeTypeEnum.BOOLEAN)) {
					comparator = new Comparator<NomenclatorValueViewModel>() {
						@Override
						public int compare(NomenclatorValueViewModel n1, NomenclatorValueViewModel n2) {
							String valoareN1 = n1.getAttributes().get(attributeKey); 
							String valoareN2 = n2.getAttributes().get(attributeKey); 
							if ( valoareN1 != null  && valoareN2 != null) {
								return valoareN2.toLowerCase().compareTo(valoareN1.toLowerCase());
							}
							return 1;
						}
					};
				} else {
					comparator = new Comparator<NomenclatorValueViewModel>() {
						@Override
						public int compare(NomenclatorValueViewModel n1, NomenclatorValueViewModel n2) {
							String valoareN1 = n1.getAttributes().get(attributeKey); 
							String valoareN2 = n2.getAttributes().get(attributeKey); 
							if ( valoareN1 != null  && valoareN2 != null) {
								return valoareN1.toLowerCase().compareTo(valoareN2.toLowerCase());
							}
							return 1;
						}
					};
				}	
				
				if (requestModel.getSortedAttributes().get(0).getType().equals(NomenclatorSortType.DESC)) {
					nomenclatorValueViewModels.sort(comparator.reversed());
				} else {
					nomenclatorValueViewModels.sort(comparator);
				}
			}
		}
		
		int fromIndex = requestModel.getOffset();
		int toIndex = fromIndex + requestModel.getPageSize();
		
		if (toIndex > nomenclatorValueViewModels.size()) {
			toIndex = nomenclatorValueViewModels.size();
		}
		
		nomenclatorValueViewModels = new ArrayList<NomenclatorValueViewModel>(nomenclatorValueViewModels.subList(fromIndex, toIndex));

		PagingList<NomenclatorValueViewModel> nomenclatorValueViewModelsPagingResults = 
				new PagingList<NomenclatorValueViewModel>(nomenclatorValuesPagingList.getTotalCount(), nomenclatorValuesPagingList.getOffset(), nomenclatorValueViewModels);

		return nomenclatorValueViewModelsPagingResults;
	}
	
	public List<NomenclatorValueViewModel> searchNomenclatorValueViewModelsWithoutPaging(NomenclatorValueAsViewSearchRequestModel requestModel) throws AppException {
		requestModel.setOffset(0);
		requestModel.setPageSize(Integer.MAX_VALUE);
		
		PagingList<NomenclatorValueViewModel> nomenclatorValueViewModelsPagingResults = searchNomenclatorValueViewModels(requestModel);
		
		return nomenclatorValueViewModelsPagingResults.getElements();
	}
	
	private NomenclatorValueSearchCriteria prepareSearchCriteria(NomenclatorValueAsViewSearchRequestModel requestModel) {
		NomenclatorValueSearchCriteria searchCriteria = new NomenclatorValueSearchCriteria();
		searchCriteria.setNomenclatorId(requestModel.getNomenclatorId());
		searchCriteria.setValueIds(requestModel.getValueIds());
		
		for (NomenclatorFilter nomenclatorFilter : requestModel.getFilters()) {
			NomenclatorAttribute nomenclatorAttribute = nomenclatorAttributeDao.findByNomenclatorIdAndKey(requestModel.getNomenclatorId(), nomenclatorFilter.getAttributeKey());
			nomenclatorFilter.setAttributeKey(nomenclatorAttribute.getColumnName().toLowerCase());
				
			if (nomenclatorFilter instanceof NomenclatorSimpleFilter) {
				if (nomenclatorAttribute.getType().equals(NomenclatorAttributeTypeEnum.DATE)) {
					String formattedDate = getFilterValueAsFormattedDate(((NomenclatorSimpleFilter)nomenclatorFilter).getValue());
					((NomenclatorSimpleFilter) nomenclatorFilter).setValue(formattedDate);
				}
			} else if (nomenclatorFilter instanceof NomenclatorMultipleFilter) {
				// TODO: Trebuie realizat daca o sa avem filtre pe intervale de date
			}
			searchCriteria.getFilterValues().add(nomenclatorFilter);
		}
		
		for (NomenclatorSortedAttribute nomenclatorSortedAttribute : requestModel.getSortedAttributes()) {
			NomenclatorAttribute nomenclatorAttribute = nomenclatorAttributeDao.findByNomenclatorIdAndKey(requestModel.getNomenclatorId(), nomenclatorSortedAttribute.getAttributeKey());
			
			NomenclatorSortedAttribute sortedAttribute = new NomenclatorSortedAttribute();
			sortedAttribute.setAttributeKey(nomenclatorAttribute.getColumnName().toLowerCase());
			sortedAttribute.setType(nomenclatorSortedAttribute.getType());
			searchCriteria.getSortedAttributes().add(sortedAttribute);
		}
		
		return searchCriteria;
	}
	
	private String getFilterValueAsFormattedDate(String dateAsString) {
		try {
			Date date =  ISODateTimeFormat.dateTime().parseDateTime(dateAsString).toDate();
			SimpleDateFormat df = new SimpleDateFormat(NomenclatorAttribute.DATE_FORMAT);
			return df.format(date);
		} catch (Exception e) {
			throw new RuntimeException("The date that came from the filter cannot be parsed", e);
		}
	}
	
	public List<JoinedNomenclatorUiAttributesValueModel> getJoinedNomenclatorUiAtributesValueByNomenclatorId(Long nomenclatorId) throws AppException {

		Nomenclator nomenclator = nomenclatorDao.find(nomenclatorId);
		List<NomenclatorValue> values = nomenclatorValueDao.getAll(nomenclatorId);
		
		List<JoinedNomenclatorUiAttributesValueModel> concatenatedAttributesViewModels = new ArrayList<JoinedNomenclatorUiAttributesValueModel>();
		for (NomenclatorValue value : values) {
			
			JoinedNomenclatorUiAttributesValueModel concatenatedAttributesViewModel = new JoinedNomenclatorUiAttributesValueModel();
			concatenatedAttributesViewModel.setId(value.getId());
			
			String concatenatedValues = "";
			for (NomenclatorUiAttribute uiAttribute : nomenclator.getUiAttributes()) {
				String uiAttributeValue = getUiAttributeConcatenatedValue(value, uiAttribute.getAttribute());
				if (StringUtils.isNotBlank(uiAttributeValue)) {
					concatenatedValues = StringUtils2.appendToStringWithSeparator(concatenatedValues, uiAttributeValue, " ");
				}
			}
			concatenatedAttributesViewModel.setValue(concatenatedValues);
			concatenatedAttributesViewModels.add(concatenatedAttributesViewModel);
		}
		return concatenatedAttributesViewModels;
	}
	
	public Map<Long,String> getJoinedNomenclatorUiAtributesValuesAsMapByNomenclatorCode(String nomenclatorCode) throws AppException {
		List<JoinedNomenclatorUiAttributesValueModel> valuesAsList = getJoinedNomenclatorUiAtributesValueByNomenclatorCode(nomenclatorCode);
		Map<Long,String> valuesAsmap = new HashMap<>();
		for (JoinedNomenclatorUiAttributesValueModel joinedNomenclatorUiAttributesValueModel : valuesAsList) {
			valuesAsmap.put(joinedNomenclatorUiAttributesValueModel.getId(), joinedNomenclatorUiAttributesValueModel.getValue());
		}
		return valuesAsmap;
	}
	
	public List<JoinedNomenclatorUiAttributesValueModel> getJoinedNomenclatorUiAtributesValueByNomenclatorCode(String nomenclatorCode) throws AppException {
		Nomenclator nomenclator = nomenclatorDao.findByCode(nomenclatorCode);
		return getJoinedNomenclatorUiAtributesValueByNomenclatorId(nomenclator.getId());
	}
	
	private String getNomenclatorAttributeValueByNomenclatorTypeAsAttribute(NomenclatorAttribute nomenclatorAttribute, NomenclatorValue nomenclatorValue) {
		try {
			Class<?> clazz = nomenclatorValue.getClass();
			String fieldName = nomenclatorAttribute.getKey();
			Field attributeField = clazz.getDeclaredField(fieldName);
			attributeField.setAccessible(true);
			
			String attributeValue = (String) attributeField.get(nomenclatorValue);
			return attributeValue;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Long saveNomenclatorAttribute(NomenclatorAttributeModel model) {
		Nomenclator nomenclator = nomenclatorDao.find(model.getNomenclatorId());
		NomenclatorAttribute nomenclatorAttribute = nomenclatorAttributeConverter.getFromModel(model, nomenclator);
		return nomenclatorAttributeDao.save(nomenclatorAttribute);
	}
	
	public NomenclatorAttributeModel getNomenclatorAttribute(Long id) {
		
		NomenclatorAttribute nomenclatorAttribute = nomenclatorAttributeDao.find(id);
		return nomenclatorAttributeConverter.getModel(nomenclatorAttribute);
	}
	
	public List<NomenclatorAttributeModel> getNomenclatorAttributesByNomenclatorId(Long nomenclatorId) {
		
		List<NomenclatorAttribute> nomenclatorAttributes = nomenclatorAttributeDao.getAllByNomenclatorId(nomenclatorId);
		
		List<NomenclatorAttributeModel> nomenclatorAttributeModels = new ArrayList<NomenclatorAttributeModel>();
		for (NomenclatorAttribute nomenclatorAttribute : nomenclatorAttributes) {
			NomenclatorAttributeModel nomenclatorAttributeModel = nomenclatorAttributeConverter.getModel(nomenclatorAttribute);
			nomenclatorAttributeModels.add(nomenclatorAttributeModel);
		}
		
		return nomenclatorAttributeModels;
	}
	
	private void updateSearchFilterMatchModeByNomenclatorAttributeValueType(NomenclatorValueSearchCriteria searchCriteria) {
		
		List<NomenclatorAttribute> nomenclatorAttributes = nomenclatorAttributeDao.getAllByNomenclatorId(searchCriteria.getNomenclatorId());
		
		
		for (NomenclatorFilter filter : searchCriteria.getFilterValues()) {
			if (filter.getMatchMode() == null) {
				for (NomenclatorAttribute nomenclatorAttribute : nomenclatorAttributes) {
					if (nomenclatorAttribute.getKey().equals(filter.getAttributeKey())) {
						if (nomenclatorAttribute.getType() ==NomenclatorAttributeTypeEnum.NOMENCLATOR) {
							filter.setMatchMode(MatchMode.EXACT);
						} else {
							filter.setMatchMode(MatchMode.ANYWHERE);
						}
					}
				}
			}
		}
	}
		
	public List<NomenclatorAttributeModel> getNomenclatorAttributesByNomenclatorCode(String nomenclatorCode) {
		Nomenclator nomenclator = nomenclatorDao.findByCode(nomenclatorCode);
		return getNomenclatorAttributesByNomenclatorId(nomenclator.getId());
	}

	@Override
	public void saveNomenclatorAttributes(List<NomenclatorAttributeModel> nomenclatorAttributesModel) {
		
		List<NomenclatorAttribute> nomenclatorAttributes = new ArrayList<NomenclatorAttribute>();
		for (NomenclatorAttributeModel nomenclatorAttributeModel : nomenclatorAttributesModel) {
			Nomenclator nomenclator = nomenclatorDao.find(nomenclatorAttributeModel.getNomenclatorId());
			NomenclatorAttribute nomenclatorAttribute = nomenclatorAttributeConverter.getFromModel(nomenclatorAttributeModel, nomenclator);
			nomenclatorAttributes.add(nomenclatorAttribute);
		}
		nomenclatorAttributeDao.saveAttributes(nomenclatorAttributes);
	}

	@Override
	public boolean nomenclatorHasValue(Long nomenclatorId) {
		// TODO - Aici ar trebui creata o metoda de cautare valori ale nomenclatorului cu EXISTS in Dao
		List<NomenclatorValue> nomenclatorValues = nomenclatorValueDao.findByNomenclatorId(nomenclatorId);
		if (CollectionUtils.isEmpty(nomenclatorValues)) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean nomenclatorHasValueByNomenclatorCode(String nomenclatorCode) {
		Nomenclator nomenclator = nomenclatorDao.findByCode(nomenclatorCode);
		return nomenclatorHasValue(nomenclator.getId());
	}
	
	@Override
	public void deleteNomenclator(Long id) {
		
		List<NomenclatorUiAttribute> nomenclatorUiAttributes = nomenclatorUiAttributeDao.getAllByNomenclatorId(id);
		if (!nomenclatorUiAttributes.isEmpty()) {
			nomenclatorUiAttributeDao.deleteAll(nomenclatorUiAttributes);
		}
		
		Nomenclator nomenclator = nomenclatorDao.find(id);
		if (nomenclator != null) {
			nomenclatorDao.delete(nomenclator);
		}
	}
	
	@Override
	public void deleteNomenclatorValue(Long valueId) {
		NomenclatorValue nomenclatorValue = nomenclatorValueDao.find(valueId);
		checkNomenclatorValueDeleteConstraints(nomenclatorValue);
		nomenclatorValue.setDeleted(true);
		nomenclatorValueDao.save(nomenclatorValue);	
	}
	
	private void checkNomenclatorValueDeleteConstraints(NomenclatorValue nomenclatorValue) {
		NomenclatorValue institutieArb = getInstitutieArb();
		if (nomenclatorValue.getId().equals(institutieArb.getId())) {
			throw new RuntimeException("institutia [" + arbConstants.getDenumireInstitutieArb() + "] nu poate fi stearsa");
		}
	}
	
	@Override
	public Map<String, Long> getNomenclatorIdByCodeMapByNomenclatorCodes(List<String> codes) {
		Map<String, Long> map = new HashMap<String, Long>();
		// TODO - Trebuie facut o metoda in dao pentru un select direct.
		List<Nomenclator> nomenclators = nomenclatorDao.getAll();
		if (CollectionUtils.isNotEmpty(nomenclators)) {
			for (Nomenclator nom : nomenclators) {
				if (nom.getCode() != null && codes.contains(nom.getCode())) {
					map.put(nom.getCode(), nom.getId());
				}
			}
		}
		return map;
	}

	@Override
	public List<NomenclatorValueModel> getNomenclatorValuesByNomenclatorId(Long nomenclatorId) {
		List<NomenclatorValue> values = nomenclatorValueDao.getAll(nomenclatorId);
		return nomenclatorValueConverter.getModels(values);
	}
	
	@Override
	public List<NomenclatorValueModel> getNomenclatorValuesByNomenclatorCode(String nomenclatorCode) {
		List<NomenclatorValue> values = nomenclatorValueDao.getAllByNomenclatorCode(nomenclatorCode);
		return nomenclatorValueConverter.getModels(values);
	}
	
	@Override
	public NomenclatorValue getInstitutieArb() {
		List<NomenclatorValue> results = this.nomenclatorValueDao.findByNomenclatorCodeAndAttribute(NomenclatorConstants.INSTITUTII_NOMENCLATOR_CODE, NomenclatorConstants.INSTITUTII_ATTRIBUTE_KEY_DENUMIRE_INSTITUTIE, arbConstants.getDenumireInstitutieArb());
		if (CollectionUtils.isEmpty(results)) {
			throw new RuntimeException("institutie arb not found");
		} else {
			if (results.size() > 1) {
				throw new RuntimeException("too many results for institutie arb");
			}
		}
		return Iterables.getFirst(results, null);
	}
	
	@Override
	public List<NomenclatorValueModel> getNomenclatorValues(GetNomenclatorValuesRequestModel requestModel) throws AppException {
		if (requestModel.getNomenclatorCode() != null) {
			requestModel.setNomenclatorId(nomenclatorDao.findByCode(requestModel.getNomenclatorCode()).getId());
		}
		
		List<NomenclatorValue> values = nomenclatorValueDao.getNomenclatorValues(requestModel);
		
		return nomenclatorValueConverter.getModels(values);
	}
	
	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}

	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}

	public void setNomenclatorAttributeDao(NomenclatorAttributeDao nomenclatorAttributeDao) {
		this.nomenclatorAttributeDao = nomenclatorAttributeDao;
	}
	
	public void setNomenclatorConverter(NomenclatorConverter nomenclatorConverter) {
		this.nomenclatorConverter = nomenclatorConverter;
	}
	
	public void setNomenclatorValueConverter(NomenclatorValueConverter nomenclatorValueConverter) {
		this.nomenclatorValueConverter = nomenclatorValueConverter;
	}
	
	public void setNomenclatorAttributeConverter(NomenclatorAttributeConverter nomenclatorAttributeConverter) {
		this.nomenclatorAttributeConverter = nomenclatorAttributeConverter;
	}
	
	public void setNomenclatorUiAttributeDao(NomenclatorUiAttributeDao nomenclatorUiAttributeDao) {
		this.nomenclatorUiAttributeDao = nomenclatorUiAttributeDao;
	}
	
	public void setArbConstants(ArbConstants arbConstants) {
		this.arbConstants = arbConstants;
	}

	@Override
	public List<Integer> getYearsFromNomenclatorValuesByNomenclatorCodeAndAttributeKey(String nomenclatorCode, String attributeKey) {
		
		List<Integer> ani = new ArrayList<Integer>();

		List <NomenclatorValueModel> nomenclatorValues = getNomenclatorValuesByNomenclatorCode(nomenclatorCode);
		
		for (NomenclatorValueModel nomenclatorValue : nomenclatorValues) {
			Integer an = NomenclatorValueUtils.getYearFromDateAttribute(nomenclatorValue,  attributeKey);
			
			if ( !ani.contains(an) ) {
				ani.add(an);
			}
		}
		
		return ani;
	}
	
	@Override
	public CustomNomenclatorSelectionFiltersResponseModel getCustomNomenclatorSelectionFilters(SecurityManager userSecurity, CustomNomenclatorSelectionFiltersRequestModel requestModel) throws AppException {
		
		NomenclatorAttribute attribute = nomenclatorAttributeDao.find(requestModel.getAttributeId());
		
		if (!attribute.getType().equals(NomenclatorAttributeTypeEnum.NOMENCLATOR)) {
			throw new AppException(AppExceptionCodes.INVALID_OPERATION);
		}
		if (StringUtils.isBlank(attribute.getTypeNomenclatorSelectionFiltersCustomClass())) {
			throw new AppException(AppExceptionCodes.INVALID_OPERATION);
		}
		String className = attribute.getTypeNomenclatorSelectionFiltersCustomClass().trim();
		try {
			Class<?> clazz = Class.forName(className);
			CustomNomenclatorSelectionFiltersExecution execution = (CustomNomenclatorSelectionFiltersExecution) clazz.newInstance();
			return execution.execute(userSecurity, requestModel);
		} catch (Exception e) {
			logger.error("Exceptie la instantiere si rulare clasa custom [" + className + "] pt selectie filtre", e, "CustomNomenclatorSelectionFilters", userSecurity);
			throw new AppException();
		}
	}
	
	@Override
	public NomenclatorRunExpressionResponseModel runExpressions(SecurityManager userSecurity, NomenclatorRunExpressionRequestModel requestModel) throws AppException {
		
		NomenclatorRunExpressionResponseModel responseModel = new NomenclatorRunExpressionResponseModel();
		responseModel.setResultsByAttributeKey(new HashMap<>());
		
		Nomenclator nomenclator = nomenclatorDao.find(requestModel.getNomenclatorId());
		
		Map<String, NomenclatorAttributeEvaluationWrapper> attributeWrapperByAttributeKey = new HashMap<>();
		for (NomenclatorAttribute attribute : nomenclator.getAttributes()) {
			if (requestModel.getAttributeValueByKey().containsKey(attribute.getKey())) {
				NomenclatorAttributeEvaluationWrapper wrapper = new NomenclatorAttributeEvaluationWrapper(attribute.getType(), requestModel.getAttributeValueByKey().get(attribute.getKey()));
				attributeWrapperByAttributeKey.put(attribute.getKey(), wrapper);
			}
		}
		
		for (NomenclatorAttribute attribute : nomenclator.getAttributes()) {
			
			Map<String, String> attributeResults = new HashMap<>();
			
			Boolean invisibleResult = null;
			if (StringUtils.isNotBlank(attribute.getInvisibleCheckExpression())) {
				invisibleResult = ExpressionEvaluator.evaluateNomenclatorExpression(attribute.getInvisibleCheckExpression(), attributeWrapperByAttributeKey);
				attributeResults.put("invisible", invisibleResult.toString());
			}
			
			if (StringUtils.isNotBlank(attribute.getRequiredCheckExpression())) {
				if (invisibleResult != null && invisibleResult) {
					attributeResults.put("required", "false");
				} else {
					Boolean requiredResult = ExpressionEvaluator.evaluateNomenclatorExpression(attribute.getRequiredCheckExpression(), attributeWrapperByAttributeKey);
					attributeResults.put("required", requiredResult.toString());
				}
			}
			
			if (attributeResults.size() > 0) {
				responseModel.getResultsByAttributeKey().put(attribute.getKey(), attributeResults);
			}			
		}
		
		return responseModel;
	}
	
	@Override
	public List<JoinedNomenclatorUiAttributesValueModel> getNomenclatorUIValuesAsFilterForNomenclatorAttributeThatUseIt(Long nomenclatorId, Long nomenclatorAttributeIdThatUseIt) throws AppException {
		
		Nomenclator nomenclator = nomenclatorDao.find(nomenclatorId);
		NomenclatorAttribute nomenclatorAttribute = nomenclatorAttributeDao.find(nomenclatorAttributeIdThatUseIt);
		Long nmenclatorIdThatUseIt = nomenclatorAttribute.getNomenclator().getId();
		
		List<NomenclatorValue> values = nomenclatorValueDao.getDistinctNomenclatorValuesByNomenclatorAndOtherNomenclatorAndAttributeThatUseIt(nomenclatorId, nmenclatorIdThatUseIt, nomenclatorAttribute.getKey());
		
		List<JoinedNomenclatorUiAttributesValueModel> concatenatedAttributesViewModels = new ArrayList<JoinedNomenclatorUiAttributesValueModel>();
		for (NomenclatorValue value : values) {
			
			JoinedNomenclatorUiAttributesValueModel concatenatedAttributesViewModel = new JoinedNomenclatorUiAttributesValueModel();
			concatenatedAttributesViewModel.setId(value.getId());
			
			String concatenatedValues = "";
			for (NomenclatorUiAttribute uiAttribute : nomenclator.getUiAttributes()) {
				String uiAttributeValue = getUiAttributeConcatenatedValue(value, uiAttribute.getAttribute());
				if (StringUtils.isNotBlank(uiAttributeValue)) {
					concatenatedValues = StringUtils2.appendToStringWithSeparator(concatenatedValues, uiAttributeValue, " ");
				}
			}
			concatenatedAttributesViewModel.setValue(concatenatedValues);
			concatenatedAttributesViewModels.add(concatenatedAttributesViewModel);
		}
		return concatenatedAttributesViewModels;
	}

	@Override
	public boolean existsPersonAndInstitutionInNomPersoane(NomenclatorValueModel nomenclatorValueModel) {
		Long nomenclatorPersoaneId = getNomenclatorByCode(NomenclatorConstants.PERSOANE_NOMENCLATOR_CODE).getId();
		if (nomenclatorValueModel.getNomenclatorId().equals(nomenclatorPersoaneId )) {
			List<NomenclatorFilter> filters = new ArrayList<>();
			filters.add(new NomenclatorSimpleFilter(NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME, NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueModel, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_NUME), MatchMode.EXACT));
			filters.add(new NomenclatorSimpleFilter(NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME, NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueModel, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_PRENUME), MatchMode.EXACT));
			filters.add(new NomenclatorSimpleFilter(NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_INSTITUTIE, NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValueModel, NomenclatorConstants.PERSOANE_ATTRIBUTE_KEY_INSTITUTIE), MatchMode.EXACT));
			GetNomenclatorValuesRequestModel requestModel =  new GetNomenclatorValuesRequestModel(nomenclatorPersoaneId, filters );
			List<NomenclatorValue> result = nomenclatorValueDao.getNomenclatorValues(requestModel );
			if (nomenclatorValueModel.getId() != null 
					&& CollectionUtils.isNotEmpty(result) 
					&& result.size() == 1
					&& result.get(0).getId().equals(nomenclatorValueModel.getId())) {
				return false;
			}
			return (CollectionUtils.isNotEmpty(result));
			
		} 
		return false;
	}

}
