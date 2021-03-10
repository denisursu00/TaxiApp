package ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.CustomAdapterFieldWithAutoWidth;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class ListMetadataItemsField extends CustomAdapterFieldWithAutoWidth {
	
	private boolean itemsRequired;

	private ContentPanel contentPanel;
	
	private ToolBar toolBar;
	private EditorGrid<ListMetadataItemModel> editorGrid;

	private Button addButton;
	private Button deleteButton;
	
	private ColumnConfig labelColumnConfig;
	private ColumnConfig valueColumnConfig;
	
	public ListMetadataItemsField() {
		
		super(160);
		
		itemsRequired = false;
		
		contentPanel = new ContentPanel();
		contentPanel.setHeaderVisible(false);
		contentPanel.setLayout(new FitLayout());
		
		initToolBar();
		initEditorGrid();
		
		setWidgetOfField(contentPanel);
	}
	
	private void initToolBar() {
		
		addButton = new Button();
		addButton.setText(GwtLocaleProvider.getConstants().ADD());
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				
				int indexOfRowWhereNewItemWillBeAdded = 0;
				int indexOfLabelColumn = editorGrid.getColumnModel().indexOf(labelColumnConfig);
				int indexOfColumnWhereEditingWillBegin = indexOfLabelColumn;
				
				ListMetadataItemModel newListItem = new ListMetadataItemModel();
				
				editorGrid.stopEditing(true);
				editorGrid.getStore().insert(newListItem, indexOfRowWhereNewItemWillBeAdded);
				editorGrid.startEditing(indexOfRowWhereNewItemWillBeAdded, indexOfColumnWhereEditingWillBegin);
			}
		});
		
		deleteButton = new Button();
		deleteButton.setText(GwtLocaleProvider.getConstants().DELETE());
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				List<ListMetadataItemModel> selectedListItems = editorGrid.getSelectionModel().getSelectedItems();
				for (ListMetadataItemModel selectedItem : selectedListItems) {
					editorGrid.getStore().remove(selectedItem);
				}
			}
		});
		
		toolBar = new ToolBar();
		
		toolBar.add(addButton);
		toolBar.add(deleteButton);

		contentPanel.setTopComponent(toolBar);
	}
	
	private void initEditorGrid() {

		CheckBoxSelectionModel<ListMetadataItemModel> checkBoxSelectionModel = new CheckBoxSelectionModel<ListMetadataItemModel>();
		ColumnConfig checkBoxColumnConfig = checkBoxSelectionModel.getColumn();

		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

		labelColumnConfig = new ColumnConfig();
		labelColumnConfig.setHeader(GwtLocaleProvider.getConstants().LABEL());
		labelColumnConfig.setId(ListMetadataItemModel.PROPERTY_LABEL);
		labelColumnConfig.setWidth(120);

		valueColumnConfig = new ColumnConfig();
		valueColumnConfig.setHeader(GwtLocaleProvider.getConstants().VALUE());
		valueColumnConfig.setId(ListMetadataItemModel.PROPERTY_VALUE);
		valueColumnConfig.setWidth(120);
		
		TextField<String> labelTextField = new TextField<String>();
		labelTextField.setEmptyText(GwtLocaleProvider.getConstants().EMPTY());
		
		CellEditor labelCellEditor = new CellEditor(labelTextField);
		labelColumnConfig.setEditor(labelCellEditor);
		
		TextField<String> valueTextField = new TextField<String>();
		valueTextField.setEmptyText(GwtLocaleProvider.getConstants().EMPTY());
		
		CellEditor valueCellEditor = new CellEditor(valueTextField);
		valueColumnConfig.setEditor(valueCellEditor);

		columnConfigs.add(checkBoxColumnConfig);
		columnConfigs.add(labelColumnConfig);
		columnConfigs.add(valueColumnConfig);

		ColumnModel columnModel = new ColumnModel(columnConfigs);
		
		editorGrid = new EditorGrid<ListMetadataItemModel>(new ListStore<ListMetadataItemModel>(), columnModel);
		editorGrid.setAutoExpandColumn(labelColumnConfig.getId());
		editorGrid.setAutoExpandMin(128);
		editorGrid.setSelectionModel(checkBoxSelectionModel);
		editorGrid.setStripeRows(true);
		
		contentPanel.add(editorGrid);
	}
	
	public void setItemsRequired(boolean itemsRequired) {
		this.itemsRequired = itemsRequired;
	}
	
	@Override
	public void recalculate() {
		super.recalculate();
		ComponentUtils.ensureGridIsProperlyRendered(editorGrid);
	}
	
	@Override
	protected boolean validateCustomAdapterField() {
		
		if (itemsRequired) {
			if (getListItems().isEmpty()) {
				markInvalid(GwtLocaleProvider.getMessages().LIST_METADATA_HAS_NO_AVAILABLE_OPTIONS());
				return false;
			}
		}
		
		for (ListMetadataItemModel listItem : getListItems()) {
			if (GwtStringUtils.isBlank(listItem.getLabel()) || GwtStringUtils.isBlank(listItem.getValue())) {
				markInvalid(GwtLocaleProvider.getMessages().LIST_ITEMS_MUST_HAVE_A_LABEL_AND_A_VALUE());
				return false;
			}
		}

		clearInvalid();
		return true;
	}
	
	public void removeAllListItems() {
		editorGrid.getStore().removeAll();
	}
	
	public void setListItems(List<ListMetadataItemModel> listItems) {
		removeAllListItems();
		editorGrid.getStore().add(listItems);
	}
	
	public List<ListMetadataItemModel> getListItems() {
		return editorGrid.getStore().getModels();
	}
}