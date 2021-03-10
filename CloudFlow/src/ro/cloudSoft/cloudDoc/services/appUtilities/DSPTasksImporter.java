package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.dao.project.TaskDao;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskAttachment;
import ro.cloudSoft.cloudDoc.domain.project.TaskPriority;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.plugins.organization.UserPersistencePlugin;

public class DSPTasksImporter {
	
	private UserPersistencePlugin userPersistencePlugin;
	private ProjectDao projectDao;
	private TaskDao taskDao;
	
	public void doImport(String excelFilePath) throws AppUtilitiesException {
		
		validateInputs(excelFilePath);
		File excelFile = new File(excelFilePath);
		if (!excelFile.exists()) {
			throw new AppUtilitiesException("Fisierul excel nu exista.");
		}
		
		DSPTasksImportParser parser = new DSPTasksImportParser(excelFilePath);
		List<DSPTaskXlsModel> data = parser.parse();
				
		DSPTasksImportValidator validator = new DSPTasksImportValidator(userPersistencePlugin);
		String atasamenteRootDir = excelFile.getParentFile().getAbsolutePath() + File.separator + "atasamente"; 
		validator.validate(data, atasamenteRootDir);
				
		saveData(data, atasamenteRootDir);
	}
	
	@Transactional
	private void saveData(List<DSPTaskXlsModel> data, String attachemntsRootDirPath) throws AppUtilitiesException {
		
		if (CollectionUtils.isEmpty(data)) {
			return;			
		}
		
		for (DSPTaskXlsModel taskModel : data) {
			
			Project project = projectDao.getByAbreviere(taskModel.getAbreviereProiect());
			if (project == null) {
				throw new AppUtilitiesException("Nu exista proiect cu abrevierea [" + taskModel.getAbreviereProiect() + "].");
			}
			
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
			
			taskDao.save(task);
		}
	}
	
	private void validateInputs(String excelFilePath) throws AppUtilitiesException {		
		List<String> errorMessages = new ArrayList<>();
		if (StringUtils.isBlank(excelFilePath)) {
			errorMessages.add("Calea fisierului excel nu poate fi null.");
		}
		File f = new File(excelFilePath);
		if (!f.exists()) {
			errorMessages.add("Fisierul excel nu exista.");			
		}
		if (CollectionUtils.isNotEmpty(errorMessages)) {
			throw new AppUtilitiesException(errorMessages);
		}
	}
	
	public void setUserPersistencePlugin(UserPersistencePlugin userPersistencePlugin) {
		this.userPersistencePlugin = userPersistencePlugin;
	}
	
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}
	
	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}
}
