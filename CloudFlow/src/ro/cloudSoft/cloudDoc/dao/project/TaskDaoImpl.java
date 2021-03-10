package ro.cloudSoft.cloudDoc.dao.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.core.constants.TaskConstants;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskAttachment;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ActiuniPeProiectReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.TaskuriCumulateReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.participariLaEvenimente.ParticipariEvenimenteReportFilterModel;
import ro.cloudSoft.common.utils.DateUtils;

public class TaskDaoImpl extends HibernateDaoSupport  implements TaskDao {

	@Override
	public Long save(Task task) {
		if (task.getId() == null) {
			return (Long) getHibernateTemplate().save(task);
		} else {
			getHibernateTemplate().saveOrUpdate(task);
			return task.getId();
		}
	}
		
	@Override
	public Task findById(Long taskId) {
		return (Task) getHibernateTemplate().get(Task.class, taskId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Task> getUserInProgressTasksModels(Long userId) {
		
		String assignedUserQuery = ""
			+ " SELECT task FROM Task task "
			+ " JOIN task.assignments taskAssignment "
			+ " WHERE taskAssignment.id = ? AND task.status = ?"
			+ " ORDER BY task.startDate DESC";
		
		Object[] queryParameters = new Object[] {
				userId,
				TaskStatus.IN_PROGRESS,
			};
		
		List<Task> assignetUserTasks = getHibernateTemplate().find(assignedUserQuery, queryParameters);
		return assignetUserTasks;
	}

	@Override
	@SuppressWarnings("unchecked")
	public TaskAttachment getTaskAttachment(Long attachmentId) {
		String query = ""
				+ " SELECT taskAttachment FROM Task task "
				+ " JOIN task.attachments taskAttachment "
				+ " WHERE taskAttachment.id = ?";
		
		
		List<TaskAttachment> taskAttachment = getHibernateTemplate().find(query, attachmentId);
		return (TaskAttachment) DataAccessUtils.singleResult(taskAttachment);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean taskCanBeFinalized(Long taskId) {
		String query = ""
				+ " SELECT task FROM Task task "
				+ " WHERE task.status = ? AND task.id = ?";
		Object[] queryParameters = new Object[] {
			TaskStatus.FINALIZED,
			taskId
		};
		List<Task> tasks = getHibernateTemplate().find(query, queryParameters);
		return CollectionUtils.isEmpty(tasks);
	}
	
	@Override
	public List<Task> getTasksAssignmentsByParticipariEvenimenteReportFilterModel(ParticipariEvenimenteReportFilterModel filter) {
		List<Object> queryParameters = new ArrayList<>(); 
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT task FROM Task task ");
		query.append("LEFT JOIN task.assignments orgnizationEntity ");
		query.append("LEFT JOIN task.subactivity subactivity ");
		query.append("JOIN task.project project ");

		query.append("WHERE  task.participationsTo IS NOT NULL  ");
		query.append("		AND  task.participationsTo != ?"); 
		queryParameters.add(TaskConstants.PARTICIPARI_LA_VALUE_COMUNICAT_DE_PRESA);
		query.append("		AND  task.participationsTo != ?"); 
		queryParameters.add(TaskConstants.PARTICIPARI_LA_VALUE_SEDINTA_COMISIE_GRUPURI_DE_LUCRU);
		query.append("		AND  task.participationsTo != ?"); 
		queryParameters.add(TaskConstants.PARTICIPARI_LA_VALUE_INTALNIRE_CONSILIUL_DIRECTOR);
		query.append("		AND  task.participationsTo != ?"); 
		queryParameters.add(TaskConstants.PARTICIPARI_LA_VALUE_SEDINTA_COMISIE_SISTEMICA);
		
		if (filter.getIdProiect() != null) {
			query.append(" AND (project.id = ?) ");
			queryParameters.add(filter.getIdProiect());
			if (!CollectionUtils.isEmpty(filter.getSubprojectIds())) {
				String subListString = "";
				List<String> questionMarks = new ArrayList<>();
				for (int i = 0; i < filter.getSubprojectIds().size(); i++) {
					if (!filter.getSubprojectIds().get(i).equals(Long.valueOf(-1L))) {
						questionMarks.add("?");
						queryParameters.add(filter.getSubprojectIds().get(i));
					}
				}
				subListString = String.join(",", questionMarks);
				if (!subListString.equals("")) {
					query.append(" AND ((subactivity.id IN ("+subListString+"))");
					if (filter.getSubprojectIds().stream().filter(subprojectId -> subprojectId.equals(Long.valueOf(-1L))).findAny().orElse(null) != null) {
						query.append(" OR (subactivity.id IS NULL))");
					}else {
						query.append(")");
					}
				} else {
					if (filter.getSubprojectIds().stream().filter(subprojectId -> subprojectId.equals(Long.valueOf(-1L))).findAny().orElse(null) != null) {
						query.append(" AND (subactivity.id IS NULL)");
					}
				}
			}
		}
		if (filter.getIdUserResponsabilTask() != null) {
			query.append(" AND " + "((orgnizationEntity IS NOT NULL) AND (orgnizationEntity.id = ?)) ");
			queryParameters.add(filter.getIdUserResponsabilTask());
		}
		if (filter.getStatusTask() != null) {
			query.append(" AND " + "(task.status = ?) ");
			queryParameters.add(TaskStatus.valueOf(filter.getStatusTask()));
		}
		if (filter.getDataInceput() != null) {
			query.append(" AND " + "(task.startDate >= ?) ");
			queryParameters.add(filter.getDataInceput());
		}
		if (filter.getDataSfarsit() != null) {
			query.append(" AND " + "(task.endDate <= ?) ");
			queryParameters.add(filter.getDataSfarsit());
		}
		query.append(" ORDER BY project.name, subactivity.name");
		
		List<Task> tasksAssignments = new ArrayList<>();
		if (queryParameters.size() == 0) {
			tasksAssignments = getHibernateTemplate().find(query.toString());
		} if (queryParameters.size() == 1) {
			tasksAssignments = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			tasksAssignments = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		return tasksAssignments;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> getAllTaskByActiuniPeProiectReportFilterModel(ActiuniPeProiectReportFilterModel filter) {
		
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT task ");
		query.append(" FROM Task task ");
		query.append("JOIN task.project project ");
		query.append("LEFT JOIN task.subactivity subactivity ");
		
		String trueCondition = " 1 = 1 ";
		query.append("WHERE " + trueCondition);

		List<Object> queryParameters = new ArrayList<>(); 
		
		if (filter.getProiectId() != null) {
			query.append(" AND (project.id = ?) ");
			queryParameters.add(filter.getProiectId());
			if (!CollectionUtils.isEmpty(filter.getSubprojectIds())) {
				String subListString = "";
				List<String> questionMarks = new ArrayList<>();
				for (int i = 0; i < filter.getSubprojectIds().size(); i++) {
					if (!filter.getSubprojectIds().get(i).equals(Long.valueOf(-1L))) {
						questionMarks.add("?");
						queryParameters.add(filter.getSubprojectIds().get(i));
					}
				}
				subListString = String.join(",", questionMarks);
				if (!subListString.equals("")) {
					query.append(" AND ((subactivity.id IN ("+subListString+"))");
					if (filter.getSubprojectIds().stream().filter(subprojectId -> subprojectId.equals(Long.valueOf(-1L))).findAny().orElse(null) != null) {
						query.append(" OR (subactivity.id IS NULL))");
					}else {
						query.append(")");
					}
				} else {
					if (filter.getSubprojectIds().stream().filter(subprojectId -> subprojectId.equals(Long.valueOf(-1L))).findAny().orElse(null) != null) {
						query.append(" AND (subactivity.id IS NULL)");
					}
				}
			}
		}
		if (filter.getDataInceput() != null) {
			query.append(" AND " + "(task.startDate >= ?) ");
			queryParameters.add(DateUtils.nullHourMinutesSeconds(filter.getDataInceput()));
		}
		if (filter.getDataSfarsit() != null) {
			query.append(" AND " + "(task.startDate <= ?) ");
			queryParameters.add(DateUtils.maximizeHourMinutesSeconds(filter.getDataSfarsit()));
		}
		

		query.append(" ORDER BY project.name, subactivity.name, task.startDate ");
		
		List<Task> task = new ArrayList<>();
		
		if (queryParameters.size() == 0) {
			task = getHibernateTemplate().find(query.toString());
		} if (queryParameters.size() == 1) {
			task = getHibernateTemplate().find(query.toString(), queryParameters.get(0));
		}else {
			task = getHibernateTemplate().find(query.toString(), queryParameters.toArray());
		}
		
		return task;
	}
	
	@Override
	public List<Task> getInProgressTaskByIdAndProjectAndEndDate(Long projectId, String taskName, Date taskEndDateDeLa,
			Date taskEndDatePanaLa) {
		
		List<Object> queryParams = new ArrayList<>();
		StringBuilder query = new StringBuilder(""
				+ "FROM Task task "
				+ "WHERE "
				+ "	task.project.id = ? "
				+ "		AND "
				+ "	task.status = ? ");
		queryParams.add( projectId);
		queryParams.add( TaskStatus.IN_PROGRESS);
		
		if (taskName != null) {
			query.append(" AND  (task.name = ?) ");
			queryParams.add( taskName);
		}
		if (taskEndDateDeLa != null) {
			query.append(" AND  (task.endDate >= ?) ");
			queryParams.add( taskEndDateDeLa);
		}
		if (taskEndDatePanaLa != null) {
			query.append(" AND  (task.endDate <= ?) ");
			queryParams.add( taskEndDatePanaLa);
		}
		
		return getHibernateTemplate().find(query.toString(), queryParams.toArray());
		
	}

	@Override
	public List<String> getAllTaskNames() {
		String query = ""
				+ " SELECT DISTINCT task.name "
				+ "	FROM Task task ";
		
		return getHibernateTemplate().find(query);
	}

	@Override
	public List<String> getAllInProgressTaskNamesByProjectAbbreviation(String projectAbbreviation) {
		
		List<Object> queryParams = new ArrayList<>();
		StringBuilder query = new StringBuilder(""
				+ " SELECT DISTINCT task.name "
				+ "	FROM Task task "
				+ " WHERE task.status = ? ");
		queryParams.add( TaskStatus.IN_PROGRESS);
		
		if (projectAbbreviation != null) {
			query.append(" AND  (task.project.projectAbbreviation = ?) ");
			queryParams.add(projectAbbreviation);
		}
		
		return getHibernateTemplate().find(query.toString(), queryParams.toArray());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> getAllTasksByTaskuriCumulateReportFilterModel(TaskuriCumulateReportFilterModel filter) {
		
		List<Object> queryParams = new ArrayList<>();
		StringBuilder query = new StringBuilder(""
				+ " SELECT task"
				+ " FROM Task task "
				+ " WHERE 1=1 ");
		
		if (filter.getStatus() != null) {
			query.append(" AND  (task.status = ?) ");
			queryParams.add(TaskStatus.valueOf(filter.getStatus()));
			if (filter.getStatus().equals(TaskStatus.IN_PROGRESS.toString()) || filter.getStatus().equals(TaskStatus.CANCELLED.name())) {
				if (filter.getDeLaData() != null) {
					query.append(" AND  (task.startDate >= ?) ");
					queryParams.add( DateUtils.nullHourMinutesSeconds(filter.getDeLaData()));
				}
				if (filter.getPanaLaData() != null) {
					query.append(" AND  (task.startDate <= ?) ");
					queryParams.add( DateUtils.maximizeHourMinutesSeconds(filter.getPanaLaData()));
				}
			} else {
				if (filter.getDeLaData() != null) {
					query.append(" AND  (task.finalizedDate >= ?) ");
					queryParams.add( DateUtils.nullHourMinutesSeconds(filter.getDeLaData()));
				}
				if (filter.getPanaLaData() != null) {
					query.append(" AND  (task.finalizedDate <= ?) ");
					queryParams.add( DateUtils.maximizeHourMinutesSeconds(filter.getPanaLaData()));
				}
			}
		} else {
			if (filter.getDeLaData() != null && filter.getPanaLaData() != null) {
				query.append("  AND ( task.status IN (?,?) ");
				queryParams.add(TaskStatus.IN_PROGRESS);
				queryParams.add(TaskStatus.CANCELLED);
				if (filter.getDeLaData() != null) {
					query.append(" AND  (task.startDate >= ?) ");
					queryParams.add( DateUtils.nullHourMinutesSeconds(filter.getDeLaData()));
				}
				if (filter.getPanaLaData() != null) {
					query.append(" AND  (task.startDate <= ?) )");
					queryParams.add( DateUtils.maximizeHourMinutesSeconds(filter.getPanaLaData()));
				}
				query.append("OR (  task.status = ? ");
				queryParams.add( TaskStatus.FINALIZED);
				
				if (filter.getDeLaData() != null) {
					query.append(" AND  (task.finalizedDate >= ?) ");
					queryParams.add( DateUtils.nullHourMinutesSeconds(filter.getDeLaData()));
				}
				if (filter.getPanaLaData() != null) {
					query.append(" AND  (task.finalizedDate <= ?) )");
					queryParams.add( DateUtils.maximizeHourMinutesSeconds(filter.getPanaLaData()));
				}
			}
		}
	
		return getHibernateTemplate().find(query.toString(), queryParams.toArray());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Task> getAllTasksBySubactivityId(Long subactivityId) {
		return getHibernateTemplate().find("FROM Task t WHERE t.subactivity.id = ?",subactivityId);
	}

}
