package ro.cloudSoft.cloudDoc.presentation.server.services.content;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.MimeType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.content.MimeTypeGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.MimeTypeConverter;
import ro.cloudSoft.cloudDoc.presentation.server.services.GxtServiceImplBase;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.content.MimeTypeService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class MimeTypeGxtServiceImpl extends GxtServiceImplBase implements MimeTypeGxtService, InitializingBean {
	
	private MimeTypeService mimeTypeService;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			mimeTypeService
		);
	}
	
	@Override
	public List<MimeTypeModel> getAllMimeTypes() {
		List<MimeType> allMimeTypes = mimeTypeService.getAllMimeTypes();
		List<MimeTypeModel> allMimeTypeModels = new ArrayList<MimeTypeModel>();
		for (MimeType mimeType : allMimeTypes) {
			MimeTypeModel mimeTypeModel = MimeTypeConverter.getModelFromMimeType(mimeType);
			allMimeTypeModels.add(mimeTypeModel);
		}
		return allMimeTypeModels;
	}
	
	@Override
	public MimeTypeModel getMimeTypeById(Long mimeTypeId) throws PresentationException {
		MimeType mimeType = mimeTypeService.getMimeTypeById(mimeTypeId, getSecurity());
		if (mimeType != null) {
			return MimeTypeConverter.getModelFromMimeType(mimeType);
		}
		return null;
	}
	
	@Override
	public void saveMimeType(MimeTypeModel mimeTypeModel) throws PresentationException{
		MimeType mimeType = MimeTypeConverter.getMimeTypeFromModel(mimeTypeModel);
		
		try {
			mimeTypeService.saveMimeType(mimeType, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}	
	}
	
	@Override
	public void deleteMimeType(Long mimeTypeId) throws PresentationException {
		
		try {
			mimeTypeService.deleteMimeType(mimeTypeId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	public void setMimeTypeService(MimeTypeService mimeTypeService) {
		this.mimeTypeService = mimeTypeService;
	}
	
}
