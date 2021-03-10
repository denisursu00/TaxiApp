package ro.cloudSoft.cloudDoc.domain.replacementProfiles;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;

/**
 * 
 */
@Entity
@Table(name = "replacement_profile_instances")
public class ReplacementProfileInstance {

	private ReplacementProfileInstancePk id = new ReplacementProfileInstancePk();
	private List<ReplacementProfileInstanceItem> items = Lists.newLinkedList();
	
	@EmbeddedId
	public ReplacementProfileInstancePk getId() {
		return id;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "id.profileInstance")
	public List<ReplacementProfileInstanceItem> getItems() {
		return items;
	}

	@Transient
	public String getDocumentLocationRealName() {
		return getId().getDocumentLocationRealName();
	}
	
	@Transient
	public String getDocumentId() {
		return getId().getDocumentId();
	}
	
	@Transient
	public void setDocumentIdentifiers(String documentLocationRealName, String documentId) {
		getId().setDocumentLocationRealName(documentLocationRealName);
		getId().setDocumentId(documentId);
	}
	
	public void setId(ReplacementProfileInstancePk id) {
		this.id = id;
	}
	public void setItems(List<ReplacementProfileInstanceItem> items) {
		this.items = items;
	}
}