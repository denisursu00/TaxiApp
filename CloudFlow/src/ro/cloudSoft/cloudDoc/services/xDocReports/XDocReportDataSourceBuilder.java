package ro.cloudSoft.cloudDoc.services.xDocReports;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;
import org.jfree.data.xy.NormalizedMatrixSeries;

import fr.opensagres.xdocreport.template.velocity.internal.Foreach;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;

public class XDocReportDataSourceBuilder{

	private Object sourceModel;
	
	private ObjectMapper mapper;
	
	public XDocReportDataSourceBuilder(Object sourceModel) {
		this.sourceModel = sourceModel;
		
		this.mapper = new ObjectMapper();
		addMapperSettings(mapper);
		addJacksonJsonDateModuleToMapper(mapper);
	}
	
	private void addMapperSettings(ObjectMapper mapper) {
		mapper.disable(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	private void addJacksonJsonDateModuleToMapper(ObjectMapper mapper) {
		SimpleModule jacksonDateModule  = new SimpleModule("jacksonJsonModule", Version.unknownVersion());
		jacksonDateModule.addSerializer(Date.class, new JsonSerializer<Date>() {
			
			@Override
			public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider arg2) throws IOException, JsonProcessingException {
				String serializedDate = null;
				if (date != null) {
					serializedDate = DateFormatUtils.format(date, FormatConstants.DATE_FOR_DISPLAY);
				}	
				jsonGenerator.writeString(serializedDate);
			}
		});
		mapper.registerModule(jacksonDateModule);
	}
	
	
	public Map build() {
		Map sourceModelAsMap = null;
		try {
			sourceModelAsMap = mapper.convertValue(sourceModel, Map.class);
			normalize(sourceModelAsMap);
		} catch (Exception e) {
			throw new RuntimeException("Error while writing object as map", e);
		}
		return sourceModelAsMap;
	}

	private void normalize(Map sourceModelAsMap) {
		sourceModelAsMap.forEach((key, value)-> {
			if (value == null) {
				sourceModelAsMap.put(key, "");
			}
			if (value instanceof Map) {
				normalize((Map) value);
			}
			if (value instanceof List) {
				((List) value).forEach(listValue -> {
					normalize((Map)listValue);
				});
			}
		});
		
	}	
}
