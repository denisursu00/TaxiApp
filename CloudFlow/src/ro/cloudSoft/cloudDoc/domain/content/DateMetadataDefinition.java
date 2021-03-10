package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DATE")
public class DateMetadataDefinition extends MetadataDefinition {
	
	private boolean autoCompleteWithCurrentDate;
	
	@Column(name = "auto_compl_with_current_date", nullable = true)
	public boolean isAutoCompleteWithCurrentDate() {
		return autoCompleteWithCurrentDate;
	}

	public void setAutoCompleteWithCurrentDate(boolean autoCompleteWithCurrentDate) {
		this.autoCompleteWithCurrentDate = autoCompleteWithCurrentDate;
	}	
}
