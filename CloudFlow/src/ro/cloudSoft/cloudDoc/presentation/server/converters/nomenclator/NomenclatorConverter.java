package ro.cloudSoft.cloudDoc.presentation.server.converters.nomenclator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorAttributeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorUiAttributeDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.Nomenclator;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttribute;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorUiAttribute;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorAttributeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorModel;

public class NomenclatorConverter {
	
	private NomenclatorAttributeConverter nomenclatorAttributeConverter;
	private NomenclatorDao nomenclatorDao;
	private NomenclatorUiAttributeDao nomenclatorUiAttributeDao;
	private NomenclatorAttributeDao nomenclatorAttributeDao;
	
	public NomenclatorModel getModelFromNomenclator(Nomenclator entity) {
		NomenclatorModel model = new NomenclatorModel();
		model.setId(entity.getId());
		model.setName(entity.getName());
		model.setAttributes(nomenclatorAttributeConverter.getModels(entity.getAttributes()));
		
		List<String> uiAttributeNames = new ArrayList<String>();
		for (NomenclatorUiAttribute uiAttribute : entity.getUiAttributes()) {
			uiAttributeNames.add(uiAttribute.getAttribute().getName());
		}
		
		model.setUiAttributeNames(uiAttributeNames);

		return model;
	}
	
	public Nomenclator getFromModel(NomenclatorModel model) {
		
		Nomenclator entity = null;		
		if (model.getId() != null) {
			entity = nomenclatorDao.find(model.getId());
		} else {
			entity = new Nomenclator();
			entity.setAttributes(new ArrayList<>());
			entity.setUiAttributes(new ArrayList<>());
		}
		
		entity.setName(model.getName());
		
		// Attributes
		List<NomenclatorAttribute> persitedAttributesToRemove = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(entity.getAttributes())) {			
			for (NomenclatorAttribute persistedAttribute : entity.getAttributes()) {
				boolean foundModelForThis = false;
				for (NomenclatorAttributeModel attributeModel : model.getAttributes()) {
					if (attributeModel.getId() != null && persistedAttribute.getId().equals(attributeModel.getId())) {
						foundModelForThis = true;
						nomenclatorAttributeConverter.updateFromModel(persistedAttribute, attributeModel, entity);
					}
				}
				if (!foundModelForThis) {
					persitedAttributesToRemove.add(persistedAttribute);
				}
			}
		}
		for (NomenclatorAttributeModel attributeModel : model.getAttributes()) {
			if (attributeModel.getId() == null) {
				entity.getAttributes().add(nomenclatorAttributeConverter.getFromModel(attributeModel, entity));
			}
		}
		if (CollectionUtils.isNotEmpty(persitedAttributesToRemove)) {
			entity.getAttributes().removeAll(persitedAttributesToRemove);
		}
		
		// UI Attributes
		List<NomenclatorUiAttribute> persitedUiAttributesToRemove = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(entity.getUiAttributes())) {
			for (NomenclatorUiAttribute persistedUiAttribute : entity.getUiAttributes()) {
				boolean foundModelForThis = false;
				for (String attributeName : model.getUiAttributeNames()) {
					if (persistedUiAttribute.getAttribute().getName().equals(attributeName)) {
						foundModelForThis = true;
					}
				}
				if (!foundModelForThis) {
					persitedUiAttributesToRemove.add(persistedUiAttribute);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(persitedUiAttributesToRemove)) {
			entity.getUiAttributes().removeAll(persitedUiAttributesToRemove);
		}
		int uiAtributeOrder = 1;
		for (String attributeName : model.getUiAttributeNames()) {
			boolean foundInPersisted = false;
			NomenclatorUiAttribute newOrUpdatedUiAttribute = null;
			for (NomenclatorUiAttribute persistedUiAttribute : entity.getUiAttributes()) {
				if (persistedUiAttribute.getAttribute().getName().equals(attributeName)) {
					foundInPersisted = true;
					newOrUpdatedUiAttribute = persistedUiAttribute;
				}
			}
			if (!foundInPersisted) {
				newOrUpdatedUiAttribute = new NomenclatorUiAttribute();
				entity.getUiAttributes().add(newOrUpdatedUiAttribute);
				for (NomenclatorAttribute attribute : entity.getAttributes()) {
					if (attribute.getName().equals(attributeName)) {
						newOrUpdatedUiAttribute.setAttribute(attribute);
						newOrUpdatedUiAttribute.setNomenclator(entity);
					}
				}
			}			
			newOrUpdatedUiAttribute.setUiOrder(uiAtributeOrder++);
		}
		
		return entity;
	}

	public void setNomenclatorAttributeConverter(NomenclatorAttributeConverter nomenclatorAttributeConverter) {
		this.nomenclatorAttributeConverter = nomenclatorAttributeConverter;
	}

	public void setNomenclatorDao(NomenclatorDao nomenclatorDao) {
		this.nomenclatorDao = nomenclatorDao;
	}

	public void setNomenclatorUiAttributeDao(NomenclatorUiAttributeDao nomenclatorUiAttributeDao) {
		this.nomenclatorUiAttributeDao = nomenclatorUiAttributeDao;
	}
	
	public void setNomenclatorAttributeDao(NomenclatorAttributeDao nomenclatorAttributeDao) {
		this.nomenclatorAttributeDao = nomenclatorAttributeDao;
	}
}
