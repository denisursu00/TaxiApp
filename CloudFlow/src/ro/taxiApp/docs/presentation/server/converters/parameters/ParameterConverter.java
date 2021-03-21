package ro.taxiApp.docs.presentation.server.converters.parameters;

import java.util.ArrayList;
import java.util.List;

import ro.taxiApp.docs.dao.parameters.ParametersDao;
import ro.taxiApp.docs.domain.parameters.Parameter;
import ro.taxiApp.docs.domain.parameters.ParameterType;
import ro.taxiApp.docs.presentation.client.shared.model.parameters.ParameterModel;

public class ParameterConverter {
	
	private ParametersDao parametersDao;
	
	public ParameterModel toModel(Parameter entity) {
		ParameterModel model = new ParameterModel();
		model.setId(entity.getId());
		model.setName(entity.getName());
		model.setDescription(entity.getDescription());
		model.setType(entity.getType().name());
		model.setValue(entity.getValue());
		return model;
	}
	
	public Parameter toEntity(ParameterModel model) {
		Parameter entity = null;
		if (model.getId() != null) {
			entity = parametersDao.getById(model.getId());
		} else {
			entity = new Parameter();
		}
		
		entity.setName(model.getName());
		entity.setDescription(model.getDescription());
		entity.setType(ParameterType.valueOf(model.getType()));
		entity.setValue(model.getValue());
		
		return entity;
	}
	
	public List<ParameterModel> toModels(List<Parameter> entities) {
		List<ParameterModel> models = new ArrayList<>();
		for (Parameter entity : entities) {
			models.add(toModel(entity));
		}
		return models;
	}
	
	public void setParametersDao(ParametersDao parametersDao) {
		this.parametersDao = parametersDao;
	}
}
