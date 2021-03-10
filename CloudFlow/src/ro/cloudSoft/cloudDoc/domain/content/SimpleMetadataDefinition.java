package ro.cloudSoft.cloudDoc.domain.content;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Entity implementation class for Entity: MetadataDefinition
 *
 */
@Entity
@DiscriminatorValue("SIMPLE")
public class SimpleMetadataDefinition extends MetadataDefinition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
}
