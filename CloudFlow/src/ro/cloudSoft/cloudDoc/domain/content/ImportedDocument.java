package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="IMPORTED_DOCUMENT")
public class ImportedDocument {
	
	private Long id;
	
	private String documentId;
	private String documentLocationRealName;
	
	private ImportSource importSource;
	
	public static enum ImportSource {
		EXCEL
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="DOCUMENT_ID")
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	
	@Column(name="DOCUMENT_LOCATION_REAL_NAME")
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}

	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	
	@Column(name = "IMPORT_SOURCE")
	@Enumerated(EnumType.STRING)	
	public ImportSource getImportSource() {
		return importSource;
	}
	
	public void setImportSource(ImportSource importSource) {
		this.importSource = importSource;
	}
}
