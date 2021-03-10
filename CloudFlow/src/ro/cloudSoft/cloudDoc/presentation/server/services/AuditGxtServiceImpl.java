package ro.cloudSoft.cloudDoc.presentation.server.services;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.domain.audit.AuditEntry;
import ro.cloudSoft.cloudDoc.domain.audit.AuditSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.AuditEntryModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.AuditGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.converters.audit.AuditEntryConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.audit.AuditSearchCriteriaConverter;
import ro.cloudSoft.cloudDoc.services.AuditService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.PagingList;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class AuditGxtServiceImpl extends GxtServiceImplBase implements AuditGxtService, InitializingBean {

	private AuditService auditService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			auditService
		);
	}
	
	@Override
	public PagingLoadResult<AuditEntryModel> searchAuditEntries(PagingLoadConfig pagingLoadConfig,
			GwtAuditSearchCriteria gwtSearchCriteria) throws PresentationException {
		
		int offset = pagingLoadConfig.getOffset();
		int pageSize = pagingLoadConfig.getLimit();
		
		AuditSearchCriteria searchCriteria = AuditSearchCriteriaConverter.getFromGwt(gwtSearchCriteria);
		
		PagingList<AuditEntry> auditEntriesPagingList = auditService.searchAuditEntries(offset, pageSize, searchCriteria);
		
		List<AuditEntryModel> auditEntryModels = AuditEntryConverter.getModels(auditEntriesPagingList.getElements());
		PagingLoadResult<AuditEntryModel> pagingLoadResult = new BasePagingLoadResult<AuditEntryModel>(auditEntryModels, auditEntriesPagingList.getOffset(), auditEntriesPagingList.getTotalCount());
		return pagingLoadResult;
	}
	
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
}