package ro.cloudSoft.cloudDoc.domain.content;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;


/**
 * Entity implementation class for Entity: MimeType
 *
 */
@Entity
@Table( uniqueConstraints = {@UniqueConstraint(columnNames={"extension"})})
public class MimeType implements Serializable 
{

	
	private Long id;
	private String name;
	
	@Column(name="extension", nullable=false, unique=true)
	private String extension;
	
	private static final long serialVersionUID = 1L;

	public MimeType() 
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
	public String getName() 
	{
		return this.name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}   
	
	
	public String getExtension() 
	{
		return this.extension;
	}

	public void setExtension(String extension) 
	{
		this.extension = extension;
	}
   
}
