package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.core.constants.BusinessConstants;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.nomenclator.NomenclatorValueDao;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.Project.ArieDeCuprindereEnum;
import ro.cloudSoft.cloudDoc.domain.project.ProjectEstimation;
import ro.cloudSoft.cloudDoc.domain.project.ProjectStatus;
import ro.cloudSoft.cloudDoc.domain.project.ProjectType;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskAttachment;
import ro.cloudSoft.cloudDoc.domain.project.TaskPriority;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;
import ro.cloudSoft.cloudDoc.security.SecurityManagerFactory;

public class DSPImporter {
	
	private SecurityManagerFactory securityManagerFactory;
	private BusinessConstants businessConstants;
	private UserPersistencePlugin userPersistencePlugin;
	private NomenclatorValueDao nomenclatorValueDao;
	private ProjectDao projectDao;
	
	public void doImport(String excelFilePath) throws AppUtilitiesException {
		
		validateInputs(excelFilePath);
		File excelFile = new File(excelFilePath);
		if (!excelFile.exists()) {
			throw new AppUtilitiesException("Fisierul excel nu exista");
		}
		
		DSPImportParser parser = new DSPImportParser(excelFilePath);
		DSPAllXlsModel data = parser.parse();
		
		DSPImportValidator validator = new DSPImportValidator(userPersistencePlugin, nomenclatorValueDao);
		String atasamenteRootDir = excelFile.getParentFile().getAbsolutePath() + File.separator + "atasamente"; 
		validator.validate(data, atasamenteRootDir);
				
		saveData(data, atasamenteRootDir);
	}
	
	@Transactional
	private void saveData(DSPAllXlsModel data, String attachemntsRootDirPath) throws AppUtilitiesException {
		
		List<DSPProiectXlsModel> proiecte = data.getProiecte();
		
		if (CollectionUtils.isNotEmpty(proiecte)) {
			for (DSPProiectXlsModel modelProiect : proiecte) {
				
				Project project = new Project();
				project.setProjectAbbreviation(modelProiect.getAbreviereProiect());
				project.setNumarProiect("P_0000");
				project.setName(modelProiect.getNumeProiect());
				project.setDescription(modelProiect.getDescriere());
				User responsabilUser = userPersistencePlugin.getUserByUsername(modelProiect.getResponsabilProiect());
				project.setInitiator(responsabilUser);
				project.setResponsibleUser(responsabilUser);
				project.setDocumentId(null);
				project.setDocumentLocationRealName(null);
				project.setStartDate(modelProiect.getDataInceput());
				project.setEndDate(modelProiect.getDataSfarsit());
				project.setImplementationDate(modelProiect.getDataImplementarii());
				project.setAutoritatiImplicate(modelProiect.getAutoritatiImplicate());
				project.setObiectiveProiect(modelProiect.getObiectiveProiect());
				project.setCadruLegal(modelProiect.getCadruLegal());
				project.setSpecificitateProiect(modelProiect.getSpecificitateProiect());
				project.setProiectInitiatDeAltaEntitate(modelProiect.getProiectInitiatDeAltaEntitate());
				project.setEvaluareaImpactului(modelProiect.getEvaluareaImpactului());
				project.setProiectInitiatArb(modelProiect.getProiectInitiatARB().getAsBoolean());
				project.setStatus(ProjectStatus.valueOf(modelProiect.getStatusProiect().name()));
				project.setType(ProjectType.DSP);
				
				List<NomenclatorValue> gradImportantaList = nomenclatorValueDao.findByNomenclatorCodeAndAttributes(NomenclatorConstants.NOMENCLATOR_CODE_IMPORTANTA_PROIECTE, 
						NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_GRAD_IMPORTANTA, modelProiect.getGradImportanta().getGrad(), 
						NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA, modelProiect.getGradImportanta().getValoare());
				project.setGradImportanta(gradImportantaList.get(0));
				
				List<NomenclatorValue> domeniuBancarList = nomenclatorValueDao.findByNomenclatorCodeAndAttribute(NomenclatorConstants.DOMENIU_BANCAR_NOMENCLATOR_CODE, 
						NomenclatorConstants.DOMENIU_BANCAR_ATTRIBUTE_KEY_DENUMIRE, modelProiect.getDomeniuBancar());
				project.setDomeniuBancar(domeniuBancarList.get(0));
				
				List<NomenclatorValue> incadrareProiectList = nomenclatorValueDao.findByNomenclatorCodeAndAttribute(NomenclatorConstants.INCADRARI_PROIECTE_NOMENCLATOR_CODE, 
						NomenclatorConstants.INCADRARI_PROIECTE_ATTRIBUTE_KEY_INCADRARE_PROIECT, modelProiect.getIncadrareProiect());
				project.setIncadrareProiect(incadrareProiectList.get(0));
				
				project.setArieDeCuprindere(ArieDeCuprindereEnum.valueOf(modelProiect.getArieDeCuprindere().name()));
				
				// Estimations
				List<ProjectEstimation> estimations = new ArrayList<>();
				ProjectEstimation estimation = new ProjectEstimation();
				estimation.setProject(project);
				estimation.setStartDate(modelProiect.getDataInceput());
				estimation.setEstimationInPercent(modelProiect.getEstimareRealizareProiect());
				estimations.add(estimation);
				project.setEstimations(estimations);
				
				// Participants				
				List<DSPParticipantXlsModel> participantModels = data.getParticipantiOfProiect(modelProiect.getAbreviereProiect());
				if (CollectionUtils.isNotEmpty(participantModels)) {
					List<OrganizationEntity> participanti = new ArrayList<>();
					for (DSPParticipantXlsModel participantModel : participantModels) {
						participanti.add(userPersistencePlugin.getUserByUsername(participantModel.getParticipant()));
					}
					project.setParticipants(participanti);
				}
				
				// Comisii GL
				List<DSPComisieGLXlsModel> comisiiGLModels = data.getComisiiGLImplicateOfProiect(modelProiect.getAbreviereProiect());
				if (CollectionUtils.isNotEmpty(comisiiGLModels)) {
					List<NomenclatorValue> comisiiGL = new ArrayList<>();
					for (DSPComisieGLXlsModel comisieGLModel : comisiiGLModels) {
						List<NomenclatorValue> comisieList = nomenclatorValueDao.findByNomenclatorCodeAndAttribute(NomenclatorConstants.COMISII_SAU_GL_NOMENCLATOR_CODE, 
								NomenclatorConstants.COMISII_SAU_GL_ATTRIBUTE_KEY_DENUMIRE, comisieGLModel.getDenumireComisieGL());
						comisiiGL.add(comisieList.get(0));
					}
					project.setComisiiSauGl(comisiiGL);
				}
				
				// Task-uri
				List<DSPTaskXlsModel> taskModels = data.getTaskuriOfProiect(modelProiect.getAbreviereProiect());
				if (CollectionUtils.isNotEmpty(taskModels)) {
					List<Task> tasks = new ArrayList<>();
					for (DSPTaskXlsModel taskModel : taskModels) {
						
						Task task = new Task();
						task.setProject(project);
						task.setName(taskModel.getNumeTask());
						task.setDescription(taskModel.getDescriere());
						task.setPriority(TaskPriority.valueOf(taskModel.getPrioritate().name()));
						task.setStartDate(taskModel.getDataInceput());
						task.setEndDate(taskModel.getDataSfarsit());
						task.setInitiator(project.getInitiator());
						task.setExplications(taskModel.getExplicatii());
						task.setParticipationsTo(taskModel.getParticipareLa());
						task.setFinalizedDate(taskModel.getDataFinalizare());
						task.setStatus(TaskStatus.valueOf(taskModel.getStatus().name()));
						
						if (CollectionUtils.isNotEmpty(taskModel.getNumeAtasamente())) {
							List<TaskAttachment> attachments = new ArrayList<>();
							for (String attachname : taskModel.getNumeAtasamente()) {
								
								File attachFile = new File(attachemntsRootDirPath + File.separator + taskModel.getAbreviereProiect() + File.separator + attachname);
								
								TaskAttachment taskAttach = new TaskAttachment();
								taskAttach.setTask(task);
								taskAttach.setName(attachname);
								try {
									taskAttach.setFileContent(FileUtils.readFileToByteArray(attachFile));
								} catch (IOException e) {
									throw new AppUtilitiesException("[" + taskModel.getAbreviereProiect()+ "] Task[" +taskModel.getNumeTask()+ "] -> eroare la citire atasament [" + attachname + "]");
								}
								
								attachments.add(taskAttach);
							}
							task.setAttachments(attachments);
						}
						
						List<OrganizationEntity> assignees = new ArrayList<OrganizationEntity>();
						assignees.add(userPersistencePlugin.getUserByUsername(taskModel.getResponsabilActivitate()));
						task.setAssignments(assignees);
						
						tasks.add(task);
					}
					
					project.setTasks(tasks);
				}
							
				projectDao.save(project);
			}
		}
	}
	
	private void validateInputs(String excelFilePath) throws AppUtilitiesException {		
		List<String> errorMessages = new ArrayList<>();
		if (StringUtils.isBlank(excelFilePath)) {
			errorMessages.add("Calea fisierului excel nu poate fi null");
		}
		File f = new File(excelFilePath);
		if (!f.exists()) {
			errorMessages.add("Fisierul excel nu exista");			
		}
		if (CollectionUtils.isNotEmpty(errorMessages)) {
			throw new AppUtilitiesException(errorMessages);
		}
	}
	
	public void setSecurityManagerFactory(SecurityManagerFactory securityManagerFactory) {
		this.securityManagerFactory = securityManagerFactory;
	}
	
	public void setBusinessConstants(BusinessConstants businessConstants) {
		this.businessConstants = businessConstants;
	}
	
	public void setNomenclatorValueDao(NomenclatorValueDao nomenclatorValueDao) {
		this.nomenclatorValueDao = nomenclatorValueDao;
	}
	
	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
	
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}
}
