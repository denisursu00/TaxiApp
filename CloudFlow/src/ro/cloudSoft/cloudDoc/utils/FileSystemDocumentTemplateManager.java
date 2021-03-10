package ro.cloudSoft.cloudDoc.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTemplateModel;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class FileSystemDocumentTemplateManager implements InitializingBean {

	private String temporaryFilesFolderPath;

	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			temporaryFilesFolderPath
		);
	}
	
	private String getTemporaryFilesFolderPathForUser(String username) {
		return this.temporaryFilesFolderPath + File.separator + username;
	}
	
	public void setTemporaryFilesFolderPath(String temporaryFilesFolderPath) {
		this.temporaryFilesFolderPath = temporaryFilesFolderPath;
	}

	public void remove(List<String> namesForTemplatesToDelete, String username) {
		for (String templateName : namesForTemplatesToDelete) {
			File file = new File(getTemporaryFilesFolderPathForUser(username) + File.separator + templateName);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	public List<DocumentTemplateModel> getAll(String username) {
		List<DocumentTemplateModel> templates = new ArrayList<DocumentTemplateModel>();
		try {		
			File userFolder = new File(getTemporaryFilesFolderPathForUser(username));
			if (userFolder.exists()) {
				File[] userTemplateFiles = userFolder.listFiles();
				for (File userTemplateFile : userTemplateFiles) {
					DocumentTemplateModel template = new DocumentTemplateModel(userTemplateFile.getName(), 
							FileUtils.readFileToByteArray(new File(getTemporaryFilesFolderPathForUser(username) + File.separator + userTemplateFile.getName())));
					templates.add(template);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return templates;
	}

	public void clear(String username) throws IOException {
		File userFolder = new File(getTemporaryFilesFolderPathForUser(username));
		if (userFolder.exists()) {
			FileUtils.deleteDirectory(new File(getTemporaryFilesFolderPathForUser(username)));
		}
	}

	public void add(DocumentTemplateModel template, String username) throws IOException {
		File userFolder = new File(getTemporaryFilesFolderPathForUser(username));
		if (!userFolder.exists()) {
			userFolder.mkdir();
		}
		FileUtils.writeByteArrayToFile(new File(userFolder.getAbsolutePath() + File.separator + template.getName()), template.getData());
	}
	
	public byte[] getTemplateAsByteArray(String templateName, String username) {
		File userFolder = new File(getTemporaryFilesFolderPathForUser(username));
		if (userFolder.exists()) {
			byte[] data = null;
			try {
				data = FileUtils.readFileToByteArray(new File(userFolder + File.separator + templateName));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return data;
		}
		return null;
	}
}
