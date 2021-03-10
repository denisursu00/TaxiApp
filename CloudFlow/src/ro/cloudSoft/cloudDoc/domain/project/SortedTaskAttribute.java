package ro.cloudSoft.cloudDoc.domain.project;

public class SortedTaskAttribute {
	
	private String propertyName;
	private SortedTaskAttributeOrderDirection order;
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public SortedTaskAttributeOrderDirection getOrder() {
		return order;
	}
	public void setOrder(SortedTaskAttributeOrderDirection order) {
		this.order = order;
	}
}