package ro.cloudSoft.cloudDoc.domain.replacementProfiles;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.google.common.base.Objects;

/**
 * 
 */
@Embeddable
public class ReplacementProfileInstancePk implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String documentLocationRealName;
	private String documentId;
	
	@Column(name = "document_location_real_name", nullable = false, length = 100)
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	
	@Column(name = "document_id", nullable = false, length = 100)
	public String getDocumentId() {
		return documentId;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof ReplacementProfileInstancePk)) {
			return false;
		}
		
		ReplacementProfileInstancePk other = (ReplacementProfileInstancePk) obj;
		
		return (
			Objects.equal(getDocumentLocationRealName(), other.getDocumentLocationRealName()) &&
			Objects.equal(getDocumentId(), other.getDocumentId())
		);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(
			getDocumentLocationRealName(),
			getDocumentId()
		);
	}
	
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
}