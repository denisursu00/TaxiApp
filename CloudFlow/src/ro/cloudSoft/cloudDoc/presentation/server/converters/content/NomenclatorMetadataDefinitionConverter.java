package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorAttributeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.domain.content.NomenclatorMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.NomenclatorMetadataDefinitionValueSelectionFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorMetadataDefinitionValueSelectionFilterModel;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class NomenclatorMetadataDefinitionConverter implements InitializingBean {
	
	private NomenclatorDao nomenclatorDao;
	private NomenclatorAttributeDao nomenclatorAttributeDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
				nomenclatorDao,
				nomenclatorAttributeDao
		);
	}
	
	public NomenclatorMetadataDefinitionModel getModel(NomenclatorMetadataDefinition entity) {		
		NomenclatorMetadataDefinitionModel model = new NomenclatorMetadataDefinitionModel();		
		model.setNomenclatorId(entity.getNomenclator().getId());
		List<NomenclatorMetadataDefinitionValueSelectionFilter> valueSelectionFilters = entity.getValueSelectionFilters();
		if (CollectionUtils.isNotEmpty(valueSelectionFilters)) {
			model.setValueSelectionFilters(new ArrayList<NomenclatorMetadataDefinitionValueSelectionFilterModel>());
			for (NomenclatorMetadataDefinitionValueSelectionFilter filter : valueSelectionFilters) {
				model.getValueSelectionFilters().add(getFromEntity(filter));
			}
		}
		return model;
	}
	
	public NomenclatorMetadataDefinition getFromModel(NomenclatorMetadataDefinitionModel model) {
		NomenclatorMetadataDefinition entity = new NomenclatorMetadataDefinition();
		entity.setNomenclator(nomenclatorDao.find(model.getNomenclatorId()));
		entity.setValueSelectionFilters(new ArrayList<>());
		if (CollectionUtils.isNotEmpty(model.getValueSelectionFilters())) {
			for (NomenclatorMetadataDefinitionValueSelectionFilterModel filterModel : model.getValueSelectionFilters()) {
				NomenclatorMetadataDefinitionValueSelectionFilter filter = new NomenclatorMetadataDefinitionValueSelectionFilter();
				filter.setId(filterModel.getId());
				filter.setMetadataDefinition(entity);
				filter.setFilterAttribute(nomenclatorAttributeDao.find(filterModel.getFilterAttributeId()));
				filter.setDefaultFilterValue(filterModel.getDefaultFilterValue());
				filter.setMetadataNameForAutocompleteFilterValue(filterModel.getMetadataNameForAutocompleteFilterValue());
				entity.getValueSelectionFilters().add(filter);
			}
		}
		return entity;
	}
	
	private NomenclatorMetadataDefinitionValueSelectionFilterModel getFromEntity(NomenclatorMetadataDefinitionValueSelectionFilter filter) {
		NomenclatorMetadataDefinitionValueSelectionFilterModel model = new NomenclatorMetadataDefinitionValueSelectionFilterModel();
		model.setId(filter.getId());
		model.setFilterAttributeId(filter.getFilterAttribute().getId());
		model.setFilterAttributeKey(filter.getFilterAttribute().getKey());
		model.setDefaultFilterValue(filter.getDefaultFilterValue());		
		String metadataNameForAutocompleteFilterValue = filter.getMetadataNameForAutocompleteFilterValue();
		if (StringUtils.isNotBlank(metadataNameForAutocompleteFilterValue)) {
			metadataNameForAutocompleteFilterValue = metadataNameForAutocompleteFilterValue.trim();
		}		
		model.setMetadataNameForAutocompleteFilterValue(metadataNameForAutocompleteFilterValue);
		return model;
	}
	
	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}
	
	public void setNomenclatorAttributeDao(NomenclatorAttributeDao nomenclatorAttributeDao) {
		this.nomenclatorAttributeDao = nomenclatorAttributeDao;
	}
}