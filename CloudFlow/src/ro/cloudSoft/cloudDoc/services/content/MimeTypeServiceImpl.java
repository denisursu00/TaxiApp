package ro.cloudSoft.cloudDoc.services.content;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.dao.content.MimeTypeDao;
import ro.cloudSoft.cloudDoc.domain.audit.AuditEntityOperation;
import ro.cloudSoft.cloudDoc.domain.content.MimeType;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.services.AuditService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class MimeTypeServiceImpl implements MimeTypeService, InitializingBean {

	private DocumentTypeService documentTypeService;
	private AuditService auditService;
	private MimeTypeDao mimeTypeDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			documentTypeService,
			auditService,
			mimeTypeDao
		);
	}
	
	@Override
	public List<MimeType> getAllMimeTypes() {
		return mimeTypeDao.getAllMimeTypes();
	}
	
	@Override
	public MimeType getMimeTypeById(Long id) {
		return mimeTypeDao.find(id);
	}
	
	@Override
	public MimeType getMimeTypeById(Long id, SecurityManager userSecurity) {
		MimeType mimeType = getMimeTypeById(id);
		auditService.auditMimeTypeOperation(userSecurity, mimeType, AuditEntityOperation.READ);
		return mimeType;
	}
	
	@Override
	public void saveMimeTypes(Collection<MimeType> mimeTypes) throws AppException {
		
		mimeTypeDao.saveAll(mimeTypes);
	}
	
	@Override
	public void saveMimeType(MimeType mimeType) throws AppException {
		
		Long mimeTypeIdForExtension = mimeTypeDao.getMimeTypeIdForExtension(mimeType.getExtension());
		
		if ((mimeTypeIdForExtension != null) && !mimeTypeIdForExtension.equals(mimeType.getId()) ) {
			throw new AppException(AppExceptionCodes.MIME_TYPE_EXTENSION_MUST_BE_UNIQUE);
		}
		mimeTypeDao.saveMimeType(mimeType);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void saveMimeType(MimeType mimeType, SecurityManager userSecurity) throws AppException {
		
		boolean isMimeTypeNew = mimeType.isNew();
		
		saveMimeType(mimeType);
		
		AuditEntityOperation operation = (isMimeTypeNew) ? AuditEntityOperation.ADD : AuditEntityOperation.MODIFY;
		auditService.auditMimeTypeOperation(userSecurity, mimeType, operation);
	}
	
	@Override
	public void deleteMimeType(Long mimeTypeId) throws AppException {
		if (documentTypeService.isMimeTypeUsedInDocumentTypes(mimeTypeId)) {
			throw new AppException(AppExceptionCodes.CANNOT_DELETE_DOCUMENT_TYPES_USING_MIME_TYPE_EXIST);
		}
		mimeTypeDao.deleteMimeType(mimeTypeId);
	}
	
	@Override
	public void deleteMimeType(Long mimeTypeId, SecurityManager userSecurity) throws AppException {
		MimeType mimeType = getMimeTypeById(mimeTypeId);
		deleteMimeType(mimeTypeId);
		auditService.auditMimeTypeOperation(userSecurity, mimeType, AuditEntityOperation.DELETE);
	}
	
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
	public void setMimeTypeDao(MimeTypeDao mimeTypeDao) {
		this.mimeTypeDao = mimeTypeDao;
	}
}