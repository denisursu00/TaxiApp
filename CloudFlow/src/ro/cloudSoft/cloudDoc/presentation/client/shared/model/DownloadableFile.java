package ro.cloudSoft.cloudDoc.presentation.client.shared.model;

public class DownloadableFile {

	private String fileName;
	private byte[] content;
	
	public DownloadableFile(String fileName, byte[] content) {
		this.fileName = fileName;
		this.content = content;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public byte[] getContent() {
		return content;
	}
}
