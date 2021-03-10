package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.presentation.client.shared.CustomGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PermissionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.SorterProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ManagePermissionsComponent extends HorizontalPanel {
	
	private static final String DATA_PROPERTY_PERMISSION = "permission";
	
	private static final int GRID_ORIGINAL_WIDTH = 448;
	private static final int GRID_ORIGINAL_HEIGHT = 490;
	private static final int RIGHT_PANEL_ORIGINAL_WIDTH = 450;
	private static final int RIGHT_PANEL_ORIGINAL_HEIGHT = 500;
	
	private Map<ModelData, String> permissionMap = new HashMap<ModelData, String>();	
	private EntitiesWithPermissionsSelectionComponent entitiesWithPermissionsSelectionComponent = new EntitiesWithPermissionsSelectionComponent();		
			
	
	public ManagePermissionsComponent() {	
		add(entitiesWithPermissionsSelectionComponent);		
		layout();		
	}
	
	public void populate(List<PermissionModel> permissions) {
		entitiesWithPermissionsSelectionComponent.populate(permissions);
	}
	
	public boolean areAllPermissionsSet() {
		for (ModelData entity : getSelectedEntities()) {
			if (permissionMap.get(entity) == null) {
				return false;
			}
		}
		return true;
	}
	
	public void changeSize(float wIndex, float hIndex){		
		entitiesWithPermissionsSelectionComponent.selectedEntitiesGrid.setSize(Math.round(GRID_ORIGINAL_WIDTH * wIndex), 
				Math.round(GRID_ORIGINAL_HEIGHT * hIndex)-30);		
		if (hIndex < 1)
		entitiesWithPermissionsSelectionComponent.rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex), 
				Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT * hIndex)-30);
		else 
			entitiesWithPermissionsSelectionComponent.rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex), 
					Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT * hIndex));
		entitiesWithPermissionsSelectionComponent.changeSize(wIndex, hIndex);
		layout();
	}
	
	public List<ModelData> getSelectedEntities() {
		return entitiesWithPermissionsSelectionComponent.getSelectedEntities();
	}
	
	public List<PermissionModel> getPermissions() {
		List<PermissionModel> permissions = new ArrayList<PermissionModel>();
		
		List<ModelData> selectedEntities = entitiesWithPermissionsSelectionComponent.getSelectedEntities();
		for (ModelData selectedEntity : selectedEntities) {
			PermissionModel permission = new PermissionModel();
			if (selectedEntity instanceof UserModel) {
				UserModel user = (UserModel) selectedEntity;
				permission.setEntityId(user.getUserId());
				permission.setEntityName(user.getName());
				permission.setEntityType(PermissionModel.TYPE_USER);
			} else if (selectedEntity instanceof OrganizationUnitModel) {
				OrganizationUnitModel organizationUnit = (OrganizationUnitModel) selectedEntity;
				permission.setEntityId(organizationUnit.getId());
				permission.setEntityName(organizationUnit.getName());
				permission.setEntityType(PermissionModel.TYPE_ORGANIZATION_UNIT);
			} else if (selectedEntity instanceof GroupModel) {
				GroupModel group = (GroupModel) selectedEntity;
				permission.setEntityId(group.getId());
				permission.setEntityName(group.getName());
				permission.setEntityType(PermissionModel.TYPE_GROUP);
			}
			if (permissionMap.get(selectedEntity)!=null ){
				permission.setPermission(permissionMap.get(selectedEntity));			
			}	
			
			permissions.add(permission);			
		}
		return permissions;
	}

	public void setReadOnly(boolean readOnly) {
		entitiesWithPermissionsSelectionComponent.addButton.setEnabled(!readOnly);
		entitiesWithPermissionsSelectionComponent.removeButton.setEnabled(!readOnly);
		entitiesWithPermissionsSelectionComponent.selectedEntitiesGrid.setEnabled(!readOnly);
		
		if (readOnly) {
			entitiesWithPermissionsSelectionComponent.removeDoubleClickListenersForAdding();
			entitiesWithPermissionsSelectionComponent.removeDoubleClickListenerForRemoving();
			entitiesWithPermissionsSelectionComponent.readOnly = true;
			entitiesWithPermissionsSelectionComponent.refreshGrid();
		} else {
			entitiesWithPermissionsSelectionComponent.addDoubleClickListenersForAdding();
			entitiesWithPermissionsSelectionComponent.addDoubleClickListenerForRemoving();
			entitiesWithPermissionsSelectionComponent.readOnly = false;
			entitiesWithPermissionsSelectionComponent.refreshGrid();
		}		
	}
	
	public void reset(){
		permissionMap.clear();
		entitiesWithPermissionsSelectionComponent.reset();
	}
	
	
	private class EntitiesWithPermissionsSelectionComponent extends AbstractOrganizationalStructureEntitiesSelectionComponent {
		
		List<PermissionModel> userPermissions;
		List<PermissionModel> organizationUnitPermissions;
		List<PermissionModel> groupPermissions;
		Grid<ModelData> selectedEntitiesGrid;
		private Listener<GridEvent<ModelData>> selectedEntitiesGridDoubleClickListenerForRemoving;
		private ColumnModel cm;
		private boolean readOnly;
		
		public EntitiesWithPermissionsSelectionComponent() {
			initSelectedEntitiesGrid(); 
			addListenerForRemoveButton();		
			readOnly = false;
			
			selectedEntitiesGrid.getStore().addStoreListener(new StoreListener<ModelData>() {
				@Override
				public void storeRemove(StoreEvent<ModelData> se) {
					// Sterge permisiunea pentru entitatea eliminata din lista.
					permissionMap.remove(se.getModel());					
					super.storeRemove(se);
				}
			});		
		}
		
		private void initSelectedEntitiesGrid() {
			List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
			
			//in functie de model este aleasa o iconita
			ColumnConfig iconColumnConfig = new ColumnConfig();
			iconColumnConfig.setRenderer(new CustomGridCellRenderer<ModelData>() {
				
				@Override
				public Object doRender(ModelData model, String property, ColumnData config, int rowIndex,
						int colIndex, ListStore<ModelData> store, Grid<ModelData> grid) {
					
					if (model instanceof UserModel) {
						AbstractImagePrototype icon = IconHelper.createStyle("icon-user", 16, 16);
						return icon.createImage();	
					} else if (model instanceof OrganizationUnitModel) {
						AbstractImagePrototype icon = IconHelper.createStyle("icon-orgUnit", 16, 16);
						return icon.createImage();	
					} else if (model instanceof GroupModel) {	
						AbstractImagePrototype icon = IconHelper.createStyle("icon-role", 16, 16);
						return icon.createImage();					
					}
					return null;								
				}			
			});
			iconColumnConfig.setWidth(23);
			columnConfigs.add(iconColumnConfig);
			
			ColumnConfig nameColumnConfig = new ColumnConfig();
			nameColumnConfig.setHeader(GwtLocaleProvider.getConstants().ENTITY());
			nameColumnConfig.setRenderer(new CustomGridCellRenderer<ModelData>() {
				
				@Override
				public Object doRender(ModelData model, String property, ColumnData config, int rowIndex,
						int colIndex, ListStore<ModelData> store, Grid<ModelData> grid) {
					
					if (model instanceof UserModel) {
						String title = ((UserModel)model).getTitle() != null ? " - "+((UserModel)model).getTitle():"";
						return (((UserModel)model).getName() + title);
					} else if (model instanceof OrganizationUnitModel) {
						return (((OrganizationUnitModel)model).getName());
					} else if (model instanceof GroupModel) {					
						return (((GroupModel)model).getName());
					}
					return null;
				}			
			});
			nameColumnConfig.setWidth(150);
			columnConfigs.add(nameColumnConfig);
			
			ColumnConfig radioColumnConfig = new ColumnConfig(){};
			radioColumnConfig.setHeader(GwtLocaleProvider.getConstants().RIGHTS());
			radioColumnConfig.setWidth(150);
			radioColumnConfig.setRenderer(new CustomGridCellRenderer<ModelData>() {
				
				@Override
				public Object doRender(ModelData model, String property, ColumnData config, final int rowIndex,
						int colIndex, ListStore<ModelData> store, final Grid<ModelData> grid) {
					
					final PermissionsRadioGroup myrg = new PermissionsRadioGroup();					
					if (permissionMap.get(model) != null) {
						if (permissionMap.get(model).equalsIgnoreCase(PermissionModel.PERMISSION_COORDINATOR)){
							myrg.setValue(myrg.coordinatorRadio);
						} else if (permissionMap.get(model).equalsIgnoreCase(PermissionModel.PERMISSION_COLABORATOR)){
							myrg.setValue(myrg.collaboratorRadio);
						} else if(permissionMap.get(model).equalsIgnoreCase(PermissionModel.PERMISSION_EDITOR)){
							myrg.setValue(myrg.editorRadio);
						} else if(permissionMap.get(model).equalsIgnoreCase(PermissionModel.PERMISSION_READER)){
							myrg.setValue(myrg.readerRadio);
						}						
					}
					if (readOnly){
						myrg.setEnabled(false);
					}						
					else{
						myrg.setEnabled(true);
					}
						
					myrg.addListener(Events.Change, new Listener<FieldEvent>(){
						public void handleEvent(FieldEvent fe) {
							grid.getSelectionModel().select(rowIndex, false);
							ModelData selectedEntity = grid.getSelectionModel().getSelectedItem();
							String permission = myrg.getValue().getData(DATA_PROPERTY_PERMISSION);						
							permissionMap.put(selectedEntity, permission);
						}					
					});				
					return myrg;
				}			
			});
			columnConfigs.add(radioColumnConfig);
			
			cm = new ColumnModel(columnConfigs);
			selectedEntitiesGrid = new Grid<ModelData>(new ListStore<ModelData>(), cm);
			selectedEntitiesGrid.getStore().setStoreSorter(SorterProvider.getOrganizationalStructureListSorter());
			selectedEntitiesGrid.getView().setForceFit(true);
			selectedEntitiesGrid.setSize(GRID_ORIGINAL_WIDTH, GRID_ORIGINAL_HEIGHT);
			selectedEntitiesGrid.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);	
			initDoubleClickListenerForRemoving();
			addDoubleClickListenerForRemoving();
			
			rightPanel.add(selectedEntitiesGrid);
			rightPanel.setHeaderVisible(false);
			rightPanel.layout();			
		}
		
		private void refreshGrid(){
			selectedEntitiesGrid.reconfigure(selectedEntitiesGrid.getStore(), cm);
		}
		
		protected void initDoubleClickListenerForRemoving() {	
			selectedEntitiesGridDoubleClickListenerForRemoving = new Listener<GridEvent<ModelData>>(){
				public void handleEvent(GridEvent<ModelData> ge) {
					ModelData selectedEntity = ge.getModel();
					if (selectedEntity != null) {
						selectedEntitiesGrid.getStore().remove(selectedEntity);
					}				
				}			
			};
		}
		
		public void addDoubleClickListenerForRemoving() {
			this.selectedEntitiesGrid.addListener(Events.OnDoubleClick, selectedEntitiesGridDoubleClickListenerForRemoving);	
		}
		
		public void removeDoubleClickListenerForRemoving() {
			this.selectedEntitiesGrid.removeListener(Events.OnDoubleClick, selectedEntitiesGridDoubleClickListenerForRemoving);
		}
		
		@Override
		public void reset() {
			userPermissions = null;
			organizationUnitPermissions = null;
			groupPermissions = null;
			selectedEntitiesGrid.getStore().removeAll();
		}
		
		@Override
		public List<ModelData> getSelectedEntities() {		
			return selectedEntitiesGrid.getStore().getModels();
		}
		
		@Override
		protected void addEntities(List<ModelData> entities) {
			ListStore<ModelData> entityStore = selectedEntitiesGrid.getStore();
			for (ModelData entity : entities) {
				boolean isUser = entity instanceof UserModel;
				boolean isOrgUnit = entity instanceof OrganizationUnitModel;
				boolean isGroup = entity instanceof GroupModel;
				boolean isNew = entityStore.findModel(entity) == null;
				if ((isUser || isOrgUnit || isGroup) && isNew) {
					entityStore.add(entity);
				}
			}	
		}
		
		private void addListenerForRemoveButton() {
			removeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent event) {
					List<ModelData> selectedEntities = selectedEntitiesGrid.getSelectionModel().getSelectedItems();
					for (ModelData selectedEntity : selectedEntities) {
						selectedEntitiesGrid.getStore().remove(selectedEntity);
					}
				}
			});
		}
		
		public void populate(List<PermissionModel> permissions) {
			if (permissions != null) {
				userPermissions = new ArrayList<PermissionModel>();
				organizationUnitPermissions = new ArrayList<PermissionModel>();
				groupPermissions = new ArrayList<PermissionModel>();
				
				// Parcurgem lista cu toate entitatile care au permisiuni si le clasificam.
				for (PermissionModel permission : permissions) {
					switch (permission.getEntityType()) {
						case PermissionModel.TYPE_USER:
							userPermissions.add(permission);
							break;
						case PermissionModel.TYPE_ORGANIZATION_UNIT:
							organizationUnitPermissions.add(permission);
							break;
						case PermissionModel.TYPE_GROUP:
							groupPermissions.add(permission);
							break;
					}
				}
				
			}		
			populateOrganization();
			populateGroups();
			refreshGrid();
		}

		@Override
		protected void doAfterPopulatingOrganization() {
			if ((userPermissions != null) && (organizationUnitPermissions != null)) {
				for (PermissionModel userPermission : userPermissions) {
					ModelData foundUser = organizationTreePanel.getStore().findModel(UserModel.USER_PROPERTY_USERID, userPermission.getEntityId());
					if (foundUser instanceof UserModel) {						
						selectedEntitiesGrid.getStore().add((UserModel) foundUser);
						permissionMap.put(foundUser, userPermission.getPermission());
					}
				}
				for (PermissionModel organizationUnitPermission : organizationUnitPermissions) {
					ModelData foundOrganizationUnit = organizationTreePanel.getStore().findModel(OrganizationUnitModel.PROPERTY_ID, organizationUnitPermission.getEntityId());
					if (foundOrganizationUnit instanceof OrganizationUnitModel) {						
						selectedEntitiesGrid.getStore().add((OrganizationUnitModel) foundOrganizationUnit);
						permissionMap.put(foundOrganizationUnit, organizationUnitPermission.getPermission());
					}
				}
			} else {
				/*
				 * Daca este vorba de adaugare, implicit se adauga utilizatorul
				 * logat cu permisiunea de coordonator.
				 */
				ModelData loggedInUser = organizationTreePanel.getStore().findModel(UserModel.USER_PROPERTY_USERID, GwtRegistryUtils.getUserSecurity().getUserIdAsString());
				if (loggedInUser instanceof UserModel) {
					selectedEntitiesGrid.getStore().add((UserModel) loggedInUser);
					permissionMap.put(loggedInUser, PermissionModel.PERMISSION_COORDINATOR);
				}
			}
		}

		@Override
		protected void doAfterPopulatingGroups() {
			if (groupPermissions != null) {
				for (PermissionModel groupPermission : groupPermissions) {
					GroupModel foundGroup = groupsListView.getStore().findModel(GroupModel.PROPERTY_ID, groupPermission.getEntityId());
					if (foundGroup != null) {
						selectedEntitiesGrid.getStore().add(foundGroup);
						permissionMap.put(foundGroup, groupPermission.getPermission());
					}
				}
			}
		}
		
		class PermissionsRadioGroup extends RadioGroup{
			private Radio coordinatorRadio;
			private Radio collaboratorRadio;
			private Radio editorRadio;
			private Radio readerRadio;
			
			PermissionsRadioGroup(){
				
				setOrientation(Orientation.HORIZONTAL);
				coordinatorRadio = new Radio();
				coordinatorRadio.setData(DATA_PROPERTY_PERMISSION, PermissionModel.PERMISSION_COORDINATOR);
				coordinatorRadio.setBoxLabel("C");
				coordinatorRadio.setToolTip(GwtLocaleProvider.getConstants().COORDINATOR());
				
				collaboratorRadio =  new Radio();
				collaboratorRadio.setData(DATA_PROPERTY_PERMISSION, PermissionModel.PERMISSION_COLABORATOR);
				collaboratorRadio.setBoxLabel("C");
				collaboratorRadio.setToolTip(GwtLocaleProvider.getConstants().COLLABORATOR());
				
				editorRadio = new Radio();
				editorRadio.setData(DATA_PROPERTY_PERMISSION, PermissionModel.PERMISSION_EDITOR);
				editorRadio.setBoxLabel("E");
				editorRadio.setToolTip(GwtLocaleProvider.getConstants().EDITOR());
				
				readerRadio = new Radio();
				readerRadio.setData(DATA_PROPERTY_PERMISSION, PermissionModel.PERMISSION_READER);
				readerRadio.setBoxLabel("R");
				readerRadio.setToolTip(GwtLocaleProvider.getConstants().READER());
				
				add(coordinatorRadio);
				add(collaboratorRadio);
				add(editorRadio);
				add(readerRadio);			
			}		
		}	
	}	
}