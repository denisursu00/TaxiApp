package ro.cloudSoft.cloudDoc.domain.content;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Entity implementation class for Entity: ListMetadataItem
 *
 */
@Entity

public class ListMetadataItem implements Serializable {

	
	private Long id;
	private String label;
	private String value;
	private Integer orderNumber;
	
	private ListMetadataDefinition list;
	
	private static final long serialVersionUID = 1L;

	public ListMetadataItem() 
	{
		super();
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
	public String getLabel() 
	{
		return this.label;
	}

	public void setLabel(String label) 
	{
		this.label = label;
	}   
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) 
	{
		this.value = value;
	}
	
	@Column(name = "ORDER_NUMBER", nullable = false)
	public Integer getOrderNumber() {
		return orderNumber;
	}
	
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	/**
	 * @param list the list to set
	 */
	public void setList(ListMetadataDefinition list) 
	{
		this.list = list;
	}
	/**
	 * @return the list
	 */
	@ManyToOne
    @JoinColumn(name="list_id")
	public ListMetadataDefinition getList() 
	{
		return list;
	}
   
}
