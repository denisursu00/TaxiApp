package ro.cloudSoft.cloudDoc.presentation.client.admin.workflows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowTransitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowTransitionModel.TransitionDeadlineActionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowTransitionModel.TransitionRoutingTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.OrganizationUnitPopupSelectionField;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.PlainOrganizationalStructureEntitiesSelectionComponent;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TransitionsTab extends WorkflowAbstractTab {

	public static final String PROPERTY_TRANSITION_HIDDEN = "transitionHidden";
    
	private WorkflowWindow mainWindow;
	
	private ContentPanel transitionsGridCP;
	private ContentPanel transitionFormCP;
	
	private ToolBar toolBar;
	private Button addTransitionButton;
	private Button deleteTransitionButton;
	private Grid<WorkflowTransitionModel> transitionsGrid;
	
	private FormPanel transitionForm;
	private HiddenField<WorkflowTransitionModel> transitionHidden;
	private TextField<String> transitionName;
	private ComboBox<WorkflowStateModel> transitionStartState;
	private ComboBox<WorkflowStateModel> transitionFinalState;
	private CheckBox transitionConditionalRouting;
	private TextField<String> transitionRoutingCondition;
	private FieldSet transitionAdditionalViewingRightsFS;
	private ExtraViewersSelectionComponent transitionExtraViewers;
	private ComboBox<TransitionRoutingTypeModel> transitionRoutingType;
	private ComboBox<GroupModel> transitionDestinationGroup;
	private OrganizationUnitPopupSelectionField transitionDestinationOrganizationUnit;
	private ComboBox<UserMetadataDefinitionModel> transitionDestinationParameter;
	private FieldSet notificationsFieldSet;
	private TransitionNotificationsComponent notificationsComponent;
	private CheckBox availableForAutomaticActionsOnlyCheckBox;
	private CheckBox transitionDeadlineAction;
	private ComboBox<TransitionDeadlineActionModel> transitionDeadlineActionType;
	private NumberField transitionDeadlinePeriod;
	private NumberField transitionDeadlineNotifyResendInterval;
	private Button saveTransitionButton;
	
	public TransitionsTab(WorkflowWindow mainWindow) {
		
		setText(GwtLocaleProvider.getConstants().TRANSITIONS());
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		
		this.mainWindow = mainWindow;
		
		initToolBar();
		initTransitionsGrid();
		initContentPanels();
		initTransitionForm();		
		addListeners();
		
		// adaugare componente
		transitionsGridCP .setTopComponent(toolBar);
		transitionsGridCP.add(transitionsGrid);
		transitionFormCP.add(transitionForm);
		
		BorderLayoutData transitionsGridData = new BorderLayoutData(LayoutRegion.WEST);
		transitionsGridData.setSplit(true);
		transitionsGridData.setSize(400);
		transitionsGridData.setMinSize(400);
		transitionsGridData.setMargins(new Margins(5,5,5,5));
		
		BorderLayoutData transitionFormData = new BorderLayoutData(LayoutRegion.CENTER);
		transitionFormData.setSplit(true);
		transitionFormData.setMinSize(400);
		transitionFormData.setMargins(new Margins(5,5,5,5));
		
		add(transitionsGridCP, transitionsGridData);
		add(transitionFormCP, transitionFormData);
	}
	
	private void initContentPanels(){
		transitionsGridCP = new ContentPanel();
		transitionsGridCP.setHeaderVisible(false);
		transitionsGridCP.setLayout(new FitLayout());
		transitionsGridCP.setBorders(false);
		
		transitionFormCP = new ContentPanel();
		transitionFormCP.setHeaderVisible(false);
		transitionFormCP.setLayout(new FitLayout());
		transitionFormCP.setBorders(false);
	}
	
	private void initToolBar(){
		toolBar = new ToolBar();
		toolBar.setSpacing(5);
		
		addTransitionButton = new Button(
				GwtLocaleProvider.getConstants().ADD_TRANSITION());
		deleteTransitionButton = new Button(
				GwtLocaleProvider.getConstants().DELETE_TRANSITION());
		deleteTransitionButton.setEnabled(false);
		
		toolBar.add(addTransitionButton);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(deleteTransitionButton);
	}
	
	private void initTransitionsGrid(){
		List<ColumnConfig> cols = new ArrayList<ColumnConfig>();
		
		ColumnConfig colTransitionName = new ColumnConfig();
		colTransitionName.setId(WorkflowTransitionModel.PROPERTY_NAME);
		colTransitionName.setHeader(GwtLocaleProvider.getConstants().TRANSITION_NAME());
		colTransitionName.setWidth(100);
		cols.add(colTransitionName);
		
		ColumnConfig colStartStateCode = new ColumnConfig();
		colStartStateCode.setId(WorkflowTransitionModel.PROPERTY_START_STATE_CODE);
		colStartStateCode.setHeader(GwtLocaleProvider.getConstants().INITIAL_STATE_CODE());
		colStartStateCode.setWidth(100);
		colStartStateCode.setHidden(true);
		cols.add(colStartStateCode);
		
		ColumnConfig colStartStateName = new ColumnConfig();
		colStartStateName.setId(WorkflowTransitionModel.PROPERTY_START_STATE_NAME);
		colStartStateName.setHeader(GwtLocaleProvider.getConstants().INITIAL_STATE());
		colStartStateName.setWidth(100);
		cols.add(colStartStateName);
		
		ColumnConfig colFinalStateCode = new ColumnConfig();
		colFinalStateCode.setId(WorkflowTransitionModel.PROPERTY_FINAL_STATE_CODE);
		colFinalStateCode.setHeader(GwtLocaleProvider.getConstants().FINAL_STATE_CODE());
		colFinalStateCode.setWidth(100);
		colFinalStateCode.setHidden(true);
		cols.add(colFinalStateCode);
		
		ColumnConfig colFinalStateName = new ColumnConfig();
		colFinalStateName.setId(WorkflowTransitionModel.PROPERTY_FINAL_STATE_NAME);
		colFinalStateName.setHeader(GwtLocaleProvider.getConstants().FINAL_STATE());
		colFinalStateName.setWidth(100);
		cols.add(colFinalStateName);
		
		ListStore<WorkflowTransitionModel> store = new ListStore<WorkflowTransitionModel>();
		ColumnModel model = new ColumnModel(cols);
		
		transitionsGrid = new Grid<WorkflowTransitionModel>(store, model);
		GridView view = new GridView();
		view.setForceFit(true);
		transitionsGrid.setView(view);		
		transitionsGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		
	}
	
	private void initTransitionForm() {
		transitionForm = new FormPanel();
		transitionForm.setBorders(false);
		transitionForm.setBodyBorder(false);
		transitionForm.setHeaderVisible(false);
		transitionForm.setScrollMode(Scroll.AUTO);
		transitionForm.setLabelWidth(150);
		
		transitionHidden = new HiddenField<WorkflowTransitionModel>();
		
		transitionName = new TextField<String>();
		transitionName.setFieldLabel(GwtLocaleProvider.getConstants().TRANSITION_NAME());
		transitionName.setAllowBlank(false);
		transitionName.setValidator(new TransitionNameValidator(transitionsGrid.getStore()));
		
		transitionStartState = new ComboBox<WorkflowStateModel>();		
		transitionStartState.setFieldLabel(
				GwtLocaleProvider.getConstants().INITIAL_STATE());
		transitionStartState.setDisplayField(WorkflowStateModel.PROPERTY_DISPLAY_STATE);
		transitionStartState.setValueField(WorkflowStateModel.PROPERTY_CODE);
		transitionStartState.setStore(new ListStore<WorkflowStateModel>());
		transitionStartState.setAllowBlank(false);
		transitionStartState.setEditable(false);
		
		transitionFinalState = new ComboBox<WorkflowStateModel>();
		transitionFinalState.setFieldLabel(
				GwtLocaleProvider.getConstants().FINAL_STATE());
		transitionFinalState.setDisplayField(WorkflowStateModel.PROPERTY_DISPLAY_STATE);
		transitionFinalState.setValueField(WorkflowStateModel.PROPERTY_CODE);
		transitionFinalState.setStore(new ListStore<WorkflowStateModel>());
		transitionFinalState.setAllowBlank(false);
		transitionFinalState.setEditable(false);
		
		transitionStartState.setValidator(new TransitionStartStateValidator(transitionsGrid.getStore(),
				transitionHidden, transitionFinalState));
		transitionFinalState.setValidator(new TransitionFinalStateValidator(transitionsGrid.getStore(),
				transitionHidden, transitionStartState));
		
		transitionConditionalRouting = new CheckBox();
		transitionConditionalRouting.setFieldLabel(
				GwtLocaleProvider.getConstants().CONDITIONAL_ROUTING());
		transitionConditionalRouting.setBoxLabel(
				GwtLocaleProvider.getConstants().CONDITIONAL_ROUTING_YES());
		
		transitionRoutingCondition = new TextField<String>();
		transitionRoutingCondition.setFieldLabel(GwtLocaleProvider.getConstants().ROUTING_CONDITION());
		transitionRoutingCondition.setAllowBlank(false);
		
		transitionAdditionalViewingRightsFS = new FieldSet();
		transitionAdditionalViewingRightsFS.setHeading(
				GwtLocaleProvider.getConstants().ADDITIONAL_VIEWING_RIGHTS());
		transitionAdditionalViewingRightsFS.setCollapsible(true);
		transitionAdditionalViewingRightsFS.setAutoWidth(true);
		transitionAdditionalViewingRightsFS.setScrollMode(Scroll.AUTOX);
		
		transitionExtraViewers = new ExtraViewersSelectionComponent();
		transitionAdditionalViewingRightsFS.add(transitionExtraViewers, new MarginData(10));
				
		transitionRoutingType = new ComboBox<TransitionRoutingTypeModel>();
		transitionRoutingType.setFieldLabel(
				GwtLocaleProvider.getConstants().ROUTING_TYPE());
		transitionRoutingType.setDisplayField(TransitionRoutingTypeModel.LABEL);
		transitionRoutingType.setEditable(false);
		transitionRoutingType.setAllowBlank(false);
		ListStore<TransitionRoutingTypeModel> transitionRoutingTypeStore = new ListStore<TransitionRoutingTypeModel>();
		transitionRoutingTypeStore.add(new WorkflowTransitionModel().ROUTING_TYPES());
		transitionRoutingType.setStore(transitionRoutingTypeStore);
		
		transitionDestinationGroup = new ComboBox<GroupModel>();
		transitionDestinationGroup.setFieldLabel(
				GwtLocaleProvider.getConstants().DESTINATION_GROUP());
		transitionDestinationGroup.setDisplayField(GroupModel.PROPERTY_NAME);
		transitionDestinationGroup.setEditable(false);
		transitionDestinationGroup.setAllowBlank(false);
		transitionDestinationGroup.setStore(new ListStore<GroupModel>());

		transitionDestinationOrganizationUnit = new OrganizationUnitPopupSelectionField();
		transitionDestinationOrganizationUnit.setFieldLabel(
				GwtLocaleProvider.getConstants().DESTINATION_OU());
		
		transitionDestinationParameter = new ComboBox<UserMetadataDefinitionModel>();
		transitionDestinationParameter.setFieldLabel(
				GwtLocaleProvider.getConstants().DESTINATION_PARAMETER());
		transitionDestinationParameter.setEditable(false);
		transitionDestinationParameter.setAllowBlank(false);
		transitionDestinationParameter.setDisplayField(MetadataDefinitionModel.PROPERTY_NAME);
		transitionDestinationParameter.setStore(new ListStore<UserMetadataDefinitionModel>());
		
		notificationsFieldSet = new FieldSet();
		notificationsFieldSet.setHeading(GwtLocaleProvider.getConstants().NOTIFICATIONS());
		notificationsFieldSet.setHeight(250);
		notificationsFieldSet.setLayout(new FitLayout());
		
		notificationsComponent = new TransitionNotificationsComponent();
		notificationsFieldSet.add(notificationsComponent);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setLabelWidth(140);
		FormLayout formLayout1 = new FormLayout();
		formLayout1.setLabelWidth(140);
		
		availableForAutomaticActionsOnlyCheckBox = new CheckBox();
		availableForAutomaticActionsOnlyCheckBox.setBoxLabel("");
		availableForAutomaticActionsOnlyCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().AVAILABLE_FOR_AUTOMATIC_ACTIONS_ONLY());
		
		transitionDeadlineAction = new CheckBox();
		transitionDeadlineAction.setFieldLabel(GwtLocaleProvider.getConstants().DEADLINE_AUTOMATIC_ACTION());
		
		transitionDeadlineActionType = new ComboBox<TransitionDeadlineActionModel>();
		transitionDeadlineActionType.setFieldLabel(
				GwtLocaleProvider.getConstants().ACTION());
		transitionDeadlineActionType.setDisplayField(WorkflowTransitionModel.TransitionDeadlineActionModel.LABEL);
		ListStore<TransitionDeadlineActionModel> deadlineActionsStore = new ListStore<TransitionDeadlineActionModel>();
		deadlineActionsStore.add(new WorkflowTransitionModel().DEADLINE_ACTIONS());
		transitionDeadlineActionType.setStore(deadlineActionsStore);
		transitionDeadlineActionType.setEditable(false);
		
		transitionDeadlinePeriod = new NumberField();
		transitionDeadlinePeriod.setFieldLabel(GwtLocaleProvider.getConstants().DEADLINE_PERIOD() + "(" + GwtLocaleProvider.getConstants().DAYS() + ")");
		transitionDeadlinePeriod.setAllowNegative(false);
		transitionDeadlinePeriod.setAllowDecimals(false);
		transitionDeadlinePeriod.setAllowBlank(false);
		
		transitionDeadlineNotifyResendInterval = new NumberField();
		transitionDeadlineNotifyResendInterval.setFieldLabel(GwtLocaleProvider.getConstants().NOTIFICATION_RESEND_INTERVAL() + "(" + GwtLocaleProvider.getConstants().DAYS() + ")");
		transitionDeadlineNotifyResendInterval.setAllowNegative(false);
		transitionDeadlineNotifyResendInterval.setAllowDecimals(false);
		transitionDeadlineNotifyResendInterval.setAllowBlank(false);
		
		FormLayout formLayout2 = new FormLayout();
		formLayout2.setLabelWidth(140);
		
		saveTransitionButton = new Button(GwtLocaleProvider.getConstants().SAVE_TRANSITION());
		
		transitionForm.add(transitionName, new FormData());
		transitionForm.add(transitionStartState, new FormData());
		transitionForm.add(transitionFinalState, new FormData());
		transitionForm.add(transitionConditionalRouting, new FormData());
		transitionForm.add(transitionRoutingCondition, new FormData());
		transitionForm.add(transitionAdditionalViewingRightsFS, new FormData("100%"));
		transitionForm.add(transitionRoutingType, new FormData());
		transitionForm.add(transitionDestinationGroup, new FormData());
		transitionForm.add(transitionDestinationOrganizationUnit, new FormData());
		transitionForm.add(transitionDestinationParameter, new FormData());
		transitionForm.add(notificationsFieldSet, new FormData("90%"));
		transitionForm.add(availableForAutomaticActionsOnlyCheckBox, new FormData());
		transitionForm.add(transitionDeadlineAction, new FormData());
		transitionForm.add(transitionDeadlineActionType, new FormData());
		transitionForm.add(transitionDeadlinePeriod, new FormData());
		transitionForm.add(transitionDeadlineNotifyResendInterval, new FormData());
		transitionForm.addButton(saveTransitionButton);
		transitionForm.setVisible(false);
	}
	
	private void addListeners() {
		this.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				// la fiecare select de tab trebuie sa repopulez anumite campuri
				// deoarece nenea user poate a mai adaugat documente si stari 
				transitionsGrid.getSelectionModel().deselectAll();
				transitionForm.hide();
				changeToolBarPerspective(null);
				
				// combobox-urile cu stari
				populateStatesComboBox();
				
				// combobox-ul cu parametri destinatie
				if (mainWindow.getGeneralTab().getDocumetTypeIds() != null && 
						mainWindow.getGeneralTab().getDocumetTypeIds().size() > 0)
					populateTransitionDestinationParameter(mainWindow.getGeneralTab().getDocumetTypeIds());
			}
		});
		
		addTransitionButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			public void componentSelected(ButtonEvent ce) {
				prepareTransitionFormForAdd();
			};
		});
		
		deleteTransitionButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				MessageBox.confirm(GwtLocaleProvider.getConstants().DELETE(), GwtLocaleProvider.getMessages().CONFIRM_DELETE_TRANSITION(),
						new Listener<MessageBoxEvent>() {
							public void handleEvent(MessageBoxEvent mbe) {
								if (mbe.getButtonClicked().getItemId().equals(Dialog.YES)) {
									transitionsGrid.getStore().remove(transitionsGrid.getSelectionModel().getSelectedItem());
									transitionForm.hide();
									changeToolBarPerspective(null);
								}
							}
						});
			}
		});
		
		transitionsGrid.addListener(Events.CellClick, new Listener<GridEvent<WorkflowTransitionModel>>(){
			@Override
			public void handleEvent(GridEvent<WorkflowTransitionModel> be) {
				changeToolBarPerspective(be.getModel());
				prepareTransitionFormForEdit(be.getModel());
				changeTransitionFormByFinalState();
			}
		});
		
		transitionConditionalRouting.addListener(Events.Change, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				transitionRoutingCondition.setVisible(transitionConditionalRouting.getValue());				
				transitionRoutingCondition.setVisible(transitionConditionalRouting.getValue());
			}
		});
		
		transitionRoutingType.addListener(Events.Select, new Listener<FieldEvent>(){
			@Override
			public void handleEvent(FieldEvent be) {
				String selectedType = transitionRoutingType.getValue().getValue();
				if (selectedType.equalsIgnoreCase(WorkflowTransitionModel.ROUTING_GROUP)){
					transitionDestinationGroup.setVisible(true);
					transitionDestinationOrganizationUnit.setVisible(false);
					transitionDestinationParameter.setVisible(false);
				}else if (selectedType.equalsIgnoreCase(WorkflowTransitionModel.ROUTING_OU)){
					transitionDestinationGroup.setVisible(false);
					transitionDestinationParameter.setVisible(false);
					transitionDestinationOrganizationUnit.setVisible(true);
				}else if (selectedType.equalsIgnoreCase(WorkflowTransitionModel.ROUTING_PARAMETER)){
					transitionDestinationParameter.setVisible(true);
					transitionDestinationGroup.setVisible(false);
					transitionDestinationOrganizationUnit.setVisible(false);
				}else {
					transitionDestinationParameter.setVisible(false);
					transitionDestinationGroup.setVisible(false);
					transitionDestinationOrganizationUnit.setVisible(false);
				}
			}
		});
		
		transitionDeadlineAction.addListener(Events.Change, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				transitionDeadlineActionType.setVisible(transitionDeadlineAction.getValue());
				transitionDeadlinePeriod.setVisible(transitionDeadlineAction.getValue());
				transitionDeadlineNotifyResendInterval.setVisible(false);

			}
		});
		
		transitionDeadlineActionType.addListener(Events.Select, new Listener<FieldEvent>(){
			@Override
			public void handleEvent(FieldEvent be) {
				TransitionDeadlineActionModel tdam = transitionDeadlineActionType.getValue();
				transitionDeadlineNotifyResendInterval.setVisible(false);
				if (tdam.getValue().intValue() == WorkflowTransitionModel.DEADLINE_ACTION_NOTIFY){
					transitionDeadlineNotifyResendInterval.setVisible(true);
				}
			}
		});
		
		saveTransitionButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (isTransitionFormValid()){
					if (transitionHidden.getValue() != null){
						WorkflowTransitionModel transition = transitionHidden.getValue();
						populateTransitionModelFromForm(transition);
						transitionsGrid.getStore().update(transition);
						prepareTransitionFormForEdit(transition);
						changeTransitionFormByFinalState();
					}else {
						WorkflowTransitionModel transition = new WorkflowTransitionModel();
						populateTransitionModelFromForm(transition);						
						transitionsGrid.getStore().add(transition);
						transitionsGrid.getSelectionModel().select(transition, false);
						transitionForm.hide();
					}
				}
			}
		});
		
		/*
		 * Cand intr-o tranzitie starea finala aleasa este de tip STOP, nu mai 
		 * este necesar sa completez urmatoarele: Routing type, 
		 * Notify destination by email, Deadline automatic action.
		 */
		transitionFinalState.addListener(Events.Select, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				changeTransitionFormByFinalState();
			}
		});
		
		//resetez campul de selectare UO destinatie atunci cand este ascuns
		transitionDestinationOrganizationUnit.addListener(Events.Hide, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				transitionDestinationOrganizationUnit.resetValue();
			}
		});
	}
	
	private void changeToolBarPerspective(WorkflowTransitionModel workflowTransitionModel){
		if (workflowTransitionModel != null){
			deleteTransitionButton.setEnabled(true);
		}else{
			deleteTransitionButton.setEnabled(false);
		}
	}
	
	/**
	 * Schimba vizbilitatea anumitor campuri in formular in functie de tipul stari finale.
	 */
	private void changeTransitionFormByFinalState(){
		WorkflowStateModel finalState = transitionFinalState.getValue();
		if (finalState != null){
			if (finalState.getStateType().equals(WorkflowStateModel.STATETYPE_STOP)){
				transitionRoutingType.setVisible(false);
				transitionDestinationGroup.setVisible(false);
				transitionDestinationOrganizationUnit.setVisible(false);
				transitionDestinationParameter.setVisible(false);
				transitionDeadlineAction.setVisible(false);
				transitionDeadlineActionType.setVisible(false);
				transitionDeadlinePeriod.setVisible(false);
				transitionDeadlineNotifyResendInterval.setVisible(false);
			} else {
				// routing type
				transitionRoutingType.setVisible(true);
				String selectedType = null;
				if (transitionRoutingType.getValue() != null)
					selectedType= transitionRoutingType.getValue().getValue();
				if (selectedType != null && selectedType.length() > 0){
					if (selectedType.equalsIgnoreCase(WorkflowTransitionModel.ROUTING_GROUP)){
						transitionDestinationGroup.setVisible(true);
						transitionDestinationOrganizationUnit.setVisible(false);
						transitionDestinationParameter.setVisible(false);
					}else if (selectedType.equalsIgnoreCase(WorkflowTransitionModel.ROUTING_OU)){
						transitionDestinationOrganizationUnit.setVisible(true);
						transitionDestinationGroup.setVisible(false);
						transitionDestinationParameter.setVisible(false);
					}else if (selectedType.equalsIgnoreCase(WorkflowTransitionModel.ROUTING_PARAMETER)){
						transitionDestinationParameter.setVisible(true);
						transitionDestinationGroup.setVisible(false);
						transitionDestinationOrganizationUnit.setVisible(false);
					}
				}
				
				// deadline
				transitionDeadlineAction.setVisible(true);
				if (transitionDeadlineAction.getValue()){
					transitionDeadlineActionType.setVisible(true);
					transitionDeadlinePeriod.setVisible(true);
					TransitionDeadlineActionModel tdam = transitionDeadlineActionType.getValue();
					transitionDeadlineNotifyResendInterval.setVisible(false);
					if (tdam != null){
						if (tdam.getValue().intValue() == WorkflowTransitionModel.DEADLINE_ACTION_NOTIFY){
							transitionDeadlineNotifyResendInterval.setVisible(true);
						}
					}	
				}
			}
		}
	}
	
	@Override
	public void doWithSelectedDocumentTypes(List<DocumentTypeModel> documentTypes) {
		notificationsComponent.doWithSelectedDocumentTypes(documentTypes);
	}
	
	/**
	 * Validarea formularului.
	 */
	private boolean isTransitionFormValid() {
		
		transitionForm.isValid();
		
		if (!transitionName.isValid())
			return false;
		if (!transitionStartState.isValid())
			return false;
		if (!transitionFinalState.isValid())
			return false;
		if (transitionConditionalRouting.getValue()){
			if (!transitionRoutingCondition.isValid())
				return false;
		}
		
		if (!transitionFinalState.getValue().getStateType().equals(WorkflowStateModel.STATETYPE_STOP)){
			
			if (!transitionRoutingType.isValid())
				return false;
			
			if (transitionRoutingType.getValue().getValue().equalsIgnoreCase(WorkflowTransitionModel.ROUTING_GROUP)){
				if (!transitionDestinationGroup.isValid())
					return false;
			}
			if (transitionRoutingType.getValue().getValue().equalsIgnoreCase(WorkflowTransitionModel.ROUTING_OU)){
				if (!transitionDestinationOrganizationUnit.isValid())
					return false;
			}
			if (transitionRoutingType.getValue().getValue().equalsIgnoreCase(WorkflowTransitionModel.ROUTING_PARAMETER)){
				if (!transitionDestinationParameter.isValid())
					return false;
			}
			if (transitionDeadlineAction.getValue()){
				if (!transitionDeadlinePeriod.isValid())
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Populeaza obiect WorkflowTransitionModel cu datele de pe formular.
	 * @param transition
	 */
	private void populateTransitionModelFromForm(WorkflowTransitionModel transition){
		// numele tranzitiei
		transition.setName(transitionName.getValue());
		
		// starea initiala a tranzitiei
		transition.setStartState(transitionStartState.getValue());
		
		// starea finala a tranzitiei
		transition.setFinalState(transitionFinalState.getValue());
		
		// conditia de rutare
		if (transitionConditionalRouting.getValue())
			transition.setRoutingCondition(transitionRoutingCondition.getValue());
		else
			transition.setRoutingCondition(null);
		
		// drepturi de vizualizare suplimentare
		transition.setExtraViewers(getSelectedExtraViewers());
		
		// daca tipul stari finale este stop sau nu
		if (!transitionFinalState.getValue().getStateType().equals(WorkflowStateModel.STATETYPE_STOP)){
			
			// tip de rutare
			transition.setRoutingType(transitionRoutingType.getValue().getValue());
			transition.setRoutingDestinationId(null); 
			transition.setRoutingDestinationParameter(null);
			if (transitionRoutingType.getValue().getValue().equalsIgnoreCase(WorkflowTransitionModel.ROUTING_GROUP))
				transition.setRoutingDestinationId(Long.valueOf(transitionDestinationGroup.getValue().getId()));
			else if (transitionRoutingType.getValue().getValue().equalsIgnoreCase(WorkflowTransitionModel.ROUTING_OU))
				transition.setRoutingDestinationId(Long.valueOf(transitionDestinationOrganizationUnit.getSelectedOrganizationUnitId())); // ia id de o.u.
			else if (transitionRoutingType.getValue().getValue().equalsIgnoreCase(WorkflowTransitionModel.ROUTING_PARAMETER))
				transition.setRoutingDestinationParameter(transitionDestinationParameter.getValue().getName());
			
			transition.setAvailableForAutomaticActionsOnly(ComponentUtils.getCheckBoxValue(availableForAutomaticActionsOnlyCheckBox));
			
			// actiune automata la expirare
			if (transitionDeadlineAction.getValue()){
				transition.setDeadlineAction(true);
				transition.setDeadlineActionType(WorkflowTransitionModel.DEADLINE_ACTION_ROUTE);
				transition.setDeadlinePeriod(transitionDeadlinePeriod.getValue().intValue());
				if (transitionDeadlineActionType.getValue().getValue().intValue() == WorkflowTransitionModel.DEADLINE_ACTION_NOTIFY) {
					transition.setDeadlineActionType(WorkflowTransitionModel.DEADLINE_ACTION_NOTIFY);
					if (transitionDeadlineNotifyResendInterval.getValue() != null)
						transition.setDeadlineNotifyResendInterval(transitionDeadlineNotifyResendInterval.getValue().intValue());
					else
						transition.setDeadlineNotifyResendInterval(0);
				}
			}else {
				transition.setDeadlineAction(false);
				transition.setDeadlinePeriod(null);
				transition.setDeadlineActionType(null);
				transition.setDeadlineNotifyResendInterval(null);
			}
		} else {
			// starea finala este STOP
			transition.setRoutingType(null);
			transition.setRoutingDestinationId(null); 
			transition.setRoutingDestinationParameter(null);
			
			transition.setAvailableForAutomaticActionsOnly(ComponentUtils.getCheckBoxValue(availableForAutomaticActionsOnlyCheckBox));
			
			transition.setDeadlineAction(false);
			transition.setDeadlinePeriod(null);
			transition.setDeadlineActionType(null);
			transition.setDeadlineNotifyResendInterval(null);
		}
		
		transition.setNotifications(notificationsComponent.getNotifications());
	}
	
	/**
	 * Face anumite componente invizibile.
	 */
	private void doFalseForVisibleCheckedExpandedFields(){
		transitionConditionalRouting.setValue(false);
		transitionRoutingCondition.setVisible(false);
		transitionAdditionalViewingRightsFS.setExpanded(false);
		transitionDestinationGroup.setVisible(false);
		transitionDestinationOrganizationUnit.setVisible(false);
		transitionDestinationParameter.setVisible(false);
		transitionDeadlineAction.setValue(false);
		transitionDeadlineActionType.setVisible(false);
		transitionDeadlinePeriod.setVisible(false);
		transitionDeadlineNotifyResendInterval.setVisible(false);
	}
	
	private void prepareTransitionFormForAdd(){
		// deselectez toate din grid ca sa nu aiba iluzii ca se afla in editarea
		// unei tranzitii daca vede selectat ceva in lista :)
		transitionsGrid.getSelectionModel().deselectAll();
		
		// sterg toate valorile din formular
		transitionForm.clear();
		notificationsComponent.reset();
		transitionExtraViewers.reset();
		
		// populez extraviewers si destinatari
		transitionExtraViewers.populate(null);
		
		transitionHidden.setValue(null);
		
		// setez valoarea Rutare automta implicit
		TransitionDeadlineActionModel dam = transitionDeadlineActionType.getStore().findModel(
				TransitionDeadlineActionModel.VALUE, WorkflowTransitionModel.DEADLINE_ACTION_ROUTE);
		transitionDeadlineActionType.setValue(dam);
		
		// ascund campurile care trebuie sa se vada numai la anumite bife
		doFalseForVisibleCheckedExpandedFields();
		transitionName.setData(PROPERTY_TRANSITION_HIDDEN, null);
		transitionForm.setVisible(true);
		
		// la prima afisare a formularului nu apare butonul de Save transition,
		// astfel prin acest scurtcircuit problema se repara :)
		transitionFormCP.setWidth(transitionFormCP.getWidth() - 1);
		transitionFormCP.setWidth(transitionFormCP.getWidth() + 1);
	}
	
	/**
	 * Pregateste formularul 'transitionForm' pentru editare de date (WorkflowTransitionModel).
	 * @param transition
	 */
	private void prepareTransitionFormForEdit(WorkflowTransitionModel transition){
		// sterg toate valorile din formular ca aduc altele in loc
		transitionForm.clear();
		notificationsComponent.reset();
		transitionExtraViewers.reset();
		
		doFalseForVisibleCheckedExpandedFields();
		transitionHidden.setValue(transition);
		transitionName.setData(PROPERTY_TRANSITION_HIDDEN, transitionHidden.getValue());
		
		// nume
		transitionName.setValue(transition.getName());
		
		// stare initiala
		transitionStartState.setValue(transition.getStartState());
		
		// stare finala
		transitionFinalState.setValue(transition.getFinalState());
		
		// rutare conditionata
		if (transition.getRoutingCondition() != null){
			transitionConditionalRouting.setValue(true);
			transitionRoutingCondition.setVisible(true);
			transitionRoutingCondition.setValue(transition.getRoutingCondition());
		}
		
		// drepturi de vizualizare suplimentare
		transitionExtraViewers.populate(transition.getExtraViewers());
		transitionAdditionalViewingRightsFS.setExpanded(!transition.getExtraViewers().isEmpty());
		
		// tip de rutare
		TransitionRoutingTypeModel trtm = transitionRoutingType.getStore().findModel(TransitionRoutingTypeModel.VALUE, transition.getRoutingType());
		transitionRoutingType.setValue(trtm);
		
		// grup destinatie
		transitionDestinationGroup.setValue(null);
		transitionDestinationParameter.setValue(null);
		if (transition.getRoutingType() != null){
			if  (transition.getRoutingType().equalsIgnoreCase(
					WorkflowTransitionModel.ROUTING_GROUP)){
				if (transition.getRoutingDestinationId() != null){
					GroupModel selectedRole = transitionDestinationGroup.getStore().findModel(
							GroupModel.PROPERTY_ID, transition.getRoutingDestinationId().toString());
					if (selectedRole != null)
						transitionDestinationGroup.setValue(selectedRole);
				}
				transitionDestinationGroup.setVisible(true);
			}else if (transition.getRoutingType().equalsIgnoreCase(
					WorkflowTransitionModel.ROUTING_OU)){
				if (transition.getRoutingDestinationId() != null){
					transitionDestinationOrganizationUnit.setSelectedOrganizationUnitId(transition.getRoutingDestinationId().toString());
				}
				transitionDestinationOrganizationUnit.setVisible(true);
			}else if (transition.getRoutingType().equalsIgnoreCase(
					WorkflowTransitionModel.ROUTING_PARAMETER)){
				UserMetadataDefinitionModel parameter = transitionDestinationParameter.getStore().findModel(MetadataDefinitionModel.PROPERTY_NAME, 
						transition.getRoutingDestinationParameter());
				if (parameter != null) {
					transitionDestinationParameter.setValue(parameter);
				}
				transitionDestinationParameter.setVisible(true);
			}
		}
		
		notificationsComponent.setNotifications(transition.getNotifications());
		
		availableForAutomaticActionsOnlyCheckBox.setValue(transition.isAvailableForAutomaticActionsOnly());
		
		// actiune automata la rutare
		transitionDeadlinePeriod.setValue(null);
		transitionDeadlineNotifyResendInterval.setValue(null);
		if (transition.getDeadlineAction() != null && transition.getDeadlineAction()){
			transitionDeadlineAction.setValue(true);
			transitionDeadlineActionType.setVisible(true);
			TransitionDeadlineActionModel dam = transitionDeadlineActionType.getStore().findModel(
					TransitionDeadlineActionModel.VALUE, transition.getDeadlineActionType());
			transitionDeadlineActionType.setValue(dam);
			transitionDeadlinePeriod.setVisible(true);
			transitionDeadlinePeriod.setValue(transition.getDeadlinePeriod());
			if (dam.getValue().intValue() == WorkflowTransitionModel.DEADLINE_ACTION_NOTIFY){
				transitionDeadlineNotifyResendInterval.setVisible(true);
				transitionDeadlineNotifyResendInterval.setValue(transition.getDeadlineNotifyResendInterval());
			}
		}else {
			// setez valoarea Rutare automta implicit
			TransitionDeadlineActionModel dam = transitionDeadlineActionType.getStore().findModel(
					TransitionDeadlineActionModel.VALUE, WorkflowTransitionModel.DEADLINE_ACTION_ROUTE);
			transitionDeadlineActionType.setValue(dam);			
		}
		
		transitionForm.setVisible(true);
		
		// la prima afisare a formularului nu apare butonul de Save transition,
		// astfel prin acest scurtcircuit problema se repara :)
		transitionFormCP.setWidth(transitionFormCP.getWidth() - 1);
		transitionFormCP.setWidth(transitionFormCP.getWidth() + 1);
	}
	
	/**
	 * Populeaza combobox-urile start si final cu stari.
	 */
	private void populateStatesComboBox(){
		List<WorkflowStateModel> states = mainWindow.getStatesTab().getStates();
		List<WorkflowStateModel> startStates = new ArrayList<WorkflowStateModel>();
		List<WorkflowStateModel> finalStates = new ArrayList<WorkflowStateModel>();
		
		for (WorkflowStateModel wsm : states){
			switch (wsm.getStateType().intValue()) {
			case WorkflowStateModel.STATETYPE_START:
				startStates.add(wsm);
				break;
			case WorkflowStateModel.STATETYPE_INTERMEDIATE:
				startStates.add(wsm);
				finalStates.add(wsm);
				break;
			case WorkflowStateModel.STATETYPE_STOP:
				finalStates.add(wsm);
				break;
			default:
				break;
			}
		}
		transitionStartState.getStore().removeAll();
		transitionFinalState.getStore().removeAll();
		transitionStartState.getStore().add(startStates);
		transitionFinalState.getStore().add(finalStates);
	}
	
	private void populateTransitionDestinationParameter(List<Long> documentTypeIds){		
		// stergem tot ca aducem alte valori pe scena
		transitionDestinationParameter.setValue(null);
		transitionDestinationParameter.getStore().removeAll();
		
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentTypeService().getUserMetadataDefinitions(documentTypeIds, new AsyncCallback<List<UserMetadataDefinitionModel>>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<UserMetadataDefinitionModel> result) {
				if (result != null) {
					transitionDestinationParameter.getStore().add(result);
				}
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private void populateTransitionDestinationGroup() {
		
		transitionDestinationGroup.getStore().removeAll();
		
		LoadingManager.get().loading();
		GwtServiceProvider.getOrgService().getGroups(new AsyncCallback<List<GroupModel>>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<GroupModel> result) {
				transitionDestinationGroup.getStore().add(result);
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	/**
	 * Returneza lista cu useri/grupuri/u.o care au drept de vizualizare.
	 * @return
	 */
	private List<OrganizationEntityModel> getSelectedExtraViewers(){
		List<OrganizationEntityModel> extraViewers = transitionExtraViewers.getSelectedEntitiesAsOrganizationEntities();
		return extraViewers;
	}
	
	@Override
	public boolean isValid() {
		// parcurg tranzitiile si vad daca a adaugat tranzitii, daca da, atunci
		// trebuie sa existe o tranzitie cu o stare initiala 'start' si cel putin
		// una finala 'stop' si cel putin una cu stare intermediara
		boolean startFound = false;
		boolean finalFound = false;
		boolean intermediateFound = false;
		if (transitionsGrid.getStore().getCount() == 0){
			MessageBox.alert(GwtLocaleProvider.getConstants().ERROR(), 
					GwtLocaleProvider.getMessages().NO_TRANSITION_ADDED(), null);
			return false;
		}else {
			for (WorkflowTransitionModel wtm : transitionsGrid.getStore().getModels()){
				if (wtm.getStartState().getStateType().intValue() == WorkflowStateModel.STATETYPE_START)
					startFound = true;
				if (wtm.getFinalState().getStateType().intValue() == WorkflowStateModel.STATETYPE_STOP)
					finalFound = true;
				if (wtm.getFinalState().getStateType().intValue() == WorkflowStateModel.STATETYPE_INTERMEDIATE)
					intermediateFound = true;
				if (startFound && intermediateFound && finalFound)
					break;
			}
			if (!startFound && !finalFound){
				MessageBox.alert(GwtLocaleProvider.getConstants().ERROR(), 
						GwtLocaleProvider.getMessages().NO_TRANSITION_START_STOP_ADDED(),
						null);
			}else if (!startFound){
				MessageBox.alert(GwtLocaleProvider.getConstants().ERROR(), 
						GwtLocaleProvider.getMessages().NO_TRANSITION_START_STATE_ADDED(), 
						null);
			}else if (!finalFound){
				MessageBox.alert(GwtLocaleProvider.getConstants().ERROR(), 
						GwtLocaleProvider.getMessages().NO_TRANSITION_STOP_STATE_ADDED(), 
						null);
			}
			if (!intermediateFound) {
				MessageBox.alert(GwtLocaleProvider.getConstants().ERROR(), 
						GwtLocaleProvider.getMessages().NO_TRANSITION_INTERMEDIATE_STATE_ADDED(), 
						null);
			}
		}
		
		boolean areTransitionNamesUnique = true;
		Set<String> uniqueTransitionNames = new HashSet<String>();
		for (WorkflowTransitionModel transition : transitionsGrid.getStore().getModels()) {
			String currentTransitionName = transition.getName();
			if (uniqueTransitionNames.contains(currentTransitionName)) {
				areTransitionNamesUnique = false;
				ErrorHelper.addError(GwtLocaleProvider.getMessages().TRANSITION_NAMES_MUST_BE_UNIQUE());
				break;
			} else {
				uniqueTransitionNames.add(currentTransitionName);
			}
		}
		
		return (startFound && intermediateFound && finalFound && areTransitionNamesUnique);
	}

	@Override
	public void populateForSave(WorkflowModel workflowModel) {
		workflowModel.setTransitions(transitionsGrid.getStore().getModels());
	}

	@Override
	public void prepareForAdd() {
		reset();
		doFalseForVisibleCheckedExpandedFields();
		// populez group
		populateTransitionDestinationGroup();
	}

	@Override
	public void prepareForEdit(WorkflowModel workflowModel) {
		reset();
		doFalseForVisibleCheckedExpandedFields();
		// populez group si o.u
		populateTransitionDestinationGroup();
		
		transitionForm.hide();
		Collections.sort(workflowModel.getTransitions(), new Comparator<WorkflowTransitionModel>(){
			@Override
			public int compare(WorkflowTransitionModel object1,
					WorkflowTransitionModel object2) {
				return object1.getStartStateCode().compareTo(object2.getStartStateCode());
			}
		});
		transitionsGrid.getStore().add(workflowModel.getTransitions());
	}

	@Override
	public void reset() {
		// sterg toate tranzitiile
		transitionsGrid.getStore().removeAll();
		
		// sterg combo cu start state
		transitionStartState.getStore().removeAll();
		
		// sterg combo cu final state
		transitionFinalState.getStore().removeAll();
				
		// sterg tot din combo cu grupuri
		transitionDestinationGroup.getStore().removeAll();
		
		// sterg tot din combo cu u.o
		transitionDestinationOrganizationUnit.resetValue();
		
		// sterg tot din combo cu parameter
		transitionDestinationParameter.getStore().removeAll();
		
		notificationsComponent.reset();
		
		changeToolBarPerspective(null);
		
		transitionForm.hide();
		//transitionForm.clear();
		transitionExtraViewers.reset();
	}
	
	public ListStore<WorkflowTransitionModel> getTransitions(){
		return transitionsGrid.getStore();
	}
	
}

class ExtraViewersSelectionComponent extends PlainOrganizationalStructureEntitiesSelectionComponent {
	
	private static final int LEFT_PANEL_ORIGINAL_WIDTH = 300;
	private static final int LEFT_PANEL_ORIGINAL_HEIGHT = 300;
	private static final int RIGHT_PANEL_ORIGINAL_WIDTH = 300;
	private static final int RIGHT_PANEL_ORIGINAL_HEIGHT = 300;
	private static final int BUTTONS_PANEL_ORIGINAL_WIDTH = 70;
	private static final int BUTTONS_PANEL_ORIGINAL_HEIGHT = 300;
	
	public ExtraViewersSelectionComponent() {
		changeSize(1, 1);
	}

	@Override
	public void changeSize(float wIndex, float hIndex){
		leftPanel.setSize(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(LEFT_PANEL_ORIGINAL_HEIGHT * hIndex));
		buttonsPanel.setSize(Math.round(BUTTONS_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(BUTTONS_PANEL_ORIGINAL_HEIGHT * hIndex));
		if (wIndex < 1)
			rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT * hIndex));
		else
			rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT * hIndex));
	}
}

/******************************************************************************/
/**                                                                          **/                               
/**                           ~  validators  ~                               **/
/**                                                                          **/
/******************************************************************************/

class TransitionNameValidator implements Validator {

	private ListStore<WorkflowTransitionModel> transitions;
	
	public TransitionNameValidator(ListStore<WorkflowTransitionModel> transitions) {
		this.transitions = transitions;
	}
	
	@Override
	public String validate(Field<?> field, String value) {
		if (value != null){
			if (value.trim().length() == 0)
				return GwtLocaleProvider.getMessages().ONLY_BLANK_SPACES_ARE_NOT_ALLOWED();
			
			WorkflowTransitionModel transitionHidden = (WorkflowTransitionModel)field.getData(
					TransitionsTab.PROPERTY_TRANSITION_HIDDEN);
			WorkflowTransitionModel model = null;
			
			for (WorkflowTransitionModel wtm : transitions.getModels()){
				if (wtm.getName().compareToIgnoreCase(value) == 0){
					model = wtm;
					break;
				}
			}
			
			if (model != null){
				if (transitionHidden != null){
					if (transitionHidden.equals(model))
						return null;
					else
						return GwtLocaleProvider.getMessages().
						TRANSITION_WITH_THIS_NAME_ALREADY_EXISTS();
				}else 
					return GwtLocaleProvider.getMessages().
					TRANSITION_WITH_THIS_NAME_ALREADY_EXISTS();
			}
		}			
		return null;
	}

}

/**
 * Validator pentru starea initiala.
 */
class TransitionStartStateValidator implements Validator {

	private ListStore<WorkflowTransitionModel> transitions;
	private HiddenField<WorkflowTransitionModel> transitionHidden;
	private ComboBox<WorkflowStateModel> finalState;
	
	public TransitionStartStateValidator(ListStore<WorkflowTransitionModel> transitions, 
			HiddenField<WorkflowTransitionModel> transitionHidden, 
			ComboBox<WorkflowStateModel> finalState) {
		this.transitions = transitions;
		this.transitionHidden = transitionHidden;
		this.finalState = finalState;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String validate(Field<?> field, String value) {
		ComboBox<WorkflowStateModel> tempValue = (ComboBox<WorkflowStateModel>)field;
		if (value != null){
			// sa nu mai existe alta tranzitie start
			if (tempValue.getValue().getStateType() == WorkflowStateModel.STATETYPE_START){
				for (WorkflowTransitionModel t : transitions.getModels()){
					if (t.getStartState().getStateType().intValue() == WorkflowStateModel.STATETYPE_START)
						if (transitionHidden.getValue() != null){
							if (!t.equals(transitionHidden.getValue())){
								return GwtLocaleProvider.getMessages().TRANSITION_START_STATE_ALREADY_EXISTS();
							}
						}else {
							return GwtLocaleProvider.getMessages().TRANSITION_START_STATE_ALREADY_EXISTS();
						}
							
				}
			}
			
			if (finalState.getValue() != null) {
				// sa nu mai existe alta tranzitie cu aceeasi stare initiala si finala
				for (WorkflowTransitionModel t : transitions.getModels()){
					if (t.getStartState().getCode().compareToIgnoreCase(tempValue.getValue().getCode()) == 0 &&
							t.getFinalState().getCode().compareToIgnoreCase(finalState.getValue().getCode()) == 0){
						if (transitionHidden.getValue() != null){
							if (!t.equals(transitionHidden.getValue())){
								return GwtLocaleProvider.getMessages().
								TRANSITION_WITH_INITIAL_FINAL_STATE_ALREADY_EXISTS();
							}
						}else {
							return GwtLocaleProvider.getMessages().
							TRANSITION_WITH_INITIAL_FINAL_STATE_ALREADY_EXISTS();
						}
					}
				}
			}
			
		}			
		return null;
	}

}

/**
 * Validator pentru starea finala.
 */
class TransitionFinalStateValidator implements Validator {

	private ListStore<WorkflowTransitionModel> transitions;
	private HiddenField<WorkflowTransitionModel> transitionHidden;
	private ComboBox<WorkflowStateModel> startState;
	
	public TransitionFinalStateValidator(ListStore<WorkflowTransitionModel> transitions, 
			HiddenField<WorkflowTransitionModel> transitionHidden,
			ComboBox<WorkflowStateModel> startState) {
		this.transitions = transitions;
		this.transitionHidden = transitionHidden;
		this.startState = startState;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String validate(Field<?> field, String value) {
		ComboBox<WorkflowStateModel> tempValue = (ComboBox<WorkflowStateModel>)field;
		if (value != null){
			// sa nu fie egala cu cea initiala
			if (startState.getValue() != null) {
				// sa nu mai existe alta tranzitie cu aceesi stare initiala si finala
				for (WorkflowTransitionModel t : transitions.getModels()){
					if (t.getFinalState().getCode().compareToIgnoreCase(tempValue.getValue().getCode()) == 0 &&
							t.getStartState().getCode().compareToIgnoreCase(startState.getValue().getCode()) == 0){
						if (transitionHidden.getValue() != null){
							if (!t.equals(transitionHidden.getValue())){
								return GwtLocaleProvider.getMessages().
								TRANSITION_WITH_INITIAL_FINAL_STATE_ALREADY_EXISTS();
							}
						}else {
							return GwtLocaleProvider.getMessages().
							TRANSITION_WITH_INITIAL_FINAL_STATE_ALREADY_EXISTS();
						}
					}
				}
			}
		}			
		return null;
	}

}