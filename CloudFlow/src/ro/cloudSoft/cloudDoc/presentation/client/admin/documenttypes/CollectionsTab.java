package ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes.MetadataDefinitionAddOrEditForm.MetadataDefinitionsParentType;
import ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes.MetadataDefinitionAddOrEditForm.ParentOfMetadataDefinitionAddOrEditForm;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.validators.MetadataDefinitionNameValidator.MetadataDefinitionNameValidatorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.DeleteConfirmDialog;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class CollectionsTab extends TabItem implements ParentOfMetadataDefinitionAddOrEditForm, MetadataDefinitionNameValidatorHelper {

	private final MetadataTab metadataTab;

	private ContentPanel collectionsListPanel;
	private ContentPanel metadataDefinitionsGridPanel;
	private MetadataDefinitionAddOrEditForm metadataDefinitionAddOrEditForm;

	private ListView<MetadataCollectionDefinitionModel> collectionListView;

	private ToolBar metadataDefinitionsToolBar;
	private MetadataDefinitionsGrid metadataDefinitionsGrid;

	private Button addMetadataDefinitionButton;
	private Button deleteMetadataDefinitionButton;

	public CollectionsTab(MetadataTab metadataTab) {

		this.metadataTab = metadataTab;
		
		initTab();
	}
	
	private void initTab() {

		setText(GwtLocaleProvider.getConstants().METADATA_COLLECTIONS());
		setScrollMode(Scroll.AUTO);
		setLayout(new RowLayout(Orientation.HORIZONTAL));

		addListener(Events.Select, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent event) {
				updateCollectionsListViewFromMetadataTab();
			}
		});

		initCollectionsListPanel();

		RowData collectionsListPanelBorderLayoutData = new RowData(0.2, 1);
		collectionsListPanelBorderLayoutData.setMargins(new Margins(2));
		add(collectionsListPanel, collectionsListPanelBorderLayoutData);
		
		initMetadataDefinitionsGridPanelPanel();

		RowData metadataDefinitionsGridPanelBorderLayoutData = new RowData(0.3, 1);
		metadataDefinitionsGridPanelBorderLayoutData.setMargins(new Margins(2));
		add(metadataDefinitionsGridPanel, metadataDefinitionsGridPanelBorderLayoutData);
		
		initMetadataDefinitionAddOrEditForm();

		RowData metadataDefinitionAddOrEditFormBorderLayoutData = new RowData(0.5, 1);
		metadataDefinitionAddOrEditFormBorderLayoutData.setMargins(new Margins(2));
		add(metadataDefinitionAddOrEditForm, metadataDefinitionAddOrEditFormBorderLayoutData);
	}

	private void initCollectionsListPanel() {
		
		collectionsListPanel = new ContentPanel();
		collectionsListPanel.setHeaderVisible(false);
		collectionsListPanel.setBodyBorder(false);
		collectionsListPanel.setLayout(new FitLayout());

		collectionListView = new ListView<MetadataCollectionDefinitionModel>(new ListStore<MetadataCollectionDefinitionModel>());
		collectionListView.setDisplayProperty(MetadataCollectionDefinitionModel.PROPERTY_NAME);
		collectionListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		collectionListView.addListener(Events.Select, new Listener<ListViewEvent<MetadataCollectionDefinitionModel>>() {
			
			@Override
			public void handleEvent(ListViewEvent<MetadataCollectionDefinitionModel> event) {
				onCollectionSelectionChange();
			}
		});
		collectionListView.getStore().addStoreListener(new StoreListener<MetadataCollectionDefinitionModel>() {
			
			@Override
			public void storeClear(StoreEvent<MetadataCollectionDefinitionModel> se) {
				onCollectionSelectionChange();
			}
		});
		collectionsListPanel.add(collectionListView);
	}

	private void initMetadataDefinitionsGridPanelPanel() {
		
		metadataDefinitionsGridPanel = new ContentPanel();
		metadataDefinitionsGridPanel.setHeaderVisible(false);
		metadataDefinitionsGridPanel.setLayout(new FitLayout());

		initMetadataDefinitionsToolBar();
		metadataDefinitionsGridPanel.setTopComponent(metadataDefinitionsToolBar);
		
		initMetadataDefinitionsGrid();
		metadataDefinitionsGridPanel.add(metadataDefinitionsGrid);
	}

	private void initMetadataDefinitionsToolBar() {
		
		metadataDefinitionsToolBar = new ToolBar();

		addMetadataDefinitionButton = new Button();
		addMetadataDefinitionButton.setText(GwtLocaleProvider.getConstants().ADD());

		deleteMetadataDefinitionButton = new Button();
		deleteMetadataDefinitionButton.setText(GwtLocaleProvider.getConstants().DELETE());

		metadataDefinitionsToolBar.add(addMetadataDefinitionButton);
		metadataDefinitionsToolBar.add(deleteMetadataDefinitionButton);

		addMetadataDefinitionButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {

				metadataDefinitionsGrid.getSelectionModel().deselectAll();

				updateMetadataDefinitionsToolBarPerspective();
				
				metadataDefinitionAddOrEditForm.prepareForAdd();
				metadataDefinitionAddOrEditForm.setEnabled(true);
			}
		});
		deleteMetadataDefinitionButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {

				final MetadataDefinitionModel selectedMetadataDefinition = getSelectedMetadataDefinitionFromGrid();
				if (selectedMetadataDefinition == null) {
					return;
				}

				new DeleteConfirmDialog() {
					
					@Override
					protected void onConfirmation() {
						metadataDefinitionsGrid.getStore().remove(selectedMetadataDefinition);
						onMetadataDefinitionRelatedAction();
						updateModifiedCollection();
					}
				};			
			}
		});
	}

	private void initMetadataDefinitionsGrid() {
		metadataDefinitionsGrid = new MetadataDefinitionsGrid();
		metadataDefinitionsGrid.addListener(Events.RowClick, new Listener<GridEvent<MetadataDefinitionModel>>() {
			
			@Override
			public void handleEvent(GridEvent<MetadataDefinitionModel> event) {

				MetadataDefinitionModel selectedMetadataDefinition = getSelectedMetadataDefinitionFromGrid();
		 		if (selectedMetadataDefinition == null) {
		 			return;
		 		}
		 		
		 		updateMetadataDefinitionsToolBarPerspective();
				
		 		metadataDefinitionAddOrEditForm.prepareForEdit(selectedMetadataDefinition);
		 		metadataDefinitionAddOrEditForm.setEnabled(true);
			}
		});
	}

	private void initMetadataDefinitionAddOrEditForm() {
		metadataDefinitionAddOrEditForm = new MetadataDefinitionAddOrEditForm(MetadataDefinitionsParentType.COLLECTION, this);
	}
	
	private MetadataCollectionDefinitionModel getSelectedCollectionFromListView() {
		return collectionListView.getSelectionModel().getSelectedItem();
	}
	
	private MetadataDefinitionModel getSelectedMetadataDefinitionFromGrid() {
		return metadataDefinitionsGrid.getSelectionModel().getSelectedItem();
	}
	
	@Override
	public void onAddedOrEditedMetadataDefinition(MetadataDefinitionModel savedMetadataDefinition) {

		ListStore<MetadataDefinitionModel> metadataStore = metadataDefinitionsGrid.getStore();
		
		int insertIndex = metadataStore.getModels().size();
		
		MetadataDefinitionModel editedMetadataDefinition = getEditedMetadataDefinition();
		if (editedMetadataDefinition != null) {
			
			// Se va inlocui cea veche (pe care s-a dat editare) cu cea noua (cea salvata).
			
			int indexOfEditedMetadataDefinition = metadataStore.indexOf(editedMetadataDefinition);
			metadataStore.remove(editedMetadataDefinition);
			insertIndex = indexOfEditedMetadataDefinition;
		}
		
		metadataStore.insert(savedMetadataDefinition, insertIndex);
		
		onMetadataDefinitionRelatedAction();
		
		updateModifiedCollection();
	}
	
	@Override
	public void onCancelAddOrEditMetadataDefinition() {
		onMetadataDefinitionRelatedAction();
	}
	
	private void onMetadataDefinitionRelatedAction() {
		
		metadataDefinitionsGrid.getSelectionModel().deselectAll();
		
		updateMetadataDefinitionsToolBarPerspective();
		
		metadataDefinitionAddOrEditForm.resetForm();
		metadataDefinitionAddOrEditForm.setEnabled(false);
	}
	
	private void updateMetadataDefinitionsGridPanel() {

		metadataDefinitionsGrid.getStore().removeAll();

		MetadataCollectionDefinitionModel selectedCollection = getSelectedCollectionFromListView();
		if (selectedCollection != null) {
			MetadataCollectionDefinitionModel selectedCollectionInMetadataTab = getCollectionFromMetadataTab(selectedCollection.getName());
			metadataDefinitionsGrid.getStore().add(selectedCollectionInMetadataTab.getMetadataDefinitions());
		}
		
		onMetadataDefinitionRelatedAction();
	}

	private void updateCollectionsListViewFromMetadataTab() {

		List<MetadataCollectionDefinitionModel> collectionsInMetadataTab = getCollectionsFromMetadataTab();
		
		collectionListView.getStore().removeAll();
		collectionListView.getStore().add(collectionsInMetadataTab);
	}
	
	private void onCollectionSelectionChange() {
		updateMetadataDefinitionsGridPanel();
	}

	private void updateModifiedCollection() {
		
		MetadataCollectionDefinitionModel modifiedCollection = getSelectedCollectionFromListView();
		if (modifiedCollection == null) {
			throw new IllegalStateException("S-a cerut actualizarea colectiei modificate, insa NU s-a gasit colectia modificata.");
		}
		String nameForCollectionToUpdate = modifiedCollection.getName();		
		
		MetadataCollectionDefinitionModel collectionToUpdate = getCollectionFromMetadataTab(nameForCollectionToUpdate);
		collectionToUpdate.setMetadataDefinitions(metadataDefinitionsGrid.getStore().getModels());
	}
	
	private List<MetadataCollectionDefinitionModel> getCollectionsFromMetadataTab() {
		
		List<MetadataDefinitionModel> metadataDefinitionsInMetadataTab = metadataTab.getMetadataDefinitionsStore().getModels();
		List<MetadataCollectionDefinitionModel> collectionsInMetadataTab = new ArrayList<MetadataCollectionDefinitionModel>();
		
		for (MetadataDefinitionModel metadataDefinitionInMetadataTab : metadataDefinitionsInMetadataTab) {
			if (metadataDefinitionInMetadataTab.getType().equals(MetadataDefinitionModel.TYPE_METADATA_COLLECTION)) {
				MetadataCollectionDefinitionModel collectionInMetadataTab = (MetadataCollectionDefinitionModel) metadataDefinitionInMetadataTab;
				collectionsInMetadataTab.add(collectionInMetadataTab);
			}
		}
		
		return collectionsInMetadataTab;
	}
	
	private MetadataCollectionDefinitionModel getCollectionFromMetadataTab(String name) {
		
		ListStore<MetadataDefinitionModel> metadataDefinitionsStoreInMetadataTab = metadataTab.getMetadataDefinitionsStore();

		MetadataDefinitionModel collectionAsMetadataDefinition = metadataDefinitionsStoreInMetadataTab.findModel(MetadataCollectionDefinitionModel.PROPERTY_NAME, name);
		if (collectionAsMetadataDefinition == null) {
			throw new IllegalStateException("Nu s-a gasit in tab-ul pentru metadate colectia cu numele [" + name + "].");
		}
		if (!collectionAsMetadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_METADATA_COLLECTION)) {
			throw new IllegalStateException("Colectia din tab-ul pentru metadate cu numele [" + name + "] NU are setat tipul corect. Tipul incorect este [" + collectionAsMetadataDefinition.getType() + "].");
		}
		MetadataCollectionDefinitionModel collection = (MetadataCollectionDefinitionModel) collectionAsMetadataDefinition;
		return collection;
	}

	private void reset() {
		
		collectionListView.getStore().removeAll();
		metadataDefinitionsGrid.getStore().removeAll();
		
		updateMetadataDefinitionsToolBarPerspective();

		metadataDefinitionAddOrEditForm.resetWorkflowStates();
		metadataDefinitionAddOrEditForm.resetForm();
		metadataDefinitionAddOrEditForm.setEnabled(false);
	}

	public void prepareForAdd() {
		reset();
	}

	public void prepareForEdit(DocumentTypeModel documentType, List<WorkflowStateModel> workflowStates) {
		reset();
		/*
		 * Ia colectiile din tab-ul "Metadate". Trebuie sa le ia si in aceasta
		 * metoda pentru ca, daca acest tab este activ la deschiderea ferestrei,
		 * nu se va activa evenimentul de selectare al tab-ului (principalul
		 * responsabil pentru popularea elementelor din tab).
		 */
		updateCollectionsListViewFromMetadataTab();		
		metadataDefinitionAddOrEditForm.setWorkflowStates(workflowStates);
	}

	public boolean isValid() {
		
		// Trebuie sa reiau din tab-ul pt. metadate intrucat s-ar putea sa se fi adaugat / sters colectii.
		updateCollectionsListViewFromMetadataTab();
		
		for (MetadataCollectionDefinitionModel collection : collectionListView.getStore().getModels()) {
			MetadataCollectionDefinitionModel collectionInMetadataTab = getCollectionFromMetadataTab(collection.getName());
			if (collectionInMetadataTab.getMetadataDefinitions().isEmpty()) {
				ErrorHelper.addError(GwtLocaleProvider.getMessages().SOME_COLLECTIONS_HAVE_NO_DEFINED_METADATAS());
				return false;
			}
		}
		
		return true;
	}

	public void populate(DocumentTypeModel documentType) {
		// Colectiile au deja datele din tab-ul pentru metadate.
	}
	
	private void updateMetadataDefinitionsToolBarPerspective() {
		
		MetadataCollectionDefinitionModel selectedCollection = getSelectedCollectionFromListView();
		boolean isCollectionSelected = (selectedCollection != null);
		
		metadataDefinitionsToolBar.setEnabled(isCollectionSelected);
		
		MetadataDefinitionModel selectedMetadataDefinition = getSelectedMetadataDefinitionFromGrid();
		boolean isMetadataDefinitionSelected = (selectedMetadataDefinition != null);

		deleteMetadataDefinitionButton.setEnabled(isMetadataDefinitionSelected);
	}
	
	@Override
	public MetadataDefinitionModel findMetadataDefinitionByName(String name) {
		return metadataDefinitionsGrid.getStore().findModel(MetadataDefinitionModel.PROPERTY_NAME, name);
	}
	
	@Override
	public MetadataDefinitionModel getEditedMetadataDefinition() {
		return getSelectedMetadataDefinitionFromGrid();
	}
}