package ro.cloudSoft.cloudDoc.webServices.client.wgvg;

/**
 * 
 */
public class WorkflowGraphView {

	private String mimeType;
	private byte[] content;
	
	public WorkflowGraphView() {}
	
	public WorkflowGraphView(String mimeType, byte[] content) {
		this.mimeType = mimeType;
		this.content = content;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	public byte[] getContent() {
		return content;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
}