package ro.cloudSoft.cloudDoc.domain.export_jr;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "export_document")
public class ExportDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "jr_id")
	private String jrId;
	@Column(name = "jr_document_location_real_name")
	private String jrDocumentLocationRealName;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "type")
	private String type;
	@Column(name = "archived")
	private Boolean archived;
	@Column(name = "jr_deleted")
	private Boolean jrDeleted;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "exportDocument", fetch=FetchType.LAZY)
	private List<ExportMetadata> metadatas;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "exportDocument", fetch=FetchType.LAZY)
	private List<ExportAttachment> attachments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJrId() {
		return jrId;
	}

	public void setJrId(String jrId) {
		this.jrId = jrId;
	}
	
	public String getJrDocumentLocationRealName() {
		return jrDocumentLocationRealName;
	}
	
	public void setJrDocumentLocationRealName(String jrDocumentLocationRealName) {
		this.jrDocumentLocationRealName = jrDocumentLocationRealName;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
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

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public List<ExportMetadata> getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(List<ExportMetadata> metadatas) {
		this.metadatas = metadatas;
	}

	public List<ExportAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ExportAttachment> attachments) {
		this.attachments = attachments;
	}
	
	public Boolean getJrDeleted() {
		return jrDeleted;
	}
	
	public void setJrDeleted(Boolean jrDeleted) {
		this.jrDeleted = jrDeleted;
	}
}
