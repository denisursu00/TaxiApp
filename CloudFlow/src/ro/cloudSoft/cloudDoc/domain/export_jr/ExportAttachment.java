package ro.cloudSoft.cloudDoc.domain.export_jr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "export_attachment")
public class ExportAttachment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="export_document_id")
	private ExportDocument exportDocument;
	@Column(name = "name")
	private String name;
	@Column(name = "content")
	private byte[] content;
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private EXPORT_ATTACHMENT_TYPE type;
	
	public ExportAttachment(ExportDocument exportDocument, String name, byte[] content, EXPORT_ATTACHMENT_TYPE type) {
		super();
		this.exportDocument = exportDocument;
		this.name = name;
		this.content = content;
		this.type = type;
	}

	public ExportAttachment() {
		super();
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

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public EXPORT_ATTACHMENT_TYPE getType() {
		return type;
	}

	public void setType(EXPORT_ATTACHMENT_TYPE type) {
		this.type = type;
	}

	public enum EXPORT_ATTACHMENT_TYPE {
		XDOC_TEMPPLATE, ATTACHMENT, METADATA
	}

}
