package ro.cloudSoft.cloudDoc.services.bpm.custom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.arb.DocumentDspConstants;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeDao;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.dao.project.TaskDao;
import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.content.CollectionInstance;
import ro.cloudSoft.cloudDoc.domain.content.Document;
import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.content.MetadataCollection;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.Project.ArieDeCuprindereEnum;
import ro.cloudSoft.cloudDoc.domain.project.ProjectEstimation;
import ro.cloudSoft.cloudDoc.domain.project.ProjectStatus;
import ro.cloudSoft.cloudDoc.domain.project.ProjectType;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskPriority;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.utils.DocumentUtils;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.common.utils.spring.SpringUtils;

// TODO: Trebuie refactorizata clasa cand este timp

public class DspInsertProjectAndTasks extends AutomaticTask {
	
	private final static String PROIECT_INITIAT_ARB_TRUE_VALUE = "da";

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void execute(WorkflowInstance workflowInstance) throws AutomaticTaskExecutionException, AppException {
		
		Document document = getDocumentService().getDocumentById(workflowInstance.getDocumentId(), workflowInstance.getWorkspaceName(), getUserSecurity());
		
		//TODO: aici datele din document flux se vor salva in db project si nu in document
		Project project = saveProject(document);
		saveTasks(document, project);
	}
	
	private Project saveProject(Document document) {
		
		DocumentType dspDocumentType = getDocumentTypeDao().getDocumentTypeByName(getDspConstants().getDocumentTypeName());

		Project project = new Project();
		project.setDocumentId(document.getId());
		project.setDocumentLocationRealName(document.getDocumentLocationRealName());
		
		String projectName = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getNumeProiectMetadataName());
		project.setName(projectName);

		String projectDescription = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getDescriereProiectMetadataName());
		project.setDescription(projectDescription);

		String projectAbreviation = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getAbreviereProiectMetadataName());
		project.setProjectAbbreviation(projectAbreviation);
		
		Long responsibleUserId = DocumentUtils.getMetadataUserValue(document, dspDocumentType, getDspConstants().getResponsabilProiectMetadataName());
		project.setResponsibleUser(getUserService().getUserById(responsibleUserId));
		
		Date startDate = DocumentUtils.getMetadataDateValue(document, dspDocumentType, getDspConstants().getDataInceputProiectMetadataName());
		project.setStartDate(startDate);
		
		Date endDate = DocumentUtils.getMetadataDateValue(document, dspDocumentType, getDspConstants().getDataSfarsitProiectMetadataName());
		project.setEndDate(endDate);
		
		Date implementationDate = DocumentUtils.getMetadataDateValue(document, dspDocumentType, getDspConstants().getDataImplementariiMetadataName());
		project.setImplementationDate(implementationDate);
		
		Long initiatorId = DocumentUtils.getMetadataUserValue(document, dspDocumentType, getDspConstants().getInitiatorProiectMetadataName());
		project.setInitiator(getUserService().getUserById(initiatorId));
		
		String numarProiect = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getNumarProiectMetadataName());
		project.setNumarProiect(numarProiect);
		
		Long domBancarNomValueId = DocumentUtils.getMetadataNomenclatorValue(document, dspDocumentType, getDspConstants().getDomeniuBancarMetadataName());
		project.setDomeniuBancar(getNomenclatorValueDao().find(domBancarNomValueId));
		
		String proiectInitiatArbStringValue = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getProiectInitiatDeArbMetadataName());
		if (StringUtils.isNotEmpty(proiectInitiatArbStringValue)) {
			Boolean proiectInitiatArbBooleanValue = null;
			if (proiectInitiatArbStringValue.equals(PROIECT_INITIAT_ARB_TRUE_VALUE)) {
				proiectInitiatArbBooleanValue = true;
			} else {
				proiectInitiatArbBooleanValue = false;
			}
			project.setProiectInitiatArb(proiectInitiatArbBooleanValue);
		}
		
		String arieDeCuprindere = DocumentUtils.getMetadataValueAsString(document, dspDocumentType,  getDspConstants().getArieDeCuprindereMetadataName());
		if (StringUtils.isNotEmpty(arieDeCuprindere)) {
			if (arieDeCuprindere.equals(ArieDeCuprindereEnum.INTERN.getValue())) {
				project.setArieDeCuprindere(ArieDeCuprindereEnum.INTERN);				
			} else if (arieDeCuprindere.equals(ArieDeCuprindereEnum.INTERNATIONAL.getValue())) {
				project.setArieDeCuprindere(ArieDeCuprindereEnum.INTERNATIONAL);				
			} else {
				throw new RuntimeException("invalid arie de cuprindere value: " + arieDeCuprindere);
			}
		}	
		
		String proiectInitiatDeAltaEntitate = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getProiectInitiatDeAltaEntitateMetadataName());
		project.setProiectInitiatDeAltaEntitate(proiectInitiatDeAltaEntitate);
		
		String evaluareaImpactului = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getEvaluareaImpactuluiMetadataName());
		project.setEvaluareaImpactului(evaluareaImpactului);
		
		Long incadrareProiectNomValueId = DocumentUtils.getMetadataNomenclatorValue(document, dspDocumentType, getDspConstants().getIncadrareProiectMetadataName());
		project.setIncadrareProiect(getNomenclatorValueDao().find(incadrareProiectNomValueId));
		
		String autoritatiImplicate = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getAutoritatiImplicateMetadataName());
		project.setAutoritatiImplicate(autoritatiImplicate);
		
		String obiectiveProiect = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getObiectiveProiectMetadataName());
		project.setObiectiveProiect(obiectiveProiect);
		
		String cadruLegal = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getCadrulLegalMetadataName());
		project.setCadruLegal(cadruLegal);
		
		String specificitateProiect = DocumentUtils.getMetadataValueAsString(document, dspDocumentType, getDspConstants().getSpecificitateProiectMetadataName());
		project.setSpecificitateProiect(specificitateProiect);

		Long gradImportantaNomValueId = DocumentUtils.getMetadataNomenclatorValue(document, dspDocumentType, getDspConstants().getGradImportantaMetadataName());
		project.setGradImportanta(getNomenclatorValueDao().find(gradImportantaNomValueId));
		
		
		List<OrganizationEntity> participantiProiect = new ArrayList<OrganizationEntity>();
		Map<Long, List<CollectionInstance>> documentCollectionMetadataInstanceList = document.getCollectionInstanceListMap();
		for (Entry<Long, List<CollectionInstance>> entry : documentCollectionMetadataInstanceList.entrySet()) {
			MetadataCollection metadataDefinition = getDocumentTypeDao().getMetadataCollectionDefinition(entry.getKey());
			if (metadataDefinition.getName().equals(getDspConstants().getParticipantiProiectMetadataName())) {
				List<CollectionInstance> collectionInstances = entry.getValue();
				for (CollectionInstance collectionInstance : collectionInstances) {
					for (MetadataInstance metadataInstance : collectionInstance.getMetadataInstanceList()) {
						MetadataDefinition collectionInstanceMetadataDefinition = getDocumentTypeDao().getMetadataDefinition(metadataInstance.getMetadataDefinitionId());
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getParticipantProiectOfParticipantiProiectMetadataName())) {
							Long participantUserId = MetadataValueHelper.getUserId(metadataInstance.getValue());
							participantiProiect.add(getUserService().getUserById(participantUserId));
						}
					}
					
				}
			}
		}
		project.setParticipants(participantiProiect);
		
		List<NomenclatorValue> comisiiSauGlImplicate = new ArrayList<NomenclatorValue>();
		for (Entry<Long, List<CollectionInstance>> entry : documentCollectionMetadataInstanceList.entrySet()) {
			MetadataCollection metadataDefinition = getDocumentTypeDao().getMetadataCollectionDefinition(entry.getKey());
			if (metadataDefinition.getName().equals(getDspConstants().getComisiiGlImplicateMetadataName())) {
				List<CollectionInstance> collectionInstances = entry.getValue();
				for (CollectionInstance collectionInstance : collectionInstances) {
					for (MetadataInstance metadataInstance : collectionInstance.getMetadataInstanceList()) {
						MetadataDefinition collectionInstanceMetadataDefinition = getDocumentTypeDao().getMetadataDefinition(metadataInstance.getMetadataDefinitionId());
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getDenumireComisieGlImplicatOfComisiiGlImplicateMetadataName())) {
							Long comisieSauGlId = MetadataValueHelper.getNomenclatorValueId(metadataInstance.getValue());
							comisiiSauGlImplicate.add(getNomenclatorValueDao().find(comisieSauGlId));
						}
					}
					
				}
			}
		}
		project.setComisiiSauGl(comisiiSauGlImplicate);

		project.setEstimations(prepareProjectEstimations(project));
		project.setType(ProjectType.DSP);
		project.setStatus(ProjectStatus.INITIATED);
		
		getProjectDao().save(project);
		
		return project;
	}

	private List<ProjectEstimation> prepareProjectEstimations(Project project) {
		ProjectEstimation projectEstimation = new ProjectEstimation();
		projectEstimation.setProject(project);
		projectEstimation.setEstimationInPercent(0);
		projectEstimation.setStartDate(project.getStartDate());
		
		List<ProjectEstimation> projectEstimations = new ArrayList<>();
		projectEstimations.add(projectEstimation);
		
		return projectEstimations;
	}
	
	private void saveTasks(Document document, Project project) throws AutomaticTaskExecutionException {
		
		List<Task> tasks = new ArrayList<Task>();
		Map<Long, List<CollectionInstance>> documentCollectionMetadataInstanceList = document.getCollectionInstanceListMap();
		for (Entry<Long, List<CollectionInstance>> entry : documentCollectionMetadataInstanceList.entrySet()) {
			MetadataCollection metadataDefinition = getDocumentTypeDao().getMetadataCollectionDefinition(entry.getKey());
			if (metadataDefinition.getName().equals(getDspConstants().getActivitatiProiectMetadataName())) {
				List<CollectionInstance> collectionInstances = entry.getValue();
				for (CollectionInstance collectionInstance : collectionInstances) {
					
					Task task = new Task();
					
					for (MetadataInstance metadataInstance : collectionInstance.getMetadataInstanceList()) {
						
						MetadataDefinition collectionInstanceMetadataDefinition = getDocumentTypeDao().getMetadataDefinition(metadataInstance.getMetadataDefinitionId());

						task.setInitiator(project.getInitiator());
						
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getNumeActivitateOfActivitatiProiectMetadataName())) {
							task.setName(MetadataValueHelper.getText(metadataInstance.getValue()));
						}
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getDescriereActivitateOfActivitatiMetadataName())) {
							task.setDescription(MetadataValueHelper.getText(metadataInstance.getValue()));
						}
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getDataInceputActivitateOfActivitatiMetadataName())) {
							task.setStartDate(MetadataValueHelper.getDate(metadataInstance.getValue()));
						}
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getDataSfarsitActivitateOfActivitatiMetadataName())) {
							task.setEndDate(MetadataValueHelper.getDate(metadataInstance.getValue()));
						}
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getParticipareLaActivitateOfActivitatiMetadataName())) {
							task.setParticipationsTo(MetadataValueHelper.getListItemValue(metadataInstance.getValue()));
						}
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getExplicatiiActivitateOfActivitatiMetadataName())) {
							task.setExplications(MetadataValueHelper.getText(metadataInstance.getValue()));
						}
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getDataInceputEvenimentOfActivitatiMetadataName())) {
							task.setEvenimentStartDate(MetadataValueHelper.getDateTime(metadataInstance.getValue()));
						}
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getDataSfarsitEvenimentOfActivitatiMetadataName())) {
							task.setEvenimentEndDate(MetadataValueHelper.getDateTime(metadataInstance.getValue()));
						}
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getPrioritateActivitateOfActivitatiMetadataName())) {
							String priority = MetadataValueHelper.getText(metadataInstance.getValue()).toUpperCase();
							if (TaskPriority.HIGH.name().equals(priority)) {
								task.setPriority(TaskPriority.HIGH);
							} else if (TaskPriority.NORMAL.name().equals(priority)) {
								task.setPriority(TaskPriority.NORMAL);
							} else if (TaskPriority.LOW.name().equals(priority)) {
								task.setPriority(TaskPriority.LOW);
							} else {
								throw new AutomaticTaskExecutionException("Task priority cannot be [" + priority + "]");
							}
						}
						if (collectionInstanceMetadataDefinition.getName().equals(getDspConstants().getUserAsignatActivitateOfActivitatiMetadataName())) {
							Long assigneeId = MetadataValueHelper.getUserId(metadataInstance.getValue());
							List<OrganizationEntity> assignees = new ArrayList<OrganizationEntity>();
							assignees.add(getUserService().getUserById(assigneeId));
							task.setAssignments(assignees);
						}
					}
					tasks.add(task);
				}
			}
		}
		
		for (Task task : tasks) {
			task.setProject(project);
			task.setStatus(TaskStatus.IN_PROGRESS);
			getTaskDao().save(task);
		}
	}
	
	public DocumentService getDocumentService() {
		return SpringUtils.getBean("documentService");
	}
	
	public DocumentTypeDao getDocumentTypeDao() {
		return SpringUtils.getBean("documentTypeDao");
	}
	
	public UserService getUserService() {
		return SpringUtils.getBean("userService");
	}
	
	public ProjectDao getProjectDao() {
		return SpringUtils.getBean("projectDao");
	}
	
	public TaskDao getTaskDao() {
		return SpringUtils.getBean("taskDao");
	}
	
	public NomenclatorValueDao getNomenclatorValueDao() {
		return SpringUtils.getBean("nomenclatorValueDao");
	}
	
	private DocumentDspConstants getDspConstants() {
		return SpringUtils.getBean("documentDspConstants");
	}
}
