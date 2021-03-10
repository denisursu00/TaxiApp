package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="DOCUMENT_VALIDATION_DEFINITION")
public class DocumentValidationDefinition {
	
	private Long id;
	private String conditionExpression;
	private String message;
	private String validationInStates;
	private Integer validationOrder;
	
	private DocumentType documentType;
	private MetadataCollection metadataCollection;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "CONDITION_EXPRESSION")
	public String getConditionExpression() {
		return conditionExpression;
	}
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	
	@Column(name = "MESSAGE")
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Column(name = "VALIDATION_IN_STATES")
	public String getValidationInStates() {
		return validationInStates;
	}
	public void setValidationInStates(String validationInStates) {
		this.validationInStates = validationInStates;
	}
	
	@Column(name = "VALIDATION_ORDER")
	public Integer getValidationOrder() {
		return validationOrder;
	}
	public void setValidationOrder(Integer validationOrder) {
		this.validationOrder = validationOrder;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCUMENT_TYPE_ID")
	public DocumentType getDocumentType() {
		return documentType;
	}	
	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "METADATA_COLLECTION_ID")
	public MetadataCollection getMetadataCollection() {
		return metadataCollection;
	}	
	public void setMetadataCollection(MetadataCollection metadataCollection) {
		this.metadataCollection = metadataCollection;
	}
}
