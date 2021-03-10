package ro.cloudSoft.cloudDoc.scheduledTasks;

import java.io.File;

import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.io.MinimumAgeFileFilter;

public class DeleteLeftoverTemporaryFilesScheduledTask implements InitializingBean {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(DeleteLeftoverTemporaryFilesScheduledTask.class);

	private String temporaryFilesFolderPath;
	private int minimumFileAgeInMinutes;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			temporaryFilesFolderPath,
			minimumFileAgeInMinutes
		);
	}
	
	public void deleteLeftoverFiles() {
		
		LOGGER.info("A inceput task-ul automat pentru stergerea fisierelor temporare ramase.", "stergerea fisierelor temporare ramase");
		
		File temporaryFilesFolder = new File(temporaryFilesFolderPath);
		if (temporaryFilesFolder.exists()) {
			File[] leftoverTemporaryFiles = temporaryFilesFolder.listFiles(new MinimumAgeFileFilter(minimumFileAgeInMinutes));
			for (File leftoverTemporaryFile : leftoverTemporaryFiles) {
				
				String leftoverTemporaryFileName = leftoverTemporaryFile.getName();

				String leftoverTemporaryFileFoundLogMessage = "S-a gasit fisierul temporar ramas (in directorul [" + temporaryFilesFolderPath + "]) " +
					"cu numele [" + leftoverTemporaryFileName + "]. Se va incerca stergerea fisierului.";
				LOGGER.info(leftoverTemporaryFileFoundLogMessage, "stergerea fisierelor temporare ramase");
				
				if (leftoverTemporaryFile.delete()) {
					String logMessage = "S-a sters fisierul temporar ramas (in directorul " +
						"[" + temporaryFilesFolderPath + "]) cu numele [" + leftoverTemporaryFileName + "].";
					LOGGER.info(logMessage, "stergerea fisierelor temporare ramase");
				} else {
					String logMessage = "NU s-a putut sterge fisierul temporar ramas (in directorul " +
						"[" + temporaryFilesFolderPath + "]) cu numele [" + leftoverTemporaryFileName + "].";
					LOGGER.warn(logMessage, "stergerea fisierelor temporare ramase");
				}
			}
		} else {
			LOGGER.warn("NU exista folder-ul ce tine fisierele temporare ramase (in calea [" + temporaryFilesFolderPath + "]).", "stergerea fisierelor temporare ramase");
		}
		
		LOGGER.info("S-a finalizat task-ul automat pentru stergerea fisierelor temporare ramase.", "stergerea fisierelor temporare ramase");
	}
	
	public void setTemporaryFilesFolderPath(String temporaryFilesFolderPath) {
		this.temporaryFilesFolderPath = temporaryFilesFolderPath;
	}
	public void setMinimumFileAgeInMinutes(int minimumFileAgeInMinutes) {
		this.minimumFileAgeInMinutes = minimumFileAgeInMinutes;
	}
}