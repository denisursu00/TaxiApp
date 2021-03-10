package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("GROUP")
public class GroupMetadataDefinition extends MetadataDefinition {

}
