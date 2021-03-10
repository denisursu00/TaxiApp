package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MONTH")
public class MonthMetadataDefinition extends MetadataDefinition {
	
	private boolean autoCompleteWithCurrentMonth;
	
	@Column(name = "auto_compl_with_current_month", nullable = true)
	public boolean isAutoCompleteWithCurrentMonth() {
		return autoCompleteWithCurrentMonth;
	}

	public void setAutoCompleteWithCurrentMonth(boolean autoCompleteWithCurrentMonth) {
		this.autoCompleteWithCurrentMonth = autoCompleteWithCurrentMonth;
	}	
}
