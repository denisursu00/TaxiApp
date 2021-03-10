package ro.cloudSoft.cloudDoc.services.jasperReports;

import java.sql.Connection;
import java.util.Map;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ExportType;

public interface JasperReportsService {
	
	void prepareTemplates();
	
	byte[] generate(String reportIdentifier, ExportType exportType, Connection connection);
	
	byte[] generate(String reportIdentifier, ExportType exportType, Connection connection, Map<String, Object> parameters);
	
	byte[] generate(String reportIdentifier, ExportType exportType, JRDataSourceBuilder dataSourceBuilder);
	
	byte[] generate(String reportIdentifier, ExportType exportType, JRDataSourceBuilder dataSourceBuilder, Map<String, Object> parameters);
	
	DownloadableFile generate(String reportIdentifier, ExportType exportType, Connection connection, String downloadableFileName);
	
	DownloadableFile generate(String reportIdentifier, ExportType exportType, Connection connection, Map<String, Object> parameters, String downloadableFileName);
		
	DownloadableFile generate(String reportIdentifier, ExportType exportType, JRDataSourceBuilder dataSourceBuilder, String downloadableFileName);
	
	DownloadableFile generate(String reportIdentifier, ExportType exportType, JRDataSourceBuilder dataSourceBuilder, Map<String, Object> parameters, String downloadableFileName);
}
