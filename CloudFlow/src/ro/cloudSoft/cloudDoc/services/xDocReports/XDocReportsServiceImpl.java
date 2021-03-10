package ro.cloudSoft.cloudDoc.services.xDocReports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ExportType;
import ro.cloudSoft.cloudDoc.services.GeneralReport;

public class XDocReportsServiceImpl implements XDocReportsService {

	@Override
	public DownloadableFile generate(byte[] templateFileContent, ExportType exportType, Map<String, Object> parameters, String downloadableFileName) {
		XDocReportGenerator generator = new XDocReportGenerator(templateFileContent, parameters);
		byte[] content = generateContent(exportType, generator);
		return new DownloadableFile(downloadableFileName, content);
	}

	private byte[] generateContent(ExportType exportType, XDocReportGenerator generator) {
		if (ExportType.PDF.equals(exportType)) {
			return generator.generateAsPdf();
		} else if (ExportType.DOCX.equals(exportType)) {
			return generator.generateAsDocx();
		} else {
			throw new RuntimeException("Unknown export type [" + exportType + "]");
		}
	}

	@Override
	public DownloadableFile generate(GeneralReport generalReport, ExportType exportType, Map dspExportModelAsMap, String downloadableFileName) {
		File sourceDirectoryOfXDocReports = null;
		try {
			Enumeration<URL> generalReportsResources = this.getClass().getClassLoader().getResources(XDocReportsConstants.SOURCE_PATH_OF_REPORTS_TEMPLATES);
			if (generalReportsResources.hasMoreElements()) {
				URL url = generalReportsResources.nextElement();				
				sourceDirectoryOfXDocReports = new File(url.toURI());
			    if (!sourceDirectoryOfXDocReports.isDirectory()) {
					throw new Exception("Resource [" + sourceDirectoryOfXDocReports.getPath() + "] is not a directory");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sourceDirectoryOfXDocReports == null) {
			throw new RuntimeException("Template reports resources directory is null.");
		}
		File templateDirectory = null;
		
		for( File fileFromSource : sourceDirectoryOfXDocReports.listFiles()) {
			if (fileFromSource.isDirectory() && fileFromSource.getName().equals(generalReport.getSourceDirectoryName())) {
				templateDirectory = fileFromSource;
				break;
			}
		}
		if (templateDirectory == null) {
			throw new RuntimeException("Template directory name: " + generalReport.getSourceDirectoryName() + " was not found.");
		}
		
		File templateFile = new File(templateDirectory, generalReport.getReportIdentifier() + ".docx");
		InputStream is;
		try {
			is = new FileInputStream(templateFile);
			return generate(IOUtils.toByteArray(is), exportType, dspExportModelAsMap, downloadableFileName);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
