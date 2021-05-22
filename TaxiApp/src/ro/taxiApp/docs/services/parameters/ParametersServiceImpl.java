package ro.taxiApp.docs.services.parameters;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.InitializingBean;

import ro.taxiApp.common.utils.DateUtils;
import ro.taxiApp.common.utils.DependencyInjectionUtils;
import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.core.AppExceptionCodes;
import ro.taxiApp.docs.core.constants.FormatConstants;
import ro.taxiApp.docs.dao.parameters.ParametersDao;
import ro.taxiApp.docs.domain.parameters.Parameter;
import ro.taxiApp.docs.domain.parameters.ParameterType;
import ro.taxiApp.docs.presentation.client.shared.model.parameters.ParameterModel;
import ro.taxiApp.docs.presentation.server.converters.parameters.ParameterConverter;

public class ParametersServiceImpl implements ParametersService, InitializingBean {
	
	private ParametersDao parametersDao;
	private ParameterConverter parametersConverter;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(parametersDao, parametersConverter);
	}
	
	@Override
	public Long save(ParameterModel parameterModel) throws AppException {
		
		validateParameterModel(parameterModel);
		
		Parameter parameter = parametersConverter.toEntity(parameterModel);
		return parametersDao.save(parameter);
	}
	
	private void validateParameterModel(ParameterModel model) throws AppException {
		
		if (StringUtils.isBlank(model.getValue())) {
			throw new AppException(AppExceptionCodes.PARAMETER_VALUE_CANNOT_BE_NULL);
		}
		
		if (model.getType().equals(ParameterType.STRING.name())) {
			return;
		} else if (model.getType().equals(ParameterType.NUMBER.name())) {
			boolean isNumericValue = NumberUtils.isParsable(model.getValue());
			if (!isNumericValue) {
				throw new AppException(AppExceptionCodes.PARAMETER_VALUE_IS_NOT_OF_TYPE_NUMBER);
			}
		} else if (model.getType().equals(ParameterType.DATE.name())) {
			
			boolean isDate = DateUtils.isDate(model.getValue(), FormatConstants.PARAMETER_DATE_FOR_STORAGE);
			if (!isDate) {
				throw new AppException(AppExceptionCodes.PARAMETER_VALUE_IS_NOT_OF_TYPE_DATE);
			}
		} else if (model.getType().equals(ParameterType.BOOLEAN.name())) {
			if (model.getValue().equals("null")) {
				model.setValue("false");
			}
			if (!model.getValue().equals("true") && !model.getValue().equals("false")) {
				throw new AppException(AppExceptionCodes.PARAMETER_VALUE_IS_NOT_OF_TYPE_BOOLEAN);
			}
		} else {
			throw new RuntimeException("Parameter type [" + model.getType() + "] is not a known type.");
		}
	}
	
	@Override
	public List<ParameterModel> getAllParameters() {
		return parametersConverter.toModels(parametersDao.getAll());
	}
	
	@Override
	public ParameterModel getParameterByName(String name) throws AppException {
		Parameter parameter = parametersDao.getByName(name);
		if (parameter == null) {
			throw new AppException(AppExceptionCodes.PARAMETER_NOT_FOUND);
		}
		return parametersConverter.toModel(parameter);
	}
	
	@Override
	public ParameterModel getParameterById(Long id) {
		Parameter parameter = parametersDao.getById(id);
		return parametersConverter.toModel(parameter);
	}
	
	@Override
	public Integer getParamaterValueAsIntegerByParameterName(String name) throws AppException {
		ParameterModel parameterModel = getParameterByName(name);
		return Integer.valueOf(parameterModel.getValue());
	}
	
	@Override
	public void deleteById(Long id) {
		parametersDao.delete(id);
	}
	
	public void setParametersDao(ParametersDao parametersDao) {
		this.parametersDao = parametersDao;
	}
	
	public void setParametersConverter(ParameterConverter parametersConverter) {
		this.parametersConverter = parametersConverter;
	}

	@Override
	public Double getParamaterValueAsDoubleByParameterName(String name) throws AppException {
		ParameterModel parameterModel = getParameterByName(name);
		return Double.valueOf(parameterModel.getValue());
	}
}
