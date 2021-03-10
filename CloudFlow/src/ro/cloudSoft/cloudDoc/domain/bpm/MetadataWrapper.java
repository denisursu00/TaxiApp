package ro.cloudSoft.cloudDoc.domain.bpm;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;

import com.google.common.collect.Lists;

public class MetadataWrapper implements Serializable {

	private static final long serialVersionUID = 16703610865725916L;
	
	private final String type;
	private final List<String> values;
	
	public MetadataWrapper(String type, String value) {
		this.type = type;
		this.values = Lists.newArrayList(value);
	}
	
	public MetadataWrapper(String type, List<String> values) {
		this.type = type;
		this.values = values;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("type", type)
			.append("values", values)
			.toString();
	}
	
	public String getType() {
		return type;
	}
	
	public String getValue() {
		return (GwtValidateUtils.hasElements(values)) ? values.get(0) : null;
	}
	
	public List<String> getValues() {
		return values;
	}
}