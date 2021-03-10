package ro.cloudSoft.cloudDoc.domain.prezentaOnline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "prezenta_online_finalizata")
public class PrezentaOnlineFinalizata {

	private Long id;
	private String documentId;
	private String documentLocationRealName;
	private boolean hasImported;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="document_id")
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	@Column(name="document_location_real_name")
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}

	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	
	@Column(name="has_imported")
	public boolean isHasImported() {
		return hasImported;
	}

	public void setHasImported(boolean hasImported) {
		this.hasImported = hasImported;
	}

}
