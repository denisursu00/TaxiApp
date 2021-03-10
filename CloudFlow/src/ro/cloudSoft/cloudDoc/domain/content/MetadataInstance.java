package ro.cloudSoft.cloudDoc.domain.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class MetadataInstance {

	private Long metadataDefinitionId;
	private List<String> values = Lists.newLinkedList();

	public MetadataInstance() {}

	/**
	 * Creeaza o instanta de metadata avand o singura valoare. Metadatele
	 * dintr-o colectie vor fi doar de acest tip.
	 * 
	 * @param metadataDefinitionId ID-ul definitiei de metadata
	 * @param value valoarea
	 */
	public MetadataInstance(Long metadataDefinitionId, String value) {
		this.metadataDefinitionId = metadataDefinitionId;
		values.add(value);
	}

	public MetadataInstance(Long metadataDefinitionId, String... values) {
		this.metadataDefinitionId = metadataDefinitionId;
		Collections.addAll(this.values, values);
	}

	public void addValue(String value) {
		values.add(value);
	}

	public String getValue() {
		return Iterables.getFirst(values, null);
	}

	public void setValue(String value) {
		this.values = new ArrayList<String>();
		this.values.add(value);
	}

	public Long getMetadataDefinitionId() {
		return metadataDefinitionId;
	}

	public void setMetadataDefinitionId(Long metadataDefinitionId) {
		this.metadataDefinitionId = metadataDefinitionId;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
}