package ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes.MetadataDefinitionAddOrEditForm.MetadataDefinitionsParentType;
import ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes.MetadataDefinitionAddOrEditForm.ParentOfMetadataDefinitionAddOrEditForm;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.validators.MetadataDefinitionNameValidator.MetadataDefinitionNameValidatorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.DeleteConfirmDialog;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class MetadataTab extends TabItem implements ParentOfMetadataDefinitionAddOrEditForm, MetadataDefinitionNameValidatorHelper {
	
	private ContentPanel mainPanel;
	
	private ContentPanel gridPanel;
	private MetadataDefinitionAddOrEditForm addOrEditForm;
	
	private MetadataDefinitionsGrid grid;
	
	private ToolBar toolBar;
	
	private Button addButton;
	private Button deleteButton;
	
	public MetadataTab() {
		initTab();
	}
	
	private void initTab() {
		
		setText(GwtLocaleProvider.getConstants().METADATA());
		setScrollMode(Scroll.AUTO);	
		setLayout(new FitLayout());
		
		addListener(Events.Select, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				onSelectTab();
			}
		});

		initMainPanel();
		add(mainPanel);	
	}

	private void initMainPanel() {
		
		mainPanel = new ContentPanel();
		mainPanel.setHeaderVisible(false);
		mainPanel.setLayout(new BorderLayout());	
		
		initGridPanel();

		BorderLayoutData borderLayoutDataForMetadataGridPanel = new BorderLayoutData(LayoutRegion.WEST);
		borderLayoutDataForMetadataGridPanel.setSize(300);
		borderLayoutDataForMetadataGridPanel.setMinSize(200);
		borderLayoutDataForMetadataGridPanel.setMaxSize(500);		
		borderLayoutDataForMetadataGridPanel.setSplit(true);

		mainPanel.add(gridPanel, borderLayoutDataForMetadataGridPanel);
		
		
		initAddOrEditForm();
		
		BorderLayoutData borderLayoutDataForForm = new BorderLayoutData(LayoutRegion.CENTER);
		borderLayoutDataForForm.setMinSize(460);
		borderLayoutDataForForm.setMaxSize(600);
		
		mainPanel.add(addOrEditForm, borderLayoutDataForForm);
	}
	
	private void initGridPanel() {
		
		gridPanel = new ContentPanel();
		gridPanel.setHeaderVisible(false);
		gridPanel.setLayout(new FitLayout());
		
		initToolBar();
		gridPanel.setTopComponent(toolBar);

		initGrid();
		gridPanel.add(grid);		
	}
	
	private void initAddOrEditForm() {
		addOrEditForm = new MetadataDefinitionAddOrEditForm(MetadataDefinitionsParentType.DOCUMENT_TYPE, this);			
	}
	
	private void initToolBar() {
		
		toolBar = new ToolBar();
		
		addButton = new Button();
		addButton.setText(GwtLocaleProvider.getConstants().ADD());
		toolBar.add(addButton);
		
		deleteButton = new Button();
		deleteButton.setText(GwtLocaleProvider.getConstants().DELETE());
		toolBar.add(deleteButton);
		
		addButtonsActions();
	}
	
	private void addButtonsActions() {
		
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				grid.getSelectionModel().deselectAll();

				updateToolBarPerspective();
				
				addOrEditForm.prepareForAdd();
				addOrEditForm.setEnabled(true);
			}			
		});
		
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				
				final MetadataDefinitionModel selectedMetadataDefinition = getSelectedMetadataDefinitionFromGrid();
				if (selectedMetadataDefinition == null) {
					return;
				}

				new DeleteConfirmDialog() {
					
					@Override
					protected void onConfirmation() {
						grid.getStore().remove(selectedMetadataDefinition);
						onMetadataDefinitionRelatedAction();
					}
				};
			}			
		});
	}
	
	private void initGrid() {
		grid = new MetadataDefinitionsGrid();
		grid.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<MetadataDefinitionModel>>(){
			
			@Override
			public void handleEvent(SelectionChangedEvent<MetadataDefinitionModel> selectEvent) {
				
				MetadataDefinitionModel selectedMetadataDefinition = getSelectedMetadataDefinitionFromGrid();
		 		if (selectedMetadataDefinition == null) {
		 			return;
		 		}
		 		
		 		updateToolBarPerspective();
				
	 			addOrEditForm.prepareForEdit(selectedMetadataDefinition);
				addOrEditForm.setEnabled(true);
			}		
		});			
	}
	
	private void onSelectTab() {
		/*
		 * Workaround: Daca nu se reconfigureaza grid-ul, la o afisare ulterioara a tab-ului,
		 * grid-ul nu va mai avea scroll orizontal (si nu se vor mai vedea toate coloanele).
		 */
		ComponentUtils.ensureGridIsProperlyRendered(grid);
		/*
		 * Workaround: Daca fereastra a mai fost deschisa si a ramas selectat alt tab decat acesta,
		 * la o deschidere ulterioara, la selectarea tab-ului acesta, formularul pentru adaugare / modificare
		 * va arata ca si cum ar fi activ, insa doar butoanele sunt interactionabile
		 * (si da eroare JS daca se da click pe butonul de adaugare / salvare).
		 */
		onMetadataDefinitionRelatedAction();
	}
	
	private void reset() {
		
		grid.getStore().removeAll();
		
		grid.getSelectionModel().deselectAll();
		
		updateToolBarPerspective();
		
		addOrEditForm.resetWorkflowStates();
		addOrEditForm.resetForm();
		addOrEditForm.setEnabled(false);
	}
	
	public boolean isValid() {
		return true;
	}
		
	public void prepareForAdd() {
		reset();
	}
	
	public void prepareForEdit(DocumentTypeModel documentType, List<WorkflowStateModel> workflowStates) {
		
		reset();
		
		grid.getStore().add(documentType.getMetadataDefinitions());
		addOrEditForm.setWorkflowStates(workflowStates);
	}
		
	public void populate(DocumentTypeModel documentType) {
		List<MetadataDefinitionModel> metadataDefinitions = grid.getStore().getModels();
		documentType.setMetadataDefinitions(metadataDefinitions);		
	}
	
	public MetadataDefinitionModel getSelectedMetadataDefinitionFromGrid() {
		return grid.getSelectionModel().getSelectedItem();
	}
	
	private void updateToolBarPerspective() {
		MetadataDefinitionModel selectedMetadataDefinition = getSelectedMetadataDefinitionFromGrid();
 		boolean isMetadataDefinitionSelected = (selectedMetadataDefinition != null);
		deleteButton.setEnabled(isMetadataDefinitionSelected);
	}
	
	@Override
	public void onAddedOrEditedMetadataDefinition(MetadataDefinitionModel savedMetadataDefinition) {
		
		int insertIndex = grid.getStore().getModels().size();
		
		MetadataDefinitionModel editedMetadataDefinition = getEditedMetadataDefinition();
		if (editedMetadataDefinition != null) {
			
			// Se va inlocui cea veche (pe care s-a dat editare) cu cea noua (cea salvata).
			
			beforeReplaceMetadataDefinition(editedMetadataDefinition, savedMetadataDefinition);
			
			int indexOfEditedMetadataDefinition = grid.getStore().indexOf(editedMetadataDefinition);
			grid.getStore().remove(editedMetadataDefinition);
			insertIndex = indexOfEditedMetadataDefinition;
		}
		
		grid.getStore().insert(savedMetadataDefinition, insertIndex);
		
		onMetadataDefinitionRelatedAction();
	}
	
	private void beforeReplaceMetadataDefinition(MetadataDefinitionModel oldMetadataDefinition, MetadataDefinitionModel newMetadataDefinition) {
		if (oldMetadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_METADATA_COLLECTION)
				&& newMetadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_METADATA_COLLECTION)) {
			
			MetadataCollectionDefinitionModel oldMetadataCollectionDefinition = (MetadataCollectionDefinitionModel) oldMetadataDefinition;
			MetadataCollectionDefinitionModel newMetadataCollectionDefinition = (MetadataCollectionDefinitionModel) newMetadataDefinition;
			
			copyCollectionPropertiesBeforeReplacement(oldMetadataCollectionDefinition, newMetadataCollectionDefinition);
		}
	}
	
	private void copyCollectionPropertiesBeforeReplacement(MetadataCollectionDefinitionModel oldMetadataCollectionDefinition, MetadataCollectionDefinitionModel newMetadataCollectionDefinition) {
		newMetadataCollectionDefinition.setMetadataDefinitions(oldMetadataCollectionDefinition.getMetadataDefinitions());
	}
	
	@Override
	public void onCancelAddOrEditMetadataDefinition() {
		onMetadataDefinitionRelatedAction();
	}
	
	private void onMetadataDefinitionRelatedAction() {
		
		grid.getSelectionModel().deselectAll();
		
		updateToolBarPerspective();
		
		addOrEditForm.resetForm();
		addOrEditForm.setEnabled(false);
	}
	
	@Override
	public MetadataDefinitionModel findMetadataDefinitionByName(String name) {
		return grid.getStore().findModel(MetadataDefinitionModel.PROPERTY_NAME, name);
	}
	
	@Override
	public MetadataDefinitionModel getEditedMetadataDefinition() {
		return getSelectedMetadataDefinitionFromGrid();
	}
	
	public ListStore<MetadataDefinitionModel> getMetadataDefinitionsStore() {
		return grid.getStore();
	}
}