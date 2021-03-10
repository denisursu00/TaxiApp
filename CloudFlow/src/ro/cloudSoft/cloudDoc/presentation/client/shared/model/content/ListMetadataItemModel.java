package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ListMetadataItemModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = -543163843495462616L;
	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_LABEL = "label";
	public static final String PROPERTY_VALUE = "value";
	private Integer orderNumber;
	
	public ListMetadataItemModel() {}
	
	public Long getId() {
		return get(PROPERTY_ID);
	}
	
	public void setId(Long id) {
		set(PROPERTY_ID, id);
	}
	
	public String getLabel() {
		return get(PROPERTY_LABEL);
	}
	
	public void setLabel(String label) {
		set(PROPERTY_LABEL, label);
	}
	
	public String getValue() {
		return get(PROPERTY_VALUE);
	}
	
	public void setValue(String value) {
		set(PROPERTY_VALUE, value);
	}
	
	public Integer getOrderNumber() {
		return orderNumber;
	}
	
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
}