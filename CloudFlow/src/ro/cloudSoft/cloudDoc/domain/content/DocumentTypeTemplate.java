package ro.cloudSoft.cloudDoc.domain.content;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DocumentTypeTemplate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long documentTypeId;
	private String name;
	private String description;
	private byte[] data;
	private TemplateType type;
	private String exportAvailabilityExpression;

	public DocumentTypeTemplate() {}

	public DocumentTypeTemplate(String name, String description, byte[] data) {
		this.name = name;
		this.description = description;
		this.data = data;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(nullable = false)
	public Long getDocumentTypeId() {
		return documentTypeId;
	}
	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	@Column(nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Basic(fetch = FetchType.LAZY)
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	public TemplateType getType() {
		return type;
	}
	public void setType(TemplateType type) {
		this.type = type;
	}
	
	@Column(name = "export_availability_expression")
	public String getExportAvailabilityExpression() {
		return exportAvailabilityExpression;
	}
	
	public void setExportAvailabilityExpression(String exportAvailabilityExpression) {
		this.exportAvailabilityExpression = exportAvailabilityExpression;
	}
	
}