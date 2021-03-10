package ro.cloudSoft.cloudDoc.dao.views;

import java.util.List;

import ro.cloudSoft.cloudDoc.domain.views.ViewDocumentHistoryCompletedTasks;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.TaskuriCumulateReportFilterModel;

public interface DocumentHistoryCompletedTasksViewDao {

	List<ViewDocumentHistoryCompletedTasks> getAllByTaskuriCumulateReportFilterModel(TaskuriCumulateReportFilterModel filter);
}
