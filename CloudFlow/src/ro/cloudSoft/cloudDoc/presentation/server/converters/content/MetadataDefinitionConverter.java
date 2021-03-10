package ro.cloudSoft.cloudDoc.presentation.server.converters.content;

import ro.cloudSoft.cloudDoc.domain.content.AutoNumberMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.CalendarMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.DateMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.DateTimeMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.DocumentMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.GroupMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ListMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.MonthMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.NomenclatorMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.ProjectMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.SimpleMetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.content.TypeOfAutoCompleteWithMetadata;
import ro.cloudSoft.cloudDoc.domain.content.UserMetadataDefinition;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutoNumberMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CalendarMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DateMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DateTimeMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.GroupMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MonthMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.NomenclatorMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ProjectMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.common.utils.spring.SpringUtils;

/**
 * Converteste definitii de metadate in obiecte de interfata si invers.
 * 
 * 
 */
public class MetadataDefinitionConverter {
	
	private static final int DEFAULT_ORDER_NUMBER = 0;
	
	public static MetadataDefinitionModel getModelFromMetadataDefinition(MetadataDefinition metadataDefinition) {
		
		MetadataDefinitionModel metadataDefinitionModel = new MetadataDefinitionModel();
		
		if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_DATE)) {
			metadataDefinitionModel = DateMetadataDefinitionConverter.getModelFromDateMetadataDefinition((DateMetadataDefinition) metadataDefinition);
		} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_DATE_TIME)) {
			metadataDefinitionModel = DateTimeMetadataDefinitionConverter.getModelFromDateTimeMetadataDefinition((DateTimeMetadataDefinition) metadataDefinition);
		} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_AUTO_NUMBER)) {
			metadataDefinitionModel = AutoNumberMetadataDefinitionConverter.getModelFromAutoNumberMetadataDefinition((AutoNumberMetadataDefinition) metadataDefinition);
		} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_USER)) {
			metadataDefinitionModel = UserMetadataDefinitionConverter.getModel((UserMetadataDefinition) metadataDefinition);
		} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_LIST)) {
			metadataDefinitionModel = ListMetadataDefinitionConverter.getModelFromListMetadataDefinition((ListMetadataDefinition) metadataDefinition);
		} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_NOMENCLATOR)) {
			metadataDefinitionModel = SpringUtils.getBean(NomenclatorMetadataDefinitionConverter.class).getModel((NomenclatorMetadataDefinition) metadataDefinition);
		} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_MONTH)) {
			metadataDefinitionModel = MonthMetadataDefinitionConverter.getModelFromDateMetadataDefinition((MonthMetadataDefinition) metadataDefinition);
		} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_GROUP)) {
			metadataDefinitionModel = SpringUtils.getBean(GroupMetadataDefinitionConverter.class).toModel((GroupMetadataDefinition) metadataDefinition);
		} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_DOCUMENT)) {
			metadataDefinitionModel = DocumentMetadataDefinitionConverter.toModel((DocumentMetadataDefinition) metadataDefinition);
		} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_CALENDAR)) {
			metadataDefinitionModel = CalendarMetadataDefinitionConverter.toModel((CalendarMetadataDefinition) metadataDefinition);
		} else if (metadataDefinition.getMetadataType().equals(MetadataDefinition.TYPE_PROJECT)) {
			metadataDefinitionModel = ProjectMetadataDefinitionConverter.toModel((ProjectMetadataDefinition) metadataDefinition);
		}
		
		metadataDefinitionModel.setId(metadataDefinition.getId());
		metadataDefinitionModel.setName(metadataDefinition.getName());
		metadataDefinitionModel.setLabel(metadataDefinition.getLabel());
		metadataDefinitionModel.setMandatory(new Boolean(metadataDefinition.getMandatory()));
		metadataDefinitionModel.setRestrictedOnEdit(new Boolean(metadataDefinition.isRestrictedOnEdit()));
		metadataDefinitionModel.setMandatoryStates(metadataDefinition.getMandatoryStates());
		metadataDefinitionModel.setRestrictedOnEditStates(metadataDefinition.getRestrictedOnEditStates());
		metadataDefinitionModel.setInvisible(metadataDefinition.isInvisible());
		metadataDefinitionModel.setInvisibleInStates(metadataDefinition.getInvisibleInStates());
		metadataDefinitionModel.setRepresentative(new Boolean(metadataDefinition.getRepresentative()));
		metadataDefinitionModel.setIndexed(new Boolean(metadataDefinition.getIndexed()));
		metadataDefinitionModel.setOrderNumber(metadataDefinition.getOrderNumber());
		metadataDefinitionModel.setType(metadataDefinition.getMetadataType());
		metadataDefinitionModel.setDefaultValue(metadataDefinition.getDefaultValue());
		metadataDefinitionModel.setMetadataNameForAutoCompleteWithMetadata(metadataDefinition.getMetadataNameForAutoCompleteWithMetadata());
		if (metadataDefinition.getTypeOfAutoCompleteWithMetadata() != null) {
			metadataDefinitionModel.setTypeOfAutoCompleteWithMetadata(metadataDefinition.getTypeOfAutoCompleteWithMetadata().name());
		}
		metadataDefinitionModel.setNomenclatorAttributeKeyForAutoCompleteWithMetadata(metadataDefinition.getNomenclatorAttributeKeyForAutoCompleteWithMetadata());
		metadataDefinitionModel.setClassNameForAutoCompleteWithMetadata(metadataDefinition.getClassNameForAutoCompleteWithMetadata());
		
		return metadataDefinitionModel;
	}

	public static MetadataDefinition getMetadataDefinitionFromModel(MetadataDefinitionModel metadataDefinitionModel, GroupService groupService) {
		
		MetadataDefinition metadataDefinition = new SimpleMetadataDefinition();
		
		if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_DATE)) {
			metadataDefinition = DateMetadataDefinitionConverter.getDateMetadataDefinitionFromModel((DateMetadataDefinitionModel) metadataDefinitionModel);
		} else if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_DATE_TIME)) {
			metadataDefinition = DateTimeMetadataDefinitionConverter.getDateTimeMetadataDefinitionFromModel((DateTimeMetadataDefinitionModel) metadataDefinitionModel);
		} else if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
			metadataDefinition = AutoNumberMetadataDefinitionConverter.getAutoNumberMetadataDefinitionFromModel((AutoNumberMetadataDefinitionModel) metadataDefinitionModel);
		} else if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_USER)) {
			metadataDefinition = UserMetadataDefinitionConverter.getFromModel((UserMetadataDefinitionModel) metadataDefinitionModel, groupService);
		} else if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_LIST)) {
			metadataDefinition = ListMetadataDefinitionConverter.getListMetadataDefinitionFromModel((ListMetadataDefinitionModel) metadataDefinitionModel);
		} else if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_NOMENCLATOR)) {
			metadataDefinition = SpringUtils.getBean(NomenclatorMetadataDefinitionConverter.class).getFromModel((NomenclatorMetadataDefinitionModel) metadataDefinitionModel);
		} else if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_MONTH)) {
			metadataDefinition = MonthMetadataDefinitionConverter.getDateMetadataDefinitionFromModel((MonthMetadataDefinitionModel) metadataDefinitionModel);
		} else if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_GROUP)) {
			metadataDefinition = SpringUtils.getBean(GroupMetadataDefinitionConverter.class).toEntity((GroupMetadataDefinitionModel) metadataDefinitionModel);
		} else if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_DOCUMENT)) {
			metadataDefinition = DocumentMetadataDefinitionConverter.toEntity((DocumentMetadataDefinitionModel) metadataDefinitionModel);
		} else if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_CALENDAR)) {
			metadataDefinition = CalendarMetadataDefinitionConverter.toEntity((CalendarMetadataDefinitionModel) metadataDefinitionModel);
		} else if (metadataDefinitionModel.getType().equals(MetadataDefinitionModel.TYPE_PROJECT)) {
			metadataDefinition = ProjectMetadataDefinitionConverter.toEntity((ProjectMetadataDefinitionModel) metadataDefinitionModel);
		}
		
		metadataDefinition.setId(metadataDefinitionModel.getId());
		metadataDefinition.setName(metadataDefinitionModel.getName());
		metadataDefinition.setLabel(metadataDefinitionModel.getLabel());
		metadataDefinition.setMandatory(metadataDefinitionModel.isMandatory().booleanValue());
		metadataDefinition.setRestrictedOnEdit(metadataDefinitionModel.isRestrictedOnEdit().booleanValue());
		metadataDefinition.setMandatoryStates(metadataDefinitionModel.getMandatoryStates());
		metadataDefinition.setRestrictedOnEditStates(metadataDefinitionModel.getRestrictedOnEditStates());
		metadataDefinition.setInvisible(metadataDefinitionModel.isInvisible());
		metadataDefinition.setInvisibleInStates(metadataDefinitionModel.getInvisibleInStates());
		metadataDefinition.setRepresentative(metadataDefinitionModel.isRepresentative().booleanValue());
		metadataDefinition.setIndexed(metadataDefinitionModel.isIndexed().booleanValue());
		
		Integer orderNumber = metadataDefinitionModel.getOrderNumber();
		if (orderNumber == null) {
			orderNumber = DEFAULT_ORDER_NUMBER;
		}
		metadataDefinition.setOrderNumber(orderNumber);
		
		metadataDefinition.setMetadataType(metadataDefinitionModel.getType());
		metadataDefinition.setDefaultValue(metadataDefinitionModel.getDefaultValue());
		metadataDefinition.setMetadataNameForAutoCompleteWithMetadata(metadataDefinitionModel.getMetadataNameForAutoCompleteWithMetadata());
		if (metadataDefinitionModel.getTypeOfAutoCompleteWithMetadata() != null) {
			metadataDefinition.setTypeOfAutoCompleteWithMetadata(TypeOfAutoCompleteWithMetadata.valueOf(metadataDefinitionModel.getTypeOfAutoCompleteWithMetadata()));
		}
		metadataDefinition.setNomenclatorAttributeKeyForAutoCompleteWithMetadata(metadataDefinitionModel.getNomenclatorAttributeKeyForAutoCompleteWithMetadata());
		metadataDefinition.setClassNameForAutoCompleteWithMetadata(metadataDefinitionModel.getClassNameForAutoCompleteWithMetadata());
		
		return metadataDefinition;
	}
}