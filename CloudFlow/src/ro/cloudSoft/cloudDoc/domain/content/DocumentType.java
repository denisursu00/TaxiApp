package ro.cloudSoft.cloudDoc.domain.content;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;

/**
 * Entity implementation class for Entity: DocumentType
 * 
 * 
 */
@Entity
public class DocumentType implements Serializable
{
	public static final int LENGTH_NAME = 128;
	public static final int LENGTH_DESCRIPTION = 1024;
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String description;
	private boolean keepAllVersions;
	
	private boolean mandatoryAttachment;
	
	private boolean mandatoryAttachmentInStates;
	private String mandatoryAttachmentStates;
	
	private boolean mandatoryAttachmentWhenMetadataHasValue;
	private String metadataNameInMandatoryAttachmentCondition;
	private String metadataValueInMandatoryAttachmentCondition;
	
	private boolean allUsersInitiators;
	
	/**
	 * Colectia care tine tipurile de atasamente permise de acest tip de
	 * document.
	 */

	private Set<MimeType> allowedMimeTypes = Sets.newLinkedHashSet();

	/**
	 * Colectia de metadate
	 */
	private List<? extends MetadataDefinition> metadataDefinitions = Lists.newLinkedList();
	
	private List<MetadataCollection> metadataCollections = Lists.newLinkedList();

	/**
	 * Colectia de initiatori: useri, grupuri sau uo, diferentiate prin
	 * initiatorType
	 */

	private Set<OrganizationEntity> initiators = Sets.newLinkedHashSet();
	
	private String parentDocumentLocationRealNameForDefaultLocation;
	private String folderIdForDefaultLocation;
	
	private List<DocumentValidationDefinition> validationDefinitions = Lists.newLinkedList();
	
	private boolean archivable;
	
	public DocumentType()
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

	@Column(nullable=false, unique=true, length = LENGTH_NAME)
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
	public boolean isKeepAllVersions() {
		return keepAllVersions;
	}
	public void setKeepAllVersions(boolean keepAllVersions) {
		this.keepAllVersions = keepAllVersions;
	}

	public boolean getMandatoryAttachment()
	{
		return this.mandatoryAttachment;
	}

	public void setMandatoryAttachment(boolean mandatoryAttachment)
	{
		this.mandatoryAttachment = mandatoryAttachment;
	}
	
	@Column(name = "mandatory_attachment_in_states", nullable = false)
	public boolean isMandatoryAttachmentInStates() {
		return mandatoryAttachmentInStates;
	}
	
	public void setMandatoryAttachmentInStates(boolean mandatoryAttachmentInStates) {
		this.mandatoryAttachmentInStates = mandatoryAttachmentInStates;
	}

	@Column(name = "mandatory_attachment_states", nullable = true, length = 500)
	public String getMandatoryAttachmentStates() {
		return mandatoryAttachmentStates;
	}
	
	public void setMandatoryAttachmentStates(String mandatoryAttachmentStates) {
		this.mandatoryAttachmentStates = mandatoryAttachmentStates;
	}
	
	@Column(name = "mand_attach_when_metad_has_val", nullable = false)
	public boolean isMandatoryAttachmentWhenMetadataHasValue() {
		return mandatoryAttachmentWhenMetadataHasValue;
	}
	
	public void setMandatoryAttachmentWhenMetadataHasValue(boolean mandatoryAttachmentWhenMetadataHasValue) {
		this.mandatoryAttachmentWhenMetadataHasValue = mandatoryAttachmentWhenMetadataHasValue;
	}
	
	@Column(name = "meta_name_in_mand_attach_cond", nullable = true)
	public String getMetadataNameInMandatoryAttachmentCondition() {
		return metadataNameInMandatoryAttachmentCondition;
	}
	
	public void setMetadataNameInMandatoryAttachmentCondition(String metadataNameInMandatoryAttachmentCondition) {
		this.metadataNameInMandatoryAttachmentCondition = metadataNameInMandatoryAttachmentCondition;
	}
	
	@Column(name = "meta_value_in_mand_attach_cond", nullable = true)
	public String getMetadataValueInMandatoryAttachmentCondition() {
		return metadataValueInMandatoryAttachmentCondition;
	}
	
	public void setMetadataValueInMandatoryAttachmentCondition(String metadataValueInMandatoryAttachmentCondition) {
		this.metadataValueInMandatoryAttachmentCondition = metadataValueInMandatoryAttachmentCondition;
	}

	/**
	 * @param allowedMimeTypes
	 *            the allowedMimeTypes to set
	 */
	public void setAllowedMimeTypes(Set<MimeType> allowedMimeTypes)
	{
		this.allowedMimeTypes = allowedMimeTypes;
	}

	/**
	 * @return the allowedMimeTypes
	 */
	
	@OrderBy("name")
	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	public Set<MimeType> getAllowedMimeTypes()
	{
		return allowedMimeTypes;
	}

	/**
	 * @param metadataDefinitions
	 *            the metadataDefinitions to set
	 */

	public void setMetadataDefinitions(List<? extends MetadataDefinition> metadataDefinitions)
	{
		this.metadataDefinitions = metadataDefinitions;
	}

	/**
	 * @return the metadataDefinitions
	 */
	@OrderBy("orderNumber")
	@JoinColumn(name = "documenttype_id")
	@OneToMany(targetEntity = MetadataDefinition.class, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<? extends MetadataDefinition> getMetadataDefinitions()
	{
		return metadataDefinitions;
	}

	/**
	 * @param initiators
	 *            the initiators to set
	 */
	public void setInitiators(Set<OrganizationEntity> initiators)
	{
		this.initiators = initiators;
	}

	/**
	 * @return the initiators
	 */
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name="DocType_OE")
	public Set<OrganizationEntity> getInitiators()
	{
		return initiators;
	}

	/**
	 * @param allUsersInitiators
	 *            the allUsersInitiators to set
	 */
	public void setAllUsersInitiators(boolean allUsersInitiators)
	{
		this.allUsersInitiators = allUsersInitiators;
	}

	/**
	 * @return the allUsersInitiators
	 */
	public boolean isAllUsersInitiators()
	{
		return allUsersInitiators;
	}

	public void setMetadataCollections(List<MetadataCollection> metadataCollections)
	{
		this.metadataCollections = metadataCollections;
	}
	
	@OrderBy("orderNumber")
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "documenttype_id")
	public List<MetadataCollection> getMetadataCollections()
	{
		return metadataCollections;
	}
	
	@Column(name = "par_doc_loc_real_nam_4_def_loc", nullable = true, length = DocumentLocation.MAX_LENGTH_REAL_NAME)
	public String getParentDocumentLocationRealNameForDefaultLocation() {
		return parentDocumentLocationRealNameForDefaultLocation;
	}
	
	public void setParentDocumentLocationRealNameForDefaultLocation(String parentDocumentLocationRealNameForDefaultLocation) {
		this.parentDocumentLocationRealNameForDefaultLocation = parentDocumentLocationRealNameForDefaultLocation;
	}

	@Column(name = "folder_id_4_default_location", nullable = true, length = Folder.MAX_LENGTH_ID)
	public String getFolderIdForDefaultLocation() {
		return folderIdForDefaultLocation;
	}
	
	public void setFolderIdForDefaultLocation(String folderIdForDefaultLocation) {
		this.folderIdForDefaultLocation = folderIdForDefaultLocation;
	}
	
	@OrderBy("validationOrder")
	@OneToMany(cascade = CascadeType.ALL, mappedBy="documentType")
	public List<DocumentValidationDefinition> getValidationDefinitions() {
		return validationDefinitions;
	}
	
	public void setValidationDefinitions(List<DocumentValidationDefinition> validationDefinitions) {
		this.validationDefinitions = validationDefinitions;
	}
	
	@Column(name = "archivable", nullable = false)
	public boolean isArchivable() {
		return archivable;
	}
	
	public void setArchivable(boolean archivable) {
		this.archivable = archivable;
	}
}