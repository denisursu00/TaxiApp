package ro.cloudSoft.cloudDoc.plugins.content;

import org.apache.commons.io.FilenameUtils;

public enum MimeType {

	TEXT_PLAIN("text/plain", new String[] { ".txt" }),
	RICH_TEXT_FORMAT("application/rtf", new String[] { ".rtf" }),
	ADOBE_PORTABLE_DOCUMENT_FORMAT("application/pdf", new String[] { ".pdf"}),
	MICRODOFT_WORD("application/msword", new String[] { ".doc" }),
	MICROSOFT_OFFICE_OOXML_WORD_DOCUMENT("application/vnd.openxmlformats-officedocument.wordprocessingml.document", new String[] { ".docx" }),
	OPEN_DOCUMENT_TEXT("application/vnd.oasis.opendocument.text", new String[] { ".odt" }),
	APPLICATION_XML("application/xml", new String[] { ".xml", ".xsl", ".xsd" }),    
	MICROSOFT_POWERPOINT_PRESENTATION("application/vnd.ms-powerpoint", new String[] { ".ppt", ".ppz", ".pps", ".pot", ".ppa"}),
	OFFICE_OPEN_XML_PRESENTATION("application/vnd.openxmlformats-officedocument.presentationml.presentation", new String[] { ".pptx" }),
	OCTET_STREAM("application/octet-stream", new String[] { ".bin" });
	
	private String mimeType;
	private String[] fileExtensions;
	
	private MimeType(String mimeType, String[] fileExtensions) {
		this.mimeType = mimeType;
		this.fileExtensions = fileExtensions;
	}
	
	public static MimeType getByFileExtension(String fileExtension) {
		for (MimeType mimeType : values()) {
			for (String extension : mimeType.fileExtensions) {
				if (extension.equalsIgnoreCase(fileExtension)) {
					return mimeType;
				}
			}
		}
		return null;
	}
	
	public static MimeType getByFileName(String fileName) {
		String ext = FilenameUtils.getExtension(fileName);
		return getByFileExtension(ext);
	}
	
	public String[] getFileExtensions() {
		return fileExtensions;
	}
	
	public String getMimeType() {
		return mimeType;
	}
}
