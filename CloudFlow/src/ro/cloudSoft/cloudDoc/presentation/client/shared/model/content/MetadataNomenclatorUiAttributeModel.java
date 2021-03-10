package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.io.Serializable;

public class MetadataNomenclatorUiAttributeModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long nomenclatorAttributeId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNomenclatorAttributeId() {
		return nomenclatorAttributeId;
	}
	public void setNomenclatorAttributeId(Long nomenclatorAttributeId) {
		this.nomenclatorAttributeId = nomenclatorAttributeId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
