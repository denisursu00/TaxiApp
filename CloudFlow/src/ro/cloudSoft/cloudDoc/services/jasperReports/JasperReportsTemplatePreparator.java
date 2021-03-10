package ro.cloudSoft.cloudDoc.services.jasperReports;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import ro.cloudSoft.cloudDoc.dao.content.DocumentTypeTemplateDao;
import ro.cloudSoft.cloudDoc.domain.content.DocumentTypeTemplate;
import ro.cloudSoft.cloudDoc.services.GeneralReport;
import ro.cloudSoft.common.utils.ZipUtils;

public class JasperReportsTemplatePreparator {
	
	private static String[] EXCLUDED_NAMES_OF_FILE_OR_FOLDER_TO_COPY_INTO_REPORT = new String[] { 
			"TestData.json", "TestDataAdapter.xml", ".gitignore" 
	};
	
	private final String baseDirectoryPathOfReports;
	private File baseDirectoryOfReports;
	private final DocumentTypeTemplateDao documentTypeTemplateDao;
	
	private File sourceDirectoryOfCommonReportsResoures;
	private File sourceDirectoryOfGeneralReports;
	
	public JasperReportsTemplatePreparator(String baseDirectoryPathOfReports, DocumentTypeTemplateDao documentTypeTemplateDao) {
		this.baseDirectoryPathOfReports = baseDirectoryPathOfReports;
		this.documentTypeTemplateDao = documentTypeTemplateDao;
	}
	
	public void prepare() {
		
		initContext();
		
		try {
			FileUtils.cleanDirectory(baseDirectoryOfReports);
		} catch (IOException e) {
			throw new RuntimeException("Error while cleaning directory of reports [" + this.baseDirectoryOfReports.getAbsolutePath() + "]", e);
		}
		
		prepareDocumentTypeTemplateReports();
		prepareGeneralReports();
		
		compileReports();
	}
	
	private void initContext() {
		
		baseDirectoryOfReports = new File(baseDirectoryPathOfReports);
		if (!baseDirectoryOfReports.exists()) {
			boolean created = baseDirectoryOfReports.mkdir();
			if (!created) {
				throw new RuntimeException("Cannot created base directory of reports [" + baseDirectoryPathOfReports + "]");
			}
		}
		
		try {
			Enumeration<URL> commonReportsResources = this.getClass().getClassLoader().getResources(JasperReportsConstants.SOURCE_PATH_OF_COMMON_REPORTS_RESOURCES);
			if (commonReportsResources.hasMoreElements()) {
				URL url = commonReportsResources.nextElement();				
				sourceDirectoryOfCommonReportsResoures = new File(url.toURI());
				if (!sourceDirectoryOfCommonReportsResoures.isDirectory()) {
					throw new Exception("Resource [" + JasperReportsConstants.SOURCE_PATH_OF_COMMON_REPORTS_RESOURCES + "] is not a directory");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception while preparing common reports resources directory.", e);
		}		
		if (sourceDirectoryOfCommonReportsResoures == null) {
			throw new RuntimeException("Common reports resources directory is null.");
		}
		
		try {
			Enumeration<URL> generalReportsResources = this.getClass().getClassLoader().getResources(JasperReportsConstants.SOURCE_PATH_OF_GENERAL_REPORTS);
			if (generalReportsResources.hasMoreElements()) {
				URL url = generalReportsResources.nextElement();				
			    sourceDirectoryOfGeneralReports = new File(url.toURI());
			    if (!sourceDirectoryOfGeneralReports.isDirectory()) {
					throw new Exception("Resource [" + JasperReportsConstants.SOURCE_PATH_OF_GENERAL_REPORTS + "] is not a directory");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sourceDirectoryOfGeneralReports == null) {
			throw new RuntimeException("General reports resources directory is null.");
		}
	}
	
	@SuppressWarnings("deprecation")
	private void prepareDocumentTypeTemplateReports() {		
		List<Closeable> closableStreams = new LinkedList<>();
		try {
			List<DocumentTypeTemplate> templates = documentTypeTemplateDao.getAllJasperTemplates();
			for (DocumentTypeTemplate template : templates) {
				
				InputStream reportInputStream = new ByteArrayInputStream(template.getData());
				closableStreams.add(reportInputStream);
				
				String reportIdentifier = JasperReportsConstants.PREFIX_OF_REPORT_IDENTIFIER_FOR_DOCUMENT_TYPE_TEMPLATE + template.getId();
				File outputDirectory = new File(baseDirectoryOfReports, reportIdentifier);
				ZipUtils.unzip(reportInputStream, outputDirectory);
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception while preparing document type template reports.", e);
		} finally {
			for (Closeable closeableStream : closableStreams) {
				IOUtils.closeQuietly(closeableStream);
			}
		}
	}
	
	private void prepareGeneralReports() {
		
		for (GeneralReport generalReport : GeneralReport.values()) {
			
			File sourceDirectoryOfReport = new File(sourceDirectoryOfGeneralReports, generalReport.getSourceDirectoryName());
			if (!sourceDirectoryOfReport.exists()) {
				throw new RuntimeException("Source diretory of report does not exist [" + sourceDirectoryOfReport.getAbsolutePath() + "]");
			}
			if (!sourceDirectoryOfReport.isDirectory()) {
				throw new RuntimeException("Source directory of report [" + sourceDirectoryOfReport.getAbsolutePath() + "] is not a directory");
			}
			
			File reportDirectory = new File(this.baseDirectoryOfReports, generalReport.getReportIdentifier());
			boolean created = reportDirectory.mkdir();
			if (!created) {
				throw new RuntimeException("Report directory [" + reportDirectory.getAbsolutePath() + "] cannot be created.");
			}
			
			try {
				FileUtils.copyDirectory(sourceDirectoryOfCommonReportsResoures, reportDirectory, new CopyReportSourcesFileFilter());
			} catch (IOException e) {
				throw new RuntimeException("Error while copying common reports resources to report directory [" + reportDirectory.getAbsolutePath() + "]");
			}
			
			try {
				FileUtils.copyDirectory(sourceDirectoryOfReport, reportDirectory, new CopyReportSourcesFileFilter());
			} catch (IOException e) {
				throw new RuntimeException("Error while copying source report to report directory [" + reportDirectory.getAbsolutePath() + "]");
			}
		}
	}
	
	private void compileReports() {
		File[] reportDirectories = baseDirectoryOfReports.listFiles(new DirectoryFileFilter());
		for (File reportDirectory : reportDirectories) {
			compileReport(reportDirectory);
		}	
	}
	
	@SuppressWarnings("deprecation")
	private void compileReport(File reportDirectory) {
		List<Closeable> closableStreams = new LinkedList<>();
		try {			
			File[] jrxmlFiles = reportDirectory.listFiles(new JrxmlFileFilter());
			for (File jrxmlFile : jrxmlFiles) {				
				
				InputStream jrxmlInputStream = new FileInputStream(jrxmlFile);				
				closableStreams.add(jrxmlInputStream);
				
				String jasperFileName = FilenameUtils.getBaseName(jrxmlFile.getName()) + "." + JasperReportsConstants.JASPER_FILE_EXTENSION;
				File jasperFile = new File(reportDirectory, jasperFileName);
				
				JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
				JRSaver.saveObject(jasperReport, jasperFile);
			}			
			File[] childReportDirectories = reportDirectory.listFiles(new DirectoryFileFilter());
			for (File childReportDirectory : childReportDirectories) {
				compileReport(childReportDirectory);
			}			
		} catch (Exception e) {
			throw new RuntimeException("Error while compiling reports from directory [" + reportDirectory.getAbsolutePath() + "]", e);
		} finally {
			for (Closeable closableStream : closableStreams) {
				IOUtils.closeQuietly(closableStream);
			}
		}
	}
	
	private static class JrxmlFileFilter implements FileFilter {

		@Override
		public boolean accept(File file) {
			String extension = FilenameUtils.getExtension(file.getName());
			return file.isFile() && extension.equalsIgnoreCase(JasperReportsConstants.JRXML_FILE_EXTENSION);
		}
	}
	
	private static class DirectoryFileFilter implements FileFilter {

		@Override
		public boolean accept(File file) {
			return file.isDirectory();
		}
		
	}
	
	private static class CopyReportSourcesFileFilter implements FileFilter {

		@Override
		public boolean accept(File file) {
			for (String excludedName : EXCLUDED_NAMES_OF_FILE_OR_FOLDER_TO_COPY_INTO_REPORT) {
				String fileName = FilenameUtils.getName(file.getName());
				if (excludedName.equals(fileName)) {
					return false;
				}
			}
			return true;
		}
		
	}
}
