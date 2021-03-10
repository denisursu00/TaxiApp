package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.SorterProvider;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelProcessor;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.MarginData;

public abstract class PlainOrganizationalStructureEntitiesSelectionComponent extends AbstractOrganizationalStructureEntitiesSelectionComponent {
	
	protected static final String PROPERTY_ICON_CLASS = "iconClass";
	protected static final String PROPERTY_DISPLAY_PROPERTY_VALUE = "displayPropertyValue";	
	
	protected ListView<ModelData> selectedEntitiesListView;
	protected Listener<ListViewEvent<ModelData>> selectedEntitiesListViewDoubleClickListenerForRemoving;
	
	private List<OrganizationEntityModel> usersToSelect;
	private List<OrganizationEntityModel> orgUnitsToSelect;
	private List<OrganizationEntityModel> groupsToSelect;
	
	public PlainOrganizationalStructureEntitiesSelectionComponent() {
		this(OrganizationTreeExpandMode.AUTOMATIC);
	}
	
	public PlainOrganizationalStructureEntitiesSelectionComponent(OrganizationTreeExpandMode organizationTreeExpandMode) {
		
		super(organizationTreeExpandMode);

		initSelectedEntitiesListView(); 
		addListenersForButtons();	
		initDoubleClickListenerForRemoving();
		addDoubleClickListenerForRemoving();
	}
	
	private void initSelectedEntitiesListView(){
		selectedEntitiesListView = new ListView<ModelData>();
		selectedEntitiesListView.setStore(new ListStore<ModelData>());
		selectedEntitiesListView.getStore().setStoreSorter(SorterProvider.getOrganizationalStructureListSorter());
		selectedEntitiesListView.setModelProcessor(new ModelProcessor<ModelData>() {
			@Override
			public ModelData prepareData(ModelData model) {
				if (model instanceof UserModel) {
					model.set(PROPERTY_ICON_CLASS, "icon-user");
					model.set(PROPERTY_DISPLAY_PROPERTY_VALUE, ((UserModel) model).getDisplayName());
				} else if (model instanceof OrganizationUnitModel) {
					model.set(PROPERTY_ICON_CLASS, "icon-orgUnit");
					model.set(PROPERTY_DISPLAY_PROPERTY_VALUE, ((OrganizationUnitModel) model).getName());
				} else if (model instanceof GroupModel) {
					model.set(PROPERTY_ICON_CLASS, "icon-role");
					model.set(PROPERTY_DISPLAY_PROPERTY_VALUE, ((GroupModel) model).getName());
				}
				return model;
			}
		});
		selectedEntitiesListView.setBorders(false);
		selectedEntitiesListView.setSimpleTemplate(getListViewTemplate());		
		rightPanel.add(selectedEntitiesListView, new MarginData(0,2,2,2));
	}
	
	private void addListenersForButtons(){
		removeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				List<ModelData> selectedEntities = selectedEntitiesListView.getSelectionModel().getSelectedItems();
				for (ModelData selectedEntity : selectedEntities) {
					selectedEntitiesListView.getStore().remove(selectedEntity);
				}
			}
		});
	}
	
	private String getListViewTemplate() {
		
		StringBuilder template = new StringBuilder();

		template.append("<table>");
		template.append("<tr>");
		template.append("<td>");
		template.append("<div class=\"{iconClass}\" style=\"width: 16px; height: 16px; float: left;\"></div>");
		template.append("</td>");
		template.append("<td>");
		template.append("<span style=\"font-size: 11px;\">{displayPropertyValue}</span>");
		template.append("</td>");
		template.append("</tr>");
		template.append("</table>");

		return template.toString();
	}
	
	protected void initDoubleClickListenerForRemoving() {			
		selectedEntitiesListViewDoubleClickListenerForRemoving = new Listener<ListViewEvent<ModelData>>() { 
			@Override
			public void handleEvent(ListViewEvent<ModelData> lve) {
				ModelData selectedEntity = lve.getModel();
				if (selectedEntity != null) {
					selectedEntitiesListView.getStore().remove(selectedEntity);
				}
			}
		};		
	}
	
	public void addDoubleClickListenerForRemoving() {
		selectedEntitiesListView.addListener(Events.OnDoubleClick, selectedEntitiesListViewDoubleClickListenerForRemoving);	
	}
	
	public void removeDoubleClickListenerForRemoving() {
		selectedEntitiesListView.removeListener(Events.OnDoubleClick, selectedEntitiesListViewDoubleClickListenerForRemoving);
	}
	
	public void populate(List<OrganizationEntityModel> entitiesToSelect) {
		
		if (entitiesToSelect != null) {
			
			usersToSelect = new ArrayList<OrganizationEntityModel>();
			orgUnitsToSelect = new ArrayList<OrganizationEntityModel>();
			groupsToSelect = new ArrayList<OrganizationEntityModel>();
			
			for (OrganizationEntityModel entityToSelect : entitiesToSelect) {
				switch (entityToSelect.getType()) {
					case OrganizationEntityModel.TYPE_USER:
						usersToSelect.add(entityToSelect);
						break;
					case OrganizationEntityModel.TYPE_ORG_UNIT:
						orgUnitsToSelect.add(entityToSelect);
						break;
					case OrganizationEntityModel.TYPE_GROUP:
						groupsToSelect.add(entityToSelect);
						break;
				}
			}
		}
		
		populateOrganization();
		populateGroups();
	}

	@Override
	protected void doAfterPopulatingOrganization() {
		if ((usersToSelect != null) && (orgUnitsToSelect != null)) {
			for (OrganizationEntityModel userToSelect : usersToSelect) {
				ModelData foundUser = organizationTreePanel.getStore().findModel(
					UserModel.USER_PROPERTY_USERID,
					userToSelect.getId().toString());
				if (foundUser instanceof UserModel) {
					selectedEntitiesListView.getStore().add((UserModel) foundUser);
				}
			}
			for (OrganizationEntityModel orgUnitToSelect : orgUnitsToSelect) {
				ModelData foundOrgUnit = organizationTreePanel.getStore().findModel(
					OrganizationUnitModel.PROPERTY_ID,
					orgUnitToSelect.getId().toString());
				if (foundOrgUnit instanceof OrganizationUnitModel) {
					selectedEntitiesListView.getStore().add((OrganizationUnitModel) foundOrgUnit);
				}
			}
		}
	}

	@Override
	protected void doAfterPopulatingGroups() {
		if (groupsToSelect != null) {
			for (OrganizationEntityModel groupToSelect : groupsToSelect) {
				GroupModel foundGroup = groupsListView.getStore().findModel(
					GroupModel.PROPERTY_ID,
					groupToSelect.getId().toString());
				if (foundGroup != null) {
					selectedEntitiesListView.getStore().add(foundGroup);
				}
			}
		}
	}
	
	@Override
	protected void addEntities(List<ModelData> entities) {
		
		ListStore<ModelData> entityStore = selectedEntitiesListView.getStore(); 
		
		for (ModelData entity : entities) {
			
			boolean isUser = (entity instanceof UserModel);
			boolean isOrgUnit = (entity instanceof OrganizationUnitModel);
			boolean isGroup = (entity instanceof GroupModel);
			
			boolean isNew = true;
			
			if (isUser) {
				
				UserModel user = (UserModel) entity;
				String userId = user.getUserId();
				
				boolean isUserFound = (entityStore.findModel(UserModel.USER_PROPERTY_USERID, userId) != null);
				if (isUserFound) {
					isNew = false;
				}
			} else if (isOrgUnit) {
				
				OrganizationUnitModel organizationUnit = (OrganizationUnitModel) entity;
				String organizationUnitId = organizationUnit.getId();
				
				boolean isOrganizationUnitFound = (entityStore.findModel(OrganizationUnitModel.PROPERTY_ID, organizationUnitId) != null);
				if (isOrganizationUnitFound) {
					isNew = false;
				}
			} else if (isGroup) {
				
				GroupModel group = (GroupModel) entity;
				String groupId = group.getId();
				
				boolean isGroupFound = (entityStore.findModel(GroupModel.PROPERTY_ID, groupId) != null);
				if (isGroupFound) {
					isNew = false;
				}
			}
			
			if ((isUser || isOrgUnit || isGroup) && isNew) {
				entityStore.add(entity);
			}
		}		
	}
	
	@Override
	public List<ModelData> getSelectedEntities() {
		return selectedEntitiesListView.getStore().getModels();
	}
	
	public List<OrganizationEntityModel> getSelectedEntitiesAsOrganizationEntities() {
		
		List<ModelData> selectedEntities = getSelectedEntities();
		List<OrganizationEntityModel> selectedEntitiesAsOrganizationEntities = new ArrayList<OrganizationEntityModel>();
		
		for (ModelData selectedEntity : selectedEntities) {
			if (selectedEntity instanceof UserModel) {
				OrganizationEntityModel selectedOrganizationEntity = new OrganizationEntityModel();
				selectedOrganizationEntity.setId(Long.valueOf(((UserModel)selectedEntity).getUserId()));
				selectedOrganizationEntity.setType(OrganizationEntityModel.TYPE_USER);
				selectedEntitiesAsOrganizationEntities.add(selectedOrganizationEntity);
			} else if (selectedEntity instanceof OrganizationUnitModel) {
				OrganizationEntityModel selectedOrganizationEntity = new OrganizationEntityModel();
				selectedOrganizationEntity.setId(Long.valueOf(((OrganizationUnitModel)selectedEntity).getId()));
				selectedOrganizationEntity.setType(OrganizationEntityModel.TYPE_ORG_UNIT);
				selectedEntitiesAsOrganizationEntities.add(selectedOrganizationEntity);
			} else if (selectedEntity instanceof GroupModel) {
				OrganizationEntityModel selectedOrganizationEntity = new OrganizationEntityModel();
				selectedOrganizationEntity.setId(Long.valueOf(((GroupModel)selectedEntity).getId()));
				selectedOrganizationEntity.setType(OrganizationEntityModel.TYPE_GROUP);
				selectedEntitiesAsOrganizationEntities.add(selectedOrganizationEntity);
			} else {
				throw new IllegalStateException("Tip necunoscut de entitate organizatorica: " + selectedEntity.getClass().getName());
			}
		}
		
		return selectedEntitiesAsOrganizationEntities;
	}
	
	public ModelData getSelectedEntity() {
		return selectedEntitiesListView.getSelectionModel().getSelectedItem();		
	}	
	
	@Override
	public void reset() {
		
		selectedEntitiesListView.getStore().removeAll();
		
		usersToSelect = null;
		orgUnitsToSelect = null;
		groupsToSelect = null;
	}
}