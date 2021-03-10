package ro.cloudSoft.cloudDoc.domain.bpm.graph;

/**
 * 
 */
public class WorkflowGraphViewRepresentation {

	private final String mimeType;
	private final byte[] content;
	
	public WorkflowGraphViewRepresentation(String mimeType, byte[] content) {
		this.mimeType = mimeType;
		this.content = content;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	public byte[] getContent() {
		return content;
	}
}