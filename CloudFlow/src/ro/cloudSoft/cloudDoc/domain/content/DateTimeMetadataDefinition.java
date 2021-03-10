package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DATE_TIME")
public class DateTimeMetadataDefinition extends MetadataDefinition {
	
	private boolean autoCompleteWithCurrentDateTime;
	
	@Column(name = "auto_compl_with_crnt_date_time", nullable = true)
	public boolean isAutoCompleteWithCurrentDateTime() {
		return autoCompleteWithCurrentDateTime;
	}
	
	public void setAutoCompleteWithCurrentDateTime(boolean autoCompleteWithCurrentDateTime) {
		this.autoCompleteWithCurrentDateTime = autoCompleteWithCurrentDateTime;
	}
}
