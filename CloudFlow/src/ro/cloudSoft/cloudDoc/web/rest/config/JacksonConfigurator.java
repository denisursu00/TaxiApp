package ro.cloudSoft.cloudDoc.web.rest.config;

import java.io.IOException;
import java.util.Date;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.module.SimpleModule;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.extjs.gxt.ui.client.data.BaseModel;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.AssignedEntityTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.HierarchicalSuperiorOfInitiatorTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.HierarchicalSuperiorOfUserMetadataTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.InitiatorTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.ManuallyChosenEntitiesTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModelType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.UserMetadataTransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarEventModelFactory;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutoNumberMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CalendarMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DateMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DateTimeMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.GroupMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MonthMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ProjectMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorMultipleFilter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorSimpleFilter;
import ro.cloudSoft.common.utils.ArrayUtils;
import ro.cloudSoft.common.utils.spring.SpringUtils;

@Provider
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public JacksonConfigurator() {
		
		addJacksonJsonDateModuleToMapper(mapper);
		addJacksonJsonMetadataDefinitionModuleToMapper(mapper);
		addJacksonJsonTransitionNotificationModuleToMapper(mapper);
		addJacksonNomenclatorFilterToMapper(mapper);
		
		addJasksonJsonCalendarEventModuleToMapper(mapper);
		
        addMapperSettings(mapper);
	}
	
	private static void addMapperSettings(ObjectMapper mapper) {
		mapper.disable(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
        	@Override
        	public String[] findPropertiesToIgnore(AnnotatedClass ac) {
        		String[] propertiesToIgnoreFromAnnotation = ArrayUtils.nullToEmpty(super.findPropertiesToIgnore(ac));
        		String[] propertiesToIgnore = propertiesToIgnoreFromAnnotation;
        		if (BaseModel.class.isAssignableFrom(ac.getAnnotated())) {
        			propertiesToIgnore = ArrayUtils.union(String.class, propertiesToIgnoreFromAnnotation, new String[] {"allowNestedValues", "properties", "propertyNames"});
        		}
        		if (FolderModel.class.isAssignableFrom(ac.getAnnotated())) {
        			propertiesToIgnore = ArrayUtils.union(String.class, propertiesToIgnore, new String[] {"folderId", "entityType"});
        		}
        		if (PermissionModel.class.isAssignableFrom(ac.getAnnotated())) {
        			propertiesToIgnore = ArrayUtils.union(String.class, propertiesToIgnore, new String[] {"entityName"});
        		}
        		return propertiesToIgnore;
        	}
        });
	}
	
	@Override
	public ObjectMapper getContext(Class<?> arg0) {
		return this.mapper;
	}
	
	private static void addJacksonJsonDateModuleToMapper(ObjectMapper mapper) {
		SimpleModule jacksonJsonDateModule  = new SimpleModule("jacksonJsonDateModule", Version.unknownVersion());
		jacksonJsonDateModule.addSerializer(Date.class, new DateJsonSerializer());
		jacksonJsonDateModule.addDeserializer(Date.class, new DateJsonDeserializer());
		mapper.registerModule(jacksonJsonDateModule);
	}
	
	private void addJacksonJsonMetadataDefinitionModuleToMapper(ObjectMapper mapper) {
		SimpleModule jacksonJsonMetadataDefinitionModule = new SimpleModule("jacksonJsonMetadataDefinitionModule", Version.unknownVersion());
		jacksonJsonMetadataDefinitionModule.addDeserializer(MetadataDefinitionModel.class, new MetadataDefinitionJsonDeserializer(mapper));
		mapper.registerModule(jacksonJsonMetadataDefinitionModule);
	}
	
	private void addJacksonJsonTransitionNotificationModuleToMapper(ObjectMapper mapper) {
		SimpleModule jacksonJsonTransitionNotificationModule = new SimpleModule("jacksonJsonTransitionNotificationModule", Version.unknownVersion());
		jacksonJsonTransitionNotificationModule.addDeserializer(TransitionNotificationModel.class, new TransitionNotificationJsonDeserializer(mapper));
		mapper.registerModule(jacksonJsonTransitionNotificationModule);
	}
	
	private void addJacksonNomenclatorFilterToMapper(ObjectMapper mapper) {
		SimpleModule jacksonNomenclatorFilterModule = new SimpleModule("jacksonNomenclatorFilterModule", Version.unknownVersion());
		jacksonNomenclatorFilterModule.addDeserializer(NomenclatorFilter.class, new NomenclatorFilterDeserializer(mapper));
		mapper.registerModule(jacksonNomenclatorFilterModule);
	}
	
	private void addJasksonJsonCalendarEventModuleToMapper(ObjectMapper mapper) {
		SimpleModule jacksonJsonDateModule  = new SimpleModule("jasksonJsonCalendarEvent", Version.unknownVersion());
		jacksonJsonDateModule.addDeserializer(CalendarEventModel.class, new CalendarEventDeserializer(mapper));
		mapper.registerModule(jacksonJsonDateModule);
	}
	
	private static class DateJsonSerializer extends JsonSerializer<Date> {

		@Override
		public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
			String serializedDate = null;
			if (date != null) {
				DateTime dateAsJoda = new DateTime(date);
				serializedDate = ISODateTimeFormat.dateTime().print(dateAsJoda);
			}			
			jsonGenerator.writeString(serializedDate);
		}
	}
	
	private static class DateJsonDeserializer extends JsonDeserializer<Date> {

		@Override
		public Date deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {			
			Date deserializedDate = null;
			String dateAsString = jsonParser.getText();
			if (StringUtils.isNotBlank(dateAsString)) {
				try {
					deserializedDate = ISODateTimeFormat.dateTime().parseDateTime(dateAsString).toDate();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return deserializedDate;
		}
	}
	
	private static class MetadataDefinitionJsonDeserializer extends JsonDeserializer<MetadataDefinitionModel> {
		
		private final ObjectMapper mapper;
		
		public MetadataDefinitionJsonDeserializer(ObjectMapper mapper) {
			this.mapper = mapper;
		}
		
		@Override
		public MetadataDefinitionModel deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {			
			JsonNode node = jsonParser.getCodec().readTree(jsonParser);			
			String type = (String)(node.get("type").asText());			
			if (type.equals(MetadataDefinitionModel.TYPE_DATE)) {
				return mapper.readValue(node, DateMetadataDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_DATE_TIME)) {
				return mapper.readValue(node, DateTimeMetadataDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_MONTH)) {
				return mapper.readValue(node, MonthMetadataDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_USER)) {
				return mapper.readValue(node, UserMetadataDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
				return mapper.readValue(node, AutoNumberMetadataDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_LIST)) {
				return mapper.readValue(node, ListMetadataDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_METADATA_COLLECTION)) {
				return mapper.readValue(node, MetadataCollectionDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_NOMENCLATOR)) {
				return mapper.readValue(node, NomenclatorMetadataDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_GROUP)) {
				return mapper.readValue(node, GroupMetadataDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_DOCUMENT)) {
				return mapper.readValue(node, DocumentMetadataDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_CALENDAR)) {
				return mapper.readValue(node, CalendarMetadataDefinitionModel.class);
			} else if (type.equals(MetadataDefinitionModel.TYPE_PROJECT)) {
				return mapper.readValue(node, ProjectMetadataDefinitionModel.class);
			}
			/*
			 * WORKAROUND: pentru StackOverflowError
			 * Pentru conversia MetadataDefinitionModel creem un nou mapper la care nu mai adaugam 
			 * deserializatorul acesta - toate acestea pentru ca deserializatorul trebuia pus pe o clasa mai
			 * abstracta decat MetadataDefinitionModel.
			 */
			ObjectMapper newMapper = new ObjectMapper();
			addJacksonJsonDateModuleToMapper(newMapper);
			addMapperSettings(newMapper);
			return newMapper.readValue(node, MetadataDefinitionModel.class);
		}
	}
	
	private static class TransitionNotificationJsonDeserializer extends JsonDeserializer<TransitionNotificationModel> {
		
		private final ObjectMapper mapper;
		
		public TransitionNotificationJsonDeserializer(ObjectMapper mapper) {
			this.mapper = mapper;
		}
		
		@Override
		public TransitionNotificationModel deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {	
			JsonNode node = jsonParser.getCodec().readTree(jsonParser);
			String type = (String)(node.get("type").asText());
			if (type.equals(TransitionNotificationModelType.ASSIGNED_ENTITY.getLabel())) {
				return mapper.readValue(node, AssignedEntityTransitionNotificationModel.class);
			} else if (type.equals(TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_INITIATOR.getLabel())) {
				return mapper.readValue(node, HierarchicalSuperiorOfInitiatorTransitionNotificationModel.class);
			} else if (type.equals(TransitionNotificationModelType.INITIATOR.getLabel())) {
				return mapper.readValue(node, InitiatorTransitionNotificationModel.class);
			} else if (type.equals(TransitionNotificationModelType.MANUALLY_CHOSEN_ENTITIES.getLabel())) {
				return mapper.readValue(node, ManuallyChosenEntitiesTransitionNotificationModel.class);
			} else if (type.equals(TransitionNotificationModelType.METADATA.getLabel())) {
				return mapper.readValue(node, UserMetadataTransitionNotificationModel.class);
			} else if (type.equals(TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_USER_METADATA.getLabel())) {
				return mapper.readValue(node, HierarchicalSuperiorOfUserMetadataTransitionNotificationModel.class);
			}
			throw new RuntimeException("Type cannot have the value [" + type + "].");
		}
	}
	
	private static class NomenclatorFilterDeserializer extends JsonDeserializer<NomenclatorFilter> {
		
		private final ObjectMapper mapper;
		
		public NomenclatorFilterDeserializer(ObjectMapper mapper) {
			this.mapper = mapper;
		}
		
		@Override
		public NomenclatorFilter deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {			
			JsonNode node = jsonParser.getCodec().readTree(jsonParser);
			
			Object filterValue = node.get("value");
			Object filterValues = node.get("values");
			
			if (filterValue != null) {
				return mapper.readValue(node, NomenclatorSimpleFilter.class);
			} else if (filterValues != null) {
				return mapper.readValue(node, NomenclatorMultipleFilter.class);
			}
			throw new RuntimeException("NomenclatorFilterDeserializer cannot deserialize the json object.");
		}
	}
	
	private static class CalendarEventDeserializer extends JsonDeserializer<CalendarEventModel> {
		
		private final ObjectMapper mapper;
		
		public CalendarEventDeserializer(ObjectMapper mapper) {
			this.mapper = mapper;
		}
		
		@Override
		public CalendarEventModel deserialize(JsonParser jsonParser, DeserializationContext context)
				throws IOException, JsonProcessingException {
			
			CalendarEventModelFactory calendarEventModelFactory = SpringUtils.getBean("calendarEventModelFactory");
			
			JsonNode node = jsonParser.getCodec().readTree(jsonParser);	
			return calendarEventModelFactory.createFromJsonNode(mapper, node);
		}
	}
}
