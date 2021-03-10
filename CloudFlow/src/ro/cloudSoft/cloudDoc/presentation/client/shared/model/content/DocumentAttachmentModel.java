package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

public class DocumentAttachmentModel {

	private String name;
	private boolean isNew;
	private String documentWorkflowStateCode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getIsNew() {
		return isNew;
	}
	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}
	public String getDocumentWorkflowStateCode() {
		return documentWorkflowStateCode;
	}
	public void setDocumentWorkflowStateCode(String documentWorkflowStateCode) {
		this.documentWorkflowStateCode = documentWorkflowStateCode;
	}
}
