package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.OrganizationalStructureLabelProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.iconproviders.OrganizationalStructureIconProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.Event;

/**
 * , Ioana Trif
 */
public abstract class AbstractOrganizationalStructureEntitiesSelectionComponent extends HorizontalPanel {

	protected static final String PROPERTY_ICON_CLASS = "iconClass";
	protected static final String PROPERTY_DISPLAY_PROPERTY_VALUE = "displayPropertyValue";	
	
	private static final int LEFT_PANEL_ORIGINAL_WIDTH = 450;	
	private static final int LEFT_PANEL_ORIGINAL_HEIGHT = 500;
	private static final int BUTTONS_PANEL_ORIGINAL_WIDTH = 120;
	private static final int BUTTONS_PANEL_ORIGINAL_HEIGHT = 450;	
	
	private final OrganizationTreeExpandMode organizationTreeExpandMode;
	
	private Listener<TreePanelEvent<ModelData>> organizationTreePanelDoubleClickListenerForAdding;
	private Listener<ListViewEvent<GroupModel>> groupsListViewDoubleClickListenerForAdding;	

	// elementele din containerul de jos
	protected ContentPanel leftPanel;
	protected VerticalPanel buttonsPanel;
	protected ContentPanel rightPanel;	

	// butoanele
	protected Button addButton;
	protected Button removeButton;

	// elementele din leftPanel
	protected ContentPanel organizationPanel;
	protected ContentPanel groupsPanel;
	
	protected ToolBar organizationToolBar;
	protected Button expandButton;

	// elementele din content panels
	protected TreePanel<ModelData> organizationTreePanel;
	protected ListView<GroupModel> groupsListView;
	
	protected AbstractOrganizationalStructureEntitiesSelectionComponent() {
		this(OrganizationTreeExpandMode.AUTOMATIC);
	}

	protected AbstractOrganizationalStructureEntitiesSelectionComponent(OrganizationTreeExpandMode organizationTreeExpandMode) {
		this.organizationTreeExpandMode = organizationTreeExpandMode;
		initBottomContainer();		
	}

	private void initBottomContainer() {
		initLeftPanel();
		initButtonsPanel();
		initRightPanel();
		initDoubleClickListenersForAdding();
		addDoubleClickListenersForAdding();	
	}

	private void initLeftPanel() {
		
		leftPanel = new ContentPanel();
		leftPanel.setHeaderVisible(false);
		leftPanel.setLayout(new AccordionLayout());
		
		initInnerPanels();		
		add(leftPanel);
		layout();
	}
	
	protected void changeSize(float wIndex, float hIndex){
		if (hIndex >=1)
			leftPanel.setSize(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(LEFT_PANEL_ORIGINAL_HEIGHT * hIndex));
		else	
			leftPanel.setSize(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(LEFT_PANEL_ORIGINAL_HEIGHT * hIndex)-30);
		layout();
		organizationPanel.setWidth(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * wIndex));		
		leftPanel.layout();
		groupsPanel.setWidth(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * wIndex));		
		leftPanel.layout();
		buttonsPanel.setSize(Math.round(BUTTONS_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(BUTTONS_PANEL_ORIGINAL_HEIGHT * hIndex));		
		layout();		
	}
	
	private void initButtonsPanel() {
		buttonsPanel = new VerticalPanel();
		buttonsPanel.setSpacing(10);

		addButton = new Button();
		addButton.setText(GwtLocaleProvider.getConstants().ADD());
		addButton.setSize(64, 36);
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				List<ModelData> selectedItems = new ArrayList<ModelData>();
				if (!organizationPanel.isCollapsed()) {
					List<ModelData> selectedItemsFromOrganization = organizationTreePanel.getSelectionModel().getSelectedItems();
					for (ModelData selectedItemFromOrganization : selectedItemsFromOrganization) {
						selectedItems.add(selectedItemFromOrganization);
					}
				} else if (!groupsPanel.isCollapsed()) {
					List<GroupModel> selectedItemsFromGroups = groupsListView.getSelectionModel().getSelectedItems();
					for (ModelData selectedItemFromGroups : selectedItemsFromGroups) {
						selectedItems.add(selectedItemFromGroups);
					}
				}
				addEntities(selectedItems);
			}
		});

		removeButton = new Button();
		removeButton.setText(GwtLocaleProvider.getConstants().REMOVE());
		removeButton.setSize(64, 36);	

		buttonsPanel.add(addButton);
		buttonsPanel.add(removeButton);

		TableData dataForButtonsPanel = new TableData();
		dataForButtonsPanel.setVerticalAlign(VerticalAlignment.MIDDLE);
		dataForButtonsPanel.setHorizontalAlign(HorizontalAlignment.CENTER);
		dataForButtonsPanel.setPadding(24);
		
		add(buttonsPanel,dataForButtonsPanel);
		layout();
	}

	private void initInnerPanels() {
		
		organizationPanel = new ContentPanel();
		organizationPanel.setBorders(false);
		organizationPanel.setScrollMode(Scroll.AUTO);
		organizationPanel.setHeading(GwtLocaleProvider.getConstants().ORGANIZATIONAL_STRUCTURE());			
		
		organizationToolBar = new ToolBar();
		if (organizationTreeExpandMode.equals(OrganizationTreeExpandMode.MANUAL)) {
			organizationPanel.setTopComponent(organizationToolBar);
		}
		
		expandButton = new Button();
		expandButton.setIconStyle("icon-expand");
		expandButton.setText(GwtLocaleProvider.getConstants().EXPAND_ALL());
		expandButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				organizationTreePanel.expandAll();
			}
		});
		organizationToolBar.add(expandButton);
		
		groupsPanel = new ContentPanel(new FitLayout());
		groupsPanel.setBorders(false);
		groupsPanel.setScrollMode(Scroll.AUTO);
		groupsPanel.setHeading(GwtLocaleProvider.getConstants().GROUPS());
		
		organizationPanel.addListener(Events.Expand, new Listener<ComponentEvent>(){
			public void handleEvent(ComponentEvent be) {
				if (groupsPanel.isExpanded())
					groupsPanel.collapse();				
			}
		});
		groupsPanel.addListener(Events.Expand, new Listener<ComponentEvent>(){
			public void handleEvent(ComponentEvent be) {
				if (organizationPanel.isExpanded())
					organizationPanel.collapse();				
			}			
		});

		initOrganizationTreePanel();
		initGroupList();
			
		leftPanel.add(organizationPanel);
		leftPanel.add(groupsPanel);
		leftPanel.layout();
	}

	private void initOrganizationTreePanel() {
		organizationTreePanel = new TreePanel<ModelData>(AppStoreCache.getOrganizationTreeStore());
		organizationTreePanel.setIconProvider(new OrganizationalStructureIconProvider(organizationTreePanel.getStore()));
		organizationTreePanel.setLabelProvider(new OrganizationalStructureLabelProvider());
		organizationTreePanel.sinkEvents(Event.ONDBLCLICK);
		organizationPanel.add(organizationTreePanel, new MarginData(0,2,2,2));			
	}

	private void initGroupList() {
		groupsListView = new ListView<GroupModel>(AppStoreCache.getGroupListStore());
		groupsListView.setDisplayProperty(GroupModel.PROPERTY_NAME);
		groupsListView.setBorders(false);
		groupsPanel.add(groupsListView, new MarginData(0,2,2,2));
	}

	private void initRightPanel() {		
		
		rightPanel = new ContentPanel();	
		rightPanel.setHeaderVisible(false);
		rightPanel.setScrollMode(Scroll.AUTO);	
		rightPanel.setLayout(new FitLayout());	
		add(rightPanel);
		layout();
	}
	
	protected void initDoubleClickListenersForAdding() {
		organizationTreePanelDoubleClickListenerForAdding = new Listener<TreePanelEvent<ModelData>>() {
			@Override
			public void handleEvent(TreePanelEvent<ModelData> tpe) {
				ModelData selectedEntity = tpe.getItem();
				if (selectedEntity != null) {
					addEntity(selectedEntity);
				}
			}
		};
		groupsListViewDoubleClickListenerForAdding = new Listener<ListViewEvent<GroupModel>>() {
			@Override
			public void handleEvent(ListViewEvent<GroupModel> lve) {
				ModelData selectedEntity = lve.getModel();
				if (selectedEntity != null) {
					addEntity(selectedEntity);
				}
			}
		};
	}
	
	public void addDoubleClickListenersForAdding() {
		this.organizationTreePanel.addListener(Events.OnDoubleClick, this.organizationTreePanelDoubleClickListenerForAdding);
		this.groupsListView.addListener(Events.OnDoubleClick, this.groupsListViewDoubleClickListenerForAdding);
	}
	
	public void removeDoubleClickListenersForAdding() {
		this.organizationTreePanel.removeListener(Events.OnDoubleClick, this.organizationTreePanelDoubleClickListenerForAdding);
		this.groupsListView.removeListener(Events.OnDoubleClick, this.groupsListViewDoubleClickListenerForAdding);
	}

	protected void populateOrganization() {
		if (organizationTreeExpandMode.equals(OrganizationTreeExpandMode.AUTOMATIC)) {
			organizationTreePanel.expandAll();
		}
		doAfterPopulatingOrganization();
	}

	protected void populateGroups() {
		doAfterPopulatingGroups();
	}
	
	public abstract void reset();

	/**
	 * Adauga o entitate ca fiind selectata.
	 * 
	 * @param entity entitatea (poate fi unitate organizatorica, utilizator sau grup)
	 */
	protected void addEntity(ModelData entity) {
		addEntities(Arrays.asList(new ModelData[] {entity}));
	}

	/**
	 * Adauga entitati ca fiind selectate.
	 * 
	 * @param entities entitatile (pot fi unitati organizatorice, utilizatori si / sau grupuri)
	 */
	protected abstract void addEntities(List<ModelData> entities); 
	/**
	 *  
	 * Returneaza modelele din store-ul listei cu entitati selectate (cea din dreapta). 
	 */
	public abstract List<ModelData> getSelectedEntities();

	protected abstract void doAfterPopulatingOrganization();

	protected abstract void doAfterPopulatingGroups();
	
	public static enum OrganizationTreeExpandMode {
		AUTOMATIC, MANUAL
	}
}