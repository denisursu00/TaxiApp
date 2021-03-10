package ro.cloudSoft.cloudDoc.services.bpm;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.bpm.TaskInstance;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public interface TaskManagementService
{
	public void acceptTask(Long taskId, SecurityManager userSecurity );
	
	public List<TaskInstance> getCurrentTasks( SecurityManager userSecurity );
	
	public void finishTask(TaskInstance task, SecurityManager userSecurity );
	
}
