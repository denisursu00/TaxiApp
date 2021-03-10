package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DOCUMENT")
public class DocumentMetadataDefinition extends MetadataDefinition {
	
	private Long metadataDocumentTypeId;
	private boolean multipleDocumentsSelection;
		
	@Column(name = "METADATA_DOCUMENT_TYPE_ID", nullable = true)
	public Long getMetadataDocumentTypeId() {
		return metadataDocumentTypeId;
	}
	
	public void setMetadataDocumentTypeId(Long metadataDocumentTypeId) {
		this.metadataDocumentTypeId = metadataDocumentTypeId;
	}
	
	@Column(name = "MULTIPLE_DOCUMENTS_SELECTION", nullable = true)
	public boolean isMultipleDocumentsSelection() {
		return multipleDocumentsSelection;
	}
	
	public void setMultipleDocumentsSelection(boolean multipleDocumentsSelection) {
		this.multipleDocumentsSelection = multipleDocumentsSelection;
	}
}
