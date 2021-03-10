package ro.cloudSoft.cloudDoc.presentation.client.admin.workflows;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowTransitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
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
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.PropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
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
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class StatesTab extends WorkflowAbstractTab {

	private WorkflowWindow mainWindow;
	
	private ContentPanel statesCP;
	private ContentPanel stateFormCP;
	
	private ToolBar toolBar;
	private Button addButton;
	private Button deleteButton;
	private Grid<WorkflowStateModel> statesGrid;
	
	private FormPanel stateForm;
	private HiddenField<Long> stateId;
	private HiddenField<WorkflowStateModel> hiddenState;
	private TextField<String> stateCode;
	private TextField<String> stateName;
	private RadioGroup stateType;
	private Radio stateStartType;
	private Radio stateIntermediateType;
	private Radio stateStopType;
	private FieldSet stateAttachmentsPermissionFS;
	private CheckBox stateAttachmentPermissionAdd;
	private CheckBox stateAttachmentPermissionDelete;
	private Button saveStateButton;
	
	private Integer tempId = 0; // folosit pentru a identifica starile din tranzitii
	
	public StatesTab(WorkflowWindow mainWindow) {
		
		this.mainWindow = mainWindow;
		
		setText(GwtLocaleProvider.getConstants().STATES());
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		
		initToolBar();
		initStatesGrid();
		initContentPanels();
		initStateForm();		
		addListeners();
		
		// adaugare componente
		statesCP.setTopComponent(toolBar);
		statesCP.add(statesGrid);
		stateFormCP.add(stateForm);
		
		BorderLayoutData statesData = new BorderLayoutData(LayoutRegion.WEST);
		statesData.setSplit(true);
		statesData.setSize(400);
		statesData.setMinSize(400);
		statesData.setMargins(new Margins(5,5,5,5));
		
		BorderLayoutData stateFormData = new BorderLayoutData(LayoutRegion.CENTER);
		stateFormData.setSplit(true);
		stateFormData.setMinSize(400);
		stateFormData.setMargins(new Margins(5,5,5,5));
		
		add(statesCP, statesData);
		add(stateFormCP, stateFormData);
	}

	private void initToolBar(){
		toolBar = new ToolBar();
		toolBar.setSpacing(5);
		
		addButton = new Button(GwtLocaleProvider.getConstants().ADD_STATE());
		deleteButton = new Button(GwtLocaleProvider.getConstants().DELETE_STATE());
		deleteButton.setEnabled(false);
		
		toolBar.add(addButton);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(deleteButton);
	}

	private void initContentPanels(){
		statesCP = new ContentPanel();
		statesCP.setHeaderVisible(false);
		statesCP.setLayout(new FitLayout());
		statesCP.setBorders(false);
		
		stateFormCP = new ContentPanel();
		stateFormCP.setHeaderVisible(false);
		stateFormCP.setLayout(new FitLayout());
		stateFormCP.setBorders(false);
		stateFormCP.setScrollMode(Scroll.AUTO);
	}
	
	private void initStatesGrid(){
		List<ColumnConfig> cols = new ArrayList<ColumnConfig>();
		
		ColumnConfig colCode = new ColumnConfig();
		colCode.setId(WorkflowStateModel.PROPERTY_CODE);
		colCode.setHeader(GwtLocaleProvider.getConstants().CODE());
		colCode.setWidth(100);
		cols.add(colCode);
		
		ColumnConfig colName = new ColumnConfig();
		colName.setId(WorkflowStateModel.PROPERTY_NAME);
		colName.setHeader(GwtLocaleProvider.getConstants().NAME());
		colName.setWidth(200);
		cols.add(colName);
		
		ColumnConfig colStateType = new ColumnConfig();
		colStateType.setId(WorkflowStateModel.PROPERTY_DISPLAY_STATE_TYPE);
		colStateType.setHeader(GwtLocaleProvider.getConstants().TYPE());
		colStateType.setWidth(100);
		cols.add(colStateType);
		
		ListStore<WorkflowStateModel> store = new  ListStore<WorkflowStateModel>();
		store.setDefaultSort(WorkflowStateModel.PROPERTY_CODE, SortDir.ASC);
		ColumnModel model = new ColumnModel(cols);

		statesGrid = new Grid<WorkflowStateModel>(store, model);
		statesGrid.getStore().sort(WorkflowStateModel.PROPERTY_CODE, SortDir.ASC);
		GridView view = new GridView();
		view.setForceFit(true);
		statesGrid.setView(view);		
		statesGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	private void initStateForm(){
		stateForm = new FormPanel();
		stateForm.setBorders(false);
		stateForm.setBodyBorder(false);
		stateForm.setHeaderVisible(false);
		stateForm.setScrollMode(Scroll.AUTO);
		stateForm.setVisible(false);
		stateForm.setFieldWidth(315);
		
		stateId = new HiddenField<Long>();
		stateId.setPropertyEditor(new PropertyEditor<Long>() {
			@Override
			public Long convertStringValue(String value) {
				return (value == null) ? null : new Long(value);
			}
			@Override
			public String getStringValue(Long value) {
				return (value == null) ? null : value.toString();
			}
		});
		
		hiddenState = new HiddenField<WorkflowStateModel>();
		
		stateCode = new TextField<String>();
		stateCode.setFieldLabel(GwtLocaleProvider.getConstants().STATE_CODE());
		stateCode.setAllowBlank(false);
		stateCode.setValidator(new StateCodeValidator(statesGrid.getStore()));
		
		stateName = new TextField<String>();
		stateName.setFieldLabel(GwtLocaleProvider.getConstants().STATE_NAME());
		stateName.setAllowBlank(false);
		stateName.setValidator(new StateNameValidator(statesGrid.getStore()));
		
		stateStartType = new Radio();
		stateStartType.setBoxLabel(GwtLocaleProvider.getConstants().STATE_START_TYPE());
		stateStartType.setValueAttribute(String.valueOf(WorkflowStateModel.STATETYPE_START));
		
		stateIntermediateType = new Radio();
		stateIntermediateType.setBoxLabel(GwtLocaleProvider.getConstants().STATE_INTERMEDIATE_TYPE());
		stateIntermediateType.setValueAttribute(String.valueOf(WorkflowStateModel.STATETYPE_INTERMEDIATE));
		stateIntermediateType.setValue(true);
		
		stateStopType = new Radio();
		stateStopType.setBoxLabel(GwtLocaleProvider.getConstants().STATE_STOP_TYPE());
		stateStopType.setValueAttribute(String.valueOf(WorkflowStateModel.STATETYPE_STOP));
		
		stateType = new RadioGroup();
		
		stateType.setFieldLabel(GwtLocaleProvider.getConstants().STATE_TYPE());
		stateType.setOrientation(Orientation.VERTICAL);	
		stateType.setResizeFields(true);
		stateType.add(stateStartType);
		stateType.add(stateIntermediateType);
		stateType.add(stateStopType);
		stateType.setSelectionRequired(true);
		
		stateAttachmentsPermissionFS = new FieldSet();
		stateAttachmentsPermissionFS.setLayout(new FormLayout());
		stateAttachmentsPermissionFS.setHeading(GwtLocaleProvider.getConstants().
				ATTACHMENTS_MANAGEMENT_PERMISSIONS());
	
		stateAttachmentPermissionAdd = new CheckBox();
		stateAttachmentPermissionAdd.setBoxLabel(GwtLocaleProvider.getConstants().
				ATTACHMENTS_MANAGEMENT_PERMISSIONS_ADD());
		stateAttachmentPermissionAdd.setHideLabel(true);
		
		stateAttachmentPermissionDelete = new CheckBox();
		stateAttachmentPermissionDelete.setBoxLabel(GwtLocaleProvider.getConstants().
				ATTACHMENTS_MANAGEMENT_PERMISSIONS_DELELTE());
		stateAttachmentPermissionDelete.setHideLabel(true);
		
		CheckBoxGroup groupPermission = new CheckBoxGroup();
		groupPermission.setHideLabel(true);
		groupPermission.setOrientation(Orientation.HORIZONTAL);
		
		groupPermission.add(stateAttachmentPermissionAdd);
		groupPermission.add(stateAttachmentPermissionDelete);
		stateAttachmentsPermissionFS.add(groupPermission);
				
		saveStateButton = new Button(GwtLocaleProvider.getConstants().SAVE_STATE());
		
		FormData fd = new FormData();
		stateForm.add(stateId);
		stateForm.add(stateCode, fd);
		stateForm.add(stateName, fd);
		stateForm.add(stateType, fd);
		stateForm.add(stateAttachmentsPermissionFS, fd);
		stateForm.addButton(saveStateButton);
	}
	
	private void addListeners(){
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				prepareStateFormForAdd();				
			}
		});
		
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				// inainte ca sa sterg ma uit sa vad daca exista folosita in tranzitii
				final WorkflowStateModel state = statesGrid.getSelectionModel().getSelectedItem();
				if (!isStateUsedInTransitions(state)) {
					MessageBox.confirm(GwtLocaleProvider.getConstants().DELETE(), GwtLocaleProvider.getMessages().CONFIRM_DELETE_STATE(),
							new Listener<MessageBoxEvent>() {
								public void handleEvent(MessageBoxEvent mbe) {
									if (mbe.getButtonClicked().getItemId().equals(Dialog.YES)) {
										statesGrid.getStore().remove(state);
										stateForm.hide();
										changeToolBarPerspective(null);
									}
								}
							});					
				} else {
					MessageBox.alert(GwtLocaleProvider.getConstants().ERROR(), GwtLocaleProvider.getMessages().CANNOT_DELETE_STATE(), null);
				}
			}
		});
		
		statesGrid.addListener(Events.CellClick, new Listener<GridEvent<WorkflowStateModel>>(){
			@Override
			public void handleEvent(GridEvent<WorkflowStateModel> be) {
				changeToolBarPerspective(be.getModel());
				prepareStateFormForEdit(be.getModel());
			}
		});
		
		saveStateButton.addSelectionListener(new  SelectionListener<ButtonEvent>(){
			public void componentSelected(ButtonEvent ce) {
				if (hiddenState.getValue() != null){
					if (stateForm.isValid()){
						WorkflowStateModel state = hiddenState.getValue();
						populateWorkflowStateModelFromForm(state);
						statesGrid.getStore().update(state);
						updateStateInTransitions(state);
						prepareStateFormForEdit(state);
					}
				}else {
					if (stateForm.isValid()){
						WorkflowStateModel state = new WorkflowStateModel();
						populateWorkflowStateModelFromForm(state);
						state.setTempId(++tempId);
						statesGrid.getStore().add(state);
						statesGrid.getSelectionModel().select(state, false);
						stateForm.hide();
					}
				}
			};
		});
	}
	
	/**
	 * Face update in tranzitii la o stare.
	 * @param state
	 */
	private void updateStateInTransitions(WorkflowStateModel state){
		for (WorkflowTransitionModel t : mainWindow.getTransitionsTab().getTransitions().getModels()){
			if (t.getStartState().getTempId().intValue() == state.getTempId().intValue()){
				t.getStartState().setCode(state.getCode());
				t.setStartStateCode(state.getCode());
				t.getStartState().setName(state.getName());
				t.setStartStateName(state.getName());
				t.getStartState().setStateType(state.getStateType());
				t.getStartState().setAttachmentsPermission(state.getAttachmentsPermission());
				mainWindow.getTransitionsTab().getTransitions().update(t);
			}
			if (t.getFinalState().getTempId().intValue() == state.getTempId().intValue()){
				t.getFinalState().setCode(state.getCode());
				t.setFinalStateCode(state.getCode());
				t.getFinalState().setName(state.getName());
				t.setFinalStateName(state.getName());
				t.getFinalState().setStateType(state.getStateType());
				t.getFinalState().setAttachmentsPermission(state.getAttachmentsPermission());
				mainWindow.getTransitionsTab().getTransitions().update(t);
			}
		}
	}
	
	private boolean isStateUsedInTransitions(WorkflowStateModel state){
		List<WorkflowTransitionModel> transitions = mainWindow.getTransitionsTab().getTransitions().getModels();
		for (WorkflowTransitionModel t : transitions){
			if (t.getStartState().getTempId().intValue() == state.getTempId().intValue()){
				return true;
			}
			if (t.getFinalState().getTempId().intValue() == state.getTempId().intValue()){
				return true;
			}
		}
		return false;
	}
	
	private void populateWorkflowStateModelFromForm(WorkflowStateModel state){
		state.setCode(stateCode.getValue());
		state.setName(stateName.getValue());
		state.setStateType(Integer.valueOf(stateType.getValue().getValueAttribute()));
		state.setDisplayStateType(state.getStateType());
		if (stateAttachmentPermissionAdd.getValue() && stateAttachmentPermissionDelete.getValue())
			state.setAttachmentsPermission(WorkflowStateModel.ATTACH_PERM_ALL);
		else if (stateAttachmentPermissionAdd.getValue())
			state.setAttachmentsPermission(WorkflowStateModel.ATTACH_PERM_ADD);
		else if (stateAttachmentPermissionDelete.getValue())
			state.setAttachmentsPermission(WorkflowStateModel.ATTACH_PERM_DELETE);
		else
			state.setAttachmentsPermission(0);
	}
	
	private void prepareStateFormForAdd(){
		statesGrid.getSelectionModel().deselectAll();
		stateType.setValue(stateIntermediateType);
		stateCode.setData("hiddenState", null);
		stateName.setData("hiddenState", null);
		stateCode.setValue("");
		stateName.setValue("");
		hiddenState.setValue(null);		
		stateAttachmentPermissionAdd.setValue(true);
		stateAttachmentPermissionDelete.setValue(true);
		// verific daca exista stare Initiala adaugata, daca da atunci fac disable
		// tipul de stare Initiala 
		WorkflowStateModel model = statesGrid.getStore().findModel(
				WorkflowStateModel.PROPERTY_STATE_TYPE ,WorkflowStateModel.STATETYPE_START);
		stateStartType.setEnabled(model != null ? false : true);
		stateForm.setVisible(true);
		
		stateFormCP.setWidth(stateFormCP.getWidth()-1);
	}
	
	private void prepareStateFormForEdit(WorkflowStateModel workflowStateModel){
		stateId.setValue(workflowStateModel.getId());
		hiddenState.setValue(workflowStateModel);
		stateCode.setData("hiddenState", hiddenState.getValue());
		stateName.setData("hiddenState", hiddenState.getValue());
		stateCode.setValue(workflowStateModel.getCode());
		stateName.setValue(workflowStateModel.getName());
		
		switch (workflowStateModel.getStateType().intValue()) {
			case WorkflowStateModel.STATETYPE_START:
				stateType.setValue(stateStartType);
				break;
			case WorkflowStateModel.STATETYPE_INTERMEDIATE:
				stateType.setValue(stateIntermediateType);
				break;
			case WorkflowStateModel.STATETYPE_STOP:
				stateType.setValue(stateStopType);
				break;
			default:
				break;
		}
		
		stateAttachmentPermissionAdd.setValue(false);
		stateAttachmentPermissionDelete.setValue(false);
		if (workflowStateModel.getAttachmentsPermission().intValue() == WorkflowStateModel.ATTACH_PERM_ALL){
			stateAttachmentPermissionAdd.setValue(true);
			stateAttachmentPermissionDelete.setValue(true);
		}else if (workflowStateModel.getAttachmentsPermission().intValue() == WorkflowStateModel.ATTACH_PERM_ADD)
			stateAttachmentPermissionAdd.setValue(true);
		else if (workflowStateModel.getAttachmentsPermission().intValue() == WorkflowStateModel.ATTACH_PERM_DELETE)
			stateAttachmentPermissionDelete.setValue(true);
		// verific daca exista stare Initiala adaugata, daca da atunci fac disable
		// tipul de stare Initiala 
		WorkflowStateModel model = statesGrid.getStore().findModel(
				WorkflowStateModel.PROPERTY_STATE_TYPE ,WorkflowStateModel.STATETYPE_START);
		stateStartType.setEnabled(model != null ? false : true);
		stateForm.setVisible(true);
		stateFormCP.setWidth(stateFormCP.getWidth()-1);
	}
	
	private void changeToolBarPerspective(WorkflowStateModel workflowStateModel){
		if (workflowStateModel != null){
			deleteButton.setEnabled(true);
		}else{
			deleteButton.setEnabled(false);
		}
	}
	
	public List<WorkflowStateModel> getStates(){
		return statesGrid.getStore().getModels();
	}
	
	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void populateForSave(WorkflowModel workflowModel) {
		// nu are ce popula
	}

	@Override
	public void prepareForAdd() {
		reset();
	}

	@Override
	public void prepareForEdit(WorkflowModel workflowModel) {
		reset();
		
		// iau starile din tranzitii
		List<WorkflowTransitionModel> transitions = workflowModel.getTransitions();
		List<WorkflowStateModel> states = new ArrayList<WorkflowStateModel>();
		if (transitions != null && transitions.size() > 0){
			for (WorkflowTransitionModel t : transitions){
				WorkflowStateModel state = t.getStartState();
				if (!existState(states, t.getStartState())){
					state.setDisplayStateType(state.getStateType());
					state.setTempId(++tempId);
					states.add(state);
				}
				state = t.getFinalState();
				if (!existState(states, state)){
					state.setDisplayStateType(state.getStateType());
					state.setTempId(++tempId);
					states.add(state);
				}
			}
		}
		statesGrid.getStore().setDefaultSort(WorkflowStateModel.PROPERTY_CODE, SortDir.ASC);
		statesGrid.getStore().add(states);
		
		// le pun la toate starile din tranzitii tempid in functie de starea 
		// din care face parte
		for (WorkflowTransitionModel wtm : workflowModel.getTransitions()){
			Long id = wtm.getStartState().getId();
			wtm.getStartState().setTempId(getTempIdFromStatesByStateId(id));
			id = wtm.getFinalState().getId();
			wtm.getFinalState().setTempId(getTempIdFromStatesByStateId(id));
		}
	}
	
	/**
	 * Cauta daca intr-o lista de stari exista o anumita stare.
	 * @param states
	 * @param state
	 * @return
	 */
	private boolean existState(List<WorkflowStateModel> states, WorkflowStateModel state){
		for (WorkflowStateModel s : states){
			if (s.getId().longValue() == state.getId().longValue()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Cauta o stare in lista starilor dupa id si returneaza tempId. Metoda
	 * returneaza null daca starile nu sunt din baza de date ci adaugate nou.
	 * @param id
	 * @return
	 */
	private Integer getTempIdFromStatesByStateId(Long id){
		for (WorkflowStateModel wsm : statesGrid.getStore().getModels()){
			if (wsm.getId().longValue() == id.longValue())
				return wsm.getTempId();
		}
		return null;
	}

	@Override
	public void reset() {
		stateForm.setVisible(false);
		stateForm.clear();
		statesGrid.getStore().removeAll();
		changeToolBarPerspective(null);
		tempId = 0;
	}
	
}

class StateCodeValidator implements Validator {

	private ListStore<WorkflowStateModel> states;
	
	public StateCodeValidator(ListStore<WorkflowStateModel> states) {
		this.states = states;
	}
	
	@Override
	public String validate(Field<?> field, String value) {
		if (value != null){
			if (value.trim().length() == 0)
				return GwtLocaleProvider.getMessages().ONLY_BLANK_SPACES_ARE_NOT_ALLOWED();
			
			WorkflowStateModel hiddenState = (WorkflowStateModel)field.getData("hiddenState");
			WorkflowStateModel model = null;
			
			for (WorkflowStateModel wsm : states.getModels()){
				if (wsm.getCode().compareToIgnoreCase(value) == 0){
					model = wsm;
					break;
				}
			}
			
			if (model != null){
				if (hiddenState != null){
					if (hiddenState.equals(model))
						return null;
					else
						return GwtLocaleProvider.getMessages().STATE_WITH_THIS_CODE_ALREADY_EXISTS();
				}else
					return GwtLocaleProvider.getMessages().STATE_WITH_THIS_CODE_ALREADY_EXISTS();
			}
		}			
		return null;
	}
	
}

class StateNameValidator implements Validator {

	private ListStore<WorkflowStateModel> states;
	
	public StateNameValidator(ListStore<WorkflowStateModel> states) {
		this.states = states;
	}
	
	@Override
	public String validate(Field<?> field, String value) {
		if (value != null){
			if (value.trim().length() == 0)
				return GwtLocaleProvider.getMessages().ONLY_BLANK_SPACES_ARE_NOT_ALLOWED();
			
			WorkflowStateModel hiddenState = (WorkflowStateModel)field.getData("hiddenState");
			WorkflowStateModel model = null;
			
			for (WorkflowStateModel wsm : states.getModels()){
				if (wsm.getName().compareToIgnoreCase(value) == 0){
					model = wsm;
					break;
				}
			}
			
			if (model != null){
				if (hiddenState != null){
					if (hiddenState.equals(model))
						return null;
					else
						return GwtLocaleProvider.getMessages().STATE_WITH_THIS_NAME_ALREADY_EXISTS();
				}else 
					return GwtLocaleProvider.getMessages().STATE_WITH_THIS_NAME_ALREADY_EXISTS();
			}
		}			
		return null;
	}

}