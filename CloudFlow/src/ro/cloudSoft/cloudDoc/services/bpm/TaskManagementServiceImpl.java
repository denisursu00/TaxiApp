package ro.cloudSoft.cloudDoc.services.bpm;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.bpm.TaskInstance;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;

public class TaskManagementServiceImpl implements TaskManagementService
{

	@Override
	public void acceptTask(Long taskId, SecurityManager userSecurity)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishTask(TaskInstance task, SecurityManager userSecurity)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaskInstance> getCurrentTasks(SecurityManager userSecurity)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
