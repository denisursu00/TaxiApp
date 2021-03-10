package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUTONUMBERMETADATASEQVALUE")
public class AutoNumberMetadataSequenceValue {
	
	private Long autoNumberMetadataDefinitionId;
	private int sequenceValue;

	@Id
	public Long getAutoNumberMetadataDefinitionId() {
		return autoNumberMetadataDefinitionId;
	}
	public void setAutoNumberMetadataDefinitionId(Long autoNumberMetadataDefinitionId) {
		this.autoNumberMetadataDefinitionId = autoNumberMetadataDefinitionId;
	}
	public int getSequenceValue() {
		return sequenceValue;
	}
	public void setSequenceValue(int sequenceValue) {
		this.sequenceValue = sequenceValue;
	}
}