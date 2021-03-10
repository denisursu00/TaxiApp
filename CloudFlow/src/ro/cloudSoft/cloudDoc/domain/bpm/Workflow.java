package ro.cloudSoft.cloudDoc.domain.bpm;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import ro.cloudSoft.cloudDoc.domain.content.DocumentType;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;

/**
 * Entity implementation class for Entity: Workflow
 * 
 * 
 */
@Entity
@Table(
	name = "workflow",
	uniqueConstraints = @UniqueConstraint(columnNames = { "name", "version_number" })
)
public class Workflow implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	public static final int LENGTH_NAME = 128;
	public static final int LENGTH_DESCRIPTION = 1024;

	private Long id;
	
	/**
	 * id-ul de ProcessDefinition din JBPM
	 */
	private String processDefinitionId; 
	
	private String name;
	private String description;
	
	private Workflow baseVersion;
	private Workflow sourceVersion;
	
	private Integer versionNumber;

	/**
	 * Tipul/tipurile de documente asociate fluxului – administratorul
	 * aplicatiei va putea alege unul sau mai multe tipuri de documente,
	 * definite in prealabil, ce vor urma pasii definiti in prezentul flux.
	 */
	private Set<DocumentType> documentTypes;
	/**
	 *lista in care se vor putea adauga utilizatori sau grupuri de utilizatori
	 * ce vor avea drept doar de vizualizare asupra tuturor documentelor lansate
	 * pe acest flux, chiar daca documentul nu a trecut pe la acestia in scopul
	 * aprobarii, avizarii, etc.
	 */
	private Set<OrganizationEntity> supervisors;

	/**
	 * Lista de tranzitii a fluxului.
	 */
	private Set<WorkflowTransition> transitions;

	public Workflow()
	{
		super();
	}
	
	@Transient
	public boolean isNew() {
		return (getId() == null);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@Column(length = LENGTH_NAME, unique=true)
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(length = LENGTH_DESCRIPTION)
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "base_version_id", nullable = true, referencedColumnName = "id")
	public Workflow getBaseVersion() {
		return baseVersion;
	}

	public void setBaseVersion(Workflow baseVersion) {
		this.baseVersion = baseVersion;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "source_version_id", nullable = true, referencedColumnName = "id")
	public Workflow getSourceVersion() {
		return sourceVersion;
	}
	
	public void setSourceVersion(Workflow sourceVersion) {
		this.sourceVersion = sourceVersion;
	}
	
	@Column(name = "version_number", nullable = false)
	public Integer getVersionNumber() {
		return versionNumber;
	}
	
	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public void setSupervisors(Set<OrganizationEntity> supervisors)
	{
		this.supervisors = supervisors;
	}

	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name="Workflow_OE")
	public Set<OrganizationEntity> getSupervisors()
	{
		return supervisors;
	}

	public void setDocumentTypes(Set<DocumentType> documentTypes)
	{
		this.documentTypes = documentTypes;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "workflow_doc_type",
		joinColumns = @JoinColumn(name = "workflow_id", nullable = false, referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "document_type_id", nullable = false, referencedColumnName = "id")
	)
	public Set<DocumentType> getDocumentTypes()
	{
		return documentTypes;
	}

	public void setTransitions(Set<WorkflowTransition> transitions)
	{
		this.transitions = transitions;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "workflow_id")
	public Set<WorkflowTransition> getTransitions()
	{
		return transitions;
	}

	public void setProcessDefinitionId(String processDefinitionId)
	{
		this.processDefinitionId = processDefinitionId;
	}

	//@Column(nullable=false)
	public String getProcessDefinitionId()
	{
		return processDefinitionId;
	}

	@Override
	public String toString() {
		if (getVersionNumber() != null) {
			return (getName() + " (" + getVersionNumber() + ")");
		} else {
			return getName();
		}
	}
}