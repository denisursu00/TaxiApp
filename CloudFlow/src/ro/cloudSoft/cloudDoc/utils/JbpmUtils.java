package ro.cloudSoft.cloudDoc.utils;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.api.ProcessEngine;
import org.jbpm.api.task.Task;

import ro.cloudSoft.cloudDoc.plugins.bpm.assignment.AssigneeHelper;

public class JbpmUtils {
	
	public static List<Task> getCurrentTasksForUser(ProcessEngine processEngine, Long userId) {
		List<Task> tasksForUser = new ArrayList<Task>();

		String asignee = AssigneeHelper.getAssigneeValueForUser(userId);
		
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().assignee(asignee).list();
		tasksForUser.addAll(tasks);
		List<Task> candidateTasks = processEngine.getTaskService().createTaskQuery().candidate(asignee).list();
		tasksForUser.addAll(candidateTasks);

		return tasksForUser;
	}
}
