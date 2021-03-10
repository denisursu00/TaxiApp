package ro.cloudSoft.cloudDoc.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;

import ro.cloudSoft.cloudDoc.domain.content.Attachment;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;

public class FileSystemAttachmentManager implements InitializingBean {

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

	public void remove(List<String> namesForAttachmentsToDelete, String username) {
		for (String attachmentName : namesForAttachmentsToDelete) {
			File file = new File(getTemporaryFilesFolderPathForUser(username) + File.separator + attachmentName);
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	public void remove(String attachmentName, String username) {
		List<String> attachments = new ArrayList<>();
		attachments.add(attachmentName);
		remove(attachments, username);
	}
	

	public List<Attachment> getAll(String username) {
		List<Attachment> attachments = new ArrayList<Attachment>();
		try {		
			File userFolder = new File(getTemporaryFilesFolderPathForUser(username));
			if (userFolder.exists()) {
				File[] userAttachments = userFolder.listFiles();
				for (File userAttachment : userAttachments) {
					Attachment attachment = new Attachment(userAttachment.getName(), FileUtils.readFileToByteArray(new File(getTemporaryFilesFolderPathForUser(username) + File.separator + userAttachment.getName())));
					attachments.add(attachment);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return attachments;
	}

	public void clear(String username) throws IOException {
		File userFolder = new File(getTemporaryFilesFolderPathForUser(username));
		if (userFolder.exists()) {
			FileUtils.deleteDirectory(new File(getTemporaryFilesFolderPathForUser(username)));
		}
	}

	public void add(Attachment attachment, String username) throws IOException {
		File userFolder = new File(getTemporaryFilesFolderPathForUser(username));
		if (!userFolder.exists()) {
			userFolder.mkdir();
		}
		FileUtils.writeByteArrayToFile(new File(userFolder.getAbsolutePath() + File.separator + attachment.getName()), attachment.getData());
	}
	
	public byte[] getAttachmentAsByteArray(String attachmentName, String username) throws IOException {
		return FileUtils.readFileToByteArray(new File(getTemporaryFilesFolderPathForUser(username) + File.separator + attachmentName));
	}
	
	public Attachment getAttachment(String attachmentName, String username) {
		try {
			return new Attachment(attachmentName, getAttachmentAsByteArray(attachmentName, username));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public DownloadableFile getAttachmentAsDownloadableFile(String attachmentName, String username) {
		try {
			return new DownloadableFile(attachmentName, getAttachmentAsByteArray(attachmentName, username));	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
