package ro.cloudSoft.cloudDoc.dao.views;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowInstance;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.domain.views.ViewDocumentHistoryCompletedTasks;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.TaskuriCumulateReportFilterModel;
import ro.cloudSoft.common.utils.DateUtils;

public class DocumentHistoryCompletedTasksViewDaoImpl extends HibernateDaoSupport implements DocumentHistoryCompletedTasksViewDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<ViewDocumentHistoryCompletedTasks> getAllByTaskuriCumulateReportFilterModel(
			TaskuriCumulateReportFilterModel filter) {
		List<Object> queryParams = new ArrayList<>();
		StringBuilder query = new StringBuilder(""
				+ " SELECT task"
				+ " FROM ViewDocumentHistoryCompletedTasks task "
				+ " WHERE 1=1 ");
		
		if (filter.getStatus() != null) {
			if (filter.getStatus().equals(TaskStatus.IN_PROGRESS.toString())) {
				query.append(" AND  (task.documentWorkflowStatus = ?) ");
				queryParams.add(WorkflowInstance.STATUS_RUNNING.toString());
				if (filter.getDeLaData() != null) {
					query.append(" AND  (task.taskCreatedDate >= ?) ");
					queryParams.add( DateUtils.nullHourMinutesSeconds(filter.getDeLaData()));
				}
				if (filter.getPanaLaData() != null) {
					query.append(" AND  (task.taskCreatedDate <= ?) ");
					queryParams.add( DateUtils.maximizeHourMinutesSeconds(filter.getPanaLaData()));
				}
			} 
			if (filter.getStatus().equals(TaskStatus.FINALIZED.toString())) {
				query.append(" AND  (task.documentWorkflowStatus = ?) ");
				queryParams.add(WorkflowInstance.STATUS_FINNISHED.toString());
				query.append(" AND  (task.documentId IN ("
						+ " SELECT DISTINCT documentId "
						+ " FROM ViewDocumentHistoryCompletedTasks "
						+ " WHERE (documentWorkflowStatus = ?)");
				queryParams.add(WorkflowInstance.STATUS_FINNISHED.toString());	
				query.append(" GROUP BY documentId"
						+ " HAVING 1=1 ");
				if (filter.getDeLaData() != null) {
					query.append(" AND  ( MAX(taskEndedDate) >= ?) ");
					queryParams.add( DateUtils.nullHourMinutesSeconds(filter.getDeLaData()));
				}
				if (filter.getPanaLaData() != null) {
					query.append(" AND  (MAX(taskEndedDate) <= ?) ");
					queryParams.add( DateUtils.maximizeHourMinutesSeconds(filter.getPanaLaData()));
				}		
				query.append(") ) ");
			}if (filter.getStatus().equals(TaskStatus.CANCELLED.name())) {
				query.append(" AND 1=2 ");
			}
		} else {
			if (filter.getDeLaData() != null && filter.getPanaLaData() != null) {
				query.append("  AND ( task.documentWorkflowStatus = ? ");
				queryParams.add(WorkflowInstance.STATUS_RUNNING.toString());
				if (filter.getDeLaData() != null) {
					query.append(" AND  (task.taskCreatedDate >= ?) ");
					queryParams.add( DateUtils.nullHourMinutesSeconds(filter.getDeLaData()));
				}
				if (filter.getPanaLaData() != null) {
					query.append(" AND  (task.taskCreatedDate <= ?) )");
					queryParams.add( DateUtils.maximizeHourMinutesSeconds(filter.getPanaLaData()));
				}
				query.append("OR (  task.documentWorkflowStatus = ? ");
				queryParams.add(WorkflowInstance.STATUS_FINNISHED.toString());
				query.append(" AND  (task.documentId IN ("
						+ " SELECT DISTINCT documentId "
						+ " FROM ViewDocumentHistoryCompletedTasks "
						+ " WHERE (documentWorkflowStatus = ?)");
				queryParams.add(WorkflowInstance.STATUS_FINNISHED.toString());	
				query.append(" GROUP BY documentId"
						+ " HAVING 1=1 ");
				if (filter.getDeLaData() != null) {
					query.append(" AND  ( MAX(taskEndedDate) >= ?) ");
					queryParams.add( DateUtils.nullHourMinutesSeconds(filter.getDeLaData()));
				}
				if (filter.getPanaLaData() != null) {
					query.append(" AND  (MAX(taskEndedDate) <= ?) ");
					queryParams.add( DateUtils.maximizeHourMinutesSeconds(filter.getPanaLaData()));
				}		
				query.append(") ) ) ");
			}
		}
		if (filter.getUserAsignat() != null) {
			query.append(" AND  (task.assigneeUserId = ?) ");
			queryParams.add(filter.getUserAsignat().toString());
		}
	
		return getHibernateTemplate().find(query.toString(), queryParams.toArray());
	}

}
