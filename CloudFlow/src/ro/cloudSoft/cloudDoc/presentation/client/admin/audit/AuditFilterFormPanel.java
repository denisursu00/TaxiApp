package ro.cloudSoft.cloudDoc.presentation.client.admin.audit;

import ro.cloudSoft.cloudDoc.presentation.client.admin.audit.AuditSearchResultsGrid.AuditSearchCriteriaProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditEntityOperation;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditEntityType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit.GwtAuditSearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.EnumWithLocalizedLabelComboBox;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.organization.UserComboBox;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;

public class AuditFilterFormPanel extends FormPanel implements AuditSearchCriteriaProvider {
	
	private DateField dateTimeIntervalStartDateField;
	private DateField dateTimeIntervalEndDateField;
	
	private UserComboBox userComboBox;
	
	private EnumWithLocalizedLabelComboBox<GwtAuditEntityType> entityTypeComboBox;
	
	private TextField<String> entityIdentifierTextField;
	private TextField<String> entityDisplayNameTextField;
	
	private EnumWithLocalizedLabelComboBox<GwtAuditEntityOperation> operationComboBox;
	
	private Button resetButton;
	private Button searchButton;
	
	public AuditFilterFormPanel(final Runnable doOnSearch) {
		
		dateTimeIntervalStartDateField = new DateField();
		dateTimeIntervalStartDateField.setFieldLabel(GwtLocaleProvider.getConstants().START_DATE());
		dateTimeIntervalStartDateField.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		
		dateTimeIntervalEndDateField = new DateField();
		dateTimeIntervalEndDateField.setFieldLabel(GwtLocaleProvider.getConstants().END_DATE());
		dateTimeIntervalEndDateField.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		
		userComboBox = new UserComboBox();
		userComboBox.setFieldLabel(GwtLocaleProvider.getConstants().USER());
		
		entityTypeComboBox = new EnumWithLocalizedLabelComboBox<GwtAuditEntityType>(GwtAuditEntityType.class);
		entityTypeComboBox.setFieldLabel(GwtLocaleProvider.getConstants().ENTITY_TYPE());
		
		entityIdentifierTextField = new TextField<String>();
		entityIdentifierTextField.setFieldLabel(GwtLocaleProvider.getConstants().ENTITY_IDENTIFIER());
		
		entityDisplayNameTextField = new TextField<String>();
		entityDisplayNameTextField.setFieldLabel(GwtLocaleProvider.getConstants().ENTITY_DISPLAY_NAME());
		
		operationComboBox = new EnumWithLocalizedLabelComboBox<GwtAuditEntityOperation>(GwtAuditEntityOperation.class);
		operationComboBox.setFieldLabel(GwtLocaleProvider.getConstants().OPERATION());

		FormData formData = ComponentUtils.FORM_DATA_FOR_FIELDS_WITH_ALL_AVAILABLE_WIDTH;
		
		add(dateTimeIntervalStartDateField, formData);
		add(dateTimeIntervalEndDateField, formData);
		add(userComboBox, formData);
		add(entityTypeComboBox, formData);
		add(entityIdentifierTextField, formData);
		add(entityDisplayNameTextField, formData);
		add(operationComboBox, formData);
		
		resetButton = new Button();
		resetButton.setText(GwtLocaleProvider.getConstants().RESET());
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent e) {
				clear();
			}
		});
		
		searchButton = new Button();
		searchButton.setText(GwtLocaleProvider.getConstants().SEARCH());
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent e) {
				doOnSearch.run();
			}
		});
		
		getButtonBar().add(resetButton);
		getButtonBar().add(searchButton);
	}
	
	@Override
	public GwtAuditSearchCriteria getSearchCriteria() {
		
		GwtAuditSearchCriteria searchCriteria = new GwtAuditSearchCriteria();
		
		searchCriteria.setStartDate(dateTimeIntervalStartDateField.getValue());
		searchCriteria.setEndDate(dateTimeIntervalEndDateField.getValue());
		
		searchCriteria.setUserId(userComboBox.getIdOfSelectedUser());
		
		searchCriteria.setEntityType(entityTypeComboBox.getEnumConstantOfSelectedItem());
		
		searchCriteria.setEntityIdentifierTextFragment(entityIdentifierTextField.getValue());
		searchCriteria.setEntityDisplayNameTextFragment(entityDisplayNameTextField.getValue());
		
		searchCriteria.setOperation(operationComboBox.getEnumConstantOfSelectedItem());
		
		return searchCriteria;
	}
}