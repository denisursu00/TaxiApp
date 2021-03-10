package ro.cloudSoft.cloudDoc.presentation.server.converters.nomenclator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorAttributeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttribute;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttribute.NomenclatorAttributeAutocompleteType;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttributeSelectionFilter;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttributeTypeEnum;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorAttributeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorAttributeSelectionFilterModel;

public class NomenclatorAttributeConverter {
	
	private NomenclatorDao nomenclatorDao;
	private NomenclatorAttributeDao nomenclatorAttributeDao;
	
	public NomenclatorAttributeModel getModel(NomenclatorAttribute entity) {
		
		NomenclatorAttributeModel model = new NomenclatorAttributeModel();
		model.setId(entity.getId());
		model.setNomenclatorId(entity.getNomenclator().getId());
		model.setName(entity.getName());
		model.setKey(entity.getKey());
		model.setUiOrder(entity.getUiOrder());
		model.setType(entity.getType().name());
		model.setRequired(entity.isRequired());
		model.setRequiredCheckExpression(entity.getRequiredCheckExpression());
		model.setInvisibleCheckExpression(entity.getInvisibleCheckExpression());
		model.setDefaultValue(entity.getDefaultValue());
		model.setReadonlyOnAdd(entity.isReadonlyOnAdd());
		model.setReadonlyOnEdit(entity.isReadonlyOnEdit());
		model.setAttributeKeyForAutocomplete(entity.getAttributeKeyForAutocomplete());
		model.setAutocompleteType(entity.getAutocompleteType());
		model.setNomenclatorAttributeKeyForAutocomplete(entity.getNomenclatorAttributeKeyForAutocomplete());
		Nomenclator completionSuggestionNomenclator = entity.getCompletionSuggestionNomenclator();
		if (completionSuggestionNomenclator != null) {
			model.setCompletionSuggestionNomenclatorId(completionSuggestionNomenclator.getId());
			model.setCompletionSuggestionNomenclatorAttributeKey(entity.getCompletionSuggestionNomenclatorAttributeKey());
		}
		
		if (entity.getType().equals(NomenclatorAttributeTypeEnum.NOMENCLATOR)) {
			model.setTypeNomenclatorId(entity.getTypeNomenclator().getId());
			
			List<NomenclatorAttributeSelectionFilter> selectionFilters = entity.getTypeNomenclatorSelectionFilters();			
			if (CollectionUtils.isNotEmpty(selectionFilters)) {
				List<NomenclatorAttributeSelectionFilterModel> selectionFilterModels = new ArrayList<>();
				model.setTypeNomenclatorSelectionFilters(selectionFilterModels);
				for (NomenclatorAttributeSelectionFilter selectionFilterEntity : selectionFilters) {
					selectionFilterModels.add(getModel(selectionFilterEntity));
				}
			}
			model.setTypeNomenclatorSelectionFiltersCustomClass(entity.getTypeNomenclatorSelectionFiltersCustomClass());
			String customClassAttributeKeys = entity.getTypeNomenclatorSelectionFiltersCustomClassAttributeKeys();
			if (StringUtils.isNotBlank(customClassAttributeKeys)) {
				customClassAttributeKeys = customClassAttributeKeys.trim();
			}
			model.setTypeNomenclatorSelectionFiltersCustomClassAttributeKeys(customClassAttributeKeys);
		}
		
		return model;
	}
	
	private NomenclatorAttributeSelectionFilterModel getModel(NomenclatorAttributeSelectionFilter entity) {
		NomenclatorAttributeSelectionFilterModel model = new NomenclatorAttributeSelectionFilterModel();
		model.setId(entity.getId());
		model.setFilterAttributeId(entity.getFilterAttribute().getId());
		model.setFilterAttributeKey(entity.getFilterAttribute().getKey());
		model.setDefaultFilterValue(entity.getDefaultFilterValue());
		String attributeNameForValue = entity.getAttributeKeyForAutocompleteFilterValue();
		if (StringUtils.isNotBlank(attributeNameForValue)) {
			attributeNameForValue = attributeNameForValue.trim();
		}
		model.setAttributeKeyForAutocompleteFilterValue(attributeNameForValue);
		return model;
	}
	
	public void updateFromModel(NomenclatorAttribute entity, NomenclatorAttributeModel model, Nomenclator nomenclator) {
		
		entity.setNomenclator(nomenclator);
		entity.setName(model.getName());
		entity.setKey(model.getKey());
		entity.setUiOrder(model.getUiOrder());
		entity.setType(NomenclatorAttributeTypeEnum.valueOf(model.getType()));
		entity.setRequired(model.isRequired());
		entity.setRequiredCheckExpression(null);
		entity.setInvisibleCheckExpression(model.getInvisibleCheckExpression());
		if (!model.isRequired()) {
			entity.setRequiredCheckExpression(model.getRequiredCheckExpression());
		}
		entity.setDefaultValue(model.getDefaultValue());
		entity.setReadonlyOnAdd(model.isReadonlyOnAdd());
		entity.setReadonlyOnEdit(model.isReadonlyOnEdit());	
		
		entity.setAttributeKeyForAutocomplete(null);
		entity.setAutocompleteType(null);
		entity.setNomenclatorAttributeKeyForAutocomplete(null);
		if (StringUtils.isNotBlank(model.getAttributeKeyForAutocomplete())) {
			entity.setAttributeKeyForAutocomplete(model.getAttributeKeyForAutocomplete());
			if (model.getAutocompleteType() != null) {
				entity.setAutocompleteType(model.getAutocompleteType());
				if (entity.getAutocompleteType().equals(NomenclatorAttributeAutocompleteType.NOMENCLATOR_ATTRIBUTE)) {
					entity.setNomenclatorAttributeKeyForAutocomplete(model.getNomenclatorAttributeKeyForAutocomplete());
				}	
			}
		}
				
		if (model.getType().equals(NomenclatorAttributeTypeEnum.NOMENCLATOR.name())) {
			Nomenclator dataTypeNomenclator = nomenclatorDao.find(model.getTypeNomenclatorId());
			entity.setTypeNomenclator(dataTypeNomenclator);
			entity.setTypeNomenclatorSelectionFiltersCustomClass(model.getTypeNomenclatorSelectionFiltersCustomClass());
			String customClassAttributeKeys = model.getTypeNomenclatorSelectionFiltersCustomClassAttributeKeys();
			if (StringUtils.isNotBlank(customClassAttributeKeys)) {
				customClassAttributeKeys = customClassAttributeKeys.trim();
			}
			entity.setTypeNomenclatorSelectionFiltersCustomClassAttributeKeys(customClassAttributeKeys);
			
			if (entity.getTypeNomenclatorSelectionFilters() == null) {
				entity.setTypeNomenclatorSelectionFilters(new ArrayList<>());
			}
			
			List<NomenclatorAttributeSelectionFilter> filtersToRemove = new ArrayList<>();
			for (NomenclatorAttributeSelectionFilter nf : entity.getTypeNomenclatorSelectionFilters()) {
				boolean foundModelForThis = false;
				if (CollectionUtils.isNotEmpty(model.getTypeNomenclatorSelectionFilters())) {					
					for (NomenclatorAttributeSelectionFilterModel nfModel : model.getTypeNomenclatorSelectionFilters()) {
						if (nfModel.getId() != null && nf.getId().equals(nfModel.getId())) {
							foundModelForThis = true;
							nf.setFilterAttribute(nomenclatorAttributeDao.find(nfModel.getFilterAttributeId()));
							nf.setDefaultFilterValue(nfModel.getDefaultFilterValue());
							String attributeNameForValue = nf.getAttributeKeyForAutocompleteFilterValue();
							if (StringUtils.isNotBlank(attributeNameForValue)) {
								attributeNameForValue = attributeNameForValue.trim();
							}
							nf.setAttributeKeyForAutocompleteFilterValue(attributeNameForValue);
						}
					}
				}
				if (!foundModelForThis) {
					filtersToRemove.add(nf);
				}				
			}
			if (CollectionUtils.isNotEmpty(model.getTypeNomenclatorSelectionFilters())) {					
				for (NomenclatorAttributeSelectionFilterModel nfModel : model.getTypeNomenclatorSelectionFilters()) {
					if (nfModel.getId() == null) {
						NomenclatorAttributeSelectionFilter newFilter = new NomenclatorAttributeSelectionFilter();
						newFilter.setAttribute(entity);
						newFilter.setFilterAttribute(nomenclatorAttributeDao.find(nfModel.getFilterAttributeId()));
						newFilter.setDefaultFilterValue(nfModel.getDefaultFilterValue());
						String attributeNameForValue = nfModel.getAttributeKeyForAutocompleteFilterValue();
						if (StringUtils.isNotBlank(attributeNameForValue)) {
							attributeNameForValue = attributeNameForValue.trim();
						}
						newFilter.setAttributeKeyForAutocompleteFilterValue(attributeNameForValue);
						entity.getTypeNomenclatorSelectionFilters().add(newFilter);
					}
				}
			}
			if (CollectionUtils.isNotEmpty(filtersToRemove)) {
				entity.getTypeNomenclatorSelectionFilters().removeAll(filtersToRemove);
			}
		} else {
			entity.setTypeNomenclator(null);
			if (CollectionUtils.isNotEmpty(entity.getTypeNomenclatorSelectionFilters())) {
				entity.getTypeNomenclatorSelectionFilters().clear();
			}
			entity.setTypeNomenclatorSelectionFiltersCustomClass(null);
			entity.setTypeNomenclatorSelectionFiltersCustomClassAttributeKeys(null);
		}
	}
	
	public NomenclatorAttribute getFromModel(NomenclatorAttributeModel model, Nomenclator nomenclator) {
		NomenclatorAttribute entity = null;
		if (model.getId() != null) {
			entity = nomenclatorAttributeDao.find(model.getId());
		} else {
			entity = new NomenclatorAttribute();
		}
		updateFromModel(entity, model, nomenclator);
		return entity;
	}
	
	public List<NomenclatorAttributeModel> getModels(List<NomenclatorAttribute> attributes) {
		List<NomenclatorAttributeModel> models = new ArrayList<NomenclatorAttributeModel>();
		for (NomenclatorAttribute attribute : attributes) {
			models.add(getModel(attribute));
		}
		return models;
	}
	
	public List<NomenclatorAttribute> getFromModels(List<NomenclatorAttributeModel> models, Nomenclator nomenclator) {
		List<NomenclatorAttribute> entities = new ArrayList<NomenclatorAttribute>();
		for (NomenclatorAttributeModel model : models) {
			entities.add(getFromModel(model, nomenclator));
		}
		return entities;
	}

	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}
	
	public void setNomenclatorAttributeDao(NomenclatorAttributeDao nomenclatorAttributeDao) {
		this.nomenclatorAttributeDao = nomenclatorAttributeDao;
	}
}
