package ro.cloudSoft.cloudDoc.services.jasperReports;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;

public class JsonJRDataSourceBuilder implements JRDataSourceBuilder {

	private Object sourceModel;
	
	private ObjectMapper mapper;
	
	public JsonJRDataSourceBuilder(Object sourceModel) {
		this.sourceModel = sourceModel;
		
		this.mapper = new ObjectMapper();
		addMapperSettings(mapper);
		addJacksonJsonDateModuleToMapper(mapper);
	}
	
	private void addMapperSettings(ObjectMapper mapper) {
		mapper.disable(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	private void addJacksonJsonDateModuleToMapper(ObjectMapper mapper) {
		SimpleModule jacksonJsonDateModule  = new SimpleModule("jacksonJsonDateModule", Version.unknownVersion());
		jacksonJsonDateModule.addSerializer(Date.class, new JsonSerializer<Date>() {
			
			@Override
			public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider arg2) throws IOException, JsonProcessingException {
				String serializedDate = null;
				if (date != null) {
					serializedDate = DateFormatUtils.format(date, JasperReportsConstants.JSON_DATE_FORMAT);
				}	
				jsonGenerator.writeString(serializedDate);
			}
		});
		mapper.registerModule(jacksonJsonDateModule);
	}
	
	@Override
	public JRDataSource build() {
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(sourceModel);
		} catch (Exception e) {
			throw new RuntimeException("Error while writing object as json string", e);
		}
		JsonDataSource jsonDataSource = null;
		try {
			InputStream jsonInputStream = new ByteArrayInputStream(jsonString.getBytes("UTF-8"));
			jsonDataSource = new JsonDataSource(jsonInputStream);
			jsonDataSource.setDatePattern(JasperReportsConstants.JSON_DATE_FORMAT);
		} catch (Exception e) {
			throw new RuntimeException("Error while creating JsonDataSource", e);
		}
		return jsonDataSource;
	}	
}
