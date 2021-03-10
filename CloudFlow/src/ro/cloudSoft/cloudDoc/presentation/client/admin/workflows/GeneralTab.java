package ro.cloudSoft.cloudDoc.presentation.client.admin.workflows;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtNumberUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.PropertyEditors;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.form.DualListField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GeneralTab extends WorkflowAbstractTab {
	
	private static final int FROM_LIST_ORIGINAL_WIDTH = 562;
	private static final int TO_LIST_ORIGINAL_WIDTH = 562;

	private WorkflowWindow mainWindow;
	
	private FormPanel formPanel;
	
	// campurile din formular
	private HiddenField<Long> idHiddenField;
	private HiddenField<Long> baseVersionWorkflowIdHiddenField;
	private HiddenField<Long> sourceVersionWorkflowIddHiddenField;
	private TextField<String> nameTextField;
	private TextArea descriptionTextArea;
	private NumberField versionNumberField;
	private FieldSet documentTypesFieldSet;
	
	// elementele din FieldSet-ul pentru tipuri de documente
	DualListField<DocumentTypeModel> documentTypesDualListField;
	
	public GeneralTab(WorkflowWindow mainWindow) {
		
		this.mainWindow = mainWindow;
		
		setText(GwtLocaleProvider.getConstants().GENERAL());
		setScrollMode(Scroll.AUTO);
		
		initForm();
		addDoubleClickActions();
	}
	
	private void initForm() {
		formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setFieldWidth(420);
		formPanel.setBodyBorder(false);
		
		idHiddenField = new HiddenField<Long>();
		idHiddenField.setPropertyEditor(PropertyEditors.LONG);
		
		baseVersionWorkflowIdHiddenField = new HiddenField<Long>();
		baseVersionWorkflowIdHiddenField.setPropertyEditor(PropertyEditors.LONG);

		sourceVersionWorkflowIddHiddenField = new HiddenField<Long>();
		sourceVersionWorkflowIddHiddenField.setPropertyEditor(PropertyEditors.LONG);
		
		nameTextField = new TextField<String>();
		nameTextField.setFieldLabel(GwtLocaleProvider.getConstants().NAME());
		nameTextField.setMaxLength(WorkflowModel.LENGTH_NAME);
		
		descriptionTextArea = new TextArea();
		descriptionTextArea.setFieldLabel(
			GwtLocaleProvider.getConstants().DESCRIPTION());
		descriptionTextArea.setMaxLength(WorkflowModel.LENGTH_DESCRIPTION);
		
		versionNumberField = new NumberField();
		versionNumberField.setEmptyText("(" + GwtLocaleProvider.getConstants().AUTO_GENERATED() + ")");
		versionNumberField.setFieldLabel(GwtLocaleProvider.getConstants().VERSION_NR());
		versionNumberField.setReadOnly(true);
		
		documentTypesFieldSet = new FieldSet();
		documentTypesFieldSet.setCollapsible(true);
		documentTypesFieldSet.setHeading(
			GwtLocaleProvider.getConstants().DOCUMENT_TYPES());
		
		documentTypesDualListField = new DualListField<DocumentTypeModel>();
		documentTypesDualListField.getFromList().setStore(
			new ListStore<DocumentTypeModel>());
		documentTypesDualListField.getFromList().setDisplayField(
			DocumentTypeModel.PROPERTY_NAME);
		documentTypesDualListField.getFromList().setWidth(FROM_LIST_ORIGINAL_WIDTH);
		documentTypesDualListField.getToList().setStore(
			new ListStore<DocumentTypeModel>());
		documentTypesDualListField.getToList().setDisplayField(
			DocumentTypeModel.PROPERTY_NAME);
		documentTypesDualListField.getToList().setWidth(TO_LIST_ORIGINAL_WIDTH);

		documentTypesDualListField.getToList().getStore().addStoreListener(new StoreListener<DocumentTypeModel>() {
			
			@Override
			public void storeAdd(StoreEvent<DocumentTypeModel> se) {
				mainWindow.doWithSelectedDocumentTypes(getSelectedDocumentTypes());
			}
			
			@Override
			public void storeRemove(StoreEvent<DocumentTypeModel> se) {
				mainWindow.doWithSelectedDocumentTypes(getSelectedDocumentTypes());
			}
			
			private List<DocumentTypeModel> getSelectedDocumentTypes() {
				return documentTypesDualListField.getToList().getStore().getModels();
			}
		});
		
		documentTypesFieldSet.add(documentTypesDualListField);
		
		formPanel.add(idHiddenField);
		formPanel.add(baseVersionWorkflowIdHiddenField);
		formPanel.add(sourceVersionWorkflowIddHiddenField);
		formPanel.add(nameTextField);
		formPanel.add(descriptionTextArea);
		formPanel.add(versionNumberField);
		formPanel.add(documentTypesFieldSet);
		
		// validarea formularului
		nameTextField.setAllowBlank(false);
		
		add(formPanel);
	}

	@Override
	public void reset() {
		// Resetam formularul.
		formPanel.clear();
		// Goleste lista dubla cu tipurile de documente.
		documentTypesDualListField.getFromList().getStore().removeAll();
		documentTypesDualListField.getToList().getStore().removeAll();
	}

	@Override
	public boolean isValid() {
		
		boolean isValid = true;
		
		if (!formPanel.isValid()) {
			isValid = false;
		}
		
		if (documentTypesDualListField.getToList().getStore().getModels().isEmpty()) {
			ErrorHelper.addError(GwtLocaleProvider.getMessages().WORKFLOW_HAS_NO_ASSOCIATED_DOCUMENT_TYPES());
			isValid = false;
		}
		
		return isValid;
	}

	@Override
	public void populateForSave(WorkflowModel workflowModel) {
		workflowModel.setId(idHiddenField.getValue());
		workflowModel.setName(nameTextField.getValue());
		workflowModel.setDescription(descriptionTextArea.getValue());
		
		workflowModel.setBaseVersionWorkflowId(baseVersionWorkflowIdHiddenField.getValue());
		workflowModel.setSourceVersionWorkflowId(sourceVersionWorkflowIddHiddenField.getValue());
		workflowModel.setVersionNumber(GwtNumberUtils.integerValue(versionNumberField.getValue()));
		
		workflowModel.setDocumentTypes(
			documentTypesDualListField.getToList().getStore().getModels());
	}
	
	private void populateDocumentTypesDualListField(final List<DocumentTypeModel> documentTypes) {
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentTypeService().getDocumentTypesWithNoWorkflow(new AsyncCallback<List<DocumentTypeModel>>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<DocumentTypeModel> result) {
				if (result != null) {
					documentTypesDualListField.getFromList().getStore().add(result);
				}
				if (documentTypes != null) {
					documentTypesDualListField.getToList().getStore().add(documentTypes);
				}
				LoadingManager.get().loadingComplete();
			}
		});
	}

	@Override
	public void prepareForAdd() {
		reset();
		
		// Populeaza lista dubla cu tipurile de documente.
		populateDocumentTypesDualListField(null);
	}

	@Override
	public void prepareForEdit(WorkflowModel workflowModel) {
		reset();
		
		// Populeaza campurile formularului.
		idHiddenField.setValue(workflowModel.getId());
		nameTextField.setValue(workflowModel.getName());
		descriptionTextArea.setValue(workflowModel.getDescription());
		
		baseVersionWorkflowIdHiddenField.setValue(workflowModel.getBaseVersionWorkflowId());
		sourceVersionWorkflowIddHiddenField.setValue(workflowModel.getSourceVersionWorkflowId());
		versionNumberField.setValue(workflowModel.getVersionNumber());
		
		// Populeaza lista dubla cu tipurile de documente.
		populateDocumentTypesDualListField(workflowModel.getDocumentTypes());
	}
	
	/**
	 * Retuneaza o lista cu toate id-urile documentelor din lista dreapta.
	 * @return List<Long>
	 */
	public List<Long> getDocumetTypeIds(){		
		List<Long> documentTypeIds = new ArrayList<Long>();
		List<DocumentTypeModel> docs = documentTypesDualListField.getToList().getStore().getModels();
		for (DocumentTypeModel doc : docs){
			documentTypeIds.add(doc.getId());
		}
		return documentTypeIds;
	}
	
	private void addDoubleClickActions(){
		documentTypesDualListField.getFromList().getListView().addListener(Events.DoubleClick, new Listener<ListViewEvent<DocumentTypeModel>>(){
			public void handleEvent(ListViewEvent<DocumentTypeModel> be) {
				documentTypesDualListField.getToList().getStore().add(be.getModel());
				documentTypesDualListField.getFromList().getStore().remove(be.getModel());
			}			
		});
		documentTypesDualListField.getToList().getListView().addListener(Events.DoubleClick, new Listener<ListViewEvent<DocumentTypeModel>>(){
			public void handleEvent(ListViewEvent<DocumentTypeModel> be) {
				documentTypesDualListField.getFromList().getStore().add(be.getModel());
				documentTypesDualListField.getToList().getStore().remove(be.getModel());
			}			
		});
	}	
	
	protected void changeSize(float wIndex, float hIndex){
		if (wIndex > 0.83){
			documentTypesDualListField.getFromList().setWidth(Math.round(FROM_LIST_ORIGINAL_WIDTH * wIndex));
			documentTypesDualListField.getToList().setWidth(Math.round(TO_LIST_ORIGINAL_WIDTH * wIndex)) ;
		} else if (wIndex > 0.62){
			documentTypesDualListField.getFromList().setWidth(Math.round(FROM_LIST_ORIGINAL_WIDTH * wIndex)-10);
			documentTypesDualListField.getToList().setWidth(Math.round(TO_LIST_ORIGINAL_WIDTH * wIndex)-10) ;
		} else {
			documentTypesDualListField.getFromList().setWidth(Math.round(FROM_LIST_ORIGINAL_WIDTH * wIndex)-20);
			documentTypesDualListField.getToList().setWidth(Math.round(TO_LIST_ORIGINAL_WIDTH * wIndex)-20) ;
		}
		
	}
}