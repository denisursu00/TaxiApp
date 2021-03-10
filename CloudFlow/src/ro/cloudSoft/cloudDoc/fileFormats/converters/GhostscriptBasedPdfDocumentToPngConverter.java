package ro.cloudSoft.cloudDoc.fileFormats.converters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.common.utils.DependencyInjectionUtils;

import com.google.common.collect.Sets;

public class GhostscriptBasedPdfDocumentToPngConverter implements PdfDocumentToPngConverter, InitializingBean {
	
	private static final Logger LOGGER = Logger.getLogger(GhostscriptBasedPdfDocumentToPngConverter.class.getName());
	
	private static final String SEPARATOR_DECIMAL_PATTERN_FROM_FILE_NAME = "_";
	private static final String DECIMAL_PATTERN = "%04d";
	private static final String PNG_EXTENSION = ".png";
	
	private String commandPlaceholderSourceFilePath;
	private String commandPlaceholderDestinationFilePathExpression;
	private String commandWithPlaceholders;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			commandPlaceholderSourceFilePath,
			commandPlaceholderDestinationFilePathExpression,
			commandWithPlaceholders
		);
	}

	@Override
	public List<String> convertToPng(String sourcePdfDocumentFilePath) {

		String sourcePdfDocumentFileExtension = FilenameUtils.getExtension(sourcePdfDocumentFilePath);
		checkExtension(sourcePdfDocumentFileExtension);
		
		String folderPath = FilenameUtils.getFullPath(sourcePdfDocumentFilePath);
		String sourcePdfDocumentFileName = FilenameUtils.getBaseName(sourcePdfDocumentFilePath);
		
		String destinationPngFileNameExpression = (sourcePdfDocumentFileName + SEPARATOR_DECIMAL_PATTERN_FROM_FILE_NAME + DECIMAL_PATTERN + PNG_EXTENSION);
		String destinationPngFilePathExpression = (folderPath + destinationPngFileNameExpression);
		
		String command = generateCommand(sourcePdfDocumentFilePath, destinationPngFilePathExpression);
		
		try {
			executeCommand(command);
		} catch (RuntimeException re) {

			String exceptionMessage = "A aparut o exceptie in timpul conversiei.";
			
			String[] leftoverPngFileNames = getDestinationPngFileNames(folderPath, sourcePdfDocumentFileName);
			if (ArrayUtils.isNotEmpty(leftoverPngFileNames)) {
				
				exceptionMessage += (" In urma conversiei, au ramas urmatoarele fisiere in folder-ul [" + folderPath + "]: [" + StringUtils.join(leftoverPngFileNames, ", ") + "].");
				
				Set<String> namesOfLeftoverPngFilesThatCannotBeDeleted = cleanupAfterConvertError(folderPath, leftoverPngFileNames);
				if (namesOfLeftoverPngFilesThatCannotBeDeleted.isEmpty()) {
					exceptionMessage += " S-au sters toate fisierele ramase in urma conversiei.";
				} else {
					exceptionMessage += (" Nu s-au putut sterge urmatoarele fisiere ramase: [" + StringUtils.join(namesOfLeftoverPngFilesThatCannotBeDeleted, ", ") + "].");
				}
			}
			
			throw new RuntimeException(exceptionMessage, re);
		}
		
		String[] destinationPngFileNames = getDestinationPngFileNames(folderPath, sourcePdfDocumentFileName);
		Arrays.sort(destinationPngFileNames, String.CASE_INSENSITIVE_ORDER);
		return Arrays.asList(destinationPngFileNames);
	}
	
	private void checkExtension(String extension) {
		String extensionInLowerCase = extension.toLowerCase();
		if (!extensionInLowerCase.equals("pdf")) {
			throw new IllegalArgumentException("Convertorul suporta doar fisiere PDF. Tipul [" + extension + "] nu este suportat.");
		}
	}
	
	private String generateCommand(String sourcePdfDocumentFilePath, String destinationPngFilePathExpression) {
		String temporaryCommand = null;
		temporaryCommand = StringUtils.replace(commandWithPlaceholders, commandPlaceholderSourceFilePath, sourcePdfDocumentFilePath);
		temporaryCommand = StringUtils.replace(temporaryCommand, commandPlaceholderDestinationFilePathExpression, destinationPngFilePathExpression);
		return temporaryCommand;
	}
	
	private void executeCommand(String commandAsString) {
		
		CommandLine command = CommandLine.parse(commandAsString);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream errorOutputStream = new ByteArrayOutputStream();
		
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorOutputStream);
		
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(0);
		executor.setStreamHandler(streamHandler);
		
		try {
			executor.execute(command);
		} catch (ExecuteException ee) {
			
			String output = outputStream.toString();
			String errorOutput = errorOutputStream.toString();
			
			String newLine = System.getProperty("line.separator");
			String exceptionMessage = "Conversia prin comanda [" + commandAsString + "] NU s-a executat cu succes." + newLine +
				"Output-ul este: " + output + newLine +
				"Output-ul (erori) este: " + errorOutput;
			
			throw new RuntimeException(exceptionMessage);
		} catch (IOException ioe) {
			throw new RuntimeException("Exceptie in timpul executiei comenzii [" + commandAsString + "]", ioe);
		}
	}
	
	private String[] getDestinationPngFileNames(String folderPath, final String sourcePdfDocumentFileName) {
		File folder = new File(folderPath);
		String[] destinationPngFileNames = folder.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return (
					name.startsWith(sourcePdfDocumentFileName + SEPARATOR_DECIMAL_PATTERN_FROM_FILE_NAME) &&
					name.endsWith(PNG_EXTENSION)
				);
			}
		});
		return destinationPngFileNames;
	}
	
	/**
	 * "Curata" dupa o eroare de conversie, incercand sa stearga fisierele ramase in urma conversiei (partiale).
	 * 
	 * @return numele fisierelor ramase care nu au putut fi sterse
	 */
	private Set<String> cleanupAfterConvertError(String folderPath, String[] leftoverPngFileNames) {
		
		Set<String> namesOfLeftoverPngFilesThatCannotBeDeleted = Sets.newHashSet();
		
		File folder = new File(folderPath);
		
		for (String leftoverPngFileName : leftoverPngFileNames) {
			File leftoverPngFile = new File(folder, leftoverPngFileName);
			boolean deletedLeftoverPngFile = leftoverPngFile.delete();
			if (!deletedLeftoverPngFile) {
				namesOfLeftoverPngFilesThatCannotBeDeleted.add(leftoverPngFileName);
			}
		}
		
		return namesOfLeftoverPngFilesThatCannotBeDeleted;
	}
	
	@Override
	public List<String> convertToPng(InputStream sourcePdfDocumentContentAsStream, String folderPath) {
		
		File folder = new File(folderPath);
		
		String temporaryPdfDocumentFileName = (RandomStringUtils.randomAlphabetic(8) + ".pdf");
		File temporaryPdfDocumentFile = new File(folder, temporaryPdfDocumentFileName);
		String temporaryPdfDocumentFilePath = temporaryPdfDocumentFile.getPath();
		
		FileOutputStream temporaryPdfDocumentFileOutputStream = null;
		try {
			temporaryPdfDocumentFileOutputStream = new FileOutputStream(temporaryPdfDocumentFile);
		} catch (FileNotFoundException fnfe) {
			String exceptionMessage = "Nu s-a putut crea fisierul temporar " +
				"pentru continutul PDF, avand calea [" + temporaryPdfDocumentFilePath + "].";
			throw new RuntimeException(exceptionMessage, fnfe);
		}

		try {
			
			try {
				IOUtils.copy(sourcePdfDocumentContentAsStream, temporaryPdfDocumentFileOutputStream);
			} catch (IOException ioe) {
				String exceptionMessage = "Nu s-a putut scrie fisierul temporar pentru continutul PDF (" + temporaryPdfDocumentFilePath + ").";
				throw new RuntimeException(exceptionMessage, ioe);
			} finally {
				IOUtils.closeQuietly(temporaryPdfDocumentFileOutputStream);
			}
		
			return convertToPng(temporaryPdfDocumentFilePath);
		} finally {
			boolean deletedTemporaryPdfDocumentFile = temporaryPdfDocumentFile.delete();
			if (!deletedTemporaryPdfDocumentFile) {
				LOGGER.warning("Nu s-a putut sterge fisierul PDF temporar [" + temporaryPdfDocumentFilePath + "].");
			}
		}
	}
	
	public void setCommandPlaceholderSourceFilePath(String commandPlaceholderSourceFilePath) {
		this.commandPlaceholderSourceFilePath = commandPlaceholderSourceFilePath;
	}
	public void setCommandPlaceholderDestinationFilePathExpression(String commandPlaceholderDestinationFilePathExpression) {
		this.commandPlaceholderDestinationFilePathExpression = commandPlaceholderDestinationFilePathExpression;
	}
	public void setCommandWithPlaceholders(String commandWithPlaceholders) {
		this.commandWithPlaceholders = commandWithPlaceholders;
	}
}