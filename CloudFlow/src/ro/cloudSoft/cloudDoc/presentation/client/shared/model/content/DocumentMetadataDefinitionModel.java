package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentMetadataDefinitionModel extends MetadataDefinitionModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	private Long metadataDocumentTypeId;
	private boolean multipleDocumentsSelection;
	
	public Long getMetadataDocumentTypeId() {
		return metadataDocumentTypeId;
	}
	
	public void setMetadataDocumentTypeId(Long metadataDocumentTypeId) {
		this.metadataDocumentTypeId = metadataDocumentTypeId;
	}
	
	public boolean isMultipleDocumentsSelection() {
		return multipleDocumentsSelection;
	}
	
	public void setMultipleDocumentsSelection(boolean multipleDocumentsSelection) {
		this.multipleDocumentsSelection = multipleDocumentsSelection;
	}
}