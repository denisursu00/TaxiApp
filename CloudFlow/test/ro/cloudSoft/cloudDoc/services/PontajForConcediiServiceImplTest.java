package ro.cloudSoft.cloudDoc.services;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.dao.ConcediuDao;
import ro.cloudSoft.cloudDoc.dao.PendingPontajForConcediuDao;
import ro.cloudSoft.cloudDoc.domain.PendingPontajForConcediu;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetForLeaveFailure;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.CreateTimesheetForLeaveProjectNotFoundFailure;
import ro.cloudSoft.cloudDoc.domain.external.webServices.pontaj.concedii.LeaveDayDetails;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.services.MailService;
import ro.cloudSoft.cloudDoc.services.PontajForConcediiServiceImpl;
import ro.cloudSoft.cloudDoc.utils.PontajForConcediuCreationFailureEmailGenerator;
import ro.cloudSoft.cloudDoc.webServices.client.TimesheetsForLeavesWebServiceClient;
import ro.cloudSoft.common.utils.test.mockito.WasInvokedCorrectlyAnswer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * 
 */
public class PontajForConcediiServiceImplTest {
	
	private TimesheetsForLeavesWebServiceClient timesheetsForLeavesWebServiceClient;
	
	private MailService mailService;
	
	private ConcediuDao concediuDao;
	private UserPersistencePlugin userPersistencePlugin;
	private PendingPontajForConcediuDao pendingPontajForConcediuDao;

	private PontajForConcediuCreationFailureEmailGenerator creationFailureEmailGenerator;

	private PontajForConcediiServiceImpl pontajForConcediiServiceImpl;
	
	@Before
	public void setUp() throws Exception {
		
		pontajForConcediiServiceImpl = new PontajForConcediiServiceImpl();
		
		timesheetsForLeavesWebServiceClient = mock(TimesheetsForLeavesWebServiceClient.class);
		pontajForConcediiServiceImpl.setTimesheetsForLeavesWebServiceClient(timesheetsForLeavesWebServiceClient);
		
		mailService = mock(MailService.class);
		pontajForConcediiServiceImpl.setMailService(mailService);
		
		concediuDao = mock(ConcediuDao.class);
		pontajForConcediiServiceImpl.setConcediuDao(concediuDao);
		userPersistencePlugin = mock(UserPersistencePlugin.class);
		pontajForConcediiServiceImpl.setUserPersistencePlugin(userPersistencePlugin);
		
		pendingPontajForConcediuDao = mock(PendingPontajForConcediuDao.class);
		pontajForConcediiServiceImpl.setPendingPontajForConcediuDao(pendingPontajForConcediuDao);
		
		creationFailureEmailGenerator = mock(PontajForConcediuCreationFailureEmailGenerator.class);
		pontajForConcediiServiceImpl.setCreationFailureEmailGenerator(creationFailureEmailGenerator);
	}
	
	private CreateTimesheetForLeaveFailure createProjectNotFoundFailureFor(String requesterEmail, Date leaveDay, String departmentName) {
		
		CreateTimesheetForLeaveProjectNotFoundFailure projectNotFoundFailure = new CreateTimesheetForLeaveProjectNotFoundFailure();
		
		projectNotFoundFailure.setRequesterEmail(requesterEmail);
		projectNotFoundFailure.setRequesterDepartmentName(departmentName);
		projectNotFoundFailure.setLeaveDay(leaveDay);
		
		return projectNotFoundFailure;
	}
	
	/**
	 * Sunt 2 utilizatori din departamente (unitati organizatorice) diferite.
	 * Pentru fiecare trebuie sa se salveze un pontaj pe ziua de ieri.
	 * De asemenea, pentru fiecare exista cate un pontaj care in trecut nu a putut fi salvat (din diverse motive).
	 * 
	 * Se presupune ca nu merge salvarea pentru utilizatorul 2 (din cauza ca nu i se gaseste proiectul pentru pontaj concediu.
	 * 
	 * Astfel, metoda ar trebui sa incerce salvarea a 4 pontaje (cate 1 nou / utilizator + cate 1 pending / utilizator).
	 * La primirea raspunsului, ar trebui sa stearga pontajul pending al utilizatorului 1 (s-a pontat cu succes)
	 * si sa salveze pontajul pending al utilizatorului 2 + un nou pontaj pending pentru pontajul pentru ieri, nereusit.
	 */
	@Test
	public void createdPontajeForConcediiForYesterday() throws ParseException, AppException {

		Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		Date yesterday = DateUtils.addDays(today, -1);
		
		Long user1Id = 1L;
		String user1Email = "user1@test.com";
		String user1OrganizationUnitName = "Org unit 1";
		
		Long user2Id = 2L;
		String user2Email = "test2@test.ro";
		String user2OrganizationUnitName = "Org unit 2";
		
		Map<Long, String> emailByUserId = ImmutableMap.<Long, String> builder()
			.put(user1Id, user1Email)
			.put(user2Id, user2Email)
			.build();
		Map<Long, String> organizationUnitNameByUserId = ImmutableMap.<Long, String> builder()
			.put(user1Id, user1OrganizationUnitName)
			.put(user2Id, user2OrganizationUnitName)
			.build();
		Map<String, String> organizationUnitNameByUserEmailInLowerCase = ImmutableMap.<String, String> builder()
			.put(user1Email.toLowerCase(), user1OrganizationUnitName)
			.put(user2Email.toLowerCase(), user2OrganizationUnitName)
			.build();
		
		DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
		
		Collection<Long> solicitantIdsForConcediiWithCerereAprobataThatContainYesterday = ImmutableSet.of(user1Id, user2Id);		
		when(concediuDao.getSolicitantIdsForConcediiWithCerereAprobataThatContainDay(Mockito.eq(yesterday))).thenReturn(solicitantIdsForConcediiWithCerereAprobataThatContainYesterday);
		when(userPersistencePlugin.getEmailByUserId(Mockito.eq(solicitantIdsForConcediiWithCerereAprobataThatContainYesterday))).thenReturn(emailByUserId);
		when(userPersistencePlugin.getOrganizationUnitNameByUserId(Mockito.eq(solicitantIdsForConcediiWithCerereAprobataThatContainYesterday))).thenReturn(organizationUnitNameByUserId);
		
		PendingPontajForConcediu pendingPontajForConcediuForUser1 = new PendingPontajForConcediu();
		pendingPontajForConcediuForUser1.setSolicitantEmail(user1Email);
		pendingPontajForConcediuForUser1.setSolicitantOrganizationUnitName(user1OrganizationUnitName);
		Date dayForPendingPontajForConcediuForUser1 = dateFormatter.parse("01.01.2000");
		pendingPontajForConcediuForUser1.setDay(dayForPendingPontajForConcediuForUser1);
		
		PendingPontajForConcediu pendingPontajForConcediuForUser2 = new PendingPontajForConcediu();
		pendingPontajForConcediuForUser2.setSolicitantEmail(user2Email);
		pendingPontajForConcediuForUser2.setSolicitantOrganizationUnitName(user2OrganizationUnitName);
		Date dayForPendingPontajForConcediuForUser2 = dateFormatter.parse("02.01.2000");
		pendingPontajForConcediuForUser2.setDay(dayForPendingPontajForConcediuForUser2);
		
		Set<PendingPontajForConcediu> pendingPontajeForConcedii = ImmutableSet.of(pendingPontajForConcediuForUser1, pendingPontajForConcediuForUser2);
		when(pendingPontajForConcediuDao.getAll()).thenReturn(pendingPontajeForConcedii);
		Set<String> solicitantEmailsForUsersWithPendingPontajForConcediu = ImmutableSet.of(user1Email, user2Email);
		when(userPersistencePlugin.getOrganizationUnitNameByUserEmailInLowerCase(Mockito.eq(solicitantEmailsForUsersWithPendingPontajForConcediu))).thenReturn(organizationUnitNameByUserEmailInLowerCase);
		
		List<LeaveDayDetails> leaveDetailsList = ImmutableList.<LeaveDayDetails> builder()
			.add(new LeaveDayDetails(user1Email, user1OrganizationUnitName, dayForPendingPontajForConcediuForUser1))
			.add(new LeaveDayDetails(user2Email, user2OrganizationUnitName, dayForPendingPontajForConcediuForUser2))
			.add(new LeaveDayDetails(user1Email, user1OrganizationUnitName, yesterday))
			.add(new LeaveDayDetails(user2Email, user2OrganizationUnitName, yesterday))
			.build();
		Collection<CreateTimesheetForLeaveFailure> failures = ImmutableSet.<CreateTimesheetForLeaveFailure> builder()
			.add(createProjectNotFoundFailureFor(user2Email, yesterday, user2OrganizationUnitName))
			.add(createProjectNotFoundFailureFor(user2Email, dayForPendingPontajForConcediuForUser2, user2OrganizationUnitName))
			.build();
		WasInvokedCorrectlyAnswer wereTheRightLeaveDetailsSentForCreatingTimesheetsAnswer = new WasInvokedCorrectlyAnswer(failures);
		when(timesheetsForLeavesWebServiceClient.createTimesheets(Mockito.eq(leaveDetailsList))).thenAnswer(wereTheRightLeaveDetailsSentForCreatingTimesheetsAnswer);
		
		Collection<PendingPontajForConcediu> resolvedPendingPontajeForConcedii = ImmutableSet.<PendingPontajForConcediu> builder()
			.add(pendingPontajForConcediuForUser1)
			.build();		
		WasInvokedCorrectlyAnswer wereTheRightPendingPontajeForConcediiDeletedAnswer = new WasInvokedCorrectlyAnswer();
		Mockito.doAnswer(wereTheRightPendingPontajeForConcediiDeletedAnswer).when(pendingPontajForConcediuDao).delete(Mockito.eq(resolvedPendingPontajeForConcedii));
		
		Collection<PendingPontajForConcediu> pendingPontajeForConcediiToSave = ImmutableSet.<PendingPontajForConcediu> builder()
			.add(new PendingPontajForConcediu(user2Email, user2OrganizationUnitName, yesterday))
			.add(pendingPontajForConcediuForUser2)
			.build();
		WasInvokedCorrectlyAnswer wereTheRightPendingPontajeForConcediiSavedAnswer = new WasInvokedCorrectlyAnswer();
		Mockito.doAnswer(wereTheRightPendingPontajeForConcediiSavedAnswer).when(pendingPontajForConcediuDao).save(Mockito.eq(pendingPontajeForConcediiToSave));
		
		pontajForConcediiServiceImpl.createPontajeForConcediiForYesterday();

		assertThat(wereTheRightLeaveDetailsSentForCreatingTimesheetsAnswer.wasInvokedCorrectly()).isTrue();
		assertThat(wereTheRightPendingPontajeForConcediiDeletedAnswer.wasInvokedCorrectly()).isTrue();
		assertThat(wereTheRightPendingPontajeForConcediiSavedAnswer.wasInvokedCorrectly()).isTrue();
	}
}