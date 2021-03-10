package ro.cloudSoft.cloudDoc.services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.ConcediuDao;
import ro.cloudSoft.cloudDoc.dao.PendingPontajForConcediuDao;
import ro.cloudSoft.cloudDoc.domain.Concediu;
import ro.cloudSoft.cloudDoc.domain.PendingPontajForConcediu;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetForLeaveFailure;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.LeaveDayDetails;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.LeaveDetails;
import ro.cloudSoft.cloudDoc.domain.mail.EmailMessage;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.utils.PontajForConcediuCreationFailureEmailGenerator;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.cloudDoc.webServices.client.TimesheetsForLeavesWebServiceClient;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Serviciu pentru pontaje
 * Comunica cu un serviciu web extern al unei aplicatii de pontaj.
 * 
 * 
 */
public class PontajForConcediiServiceImpl implements PontajForConcediiService, InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(PontajForConcediiServiceImpl.class);
	
	private TimesheetsForLeavesWebServiceClient timesheetsForLeavesWebServiceClient;
	
	private MailService mailService;

	private ConcediuDao concediuDao;
	private UserPersistencePlugin userPersistencePlugin;
	private PendingPontajForConcediuDao pendingPontajForConcediuDao;
	
	private PontajForConcediuCreationFailureEmailGenerator creationFailureEmailGenerator;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		DependencyInjectionUtils.checkRequiredDependencies(
			
			mailService,
			
			concediuDao,
			userPersistencePlugin,
			pendingPontajForConcediuDao,
			
			creationFailureEmailGenerator
		);
	}
	
	@Override
	@Transactional(rollbackFor = Throwable.class, noRollbackFor = AppException.class)
	public void createPontajeForConcediiForYesterday() throws AppException {
		

		Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		Date yesterday = DateUtils.addDays(today, -1);
		

		List<LeaveDayDetails> leaveDetailsList = Lists.newLinkedList();
		
		
		/*
		 * Elimin cele pending pentru ziua de ieri pentru ca pontajele noi pe care
		 * le voi trimite sunt chiar pe ziua de ieri si nu vreau sa trimit duplicate.
		 */
		pendingPontajForConcediuDao.deleteAllForDay(yesterday);

		Set<PendingPontajForConcediu> pendingPontajeForConcedii = pendingPontajForConcediuDao.getAll();
		for (PendingPontajForConcediu pendingPontajForConcediu : pendingPontajeForConcedii) {
			
			LeaveDayDetails leaveDetails = new LeaveDayDetails();
			
			String requesterEmail = pendingPontajForConcediu.getSolicitantEmail();
			leaveDetails.setRequesterEmail(requesterEmail);
			String requesterDepartmentName = pendingPontajForConcediu.getSolicitantOrganizationUnitName();
			leaveDetails.setRequesterDepartmentName(requesterDepartmentName);
			Date day = new Date(pendingPontajForConcediu.getDay().getTime());
			leaveDetails.setDay(day);
			
			leaveDetailsList.add(leaveDetails);
		}
		
		
		Collection<Long> solicitantUserIdsWithConcediiOnYesterday = concediuDao.getSolicitantIdsForConcediiWithCerereAprobataThatContainDay(yesterday);
		Map<Long, String> emailBySolicitantUserIdForUsersWithConcediiOnYesterday = userPersistencePlugin.getEmailByUserId(solicitantUserIdsWithConcediiOnYesterday);
		Map<Long, String> organizationUnitNameBySolicitantUserIdForUsersWithConcediiOnYesterday = userPersistencePlugin.getOrganizationUnitNameByUserId(solicitantUserIdsWithConcediiOnYesterday);
		
		for (Long solicitantUserIdWithConcediuOnYesterday : solicitantUserIdsWithConcediiOnYesterday) {

			LeaveDayDetails leaveDetails = new LeaveDayDetails();
			
			String requesterEmail = emailBySolicitantUserIdForUsersWithConcediiOnYesterday.get(solicitantUserIdWithConcediuOnYesterday);			
			leaveDetails.setRequesterEmail(requesterEmail);			
			String requesterDepartmentName = organizationUnitNameBySolicitantUserIdForUsersWithConcediiOnYesterday.get(solicitantUserIdWithConcediuOnYesterday);
			leaveDetails.setRequesterDepartmentName(requesterDepartmentName);
			
			leaveDetails.setDay(yesterday);
			
			leaveDetailsList.add(leaveDetails);
		}
		
		
		if (leaveDetailsList.isEmpty()) {
			return;
		}
		
		Collection<CreateTimesheetForLeaveFailure> failures = null;
		try {
			failures = timesheetsForLeavesWebServiceClient.createTimesheets(leaveDetailsList);
		} catch (Exception e) {
			
			String logMessage = "A fost aruncata o exceptie in timpul apelarii serviciului web pentru " +
				"pontaje concedii. Pentru a nu pierde pontajele pentru ziua de ieri care trebuiau salvate, " +
				"le voi salva ca pending a.i. la urmatoarea apelare (probabil in alta zi) sa le reincerce.";
			LOGGER.warn(logMessage, e, "createPontajeForConcediiForYesterday");
			
			Collection<PendingPontajForConcediu> pendingPontajeForConcediiOnYesterday = Lists.newLinkedList();
			for (Long solicitantUserIdWithConcediuOnYesterday : solicitantUserIdsWithConcediiOnYesterday) {

				PendingPontajForConcediu pendingPontajForConcediuOnYesterday = new PendingPontajForConcediu();
				
				String emailForSolicitantWithConcediuOnYesterday = emailBySolicitantUserIdForUsersWithConcediiOnYesterday.get(solicitantUserIdWithConcediuOnYesterday);
				pendingPontajForConcediuOnYesterday.setSolicitantEmail(emailForSolicitantWithConcediuOnYesterday);
				
				String organizationUniNameForSolicitantWithConcediuOnYesterday = organizationUnitNameBySolicitantUserIdForUsersWithConcediiOnYesterday.get(solicitantUserIdWithConcediuOnYesterday);
				pendingPontajForConcediuOnYesterday.setSolicitantOrganizationUnitName(organizationUniNameForSolicitantWithConcediuOnYesterday);
				
				pendingPontajForConcediuOnYesterday.setDay(yesterday);

				pendingPontajeForConcediiOnYesterday.add(pendingPontajForConcediuOnYesterday);
			}
			
			/*
			 * Incercarea de pontare a esuat. Ca sa nu pierd pontajele pentru
			 * ziua de ieri, le adaug ca pending ca sa incerc in alta zi.
			 */
			pendingPontajForConcediuDao.save(pendingPontajeForConcediiOnYesterday);
			
			throw new AppException();
		}
		
		Set<PendingPontajForConcediu> pendingPontajForConcediuFromFailures = Sets.newHashSet();
		for (CreateTimesheetForLeaveFailure failure : failures) {
			
			Date day = failure.getLeaveDay();
			
			PendingPontajForConcediu pendingPontajForConcediu = new PendingPontajForConcediu();
			pendingPontajForConcediu.setSolicitantEmail(failure.getRequesterEmail());
			pendingPontajForConcediu.setSolicitantOrganizationUnitName(failure.getRequesterDepartmentName());
			pendingPontajForConcediu.setDay(day);
			
			pendingPontajForConcediuFromFailures.add(pendingPontajForConcediu);
			
			boolean isNewFailure = day.equals(yesterday);
			if (isNewFailure) {
				EmailMessage emailMessage = creationFailureEmailGenerator.getEmailForFailure(failure);
				try {
					mailService.send(emailMessage);
				} catch (RuntimeException re) {
					String logMessage = "Nu s-a putut notifica o persoana legat de esecul unui pontaj. " +
						"Mail-ul pentru care s-a incercat trimiterea este: [" + emailMessage + "].";
					LOGGER.error(logMessage, re, "createPontajeForConcediiForYesterday");
				}
			}
		}
		
		Set<PendingPontajForConcediu> pendingPontajeForConcediiToSave = pendingPontajForConcediuFromFailures;
		pendingPontajForConcediuDao.save(pendingPontajeForConcediiToSave);
		
		Set<PendingPontajForConcediu> resolvedPendingPontajeForConcedii = Sets.newHashSet();
		for (PendingPontajForConcediu pendingPontajForConcediu : pendingPontajeForConcedii) {
			if (!pendingPontajForConcediuFromFailures.contains(pendingPontajForConcediu)) {
				resolvedPendingPontajeForConcedii.add(pendingPontajForConcediu);
			}
		}
		pendingPontajForConcediuDao.delete(resolvedPendingPontajeForConcedii);
	}
	
	@Override
	public boolean hasPontajeAprobate(Concediu concediu) throws AppException {
		
		Collection<Long> solicitantUserIdAsCollection = Collections.singleton(concediu.getSolicitantId());
		Map<Long, String> emailBySolicitantUserId = userPersistencePlugin.getEmailByUserId(solicitantUserIdAsCollection);
		Map<Long, String> organizationUnitNameBySolicitantUserId = userPersistencePlugin.getOrganizationUnitNameByUserId(solicitantUserIdAsCollection); 
		
		LeaveDetails leaveDetails = new LeaveDetails();
		
		String requesterEmail = emailBySolicitantUserId.get(concediu.getSolicitantId());
		leaveDetails.setRequesterEmail(requesterEmail);
		String requesterDepartmentName = organizationUnitNameBySolicitantUserId.get(concediu.getSolicitantId());
		leaveDetails.setRequesterDepartmentName(requesterDepartmentName);
		
		Date startDate = new Date(concediu.getDataInceput().getTime());
		leaveDetails.setStartDate(startDate);
		Date endDate = new Date(concediu.getDataSfarsit().getTime());
		leaveDetails.setEndDate(endDate);
		
		boolean hasApprovedTimesheets = timesheetsForLeavesWebServiceClient.hasApprovedTimesheets(leaveDetails);
		return hasApprovedTimesheets;
	}
	
	@Override
	public void deletePendingPontajeForConcediu(Concediu concediu) {
		String solicitantEmail = userPersistencePlugin.getEmail(concediu.getSolicitantId());
		pendingPontajForConcediuDao.deleteAllInIntervalForSolicitant(solicitantEmail, concediu.getDataInceput(), concediu.getDataSfarsit());
	}
	
	public void setTimesheetsForLeavesWebServiceClient(TimesheetsForLeavesWebServiceClient timesheetsForLeavesWebServiceClient) {
		this.timesheetsForLeavesWebServiceClient = timesheetsForLeavesWebServiceClient;
	}
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	public void setConcediuDao(ConcediuDao concediuDao) {
		this.concediuDao = concediuDao;
	}
	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
	public void setPendingPontajForConcediuDao(PendingPontajForConcediuDao pendingPontajForConcediuDao) {
		this.pendingPontajForConcediuDao = pendingPontajForConcediuDao;
	}
	public void setCreationFailureEmailGenerator(PontajForConcediuCreationFailureEmailGenerator creationFailureEmailGenerator) {
		this.creationFailureEmailGenerator = creationFailureEmailGenerator;
	}
}