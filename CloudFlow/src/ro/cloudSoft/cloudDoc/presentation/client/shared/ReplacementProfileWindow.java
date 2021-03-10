package ro.cloudSoft.cloudDoc.presentation.client.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtReplacementProfilesOutOfOfficeConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentWithRenderingProblems;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRunnableUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.PropertyEditors;
import ro.cloudSoft.cloudDoc.presentation.client.shared.validators.DateFieldDependentOnOtherValidator;
import ro.cloudSoft.cloudDoc.presentation.client.shared.validators.DateFieldDependentOnOtherValidator.ComparisonType;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 */
public class ReplacementProfileWindow extends Window implements ComponentWithRenderingProblems {
	
	private FormPanel formPanel;
	
	private HiddenField<Long> idHiddenField;
	
	private SimpleComboBox<String> requesterUsernameComboBox;
	private ComboBox<UserModel> replacementComboBox;
	
	private CheckBoxListView<UserModel> selectedRequesterUserProfilesCheckBoxListView;
	private LayoutContainer selectedRequesterUserProfilesLayoutContainer;
	private AdapterField selectedRequesterUserProfilesAdapterField;
	
	private TextArea commentsTextArea;
	
	private DateField startDateField;
	private DateField endDateField;
	
	private Grid<ReplacementProfileModel> selectedReplacementProfilesInWhichRequesterIsReplacementGrid;
	private ContentPanel selectedReplacementProfilesInWhichRequesterIsReplacementContentPanel;
	private AdapterField selectedReplacementProfilesInWhichRequesterIsReplacementAdapterField;
	
	private CheckBox outOfOfficeCheckBox;
	
	private FieldSet outOfOfficeEmailSettingsFieldSet;
	
	private TextField<String> outOfOfficeEmailSubjectTemplateTextField;
	private TextArea outOfOfficeEmailBodyTemplateTextArea;
	
	private Button saveButton;
	private Button returnedButton;
	private Button cancelButton;
	
	private SelectionChangedListener<SimpleComboValue<String>> requesterUsernameComboBoxSelectionChangedListener;
	private Listener<BaseEvent> selectedReplacementProfilesInWhichRequesterIsReplacementDependencyFieldChangeListener;
	private Listener<BaseEvent> outOfOfficeCheckBoxChangeListener;
	
	public ReplacementProfileWindow() {
		
		setHeading(GwtLocaleProvider.getConstants().REPLACEMENT_PROFILE());
		setLayout(new FitLayout());
		setMaximizable(false);
		setModal(true);
		setResizable(false);
		setSize(960, 600);
		
		initFormPanel();
		initChangeListeners();
		initButtons();
		populateStaticData();
		
		LoadingManager.get().registerComponentWithRenderingProblems(this);
	}
	
	private void initFormPanel() {
		
		formPanel = new FormPanel();
		formPanel.setBodyBorder(false);
		formPanel.setBorders(false);
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(150);
		formPanel.setScrollMode(Scroll.AUTO);
		add(formPanel, new FitData(5));
		
		FormData formData = new FormData("92%");
		
		idHiddenField = new HiddenField<Long>();
		idHiddenField.setPropertyEditor(PropertyEditors.LONG);
		formPanel.add(idHiddenField);
		
		requesterUsernameComboBox = new SimpleComboBox<String>();
		requesterUsernameComboBox.setAllowBlank(false);
		requesterUsernameComboBox.setEditable(false);
		requesterUsernameComboBox.setFieldLabel(GwtLocaleProvider.getConstants().REQUESTER());
		requesterUsernameComboBox.setReadOnly(!GwtRegistryUtils.getUserSecurity().isUserAdmin());
		requesterUsernameComboBox.setTriggerAction(TriggerAction.ALL);
		formPanel.add(requesterUsernameComboBox, formData);
		
		replacementComboBox = new ComboBox<UserModel>();
		replacementComboBox.setAllowBlank(false);
		replacementComboBox.setDisplayField(UserModel.USER_PROPERTY_DISPLAY_NAME);
		replacementComboBox.setEditable(false);
		replacementComboBox.setFieldLabel(GwtLocaleProvider.getConstants().REPLACEMENT());
		replacementComboBox.setStore(AppStoreCache.getUserListStore());
		replacementComboBox.setMinListWidth(400);
		replacementComboBox.setTriggerAction(TriggerAction.ALL);
		replacementComboBox.setValidator(new Validator() {
			
			@Override
			public String validate(Field<?> field, String value) {
				
				UserModel selectedReplacement = replacementComboBox.getValue();
				
				if (selectedReplacement == null) {
					return GwtLocaleProvider.getMessages().REQUIRED_FIELD();
				}
				
				String selectedRequesterUsername = requesterUsernameComboBox.getSimpleValue();
				if (GwtStringUtils.isNotBlank(selectedRequesterUsername)) {
					String selectedReplacementUsername = selectedReplacement.getUserName();
					if (selectedReplacementUsername.equalsIgnoreCase(selectedRequesterUsername)) {
						return GwtLocaleProvider.getMessages().REPLACEMENT_CANNOT_BE_THE_REQUESTER();
					}
				}
				
				return null;
			}
		});
		formPanel.add(replacementComboBox, formData);
		
		selectedRequesterUserProfilesLayoutContainer = new LayoutContainer();
		selectedRequesterUserProfilesLayoutContainer.setSize(686, 80);
		selectedRequesterUserProfilesLayoutContainer.setLayout(new FitLayout());
		
		selectedRequesterUserProfilesAdapterField = new AdapterField(selectedRequesterUserProfilesLayoutContainer) {
			
			@Override
			public boolean isValid(boolean preventMark) {
				if (selectedRequesterUserProfilesCheckBoxListView.getChecked().isEmpty()) {
					if (!preventMark) {
						markInvalid(GwtLocaleProvider.getMessages().REPLACEMENT_PROFILE_MUST_HAVE_USER_PROFILES_SELECTED());
					}
					return false;
				} else {
					clearInvalid();
					return true;
				}
			}
		};
		selectedRequesterUserProfilesAdapterField.setFieldLabel(GwtLocaleProvider.getConstants().PROFILES());
		formPanel.add(selectedRequesterUserProfilesAdapterField, formData);
		
		selectedRequesterUserProfilesCheckBoxListView = new CheckBoxListView<UserModel>() {
			
			@Override
			public List<UserModel> getChecked() {
				if (this.isRendered()) {
					return super.getChecked();
				} else {
					if (checkedPreRender != null) {
						return checkedPreRender;
					} else {
						return Collections.emptyList();
					}
				}
			}
		};
		selectedRequesterUserProfilesCheckBoxListView.setDisplayProperty(UserModel.USER_PROPERTY_TITLE);
		selectedRequesterUserProfilesCheckBoxListView.setStore(new ListStore<UserModel>());
		selectedRequesterUserProfilesLayoutContainer.add(selectedRequesterUserProfilesCheckBoxListView);
		
		commentsTextArea = new TextArea();
		commentsTextArea.setFieldLabel(GwtLocaleProvider.getConstants().COMMENTS());
		formPanel.add(commentsTextArea, formData);
		
		startDateField = new DateField();
		startDateField.setAllowBlank(false);
		startDateField.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		startDateField.setEditable(false);
		startDateField.setFieldLabel(GwtLocaleProvider.getConstants().START_DATE());
		formPanel.add(startDateField, formData);
		
		endDateField = new DateField();
		endDateField.setAllowBlank(false);
		endDateField.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		endDateField.setEditable(false);
		endDateField.setFieldLabel(GwtLocaleProvider.getConstants().END_DATE());
		formPanel.add(endDateField, formData);
		
		selectedReplacementProfilesInWhichRequesterIsReplacementContentPanel = new ContentPanel();
		selectedReplacementProfilesInWhichRequesterIsReplacementContentPanel.setHeaderVisible(false);
		selectedReplacementProfilesInWhichRequesterIsReplacementContentPanel.setSize(686, 160);
		selectedReplacementProfilesInWhichRequesterIsReplacementContentPanel.setLayout(new FitLayout());
		
		selectedReplacementProfilesInWhichRequesterIsReplacementAdapterField = new AdapterField(selectedReplacementProfilesInWhichRequesterIsReplacementContentPanel);
		selectedReplacementProfilesInWhichRequesterIsReplacementAdapterField.setFieldLabel("Profile pt. care solicitantul e inlocuitor");
		formPanel.add(selectedReplacementProfilesInWhichRequesterIsReplacementAdapterField, formData);
		
		selectedReplacementProfilesInWhichRequesterIsReplacementGrid = createSelectedReplacementProfilesInWhichRequesterIsReplacementGrid();
		selectedReplacementProfilesInWhichRequesterIsReplacementContentPanel.add(selectedReplacementProfilesInWhichRequesterIsReplacementGrid);
		
		startDateField.setValidator(new DateFieldDependentOnOtherValidator(ComparisonType.LESS_THAN_OR_EQUAL, startDateField, endDateField));
		endDateField.setValidator(new DateFieldDependentOnOtherValidator(ComparisonType.MORE_THAN_OR_EQUAL, endDateField, startDateField));
		
		outOfOfficeCheckBox = new CheckBox();
		outOfOfficeCheckBox.addListener(Events.Change, new Listener<BaseEvent> () {
			
			@Override
			public void handleEvent(BaseEvent event) {
				
				boolean isChecked = ComponentUtils.isChecked(outOfOfficeCheckBox);
				
				outOfOfficeEmailSettingsFieldSet.setEnabled(isChecked);
				
				outOfOfficeEmailSubjectTemplateTextField.setAllowBlank(!isChecked);
				outOfOfficeEmailBodyTemplateTextArea.setAllowBlank(!isChecked);
			}
		});
		outOfOfficeCheckBox.setBoxLabel("");
		outOfOfficeCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().OUT_OF_OFFICE());
		formPanel.add(outOfOfficeCheckBox);
		
		outOfOfficeEmailSettingsFieldSet = new FieldSet();
		outOfOfficeEmailSettingsFieldSet.setHeading(GwtLocaleProvider.getConstants().OUT_OF_OFFICE_EMAIL_SETTINGS());
		outOfOfficeEmailSettingsFieldSet.setLayout(new FormLayout());
		formPanel.add(outOfOfficeEmailSettingsFieldSet);
		
		outOfOfficeEmailSubjectTemplateTextField = new TextField<String>();
		outOfOfficeEmailSubjectTemplateTextField.setFieldLabel(GwtLocaleProvider.getConstants().EMAIL_SUBJECT());
		outOfOfficeEmailSettingsFieldSet.add(outOfOfficeEmailSubjectTemplateTextField, formData);
		
		outOfOfficeEmailBodyTemplateTextArea = new TextArea();
		outOfOfficeEmailBodyTemplateTextArea.setFieldLabel(GwtLocaleProvider.getConstants().EMAIL_BODY());
		outOfOfficeEmailSettingsFieldSet.add(outOfOfficeEmailBodyTemplateTextArea, formData);
	}
	
	private Grid<ReplacementProfileModel> createSelectedReplacementProfilesInWhichRequesterIsReplacementGrid() {
		
		List<ColumnConfig> columnConfigs = new LinkedList<ColumnConfig>();
		
		CheckBoxSelectionModel<ReplacementProfileModel> checkBoxSelectionModel = new CheckBoxSelectionModel<ReplacementProfileModel>();
		columnConfigs.add(checkBoxSelectionModel.getColumn());
		
		ColumnConfig requesterUsernameColumnConfig = new ColumnConfig();
		requesterUsernameColumnConfig.setHeader(GwtLocaleProvider.getConstants().REQUESTER());
		requesterUsernameColumnConfig.setId(ReplacementProfileModel.PROPERTY_REQUESTER_USERNAME);
		requesterUsernameColumnConfig.setWidth(200);
		columnConfigs.add(requesterUsernameColumnConfig);
		
		ColumnConfig replacementColumnConfig = new ColumnConfig();
		replacementColumnConfig.setHeader(GwtLocaleProvider.getConstants().REPLACEMENT());
		replacementColumnConfig.setId(ReplacementProfileModel.PROPERTY_REPLACEMENT_DISPLAY_NAME);
		replacementColumnConfig.setWidth(200);
		columnConfigs.add(replacementColumnConfig);
		
		ColumnConfig startDateColumnConfig = new ColumnConfig();
		startDateColumnConfig.setDateTimeFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		startDateColumnConfig.setHeader(GwtLocaleProvider.getConstants().START_DATE());
		startDateColumnConfig.setId(ReplacementProfileModel.PROPERTY_START_DATE);
		startDateColumnConfig.setWidth(100);
		columnConfigs.add(startDateColumnConfig);
		
		ColumnConfig endDateColumnConfig = new ColumnConfig();
		endDateColumnConfig.setDateTimeFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		endDateColumnConfig.setHeader(GwtLocaleProvider.getConstants().END_DATE());
		endDateColumnConfig.setId(ReplacementProfileModel.PROPERTY_END_DATE);
		endDateColumnConfig.setWidth(100);
		columnConfigs.add(endDateColumnConfig);
		
		ListStore<ReplacementProfileModel> store = new ListStore<ReplacementProfileModel>();
		ColumnModel columnModel = new ColumnModel(columnConfigs);
		
		Grid<ReplacementProfileModel> selectedReplacementProfilesInWhichRequesterIsReplacementGrid = new Grid<ReplacementProfileModel>(store, columnModel);
		selectedReplacementProfilesInWhichRequesterIsReplacementGrid.setAutoExpandColumn(requesterUsernameColumnConfig.getId());
		selectedReplacementProfilesInWhichRequesterIsReplacementGrid.setAutoExpandMax(1000);
		selectedReplacementProfilesInWhichRequesterIsReplacementGrid.setSelectionModel(checkBoxSelectionModel);
		selectedReplacementProfilesInWhichRequesterIsReplacementGrid.addPlugin(checkBoxSelectionModel);
		
		return selectedReplacementProfilesInWhichRequesterIsReplacementGrid;
	}
	
	private void initChangeListeners() {
		requesterUsernameComboBoxSelectionChangedListener = new SelectionChangedListener<SimpleComboValue<String>>() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> event) {
				populateSelectedRequesterUserProfilesCheckBoxListView(Collections.<UserModel> emptySet(), GwtRunnableUtils.DO_NOTHING);
			}
		};
		selectedReplacementProfilesInWhichRequesterIsReplacementDependencyFieldChangeListener = new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent event) {
				populateSelectedReplacementProfilesInWhichRequesterIsReplacementGrid(Collections.<Long> emptySet(), GwtRunnableUtils.DO_NOTHING);
			}
		};
		outOfOfficeCheckBoxChangeListener = new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent event) {
				
				if (!ComponentUtils.isChecked(outOfOfficeCheckBox)) {
					return;
				}
				
				if (GwtStringUtils.isBlank(outOfOfficeEmailSubjectTemplateTextField.getValue())
						&& GwtStringUtils.isBlank(outOfOfficeEmailBodyTemplateTextArea.getValue())) {
					
					GwtReplacementProfilesOutOfOfficeConstants outOfOfficeConstants = GwtRegistryUtils.getReplacementProfilesOutOfOfficeConstants();
					
					outOfOfficeEmailSubjectTemplateTextField.setValue(outOfOfficeConstants.getDefaultTemplateForEmailSubject());
					outOfOfficeEmailBodyTemplateTextArea.setValue(outOfOfficeConstants.getDefaultTemplateForEmailBody());
				}
			}
		};
	}
	
	private void initButtons() {
		
		saveButton = new Button();
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				
				if (!isValid()) {
					return;
				}
				
				ReplacementProfileModel replacementProfile = getReplacementProfileFromForm();
				
				LoadingManager.get().loading();
				GwtServiceProvider.getReplacementProfilesService().saveReplacementProfile(replacementProfile, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					
					@Override
					public void onSuccess(Void nothing) {
						MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(),
							GwtLocaleProvider.getMessages().REPLACEMENT_PROFILE_SAVED());
						AppEventController.fireEvent(AppEventType.ReplacementProfile);
						hide();
						LoadingManager.get().loadingComplete();
					}
				});
			}
		});
		saveButton.setText(GwtLocaleProvider.getConstants().SAVE());
		getButtonBar().add(saveButton);
		
		returnedButton = new Button();
		returnedButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				ComponentUtils.confirm(GwtLocaleProvider.getConstants().RETURNED(),
						GwtLocaleProvider.getMessages().CONFIRM_REPLACEMENT_PROFILE_RETURN_OPERATION(),
						new ConfirmCallback() {
						
					@Override
					public void onYes() {
						
						Long replacementProfileId = idHiddenField.getValue();
						if (replacementProfileId == null) {
							return;
						}
						
						LoadingManager.get().loading();
						GwtServiceProvider.getReplacementProfilesService().returned(replacementProfileId, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							
							@Override
							public void onSuccess(Void nothing) {
								MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(),
									GwtLocaleProvider.getMessages().REPLACEMENT_PROFILE_RETURN_OPERATION_SUCCEDED());
								AppEventController.fireEvent(AppEventType.ReplacementProfile);
								hide();
								LoadingManager.get().loadingComplete();
							}
						});
					}
				});
				
			}
		});
		returnedButton.setText(GwtLocaleProvider.getConstants().RETURNED());
		getButtonBar().add(returnedButton);
		
		cancelButton = new Button();
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				hide();
			}
		});
		cancelButton.setText(GwtLocaleProvider.getConstants().CANCEL());
		getButtonBar().add(cancelButton);
	}
	
	private void populateStaticData() {
		LoadingManager.get().loading();
		GwtServiceProvider.getOrgService().getAllUsernames(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			
			@Override
			public void onSuccess(List<String> allUsernames) {
				requesterUsernameComboBox.add(allUsernames);
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	@Override
	public boolean needsEnsuringIsProperlyRenderedNow() {
		return isVisible();
	}
	
	@Override
	public void ensureIsProperlyRendered() {
		ComponentUtils.ensureGridIsProperlyRendered(selectedReplacementProfilesInWhichRequesterIsReplacementGrid);
	}
	
	/**
	 * Populeaza lista cu profile ale utilizatorului titular pe baza valorii selectate in combo box-ul pentru titular.
	 * 
	 * @param selectedRequesterUserProfiles profilele care trebuiesc selectate (bifate)
	 * @param doAfterPopulating ce sa faca dupa ce s-a populat lista
	 */
	private void populateSelectedRequesterUserProfilesCheckBoxListView(
			final Collection<UserModel> selectedRequesterUserProfiles,
			final Runnable doAfterPopulating) {

		selectedRequesterUserProfilesCheckBoxListView.getStore().removeAll();
		
		String selectedRequesterUsername = requesterUsernameComboBox.getSimpleValue();
		if (GwtStringUtils.isBlank(selectedRequesterUsername)) {
			doAfterPopulating.run();
			return;
		}
		
		LoadingManager.get().loading();
		GwtServiceProvider.getOrgService().getUsersWithUsername(selectedRequesterUsername, new AsyncCallback<List<UserModel>>() {

			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			
			@Override
			public void onSuccess(List<UserModel> usersWithUsername) {
				
				selectedRequesterUserProfilesCheckBoxListView.getStore().add(usersWithUsername);
				
				for (UserModel selectedRequesterUserProfile : selectedRequesterUserProfiles) {
					UserModel selectedRequesterUserProfileFromListView = selectedRequesterUserProfilesCheckBoxListView
						.getStore().findModel(UserModel.USER_PROPERTY_USERID, selectedRequesterUserProfile.getUserId());
					if (selectedRequesterUserProfileFromListView != null) {
						selectedRequesterUserProfilesCheckBoxListView.setChecked(selectedRequesterUserProfileFromListView, true);
					}
				}
				
				doAfterPopulating.run();

				LoadingManager.get().loadingComplete();
			}
		});
	}

	/**
	 * Populeaza grid-ul cu profile de inlocuire selectate in care titularul este inlocuitor
	 * pe baza valorilor din formular (profilele de utilizator selectate ale titularului, perioada inlocuirii).
	 * 
	 * @param idsForSelectedReplacementProfilesInWhichRequesterIsReplacement ID-urile profilelor care trebuiesc selectate (bifate)
	 * @param doAfterPopulating ce sa faca dupa ce s-a populat grid-ul
	 */
	private void populateSelectedReplacementProfilesInWhichRequesterIsReplacementGrid(
			final Collection<Long> idsForSelectedReplacementProfilesInWhichRequesterIsReplacement,
			final Runnable doAfterPopulating) {
		selectedReplacementProfilesInWhichRequesterIsReplacementGrid.getStore().removeAll();

		Long idForRequestingReplacementProfile = idHiddenField.getValue();
		
		Collection<Long> idsForRequesterUserProfiles = new ArrayList<Long>();
		for (UserModel selectedRequesterUserProfile : selectedRequesterUserProfilesCheckBoxListView.getChecked()) {
			idsForRequesterUserProfiles.add(selectedRequesterUserProfile.getUserIdAsLong());
		}
		
		Date startDate = startDateField.getValue();
		Date endDate = endDateField.getValue();
		
		if (idsForRequesterUserProfiles.isEmpty() || (startDate == null) || (endDate == null)) {
			doAfterPopulating.run();
			return;
		}

		LoadingManager.get().loading();
		GwtServiceProvider.getReplacementProfilesService().getAvailableReplacementProfilesInWhichRequesterIsReplacement(
				idForRequestingReplacementProfile, idsForRequesterUserProfiles, startDate, endDate,
				new AsyncCallback<List<ReplacementProfileModel>>() {
			
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			
			@Override
			public void onSuccess(List<ReplacementProfileModel> availableReplacementProfilesInWhichRequesterIsReplacement) {
				
				selectedReplacementProfilesInWhichRequesterIsReplacementGrid.getStore().add(availableReplacementProfilesInWhichRequesterIsReplacement);
				
				for (Long idForSelectedReplacementProfileInWhichRequesterIsReplacement : idsForSelectedReplacementProfilesInWhichRequesterIsReplacement) {
					ReplacementProfileModel selectedReplacementProfileInWhichRequesterIsReplacement = selectedReplacementProfilesInWhichRequesterIsReplacementGrid.getStore().findModel(ReplacementProfileModel.PROPERTY_ID, idForSelectedReplacementProfileInWhichRequesterIsReplacement);
					if (selectedReplacementProfileInWhichRequesterIsReplacement != null) {
						selectedReplacementProfilesInWhichRequesterIsReplacementGrid.getSelectionModel().select(selectedReplacementProfileInWhichRequesterIsReplacement, true);
					}
				}
				
				doAfterPopulating.run();
				
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private ReplacementProfileModel getReplacementProfileFromForm() {
		
		ReplacementProfileModel replacementProfile = new ReplacementProfileModel();
		
		replacementProfile.setId(idHiddenField.getValue());
		replacementProfile.setRequesterUsername(requesterUsernameComboBox.getSimpleValue());
		replacementProfile.setReplacement(replacementComboBox.getValue());
		replacementProfile.setSelectedRequesterUserProfiles(selectedRequesterUserProfilesCheckBoxListView.getChecked());
		
		Set<Long> idsForSelectedReplacementProfilesInWhichRequesterIsReplacement = new HashSet<Long>();
		for (ReplacementProfileModel selectedReplacementProfilesInWhichRequesterIsReplacement : selectedReplacementProfilesInWhichRequesterIsReplacementGrid.getSelectionModel().getSelectedItems()) {
			idsForSelectedReplacementProfilesInWhichRequesterIsReplacement.add(selectedReplacementProfilesInWhichRequesterIsReplacement.getId());
		}
		replacementProfile.setIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement(idsForSelectedReplacementProfilesInWhichRequesterIsReplacement);
		
		replacementProfile.setComments(commentsTextArea.getValue());
		replacementProfile.setStartDate(startDateField.getValue());
		replacementProfile.setEndDate(endDateField.getValue());
		
		boolean outOfOffice = ComponentUtils.getCheckBoxValue(outOfOfficeCheckBox);
		replacementProfile.setOutOfOffice(outOfOffice);
		if (outOfOffice) {
			replacementProfile.setOutOfOfficeEmailSubjectTemplate(outOfOfficeEmailSubjectTemplateTextField.getValue());
			replacementProfile.setOutOfOfficeEmailBodyTemplate(outOfOfficeEmailBodyTemplateTextArea.getValue());
		} else {
			replacementProfile.setOutOfOfficeEmailSubjectTemplate(null);
			replacementProfile.setOutOfOfficeEmailBodyTemplate(null);
		}
		
		return replacementProfile;
	}
	
	private void reset() {
		
		formPanel.clear();
		
		selectedRequesterUserProfilesCheckBoxListView.getStore().removeAll();
		selectedReplacementProfilesInWhichRequesterIsReplacementGrid.getStore().removeAll();
	}
	
	public void prepareForAdd() {
		
		deactivateChangeListeners();
		
		reset();

		String usernameForCurrentUser = GwtRegistryUtils.getUserSecurity().getUserUsername();
		requesterUsernameComboBox.setSimpleValue(usernameForCurrentUser);
		
		populateSelectedRequesterUserProfilesCheckBoxListView(Collections.<UserModel> emptySet(), new Runnable() {
			
			@Override
			public void run() {
				populateSelectedReplacementProfilesInWhichRequesterIsReplacementGrid(Collections.<Long> emptySet(), new Runnable() {
					
					@Override
					public void run() {
						returnedButton.setEnabled(false);
						show();
						activateChangeListeners();
					}
				});
			}
		});
	}
	
	public void prepareForEdit(Long replacementProfileId) {
		LoadingManager.get().loading();
		GwtServiceProvider.getReplacementProfilesService().getReplacementProfileById(replacementProfileId, new AsyncCallback<ReplacementProfileModel>() {

			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			
			@Override
			public void onSuccess(final ReplacementProfileModel replacementProfile) {

				deactivateChangeListeners();
				
				reset();

				requesterUsernameComboBox.setSimpleValue(replacementProfile.getRequesterUsername());
				
				populateSelectedRequesterUserProfilesCheckBoxListView(replacementProfile.getSelectedRequesterUserProfiles(), new Runnable() {
					
					@Override
					public void run() {
						
						idHiddenField.setValue(replacementProfile.getId());
						
						UserModel replacement = replacementProfile.getReplacement();
						UserModel replacementInComboBox = replacementComboBox.getStore().findModel(UserModel.USER_PROPERTY_USERID, replacement.getUserId());
						replacementComboBox.setValue(replacementInComboBox);
						
						commentsTextArea.setValue(replacementProfile.getComments());
						
						startDateField.setValue(replacementProfile.getStartDate());
						endDateField.setValue(replacementProfile.getEndDate());
						
						outOfOfficeCheckBox.setValue(replacementProfile.isOutOfOffice());
						outOfOfficeEmailSubjectTemplateTextField.setValue(replacementProfile.getOutOfOfficeEmailSubjectTemplate());
						outOfOfficeEmailBodyTemplateTextArea.setValue(replacementProfile.getOutOfOfficeEmailBodyTemplate());
						
						populateSelectedReplacementProfilesInWhichRequesterIsReplacementGrid(replacementProfile.getIdsForSelectedReplacementProfilesInWhichRequesterIsReplacement(), new Runnable() {
							
							@Override
							public void run() {
								returnedButton.setEnabled(true);
								show();								
								activateChangeListeners();
							}
						});
					}
				});
				
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private void deactivateChangeListeners() {
		
		requesterUsernameComboBox.removeListener(Events.SelectionChange, requesterUsernameComboBoxSelectionChangedListener);
		
		selectedRequesterUserProfilesCheckBoxListView.removeListener(Events.OnClick, selectedReplacementProfilesInWhichRequesterIsReplacementDependencyFieldChangeListener);
		startDateField.getDatePicker().removeListener(Events.Select, selectedReplacementProfilesInWhichRequesterIsReplacementDependencyFieldChangeListener);
		endDateField.getDatePicker().removeListener(Events.Select, selectedReplacementProfilesInWhichRequesterIsReplacementDependencyFieldChangeListener);
		
		outOfOfficeCheckBox.removeListener(Events.Change, outOfOfficeCheckBoxChangeListener);
	}
	
	private void activateChangeListeners() {
		
		requesterUsernameComboBox.addListener(Events.SelectionChange, requesterUsernameComboBoxSelectionChangedListener);

		selectedRequesterUserProfilesCheckBoxListView.addListener(Events.OnClick, selectedReplacementProfilesInWhichRequesterIsReplacementDependencyFieldChangeListener);
		startDateField.getDatePicker().addListener(Events.Select, selectedReplacementProfilesInWhichRequesterIsReplacementDependencyFieldChangeListener);
		endDateField.getDatePicker().addListener(Events.Select, selectedReplacementProfilesInWhichRequesterIsReplacementDependencyFieldChangeListener);

		outOfOfficeCheckBox.addListener(Events.Change, outOfOfficeCheckBoxChangeListener);
	}
	
	private boolean isValid() {
		return formPanel.isValid();
	}
}