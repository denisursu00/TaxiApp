package ro.cloudSoft.cloudDoc.domain.content;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Document extends Entity {
	
	private String documentLocationRealName;

	private List<MetadataInstance> metadataInstanceList = Lists.newLinkedList();
	/**
	 * Reprezinta o "harta" cu toate colectiile completate, impartite pe
	 * categorii (in functie de colectia definita in tipul documentului). Cheia
	 * este id-ul colectiei definite in tipul documentului. Valoarea este o
	 * lista ce cuprinde toate colectiile de acel tip, completate de catre
	 * utilizator.
	 */
	private Map<Long, List<CollectionInstance>> collectionInstanceListMap = Maps.newLinkedHashMap();

	//private String content;
	private List<String> attachmentNames = Lists.newLinkedList();
	
	private Long workflowStateId;
	/** indica daca documentul are o versiune stabila sau nu */
	private Boolean hasStableVersion;
	/**
	 * Numarul versiunii (daca documentul reprezinta o versiune)
	 * sau null daca documentul NU reprezinta o versiune
	 */
	private Integer versionNumber;
	
	/**
	 * Indica id-ul starii pe flux in care s-a creat versiunea.
	 */
	private Long versionWorkflowStateId;
	
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	public List<MetadataInstance> getMetadataInstanceList() {
		return metadataInstanceList;
	}
	public void setMetadataInstanceList(List<MetadataInstance> metadataInstanceList) {
		this.metadataInstanceList = metadataInstanceList;
	}
	public Map<Long, List<CollectionInstance>> getCollectionInstanceListMap() {
		return collectionInstanceListMap;
	}
	public void setCollectionInstanceListMap(Map<Long, List<CollectionInstance>> collectionInstanceListMap) {
		this.collectionInstanceListMap = collectionInstanceListMap;
	}
	public List<String> getAttachmentNames() {
		return attachmentNames;
	}
	public void setAttachmentNames(List<String> attachmentNames) {
		this.attachmentNames = attachmentNames;
	}
	public Long getWorkflowStateId() {
		return workflowStateId;
	}
	public void setWorkflowStateId(Long workflowStateId) {
		this.workflowStateId = workflowStateId;
	}
	public Boolean getHasStableVersion() {
		return hasStableVersion;
	}
	public void setHasStableVersion(Boolean hasStableVersion) {
		this.hasStableVersion = hasStableVersion;
	}
	public Integer getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}
	public Long getVersionWorkflowStateId() {
		return versionWorkflowStateId;
	}
	public void setVersionWorkflowStateId(Long versionWorkflowStateId) {
		this.versionWorkflowStateId = versionWorkflowStateId;
	}
}