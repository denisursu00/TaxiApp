package ro.cloudSoft.cloudDoc.presentation.client.shared.model.views;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;

import com.extjs.gxt.ui.client.data.BaseModel;

public class DocumentViewModel extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	public static final String PREFIX_REPRESENTATIVE_METADATA_PROPERTY_NAME = "prefix";

	public static final String PROPERTY_DOCUMENT_ID = "documentId";
	public static final String PROPERTY_DOCUMENT_NAME = "documentName";
	public static final String PROPERTY_DOCUMENT_TYPE_NAME = "documentTypeName";
	public static final String PROPERTY_DOCUMENT_AUTHOR_NAME = "documentAuthorName";
	public static final String PROPERTY_DOCUMENT_CREATED_DATE = "documentCreatedDate";
	public static final String PROPERTY_DOCUMENT_LAST_MODIFIED_DATE = "documentLastModifiedDate";
	public static final String PROPERTY_DOCUMENT_LOCKED_BY_NAME = "documentLockedByName";
	public static final String PROPERTY_DOCUMENT_REPRESENTATIVE_METADATA_LABELS = "documentRepresentativeMetadataLabels";
	public static final String PROPERTY_DOCUMENT_METADATA ="documentMetadata";
	public static final String PROPERTY_DOCUMENT_STATUS ="documentStatus";
	
	/**
	 * Verifica daca documentul este blocat.
	 */
	public boolean isLocked() {
		return GwtValidateUtils.isCompleted(this.getDocumentLockedByName());
	}
	
	/**
	 * Seteaza valoarea unei metadate.
	 * @param name numele metadatei
	 * @param value valoarea metadatei
	 */
	public void setMetadataValue(String name, String value) {
		this.set(PREFIX_REPRESENTATIVE_METADATA_PROPERTY_NAME + name, value);
	}
	
	public String getDocumentId() {
		return this.get(PROPERTY_DOCUMENT_ID);
	}
	public void setDocumentId(String documentId) {
		this.set(PROPERTY_DOCUMENT_ID, documentId);
	}
	public String getDocumentName() {
		return this.get(PROPERTY_DOCUMENT_NAME);
	}
	public void setDocumentName(String documentName) {
		this.set(PROPERTY_DOCUMENT_NAME, documentName);
	}
	public String getDocumentTypeName() {
		return this.get(PROPERTY_DOCUMENT_TYPE_NAME);
	}
	public void setDocumentTypeName(String documentTypeName) {
		this.set(PROPERTY_DOCUMENT_TYPE_NAME, documentTypeName);
	}
	public String getDocumentAuthorName() {
		return this.get(PROPERTY_DOCUMENT_AUTHOR_NAME);
	}
	public void setDocumentAuthorName(String documentAuthorName) {
		this.set(PROPERTY_DOCUMENT_AUTHOR_NAME, documentAuthorName);
	}
	public Date getDocumentCreatedDate() {
		return this.get(PROPERTY_DOCUMENT_CREATED_DATE);
	}
	public void setDocumentCreatedDate(Date documentCreatedDate) {
		this.set(PROPERTY_DOCUMENT_CREATED_DATE, documentCreatedDate);
	}
	public Date getDocumentLastModifiedDate() {
		return this.get(PROPERTY_DOCUMENT_LAST_MODIFIED_DATE);
	}
	public void setDocumentLastModifiedDate(Date documentLastModifiedDate) {
		this.set(PROPERTY_DOCUMENT_LAST_MODIFIED_DATE, documentLastModifiedDate);
	}
	public String getDocumentLockedByName() {
		return this.get(PROPERTY_DOCUMENT_LOCKED_BY_NAME);
	}
	public void setDocumentLockedByName(String documentLockedByName) {
		this.set(PROPERTY_DOCUMENT_LOCKED_BY_NAME, documentLockedByName);
	}
	public List<String> getDocumentRepresentativeMetadataLabels() {
		return this.get(PROPERTY_DOCUMENT_REPRESENTATIVE_METADATA_LABELS);
	}
	public void setDocumentRepresentativeMetadataLabels(List<String> documentRepresentativeMetadataLabels) {
		this.set(PROPERTY_DOCUMENT_REPRESENTATIVE_METADATA_LABELS, documentRepresentativeMetadataLabels);
	}
	public List<DocumentMetadataViewModel> getDocumentMetadata() {
		return this.get(PROPERTY_DOCUMENT_METADATA);
	}
	public void setDocumentMetadata(List<DocumentMetadataViewModel> documentRepresentativeMetadataLabels) {
		this.set(PROPERTY_DOCUMENT_METADATA, documentRepresentativeMetadataLabels);
	}
	public String getDocumentStatus() {
		return this.get(PROPERTY_DOCUMENT_STATUS);
	}
	public void setDocumentStatus(String documentStatus) {
		this.set(PROPERTY_DOCUMENT_STATUS, documentStatus);
	}
}
