package ro.cloudSoft.cloudDoc.services.jasperReports;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeTemplateDao;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ExportType;

public class JasperReportsServiceImpl implements JasperReportsService {

	private String baseDirectoryPathOfReports;
	private Map<String, Object> commonParametersOfReports;
	private DocumentTypeTemplateDao documentTypeTemplateDao;
	
	public JasperReportsServiceImpl() {
	}
	
	@Override
	public void prepareTemplates() {	
		JasperReportsTemplatePreparator preparator = new JasperReportsTemplatePreparator(baseDirectoryPathOfReports, documentTypeTemplateDao);
		preparator.prepare();
	}
	
	private byte[] generateContent(ExportType exportType, JasperReportGenerator generator) {
		if (ExportType.PDF.equals(exportType)) {
			return generator.generateAsPdf();
		} else if (ExportType.DOCX.equals(exportType)) {
			return generator.generateAsDocx();
		} else {
			throw new RuntimeException("Unknown export type [" + exportType + "]");
		}
	}
	
	@Override
	public byte[] generate(String reportIdentifier, ExportType exportType, Connection connection) {
		JasperReportGenerator generator = new JasperReportGenerator(baseDirectoryPathOfReports, reportIdentifier, connection, getCommonParametersOfReports());
		return generateContent(exportType, generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public byte[] generate(String reportIdentifier, ExportType exportType, Connection connection, Map<String, Object> parameters) {
		JasperReportGenerator generator = new JasperReportGenerator(baseDirectoryPathOfReports, reportIdentifier, connection, unionParameters(parameters, getCommonParametersOfReports()));
		return generateContent(exportType, generator);
	}
	
	@Override
	public byte[] generate(String reportIdentifier, ExportType exportType, JRDataSourceBuilder dataSourceBuilder) {
		JasperReportGenerator generator = new JasperReportGenerator(baseDirectoryPathOfReports, reportIdentifier, dataSourceBuilder.build(),  getCommonParametersOfReports());
		return generateContent(exportType, generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public byte[] generate(String reportIdentifier, ExportType exportType, JRDataSourceBuilder dataSourceBuilder, Map<String, Object> parameters) {
		
		JasperReportGenerator generator = new JasperReportGenerator(baseDirectoryPathOfReports, reportIdentifier, dataSourceBuilder.build(), unionParameters(parameters, getCommonParametersOfReports()));
		return generateContent(exportType, generator);
	}
	
	@Override
	public DownloadableFile generate(String reportIdentifier, ExportType exportType, JRDataSourceBuilder dataSourceBuilder, String downloadableFileName) {
		JasperReportGenerator generator = new JasperReportGenerator(baseDirectoryPathOfReports, reportIdentifier, dataSourceBuilder.build(), getCommonParametersOfReports());
		byte[] content = generateContent(exportType, generator);
		return new DownloadableFile(downloadableFileName, content);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DownloadableFile generate(String reportIdentifier, ExportType exportType, JRDataSourceBuilder dataSourceBuilder, Map<String, Object> parameters, String downloadableFileName) {
		JasperReportGenerator generator = new JasperReportGenerator(baseDirectoryPathOfReports, reportIdentifier, dataSourceBuilder.build(), unionParameters(parameters, getCommonParametersOfReports()));
		byte[] content = generateContent(exportType, generator);
		return new DownloadableFile(downloadableFileName, content);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DownloadableFile generate(String reportIdentifier, ExportType exportType, Connection connection, Map<String, Object> parameters, String downloadableFileName) {
		JasperReportGenerator generator = new JasperReportGenerator(baseDirectoryPathOfReports, reportIdentifier, connection, unionParameters(parameters, getCommonParametersOfReports()));
		byte[] content = generateContent(exportType, generator);
		return new DownloadableFile(downloadableFileName, content);
	}
	
	@Override
	public DownloadableFile generate(String reportIdentifier, ExportType exportType, Connection connection, String downloadableFileName) {
		JasperReportGenerator generator = new JasperReportGenerator(baseDirectoryPathOfReports, reportIdentifier, connection,  getCommonParametersOfReports());
		byte[] content = generateContent(exportType, generator);
		return new DownloadableFile(downloadableFileName, content);
	}
	
	public Map<String, Object> getCommonParametersOfReports() {
		return commonParametersOfReports;
	}
	
	public void setCommonParametersOfReports(Map<String, Object> commonParametersOfReports) {
		this.commonParametersOfReports = commonParametersOfReports;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> unionParameters(Map<String, Object>... maps) {
		Map<String, Object> all = new HashMap<>();
		for (Map<String, Object> map : maps) {
			if (map != null) {
				all.putAll(map);				
			}
		}
		return all;
	}
	
	public void setBaseDirectoryPathOfReports(String baseDirectoryPathOfReports) {
		this.baseDirectoryPathOfReports = baseDirectoryPathOfReports;
	}
	
	public void setDocumentTypeTemplateDao(DocumentTypeTemplateDao documentTypeTemplateDao) {
		this.documentTypeTemplateDao = documentTypeTemplateDao;
	}
}
