package ro.cloudSoft.cloudDoc.services.jasperReports;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class JasperReportGenerator {
		
	private final String baseDirectoryPathOfReports;
	private final String reportIdentifier;
	private final Map<String, Object> parameters;
	private final JRDataSource dataSource;
	private final Connection connection;
	
	private Map<String, Object> allParameters;
	private File reportDirectory;
	private List<InputStream> streamsWillBeClose = new LinkedList<InputStream>();
	
	public JasperReportGenerator(String baseDirectoryPathOfReports, String reportIdentifier, JRDataSource dataSource) {
		this(baseDirectoryPathOfReports, reportIdentifier, dataSource, null);
	}
	
	public JasperReportGenerator(String baseDirectoryPathOfReports, String reportIdentifier, Connection connection) {
		this(baseDirectoryPathOfReports, reportIdentifier, connection, null);
	}
	
	public JasperReportGenerator(String baseDirectoryPathOfReports, String reportIdentifier, JRDataSource dataSource, Map<String, Object> parameters) {
		this.baseDirectoryPathOfReports = baseDirectoryPathOfReports;
		this.reportIdentifier = reportIdentifier;
		this.dataSource = dataSource;
		this.connection = null;
		if (parameters == null) {
			parameters = new HashMap<>();
		}
		this.parameters = parameters;
	}
	
	public JasperReportGenerator(String baseDirectoryPathOfReports, String reportIdentifier, Connection connection, Map<String, Object> parameters) {
		this.baseDirectoryPathOfReports = baseDirectoryPathOfReports;
		this.reportIdentifier = reportIdentifier;
		this.connection = connection;
		this.dataSource = null;
		if (parameters == null) {
			parameters = new HashMap<>();
		}
		this.parameters = parameters;
	}
	
	private JasperPrint generateJasperPrint() {
		try {
			setupContext();
			
			prepareImages();
			prepareStyles();
			prepareSubreports();
			
			File[] jasperFiles = reportDirectory.listFiles(new ReportJasperFileFilter());
			if (jasperFiles.length == 0) {
				throw new RuntimeException("No jasper file found in the directory [" + reportDirectory.getAbsolutePath() + "]");
			}
			if (jasperFiles.length > 1) {
				throw new RuntimeException("Too many jasper files found in the directory [" + reportDirectory.getAbsolutePath() + "] while one is expected");
			}
			
			JasperReport report = null;
			try {
				report = (JasperReport) JRLoader.loadObject(jasperFiles[0]);
			} catch (JRException e) {
				throw new RuntimeException("Error while loading report.", e);
			}
			
			
			try {
				JasperPrint reportGenerationResult = null;
				if (dataSource != null) {
					reportGenerationResult = JasperFillManager.fillReport(report, allParameters, dataSource);					
				} else if (connection != null) {
					reportGenerationResult = JasperFillManager.fillReport(report, allParameters, connection);
				} else {
					throw new IllegalArgumentException("DataSource or Connection is null.");
				}
				return reportGenerationResult;
			} catch (JRException jre) {
				throw new RuntimeException("Error while filling report [" + jre.getMessage() + "]", jre);
			}		
		} finally {
			destroyContext();
		}
	}
	
	private void setupContext() {
		
		allParameters = new HashMap<>();
		allParameters.putAll(parameters);
		
		File baseReportsDirectory = new File(baseDirectoryPathOfReports);
		checkRequiredFileAsDirectory(baseReportsDirectory);
		
		reportDirectory = new File(baseReportsDirectory, this.reportIdentifier);
		checkRequiredFileAsDirectory(reportDirectory);
	}
	
	@SuppressWarnings("deprecation")
	private void destroyContext() {
		for (InputStream is : streamsWillBeClose) {
			IOUtils.closeQuietly(is);
		}
	}

	private void checkRequiredFileAsDirectory(File fileAsDirectory) {
		if (!fileAsDirectory.exists()) {
			throw new RuntimeException("Directory not found [" + fileAsDirectory.getAbsolutePath() + "]");
		}
		if (!fileAsDirectory.isDirectory()) {
			throw new RuntimeException("Directory not found [" + fileAsDirectory.getAbsolutePath() + "], this is a file.");
		}
	}
	
	private void prepareSubreports() {		
		File[] jasperFiles = reportDirectory.listFiles(new SubreportJasperFileFilter());
		for (File jasperFile : jasperFiles) {
			String subreportParamName = FilenameUtils.getBaseName(jasperFile.getName());
			JasperReport subreport;
			try {
				subreport = (JasperReport) JRLoader.loadObject(jasperFile);
			} catch (JRException e) {
				throw new RuntimeException("Error while loading subreport.", e);
			}
			allParameters.put(subreportParamName, subreport);
		}
	}
	
	private void prepareStyles() {
		File[] styleFiles = reportDirectory.listFiles(new JrtxFileFilter());
		for (File styleFile : styleFiles) {
			String styleParamName = FilenameUtils.getBaseName(styleFile.getName());
			JRTemplate styleTemplate = JRXmlTemplateLoader.load(styleFile);
			allParameters.put(styleParamName, styleTemplate);
		}
	}
	
	private void prepareImages() {
		File[] imageFiles = reportDirectory.listFiles(new ReportImagesFileFilter());
		for (File imageFile : imageFiles) {
			String imageParamName = FilenameUtils.getBaseName(imageFile.getName());
			FileInputStream imageAsStream;
			try {
				imageAsStream = new FileInputStream(imageFile);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Error while loading image.", e);
			}
			allParameters.put(imageParamName, imageAsStream);
			streamsWillBeClose.add(imageAsStream);
		}
	}
	
	public byte[] generateAsPdf() {
		try {
			JasperPrint jasperPrint = generateJasperPrint();
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException jre) {
			throw new RuntimeException(jre.getMessage(), jre);
		}
	}
	
	public byte[] generateAsDocx() {
		
		JasperPrint jasperPrint = generateJasperPrint();
		
		JRDocxExporter exporter = new JRDocxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		
		SimpleDocxReportConfiguration configuration = new SimpleDocxReportConfiguration();
		exporter.setConfiguration(configuration);
		
		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new RuntimeException("Error while generating report as DOCX", e);
		}
		
		return byteArrayOutputStream.toByteArray();
	}
	
	private static class ReportJasperFileFilter implements FileFilter {
		
		@Override
		public boolean accept(File theFile) {
			String fileName = FilenameUtils.getBaseName(theFile.getName());
			String extension = FilenameUtils.getExtension(theFile.getName());
			return (extension.equalsIgnoreCase(JasperReportsConstants.JASPER_FILE_EXTENSION) 
					&& fileName.endsWith(JasperReportsConstants.SUFFIX_OF_REPORT_JASPER_FILENAME));
		}
	}

	private static class SubreportJasperFileFilter implements FileFilter {
		
		@Override
		public boolean accept(File theFile) {
			String fileName = FilenameUtils.getBaseName(theFile.getName());
			String extension = FilenameUtils.getExtension(theFile.getName());
			return (extension.equalsIgnoreCase(JasperReportsConstants.JASPER_FILE_EXTENSION) 
					&& fileName.endsWith(JasperReportsConstants.SUFFIX_OF_SUBREPORT_JASPER_FILENAME));
		}
	}
	
	private static class JrtxFileFilter implements FileFilter {
		
		@Override
		public boolean accept(File theFile) {
			String extension = FilenameUtils.getExtension(theFile.getName());
			return extension.equalsIgnoreCase(JasperReportsConstants.JRTX_FILE_EXTENSION);
		}
	}
	
	private static class ReportImagesFileFilter implements FileFilter {
		
		@Override
		public boolean accept(File theFile) {
			String extension = FilenameUtils.getExtension(theFile.getName());
			for (String supportedExtension : JasperReportsConstants.SUPPORTED_IMAGE_FILE_EXTENSIONS) {
				if (supportedExtension.equalsIgnoreCase(extension)) {
					return true;
				}
			}
			return false;
		}
	}
}
