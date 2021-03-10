package ro.cloudSoft.cloudDoc.dao.project;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskAttachment;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.TaskuriCumulateReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.participariLaEvenimente.ParticipariEvenimenteReportFilterModel;

public interface TaskDao {
	
	Long save(Task task);
	
	Task findById(Long taskId);
	
	List<Task> getUserInProgressTasksModels(Long userId);
	
	TaskAttachment getTaskAttachment(Long attachmentId);
	
	boolean taskCanBeFinalized(Long taskId);

	List<Task> getTasksAssignmentsByParticipariEvenimenteReportFilterModel(ParticipariEvenimenteReportFilterModel filter);
	
	List <Task> getAllTaskByActiuniPeProiectReportFilterModel(ActiuniPeProiectReportFilterModel filter);

	List<Task> getInProgressTaskByIdAndProjectAndEndDate(Long projectId, String taskName,
			Date taskEndDateDeLa, Date taskEndDatePanaLa);

	List<String> getAllTaskNames();
	
	List<String> getAllInProgressTaskNamesByProjectAbbreviation(String projectAbbreviation);
	
	List<Task> getAllTasksByTaskuriCumulateReportFilterModel(TaskuriCumulateReportFilterModel filter);
	
	List<Task> getAllTasksBySubactivityId(Long subactivityId);

}
