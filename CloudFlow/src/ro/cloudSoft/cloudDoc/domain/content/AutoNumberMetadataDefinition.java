package ro.cloudSoft.cloudDoc.domain.content;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AUTO")
public class AutoNumberMetadataDefinition extends MetadataDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	private String prefix;
	private int numberLength;

	public AutoNumberMetadataDefinition() {
		super();
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getNumberLength() {
		return this.numberLength;
	}

	public void setNumberLength(int numberLength) {
		this.numberLength = numberLength;
	}
}