package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CALENDAR")
public class CalendarMetadataDefinition extends MetadataDefinition {
	
}
