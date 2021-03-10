package ro.cloudSoft.cloudDoc.services.xDocReports;

import java.util.Map;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ExportType;
import ro.cloudSoft.cloudDoc.services.GeneralReport;

public interface XDocReportsService {
	DownloadableFile generate(byte[] templateFileContent, ExportType exportType, Map<String, Object> parameters, String downloadableFileName);

	DownloadableFile generate(GeneralReport generalReport, ExportType exportType, Map dspExportModelAsMap, String downloadableFileName);
}
