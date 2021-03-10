package ro.cloudSoft.cloudDoc.domain.content;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Reprezinta o instanta de colectie, adica o colectie de "tipul" definit in
 * tipul documentului din care face parte.
 * Fiecare instanta de colectie are metadatele definite in tipul documentului
 * completate.
 */
public class CollectionInstance {
	
	private String id;
	private List<MetadataInstance> metadataInstanceList = Lists.newLinkedList();
 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<MetadataInstance> getMetadataInstanceList() {
		return metadataInstanceList;
	}
	public void setMetadataInstanceList(List<MetadataInstance> metadataInstanceList) {
		this.metadataInstanceList = metadataInstanceList;
	}
}