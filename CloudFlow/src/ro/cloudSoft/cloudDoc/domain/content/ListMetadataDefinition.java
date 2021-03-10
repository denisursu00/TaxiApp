package ro.cloudSoft.cloudDoc.domain.content;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
@DiscriminatorValue("LIST")
public class ListMetadataDefinition extends MetadataDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean multipleSelection;
	private boolean extendable;
	private Set<ListMetadataItem> listItems;

	public ListMetadataDefinition() {
		super();
	}

	public boolean getMultipleSelection() {
		return this.multipleSelection;
	}

	public void setMultipleSelection(boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
	}

	public boolean getExtendable() {
		return this.extendable;
	}

	public void setExtendable(boolean extendable) {
		this.extendable = extendable;
	}

	public void setListItems(Set<ListMetadataItem> listItems) {
		this.listItems = listItems;
	}

	@OrderBy("orderNumber")
	@OneToMany(mappedBy = "list", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public Set<ListMetadataItem> getListItems() 
	{
		return listItems;
	}
}