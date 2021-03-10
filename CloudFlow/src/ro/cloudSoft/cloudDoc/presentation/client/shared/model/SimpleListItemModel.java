package ro.cloudSoft.cloudDoc.presentation.client.shared.model;

public class SimpleListItemModel implements ListItemModel {
	
	private final String value;
	private final String label;
	
	public SimpleListItemModel(String value) {
		this.value = value;
		this.label = value;
	}
	public SimpleListItemModel(String value, String label) {
		this.value = value;
		this.label = label;
	}

	public SimpleListItemModel(Long value, String label) {
		this.value = value.toString();
		this.label = label;
	}

	@Override
	public String getItemValue() {
		return value;
	}

	@Override
	public String getItemLabel() {
		return label;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleListItemModel other = (SimpleListItemModel) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
}