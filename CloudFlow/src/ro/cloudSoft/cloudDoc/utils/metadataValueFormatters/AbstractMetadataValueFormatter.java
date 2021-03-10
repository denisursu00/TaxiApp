package ro.cloudSoft.cloudDoc.utils.metadataValueFormatters;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Iterables;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition;
import ro.cloudSoft.cloudDoc.domain.organization.Group;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.calendar.CalendarModel;
import ro.cloudSoft.cloudDoc.services.calendar.CalendarService;
import ro.cloudSoft.cloudDoc.services.content.DocumentService;
import ro.cloudSoft.cloudDoc.services.nomenclator.NomenclatorService;
import ro.cloudSoft.cloudDoc.services.organization.GroupService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.cloudDoc.utils.MetadataValueHelper;
import ro.cloudSoft.cloudDoc.utils.listItemLabelProviders.ListItemLabelProvider;

/**
 * Formateaza valori de metadate.
 * 
 * 
 */
public abstract class AbstractMetadataValueFormatter {
	
	private static final String DATE_FOR_DISPLAY = "dd.MM.yyyy";
	private static final String DATE_TIME_FOR_DISPLAY = "dd.MM.yyyy HH:mm";
	private static final String NUMBER_FOR_DISPLAY = "0.######";
	
	private static final String TEXT_FOR_INEXISTING_ENTITY = "(sters)";
	/**
	 * Text ce va fi folosit in cazul in care se cere numele unui utilizator care nu (mai) exista
	 */
	private static final String TEXT_FOR_INEXISTING_USER = TEXT_FOR_INEXISTING_ENTITY;
	
	/**
	 * Text ce va fi folosit in cazul in care se cere valoarea unui nomenclator care nu mai e.
	 */
	private static final String TEXT_FOR_INEXISTING_NOMENCLATOR_VALUE = TEXT_FOR_INEXISTING_ENTITY;
	/**
	 * Text ce va fi folosit in cazul in care se cere valoarea unui grup care nu mai e.
	 */
	private static final String TEXT_FOR_INEXISTING_GROUP_VALUE = TEXT_FOR_INEXISTING_ENTITY;
	private static final String TEXT_FOR_INEXISTING_CALENDAR_VALUE = TEXT_FOR_INEXISTING_ENTITY;
	private static final String TEXT_FOR_INEXISTING_DOCUMENT_VALUE = TEXT_FOR_INEXISTING_ENTITY;
	private static final String TEXT_FOR_INEXISTING_PROJECT_VALUE = TEXT_FOR_INEXISTING_ENTITY;
	
	private final UserService userService;
	private final NomenclatorService nomenclatorService;
	private final GroupService groupService;
	private final DocumentService documentService;
	private final CalendarService calendarService;
	private final ProjectService projectService;
	
	protected AbstractMetadataValueFormatter(UserService userService, NomenclatorService nomenclatorService, 
			GroupService groupService, DocumentService documentService, CalendarService calendarService,
			ProjectService projectService) {
		this.userService = userService;
		this.nomenclatorService = nomenclatorService;
		this.groupService = groupService;
		this.documentService = documentService;
		this.calendarService = calendarService;
		this.projectService = projectService;
	}
	
	private String getSingleValue(Collection<String> metadataValues) {
		return Iterables.getOnlyElement(metadataValues, null);
	}
	
	private String getUserDisplayName(Long userId) {
		String displayName = userService.getDisplayName(userId);
		return (displayName != null) ? displayName : TEXT_FOR_INEXISTING_USER;
	}
	
	private String getNomenclatorDisplayValue(Long nomenclatorId) {
		String nomenclatorUiValue = nomenclatorService.getNomenclatorUiValue(nomenclatorId);
		return (nomenclatorUiValue != null) ? nomenclatorUiValue : TEXT_FOR_INEXISTING_NOMENCLATOR_VALUE;
	}
	
	private String getDocumentDisplayValue(Collection<String> metadataDocumentValues) {
		if (CollectionUtils.isEmpty(metadataDocumentValues)) {
			return "";
		}
		List<String> documentNames = new ArrayList<>();
		for (String documentLocationRealNameAndDocumentId : metadataDocumentValues) {
			String[] documentLocationRealNameAndDocumentIdArray = StringUtils.splitByWholeSeparator(documentLocationRealNameAndDocumentId, ":");
			String documentName;
			try {
				documentName = documentService.getDocumentName(documentLocationRealNameAndDocumentIdArray[0], documentLocationRealNameAndDocumentIdArray[1]);
			} catch (AppException e) {
				throw new RuntimeException(e);
			}
			documentNames.add("[" + documentName + "]");
			
		}
		return StringUtils.join(documentNames, ", ");
	}
	
	private String getProjectDisplayValue(Collection<String> metadataProjectValues) {
		if (CollectionUtils.isEmpty(metadataProjectValues)) {
			return "";
		}
		List<String> projectNames = new ArrayList<>();
		for (String projectId : metadataProjectValues) {
			String projectName = projectService.getProjectNameById(Long.valueOf(projectId));
			if (StringUtils.isBlank(projectName)) {
				projectName = TEXT_FOR_INEXISTING_PROJECT_VALUE;
			}
			projectNames.add("[" + projectName + "]");
		}
		return StringUtils.join(projectNames, ", ");
	}
	
	private String getGroupDisplayName(Long groupId) {
		Group group = groupService.getGroupById(groupId);
		return (group != null) ? group.getName() : TEXT_FOR_INEXISTING_GROUP_VALUE;
	}
	
	private String getCalendarDisplayValue(Long calendarId) {
		CalendarModel calendar = calendarService.get(calendarId);
		return (calendar != null) ? calendar.getName() : TEXT_FOR_INEXISTING_CALENDAR_VALUE;
	}

	protected String format(String metadataName, String metadataType,
			Collection<String> metadataValues, ListItemLabelProvider listItemLabelProvider) {
		
		if (metadataType.equals(MetadataDefinition.TYPE_TEXT)) {
			String text = MetadataValueHelper.getText(getSingleValue(metadataValues));
			return text;
		} else if (metadataType.equals(MetadataDefinition.TYPE_NUMERIC)) {
			BigDecimal number = MetadataValueHelper.getNumber(getSingleValue(metadataValues));
			NumberFormat formatter = new DecimalFormat(NUMBER_FOR_DISPLAY);
			return formatter.format(number);
		} else if (metadataType.equals(MetadataDefinition.TYPE_AUTO_NUMBER)) {
			String autoNumberValue = MetadataValueHelper.getAutoNumberValue(getSingleValue(metadataValues));
			return autoNumberValue;
		} else if (metadataType.equals(MetadataDefinition.TYPE_DATE)) {
			Date date = MetadataValueHelper.getDate(getSingleValue(metadataValues));
			DateFormat formatter = new SimpleDateFormat(DATE_FOR_DISPLAY);
			return formatter.format(date);
		} else if (metadataType.equals(MetadataDefinition.TYPE_DATE_TIME)) {
			Date date = MetadataValueHelper.getDateTime(getSingleValue(metadataValues));
			DateFormat formatter = new SimpleDateFormat(DATE_TIME_FOR_DISPLAY);
			return formatter.format(date);
		} else if (metadataType.equals(MetadataDefinition.TYPE_MONTH)) {
			// TODO - De refacut zona cu month.
			String yearMonth = getSingleValue(metadataValues);
			if (StringUtils.isNotBlank(yearMonth)) {
				String[] yearMonthArray = StringUtils.split(yearMonth, ".");
				return yearMonthArray[1] + "/" + yearMonthArray[0];
			}
			return null;
		} else if (metadataType.equals(MetadataDefinition.TYPE_LIST)) {
			List<String> listItemValues = MetadataValueHelper.getListItemValues(metadataValues);
			List<String> listItemLabels = listItemLabelProvider.getListItemLabels(metadataName, listItemValues);
			String listItemLabelsJoined = StringUtils.join(listItemLabels, ", ");
			return listItemLabelsJoined;
		} else if (metadataType.equals(MetadataDefinition.TYPE_USER)) {
			Long userId = MetadataValueHelper.getUserId(getSingleValue(metadataValues));
			String userDisplayName = getUserDisplayName(userId);
			return userDisplayName;
		} else if (metadataType.equals(MetadataDefinition.TYPE_TEXT_AREA)) {
			String textAreaValue = MetadataValueHelper.getTextAreaValue(getSingleValue(metadataValues));
			return textAreaValue;
		} else if (metadataType.equals(MetadataDefinition.TYPE_NOMENCLATOR)) {
			Long nomenclatorId = MetadataValueHelper.getNomenclatorValueId(getSingleValue(metadataValues));
			return getNomenclatorDisplayValue(nomenclatorId);
		} else if (metadataType.equals(MetadataDefinition.TYPE_METADATA_COLLECTION)) {
			String exceptionMessage = "O colectie de metadate NU poate fi formatata.";
			throw new IllegalArgumentException(exceptionMessage);
		} else if (metadataType.equals(MetadataDefinition.TYPE_GROUP)) {
			Long groupId = MetadataValueHelper.getGroupId(getSingleValue(metadataValues));
			return getGroupDisplayName(groupId);
		} else if (metadataType.equals(MetadataDefinition.TYPE_DOCUMENT)) {
			return getDocumentDisplayValue(metadataValues);
		} else if (metadataType.equals(MetadataDefinition.TYPE_CALENDAR)) {
			Long calendarId = MetadataValueHelper.getCalendarId(getSingleValue(metadataValues));
			return getCalendarDisplayValue(calendarId);
		} else if (metadataType.equals(MetadataDefinition.TYPE_PROJECT)) {
			return getProjectDisplayValue(metadataValues);
		} else {
			String exceptionMessage = "Tipul de metadata [" + metadataType + "] NU este cunoscut de aplicatie.";
			throw new IllegalArgumentException(exceptionMessage);
		}
	}
}