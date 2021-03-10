package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import java.util.LinkedHashMap;
import java.util.Map;

public class NomenclatorValueViewModel {
	
	private Long id;
	private Map<String, String> attributes;
	
	public NomenclatorValueViewModel() {
		this.attributes = new LinkedHashMap<String, String>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
