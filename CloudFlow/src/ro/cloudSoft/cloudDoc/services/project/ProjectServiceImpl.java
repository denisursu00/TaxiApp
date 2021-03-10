package ro.cloudSoft.cloudDoc.services.project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.AppExceptionCodes;
import ro.cloudSoft.cloudDoc.core.constants.CalendarConstants;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ProjectConstants;
import ro.cloudSoft.cloudDoc.core.constants.TaskConstants;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentDspConstants;
import ro.cloudSoft.cloudDoc.dao.calendar.CalendarDao;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.dao.project.TaskDao;
import ro.cloudSoft.cloudDoc.domain.calendar.Calendar;
import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.project.DspActivitateAttachmentViewModel;
import ro.cloudSoft.cloudDoc.domain.project.DspActivitateViewModel;
import ro.cloudSoft.cloudDoc.domain.project.DspComisieGlViewModel;
import ro.cloudSoft.cloudDoc.domain.project.DspExportActivitateModel;
import ro.cloudSoft.cloudDoc.domain.project.DspExportComisieGLModel;
import ro.cloudSoft.cloudDoc.domain.project.DspExportGrupActivitateModel;
import ro.cloudSoft.cloudDoc.domain.project.DspExportModel;
import ro.cloudSoft.cloudDoc.domain.project.DspParticipantViewModel;
import ro.cloudSoft.cloudDoc.domain.project.DspViewModel;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.ProjectStatus;
import ro.cloudSoft.cloudDoc.domain.project.ProjectType;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskAttachment;
import ro.cloudSoft.cloudDoc.domain.project.TaskSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.domain.project.TasksActivitateAttachmentViewModel;
import ro.cloudSoft.cloudDoc.domain.project.TasksActivitateViewModel;
import ro.cloudSoft.cloudDoc.domain.project.TasksComisieGlViewModel;
import ro.cloudSoft.cloudDoc.domain.project.TasksParticipantViewModel;
import ro.cloudSoft.cloudDoc.domain.project.TasksViewModel;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ExportType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PageRequest;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.MeetingCalendarEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CompleteTaskRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.GetPagedProjectTaskViewModelsRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.GradDeRealizarePentruProiecteleCuDspModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectWithDspViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.SubactivityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskAttachmentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskViewModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.UserConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.project.ProjectConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.project.TaskConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.project.TaskFilterConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.project.TaskSortedAttributeConverter;
import ro.cloudSoft.cloudDoc.services.GeneralReport;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.parameters.ParametersService;
import ro.cloudSoft.cloudDoc.services.xDocReports.XDocReportDataSourceBuilder;
import ro.cloudSoft.cloudDoc.services.xDocReports.XDocReportsService;
import ro.cloudSoft.cloudDoc.utils.FileSystemAttachmentManager;
import ro.cloudSoft.cloudDoc.utils.ProjectUtils;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.PagingList;

public class ProjectServiceImpl implements ProjectService, InitializingBean {
	private static final String PARTICIPARE_LA_EVENT_PREFIX = "Participare la";
	
	private ProjectDao projectDao;
	private TaskDao taskDao;
	private DocumentService documentService;
	private DocumentTypeDao documentTypeDao;
	private NomenclatorValueDao nomenclatorValueDao;
	private ParametersService parametersService;
	private UserService userService;
	private NomenclatorService nomenclatorService;
	private XDocReportsService xDocReportsService;
	
	private DocumentDspConstants documentDspConstants;
	
	private ProjectConverter projectConverter;
	private TaskConverter taskConverter;
	private TaskFilterConverter taskFilterConverter;
	private TaskSortedAttributeConverter taskSortedAttributeConverter;
	
	private CalendarDao calendarDao;
	private CalendarService calendarService;

	private FileSystemAttachmentManager fileSystemAttachmentManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			projectDao,
			taskDao,
			documentService,
			documentTypeDao,
			nomenclatorValueDao,
			parametersService,
			userService,
			nomenclatorService,
			
			documentDspConstants,
			
			projectConverter,
			taskConverter,
			taskFilterConverter,
			taskSortedAttributeConverter,

			fileSystemAttachmentManager,
			xDocReportsService
		);
	}

	public List<ProjectModel> getAllProjects() {
		List<Project> projects = projectDao.getAll();
		return projectConverter.toModels(projects);
	}
	
	@Override
	public List<TaskModel> getProjectTasks(Long projectId) {
		List<Task> tasks = projectDao.getProjectTasks(projectId);
		return taskConverter.toModels(tasks);
	}
	
	@Override
	public List<TaskModel> getUserTasks(Long userId) {
		// TODO: Implementare
		throw new RuntimeException("method projectService.getUserTasks  not implemented!!!");
		//return null;
	}
	
	@Override
	public TaskModel getTask(Long taskId) {
		Task task = taskDao.findById(taskId);
		return taskConverter.toModel(task);
	}
	

	@Override
	@Transactional(noRollbackFor = Throwable.class)
	public Long saveTask(TaskModel taskModel, SecurityManager userSecurity) throws AppException {
		
		Boolean isNewTask = taskModel.getId() == null ? true : false;
		if (isNewTask) {
			taskModel.setInititatorId(userSecurity.getUserId());
		}
		Task task;
		if (isNewTask) {
			task = taskConverter.toEntity(taskModel);
		}else {
			task = this.taskDao.findById(taskModel.getId());
			task.setName(taskModel.getName());
			task.setSubactivity(this.projectDao.getProjectSubactivities(taskModel.getProjectId()).stream()
					.filter(elem -> elem.getId().equals(taskModel.getSubactivityId()))
					.findAny().orElse(null));
			task.setDescription(taskModel.getDescription());
			task.setPriority(taskModel.getPriority());
			task.setExplications(taskModel.getExplications());
			task.setComments(taskModel.getComments());
		}
		
		if (task.getProject().getStatus().equals(ProjectStatus.CLOSED)) {
			throw new AppException(AppExceptionCodes.TASK_CANNOT_BE_ADDED_BECAUSE_PROJECT_IS_CLOSED);
		}
		
		List<Attachment> attachments = fileSystemAttachmentManager.getAll(userSecurity.getUserUsername());
		List<TaskAttachment> taskAttachments = task.getAttachments();
		if (!isNewTask) {
			List<TaskAttachmentModel> modelAttachments = taskModel.getTaskAttachments();
			Iterator<TaskAttachment> taskIter = taskAttachments.iterator();
			while(taskIter.hasNext()) {
				boolean found = false;
				TaskAttachment attachment = taskIter.next();
				for (TaskAttachmentModel attachModel : modelAttachments) {
					if (attachment.getId().equals(attachModel.getId())) {
						found = true;
						break;
					}
				}
				if (!found) taskIter.remove();
			}
			modelAttachments.forEach(taskAttachment -> {
				if (taskAttachment.getId() == null) {
					TaskAttachment newAttachment = new TaskAttachment();
					newAttachment.setName(taskAttachment.getName());
					for (Attachment attachment : attachments) {
						if (newAttachment.getName().equals(attachment.getName())) {
							newAttachment.setFileContent(attachment.getData());
							newAttachment.setTask(task);
						}
					}
					taskAttachments.add(newAttachment);
				}
			});
			task.setAttachments(taskAttachments);
		}
		
		for (Attachment attachment : attachments) {
			for (TaskAttachment taskAttachment : task.getAttachments()) {
				if (attachment.getName().equals(taskAttachment.getName())) {
					taskAttachment.setFileContent(attachment.getData());
				}
			}
		}
		
		Long savedTaskId = taskDao.save(task);
		if ((savedTaskId != null) && (savedTaskId > 0) && isNewTask) {
			
			String participationsTo = task.getParticipationsTo();
			
			// Se marcheaza in calendar doar daca...
			if (StringUtils.isNotEmpty(participationsTo)
					&& !participationsTo.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_COMUNICAT_DE_PRESA)
					&& !participationsTo.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_SEDINTA_COMISIE_GRUPURI_DE_LUCRU)
					&& !participationsTo.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_INTALNIRE_CONSILIUL_DIRECTOR)
					&& !participationsTo.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_SEDINTA_COMISIE_SISTEMICA)
					&& !participationsTo.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_ALTELE)) {
				
				String calendarName;
				if (task.getParticipationsTo().equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_SEDINTE_PARLAMENT)) {
					calendarName = CalendarConstants.CALENDAR_NAME_VALUE_SEDINTE_PARLAMENT;
				} else {
					calendarName = CalendarConstants.CALENDAR_NAME_VALUE_EVENIMENTE_INTALNIRI;
				}
			
				Calendar calendar = calendarDao.findByName(calendarName);
				MeetingCalendarEventModel meetingCalendarEventModel = new MeetingCalendarEventModel();
				meetingCalendarEventModel.setStartDate(task.getEvenimentStartDate());
				meetingCalendarEventModel.setEndDate(task.getEvenimentEndDate());
				meetingCalendarEventModel.setCalendarId(calendar.getId());
				meetingCalendarEventModel.setLocation(task.getParticipationsTo());
				String description = task.getDescription();
				if (description != null) {
					meetingCalendarEventModel.setDescription(description);
				}
				String explications = task.getExplications();
				String subject = PARTICIPARE_LA_EVENT_PREFIX + " " + task.getParticipationsTo();
				if (!participationsTo.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_INTALNIRE_BNR) && !participationsTo.equalsIgnoreCase(TaskConstants.PARTICIPARI_LA_VALUE_INTALNIRE_CU_CONDUCEREA_BNR)) {
					if (StringUtils.isNotEmpty(explications)) {
						subject += " - " + explications;
					}
				}
				meetingCalendarEventModel.setSubject(subject);
				
				List<OrganizationEntity> assignmentUsers = task.getAssignments();
				List<OrganizationEntityModel> attendees = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(assignmentUsers)) {
					assignmentUsers.forEach(user -> {
						OrganizationEntityModel oe = new OrganizationEntityModel();
						oe.setId(user.getId());
						attendees.add(oe);
					});
				}
				
				meetingCalendarEventModel.setAttendees(attendees);
				calendarService.saveCalendarEvent(meetingCalendarEventModel, userSecurity, false);
			}
		}
		return savedTaskId;
	}
	
	@Override
	@Transactional(noRollbackFor = Throwable.class)
	public void finalizeTask(CompleteTaskRequestModel requestModel, SecurityManager userSecurity) throws AppException {
		
		Task task = taskDao.findById(requestModel.getTaskId());
		if (task.getStatus().equals(TaskStatus.FINALIZED)) {
			throw new AppException(AppExceptionCodes.TAKS_IS_ALREADY_FINALIZED);
		}
		task.setFinalizedDate(new Date());
		task.setStatus(TaskStatus.FINALIZED);
		task.setComments(requestModel.getComments());
		
		List<Attachment> attachments = fileSystemAttachmentManager.getAll(userSecurity.getUserUsername());
		List<TaskAttachment> taskAttachments = task.getAttachments();
		List<TaskAttachmentModel> modelAttachments = requestModel.getAttachments();
				modelAttachments.forEach(taskAttachment -> {
			if (taskAttachment.getId() == null) {
				TaskAttachment newAttachment = new TaskAttachment();
				newAttachment.setName(taskAttachment.getName());
				for (Attachment attachment : attachments) {
					if (newAttachment.getName().equals(attachment.getName())) {
						newAttachment.setFileContent(attachment.getData());
						newAttachment.setTask(task);
					}
				}
				taskAttachments.add(newAttachment);
			}
		});
		task.setAttachments(taskAttachments);
		
		taskDao.save(task);
	}
	
	@Override
	@Transactional(noRollbackFor = Throwable.class)
	public void cancelTask(Long taskId) throws AppException {
		Task task = taskDao.findById(taskId);
		if (task.getStatus().equals(TaskStatus.CANCELLED)) throw new AppException(AppExceptionCodes.TASK_IS_ALREADY_CANCELLED);
		task.setStatus(TaskStatus.CANCELLED);
		taskDao.save(task);
	}

	@Override
	public List<TaskViewModel> getUserInProgressTasksModels(Long userId) {
		List<Task> tasks = taskDao.getUserInProgressTasksModels(userId);
		return taskConverter.toViewModels(tasks);
	}
	
	@Override
	public TaskViewModel getTaskViewModel(Long taskId) {
		Task task = taskDao.findById(taskId);
		return taskConverter.toViewModel(task);
	}

	@Override
	public TaskAttachment getTaskAttachment(Long attachmentId) {
		return taskDao.getTaskAttachment(attachmentId);
	}
	
	@Override
	public List<ProjectModel> getUserProjects(Long userId) {
		List<Project> projects = projectDao.getUserProjectsByStatus(userId, ProjectStatus.INITIATED);
		return projectConverter.toModels(projects);
	}
	
	@Override
	public Long saveProject(ProjectModel projectModel, SecurityManager userSecurity) {
		projectModel.setInitiatorId(userSecurity.getUserId());
		return projectDao.save(projectConverter.toEntity(projectModel));
	}

	@Override
	public List<UserModel> getProjectParticipants(Long projectId) {
		List<UserModel> participants = new ArrayList<UserModel>();
		Set<User> participantsSet = projectDao.getProjectParticipants(projectId);
		for (User user : participantsSet) {
			participants.add(UserConverter.getModelFromUser(user));
		}
		return participants;
	}

	@Override
	public ProjectModel getProjectById(Long projectId) {
		Project project = projectDao.getById(projectId);
		return projectConverter.toModel(project);
	}
	
	@Override
	public void closeProject(Long projectId, SecurityManager userSecurity) throws AppException {
		Project project = projectDao.getById(projectId);
		
		if (!project.getResponsibleUser().getId().equals(userSecurity.getUserId())) {
			throw new AppException(AppExceptionCodes.PROJECT_CAN_BE_CLOSED_ONLY_BY_RESPONSIBLE_USER);
		}
		
		boolean existsTasksInProgress = false;
		List<Task> tasks = projectDao.getProjectTasks(project.getId()); 
		for (Task task : tasks) {
			if (task.getStatus().equals(TaskStatus.IN_PROGRESS)) {
				existsTasksInProgress = true;
			}
		}
		
		if (existsTasksInProgress) {
			throw new AppException(AppExceptionCodes.PROJECT_CANNOT_BE_CLOSED_BECAUSE_IN_PROGRESS_TASKS_EXISTS);
		}
		
		project.setStatus(ProjectStatus.CLOSED);
		projectDao.save(project);
	}
	
	@Override
	public PagingList<TaskViewModel> getPagedProjectTaskViewModels(PageRequest<GetPagedProjectTaskViewModelsRequestModel> pageRequest, SecurityManager userSecurity) {
		
		TaskSearchCriteria searchCriteria = new TaskSearchCriteria();
		searchCriteria.setProjectId(pageRequest.getPayload().getProjectId());
		searchCriteria.setUserId(userSecurity.getUserId());
		
		if (CollectionUtils.isNotEmpty(pageRequest.getPayload().getFilters())) {
			searchCriteria.setFilters(taskFilterConverter.toTaskFilters(pageRequest.getPayload().getFilters()));
		}
		if (pageRequest.getPayload().getSortedAttribute() != null) {
			searchCriteria.setSortedAttribute(taskSortedAttributeConverter.toTaskSortedAttribute(pageRequest.getPayload().getSortedAttribute()));
		}
		PagingList<Task> tasksPagingList = projectDao.getPagedProjectTasks(pageRequest.getOffset(), pageRequest.getLimit(), searchCriteria);
		
		List<TaskViewModel> taskViewModels = new ArrayList<>();
		for (Task task : tasksPagingList.getElements()) {
			taskViewModels.add(taskConverter.toViewModel(task));
		}
		PagingList<TaskViewModel> taskViewModelsPagingList = new PagingList<>(tasksPagingList.getTotalCount(), tasksPagingList.getOffset(), taskViewModels);
		
		return taskViewModelsPagingList;
	}
	
	@Override
	public Set<ProjectWithDspViewModel> getProjectWithDspViewModels(SecurityManager userSecurity, boolean allProjects) throws AppException {
		List<Project> userProjects = projectDao.getUserProjectsWithDspByStatus(userSecurity.getUserId(), ProjectStatus.INITIATED);
		ProjectWithDspViewModelsBuilder projectWithDspViewModelsBuilder = new ProjectWithDspViewModelsBuilder( projectDao, parametersService, userProjects, allProjects);
		return projectWithDspViewModelsBuilder.build();
	}
	
	@Override
	public List<TaskModel> getFinalizedTasksOfProject(Long projectId) {
		List<Task> tasks = projectDao.getProjectTasksByStatus(projectId, TaskStatus.FINALIZED);
		return taskConverter.toModels(tasks);
	}
	
	@Override
	public List<TaskModel> getInProgressTasksOfProject(Long projectId) {
		List<Task> tasks = projectDao.getProjectTasksByStatus(projectId, TaskStatus.IN_PROGRESS);
		return taskConverter.toModels(tasks);
	}
	
	@Override
	public List<ProjectModel> getAllOpenedProjectsWithDsp() {
		List<Project> projects = projectDao.getAllOpenedWithDsp();
		return projectConverter.toModels(projects);
	}
	
	@Override
	public String getProjectNameById(Long projectId) {
		return projectDao.getProjectNameById(projectId);
	}
	
	
	@Override
	public DspViewModel getDspViewModelByProjectId(Long projectId, SecurityManager userSecurity) throws AppException {
		
		Project project = this.projectDao.getById(projectId);
		if (!project.getType().equals(ProjectType.DSP)) {
			throw new IllegalStateException("Proiectul cu id [" + projectId + "] nu este de tip DSP.");
		}
		
		DspViewModel dsp = new DspViewModel();
		dsp.setNumeProiect(project.getName());
		dsp.setAbreviereProiect(project.getProjectAbbreviation());
		dsp.setDescriere(project.getDescription());
		dsp.setInitiatorProiect(project.getInitiator().getDisplayNameWithTitle());
		
		dsp.setDomeniuBancar(NomenclatorValueUtils.getAttributeValueAsString(project.getDomeniuBancar(), NomenclatorConstants.DOMENIU_BANCAR_ATTRIBUTE_KEY_DENUMIRE));
		
		if (project.getProiectInitiatArb() != null) {
			if (project.getProiectInitiatArb()) {
				dsp.setProiectInitiatDeArb(ProjectConstants.INITIAT_DE_ARB_TRUE_LABEL_VALUE);
			} else {
				dsp.setProiectInitiatDeArb(ProjectConstants.INITIAT_DE_ARB_FALSE_LABEL_VALUE);
			}
		}
		
		dsp.setIncadrareProiect(NomenclatorValueUtils.getAttributeValueAsString(project.getIncadrareProiect(), NomenclatorConstants.INCADRARI_PROIECTE_ATTRIBUTE_KEY_INCADRARE_PROIECT));
		
		dsp.setArieDeCuprindere(project.getArieDeCuprindere().toString()); //TODO: de luat eticheta
		
		dsp.setProiectInitiatDeAltaEntitate(project.getProiectInitiatDeAltaEntitate());
		
		dsp.setDataInceputProiect(project.getStartDate());
		dsp.setDataSfarsitProiect(project.getEndDate());
		dsp.setDataImplementarii(project.getImplementationDate());
		
		dsp.setEvaluareaImpactului(project.getEvaluareaImpactului());
		
		dsp.setGradDeImportanta(NomenclatorValueUtils.getAttributeValueAsString(project.getGradImportanta(), NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_GRAD_IMPORTANTA));
		
		dsp.setGradDeRealizareEstimatDeResponsabil(ProjectUtils.getLastEstimation(project));
		dsp.setResponsabilProiect(project.getResponsibleUser().getDisplayName());
		dsp.setAutoritatiImplicate(project.getAutoritatiImplicate());
		dsp.setObiectiveProiect(project.getObiectiveProiect());
		dsp.setCadrulLegal(project.getCadruLegal());
		dsp.setSpecificitateProiect(project.getSpecificitateProiect());
		
		// Comisii/GL.
		List<DspComisieGlViewModel> comisii = new ArrayList<>();
		List<NomenclatorValue> comisiiSauGLAsNomenclatorValues = project.getComisiiSauGl();
		if (CollectionUtils.isNotEmpty(comisiiSauGLAsNomenclatorValues)) {
			for (NomenclatorValue nomenclatorValue : comisiiSauGLAsNomenclatorValues) {
				DspComisieGlViewModel comisieView = new DspComisieGlViewModel();
				comisieView.setDenumire(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE));
				comisieView.setAbreviere(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_ABREVIERE));
				comisii.add(comisieView);
			}
		}
		
		comisii.sort(Comparator.comparing(DspComisieGlViewModel::getDenumire, String.CASE_INSENSITIVE_ORDER));
		
		dsp.setComisiiGlImplicate(comisii);
		
		// Participanti.
		List<DspParticipantViewModel> participanti = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(project.getParticipants())) {
			for (OrganizationEntity participant : project.getParticipants()) {
				DspParticipantViewModel participantViewModel = new DspParticipantViewModel();
				participantViewModel.setNumeParticipant(participant.getDisplayName());
				participanti.add(participantViewModel);
			}
		}

		participanti.sort(Comparator.comparing(DspParticipantViewModel::getNumeParticipant, String.CASE_INSENSITIVE_ORDER));
		
		dsp.setParticipanti(participanti);
		
		// Activitati.
		List<DspActivitateViewModel> activitati = new ArrayList<>();
		List<Task> tasks = projectDao.getProjectTasks(projectId).stream().filter(task -> task.getStatus() != TaskStatus.CANCELLED).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(tasks)) {
			for (Task task : tasks) {
				DspActivitateViewModel activitateViewModel = new DspActivitateViewModel();
				activitateViewModel.setId(task.getId());
				activitateViewModel.setDenumire(task.getName());
				activitateViewModel.setDescriere(task.getDescription());
				activitateViewModel.setDataInceput(task.getStartDate());
				activitateViewModel.setDataSfarsit(task.getEndDate());
				StringBuilder responsabil = new StringBuilder();
				List<OrganizationEntity> assigns = task.getAssignments();
				if (CollectionUtils.isNotEmpty(assigns)) {
					for (OrganizationEntity asignat : task.getAssignments()) {
						if (responsabil.length() > 0) {
							responsabil.append(", ");
						}
						responsabil.append(asignat.getDisplayName());
					}	
				}
				activitateViewModel.setResponsabil(responsabil.toString());
				activitateViewModel.setExplicatii(task.getExplications());
				activitateViewModel.setComentarii(task.getComments());
				activitateViewModel.setParticipareLa(task.getParticipationsTo());
				activitateViewModel.setPrioritate(task.getPriority()); 
				activitateViewModel.setStatus(task.getStatus());
				
				List<DspActivitateAttachmentViewModel> attachmentViews = new ArrayList<>();
				List<TaskAttachment> taskAttachments = task.getAttachments();
				if (CollectionUtils.isNotEmpty(taskAttachments)) {
					for (TaskAttachment attachment : taskAttachments) {
						DspActivitateAttachmentViewModel attachmentViewModel = new DspActivitateAttachmentViewModel();
						attachmentViewModel.setId(attachment.getId());
						attachmentViewModel.setName(attachment.getName());
						attachmentViews.add(attachmentViewModel);
					}
				}
				activitateViewModel.setAttachments(attachmentViews);
				
				activitati.add(activitateViewModel);
			}
		}		
		dsp.setActivitati(activitati);
		
		return dsp;
	}
	
	@Override
	public TasksViewModel getTasksViewModelByProjectId(Long projectId, SecurityManager userSecurity) throws AppException {
		
		Project project = this.projectDao.getById(projectId);
		if (!project.getType().equals(ProjectType.SIMPLE)) {
			throw new IllegalStateException("Proiectul cu id [" + projectId + "] nu este de tip SIMPLE.");
		}
		
		TasksViewModel tasksView = new TasksViewModel();
		tasksView.setNumeProiect(project.getName());
		tasksView.setAbreviereProiect(project.getProjectAbbreviation());
		tasksView.setDescriere(project.getDescription());
		tasksView.setUtilizatorResponsabil(project.getResponsibleUser().getDisplayName());
		
		tasksView.setDataInceputProiect(project.getStartDate());
		tasksView.setDataSfarsitProiect(project.getEndDate());
		tasksView.setDataImplementarii(project.getImplementationDate());
		tasksView.setGradDeRealizareEstimatDeResponsabil(ProjectUtils.getLastEstimation(project));
		
		// Comisii/GL.
		List<TasksComisieGlViewModel> comisii = new ArrayList<>();
		List<NomenclatorValue> comisiiSauGLAsNomenclatorValues = project.getComisiiSauGl();
		if (CollectionUtils.isNotEmpty(comisiiSauGLAsNomenclatorValues)) {
			for (NomenclatorValue nomenclatorValue : comisiiSauGLAsNomenclatorValues) {
				TasksComisieGlViewModel comisieView = new TasksComisieGlViewModel();
				comisieView.setDenumire(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE));
				comisieView.setAbreviere(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_ABREVIERE));
				comisii.add(comisieView);
			}
		}
		tasksView.setComisiiGlImplicate(comisii);
		
		// Participanti.
		List<TasksParticipantViewModel> participanti = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(project.getParticipants())) {
			for (OrganizationEntity participant : project.getParticipants()) {
				TasksParticipantViewModel participantViewModel = new TasksParticipantViewModel();
				participantViewModel.setNumeParticipant(participant.getDisplayName());
				participanti.add(participantViewModel);
			}
		}
		tasksView.setParticipanti(participanti);
		
		// Activitati.
		List<TasksActivitateViewModel> activitati = new ArrayList<>();
		List<Task> tasks = projectDao.getProjectTasks(projectId);
		if (CollectionUtils.isNotEmpty(tasks)) {
			for (Task task : tasks) {
				TasksActivitateViewModel activitateViewModel = new TasksActivitateViewModel();
				activitateViewModel.setId(task.getId());
				activitateViewModel.setDenumire(task.getName());
				activitateViewModel.setDescriere(task.getDescription());
				activitateViewModel.setDataInceput(task.getStartDate());
				activitateViewModel.setDataSfarsit(task.getEndDate());
				StringBuilder responsabil = new StringBuilder();
				List<OrganizationEntity> assigns = task.getAssignments();
				if (CollectionUtils.isNotEmpty(assigns)) {
					for (OrganizationEntity asignat : task.getAssignments()) {
						if (responsabil.length() > 0) {
							responsabil.append(", ");
						}
						responsabil.append(asignat.getDisplayName());
					}	
				}
				activitateViewModel.setResponsabil(responsabil.toString());
				activitateViewModel.setExplicatii(task.getExplications());
				activitateViewModel.setComentarii(task.getComments());
				activitateViewModel.setParticipareLa(task.getParticipationsTo());
				activitateViewModel.setPrioritate(task.getPriority()); 
				activitateViewModel.setStatus(task.getStatus());
				
				List<TasksActivitateAttachmentViewModel> attachmentViews = new ArrayList<>();
				List<TaskAttachment> taskAttachments = task.getAttachments();
				if (CollectionUtils.isNotEmpty(taskAttachments)) {
					for (TaskAttachment attachment : taskAttachments) {
						TasksActivitateAttachmentViewModel attachmentViewModel = new TasksActivitateAttachmentViewModel();
						attachmentViewModel.setId(attachment.getId());
						attachmentViewModel.setName(attachment.getName());
						attachmentViews.add(attachmentViewModel);
					}
				}
				activitateViewModel.setAttachments(attachmentViews);
				
				activitati.add(activitateViewModel);
			}
		}
		tasksView.setActivitati(activitati);
		
		return tasksView;
	}
	
	
	@Override
	public GradDeRealizarePentruProiecteleCuDspModel getGradDeRealizarePentruProiecteleCuDspModel(SecurityManager userSecurity) throws AppException {
		List<Project> projects = projectDao.getUserProjectsWithDspByStatus(userSecurity.getUserId(), ProjectStatus.INITIATED);
		GradDeRealizarePentruProiecteleCuDspModelBuilder gradDeRealizarePentruProiecteleCuDspModelBuilder = new GradDeRealizarePentruProiecteleCuDspModelBuilder(projectDao, parametersService, projects);
		return gradDeRealizarePentruProiecteleCuDspModelBuilder.build();
	}
	
	@Override
	public DownloadableFile downloadTaskAttachment(Long id) {
		TaskAttachment taskAttachment = this.taskDao.getTaskAttachment(id);
		return new DownloadableFile(taskAttachment.getName(), taskAttachment.getFileContent());
	}
	
	@Override
	public ProjectViewModel getProjectViewModelById(Long id) {
		Project project = projectDao.getById(id);
		return projectConverter.toViewModel(project);
	}
	
	@Override
	public DownloadableFile exportDsp(Long projectId, ExportType exportType) throws AppException {
		
		Project project = this.projectDao.getById(projectId);
		if (!project.getType().equals(ProjectType.DSP)) {
			throw new IllegalStateException("Proiectul cu id [" + projectId + "] nu este de tip DSP.");
		}
		
		DspExportModel dspExportModel = new DspExportModel();
		dspExportModel.setNumeProiect(project.getName());
		dspExportModel.setAbreviereProiect(project.getProjectAbbreviation());
		dspExportModel.setDescriere(project.getDescription());
		dspExportModel.setDataInceputProiect(project.getStartDate());
		dspExportModel.setDataSfarsitProiect(project.getEndDate());
		dspExportModel.setDataImplementarii(project.getImplementationDate());
		dspExportModel.setInitiatorProiect(project.getInitiator().getDisplayNameWithTitle());
		dspExportModel.setDomeniuBancar(NomenclatorValueUtils.getAttributeValueAsString(project.getDomeniuBancar(), NomenclatorConstants.DOMENIU_BANCAR_ATTRIBUTE_KEY_DENUMIRE));
		dspExportModel.setIncadrareProiect(NomenclatorValueUtils.getAttributeValueAsString(project.getIncadrareProiect(), NomenclatorConstants.INCADRARI_PROIECTE_ATTRIBUTE_KEY_INCADRARE_PROIECT));
		String arieDeCuprindere = null;
		if (project.getArieDeCuprindere() != null) {
			arieDeCuprindere = project.getArieDeCuprindere().getValue();
		}
		dspExportModel.setArieDeCuprindere(arieDeCuprindere);
		dspExportModel.setEvaluareaImpactului(project.getEvaluareaImpactului());
		dspExportModel.setGradImportanta(NomenclatorValueUtils.getAttributeValueAsString(project.getGradImportanta(), NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_GRAD_IMPORTANTA));
		dspExportModel.setGradRealizareEstimat(ProjectUtils.getLastEstimation(project));
		User userResponsabil =  project.getResponsibleUser();
		if (userResponsabil != null) {
			dspExportModel.setResponsabil(userResponsabil.getDisplayName());
		}
		dspExportModel.setAutoritatiImplicate(project.getAutoritatiImplicate());
		dspExportModel.setObiectiveProiect(project.getObiectiveProiect());
		dspExportModel.setCadrulLegal(project.getCadruLegal());
		dspExportModel.setSpecificitateProiect(project.getSpecificitateProiect());
		
		List<DspExportComisieGLModel> comisii = new LinkedList<>();
		List<NomenclatorValue> comisiiSauGLAsNomenclatorValues = project.getComisiiSauGl();
		if (CollectionUtils.isNotEmpty(comisiiSauGLAsNomenclatorValues)) {
			for (NomenclatorValue nomenclatorValue : comisiiSauGLAsNomenclatorValues) {
				DspExportComisieGLModel comisieGL = new DspExportComisieGLModel();
				comisieGL.setDenumire(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE));
				comisieGL.setAbreviere(NomenclatorValueUtils.getAttributeValueAsString(nomenclatorValue, NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_ABREVIERE));
				comisii.add(comisieGL);
			}
		}
		if(comisii.isEmpty()) {
			comisii.add(0, new DspExportComisieGLModel());
		}
		dspExportModel.setComisiiGLImplicate(comisii);
		List<DspExportGrupActivitateModel> grupuriActiuniIntreprinse = new ArrayList<>();
		List<DspExportGrupActivitateModel> grupuriActiuniViitoare = new ArrayList<>();
		List<DspExportActivitateModel> actiuniIntreprinse = new ArrayList<>();
		List<DspExportActivitateModel> actiuniViitoare = new ArrayList<>();
		List<Task> tasks = projectDao.getProjectTasks(projectId).stream().filter(task -> task.getStatus() != TaskStatus.CANCELLED).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(tasks)) {
			for (Task task : tasks) {
				DspExportActivitateModel activitate = new DspExportActivitateModel();
				activitate.setNume(task.getName());
				activitate.setDescriere(task.getDescription());
				activitate.setDataSfarsit(task.getEndDate());
				if (task.getSubactivity() != null) {
					activitate.setSubactivity(task.getSubactivity().getName());
				}else {
					activitate.setSubactivity(new String(""));
				}
				StringBuilder responsabil = new StringBuilder();
				List<OrganizationEntity> assigns = task.getAssignments();
				if (CollectionUtils.isNotEmpty(assigns)) {
					for (OrganizationEntity asignat : task.getAssignments()) {						
						if (asignat instanceof User) {
							if (responsabil.length() > 0) {
								responsabil.append(", ");
							}
							responsabil.append(asignat.getDisplayName());
						} else {
							throw new RuntimeException("asignat must be instance of User");
						}						
					}	
				}
				activitate.setUserAsignat(responsabil.toString());
				if (task.getStatus().equals(TaskStatus.FINALIZED)) {
					actiuniIntreprinse.add(activitate);
				} else if (task.getStatus().equals(TaskStatus.IN_PROGRESS)) {
					actiuniViitoare.add(activitate);
				}
			}
		}
		Map<String, DspExportGrupActivitateModel> completedSubactivities = new HashMap<>();
		actiuniIntreprinse.forEach(actiune -> {
			String subactivity = actiune.getSubactivity();
			if (!completedSubactivities.containsKey(subactivity)) {
				DspExportGrupActivitateModel newGroup = new DspExportGrupActivitateModel();
				newGroup.setActivities(new ArrayList<>());
				newGroup.setSubactivityName(subactivity);
				newGroup.getActivities().add(actiune);
				grupuriActiuniIntreprinse.add(newGroup);
				completedSubactivities.put(subactivity, newGroup);
			}else {
				completedSubactivities.get(subactivity).getActivities().add(actiune);
			}
		});
		Map<String, DspExportGrupActivitateModel> futureSubactivities = new HashMap<>();
		actiuniViitoare.forEach(actiune -> {
			String subactivity = actiune.getSubactivity();
			if (!futureSubactivities.containsKey(subactivity)) {
				DspExportGrupActivitateModel newGroup = new DspExportGrupActivitateModel();
				newGroup.setActivities(new ArrayList<>());
				newGroup.setSubactivityName(subactivity);
				newGroup.getActivities().add(actiune);
				grupuriActiuniViitoare.add(newGroup);
				futureSubactivities.put(subactivity, newGroup);
			}else {
				futureSubactivities.get(subactivity).getActivities().add(actiune);
			}
		});
		grupuriActiuniIntreprinse.forEach(grup -> grup.getActivities().sort(new Comparator<DspExportActivitateModel>() {
			@Override
			public int compare(DspExportActivitateModel o1, DspExportActivitateModel o2) {
				return o1.getDataSfarsit().compareTo(o2.getDataSfarsit());
			}
		}));
		grupuriActiuniViitoare.forEach(grup -> grup.getActivities().sort(new Comparator<DspExportActivitateModel>() {
			@Override
			public int compare(DspExportActivitateModel o1, DspExportActivitateModel o2) {
				return o1.getDataSfarsit().compareTo(o2.getDataSfarsit());
			}
		}));
		dspExportModel.setActiuniIntreprinse(grupuriActiuniIntreprinse);
		dspExportModel.setActiuniViitoare(grupuriActiuniViitoare);
		
		String downloadableFileName = project.getProjectAbbreviation() + exportType.getFileExtension();
		Map dspExportModelAsMap = new XDocReportDataSourceBuilder(dspExportModel).build();//.convertValue(dspExportModel, Map.class);
		
		return xDocReportsService.generate(GeneralReport.DSP, exportType,  dspExportModelAsMap, downloadableFileName);
	}
	
	@Override
	public List<TaskEventModel> getAllTaskEvents(){
		List<NomenclatorValueModel> taskEvents = nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.EVENIMENT_TASK_NOMENCLATOR_CODE);
		return taskEvents.stream().map(taskEvent -> new TaskEventModel(taskEvent.getId(), taskEvent.getAttribute1(), taskEvent.getAttribute2())).collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void updateTaskEventDescription(TaskEventModel taskEvent) {
		NomenclatorValueModel currentTaskEvent = this.nomenclatorService.getNomenclatorValue(taskEvent.getId());
		currentTaskEvent.setAttribute2(taskEvent.getDescription());
		this.nomenclatorService.saveNomenclatorValue(currentTaskEvent);
	}
	
	@Override
	public List<SubactivityModel> getAllProjectSubactivities(Long projectId) {
		return this.projectDao.getProjectSubactivities(projectId)
				.stream().map(subactivity -> new SubactivityModel(subactivity.getId(), subactivity.getName()))
				.sorted(Comparator.comparing(SubactivityModel::getName))
				.collect(Collectors.toList());
	}

	@Override
	public boolean isSubactivityUsed(Long subactivityId) {
		if (subactivityId == null) {
			return false;
		}
		List<Task> tasks = this.taskDao.getAllTasksBySubactivityId(subactivityId);
		if (CollectionUtils.isNotEmpty(tasks)) {
			return true;
		}
		return false;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}
	
	public void setDocumentService(DocumentService documentService) {
		this.documentService = documentService;
	}
	
	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}
	
	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
	
	public void setParametersService(ParametersService parametersService) {
		this.parametersService = parametersService;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setNomenclatorService(NomenclatorService nomenclatorService) {
		this.nomenclatorService = nomenclatorService;
	}
	
	public void setDocumentDspConstants(DocumentDspConstants dspConstants) {
		this.documentDspConstants = dspConstants;
	}
	
	public void setProjectConverter(ProjectConverter projectConverter) {
		this.projectConverter = projectConverter;
	}
	
	public void setTaskConverter(TaskConverter taskConverter) {
		this.taskConverter = taskConverter;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}
	
	public void setTaskFilterConverter(TaskFilterConverter taskFilterConverter) {
		this.taskFilterConverter = taskFilterConverter;
	}

	public void setTaskSortedAttributeConverter(TaskSortedAttributeConverter taskSortedAttributeConverter) {
		this.taskSortedAttributeConverter = taskSortedAttributeConverter;
	}

	public void setFileSystemAttachmentManager(FileSystemAttachmentManager fileSystemAttachmentManager) {
		this.fileSystemAttachmentManager = fileSystemAttachmentManager;
	}

	public void setxDocReportsService(XDocReportsService xDocReportsService) {
		this.xDocReportsService = xDocReportsService;
	}

	public void setCalendarDao(CalendarDao calendarDao) {
		this.calendarDao = calendarDao;
	}

	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	@Override
	public List<String> getAllInProgressTaskNamesByProjectAbbreviation(String projectAbbreviation) {
		return taskDao.getAllInProgressTaskNamesByProjectAbbreviation(projectAbbreviation);
	}

	@Override
	public boolean existsAbbreviation(String projectAbbreviation) {
		return projectDao.existsAbbreviation(projectAbbreviation);
	}

	@Override
	public boolean existsName(String name) {
		return projectDao.existsName(name);
	}
	
}
