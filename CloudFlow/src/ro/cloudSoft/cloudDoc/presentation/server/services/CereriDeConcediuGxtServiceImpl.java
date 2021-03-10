package ro.cloudSoft.cloudDoc.presentation.server.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.CereriDeConcediuGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.CereriDeConcediuService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.Sets;

/**
 * 
 */
public class CereriDeConcediuGxtServiceImpl extends GxtServiceImplBase implements CereriDeConcediuGxtService, InitializingBean {

	private CereriDeConcediuService cereriDeConcediuService;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			cereriDeConcediuService
		);
	}
	
	@Override
	public void anuleaza(String documentLocationRealName, String documentId) throws PresentationException {
		try {
			cereriDeConcediuService.anuleaza(documentLocationRealName, documentId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@Override
	public Collection<Long> getIdsForUsersInConcediu(Date dataInceput, Date dataSfarsit) {
		Collection<Long> idsForUsersInConcediu = cereriDeConcediuService.getIdsForUsersInConcediu(dataInceput, dataSfarsit);
		return Sets.newHashSet(idsForUsersInConcediu);
	}
	
	public void setCereriDeConcediuService(CereriDeConcediuService cereriDeConcediuService) {
		this.cereriDeConcediuService = cereriDeConcediuService;
	}
}