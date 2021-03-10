package ro.cloudSoft.cloudDoc.domain.export_jr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "export_metadata")
public class ExportMetadata {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne()
	@JoinColumn(name = "export_document_id")
	private ExportDocument exportDocument;
	@Column(name = "name")
	private String name;
	@Column(name = "value")
	private String value;
	
	public ExportMetadata() {
		super();
	}

	public ExportMetadata(ExportDocument exportDocument, String name, String value) {
		super();
		this.exportDocument = exportDocument;
		this.name = name;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ExportDocument getExportDocument() {
		return exportDocument;
	}

	public void setExportDocument(ExportDocument exportDocument) {
		this.exportDocument = exportDocument;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
