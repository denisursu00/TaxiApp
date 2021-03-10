package ro.cloudSoft.cloudDoc.presentation.client.admin;

import ro.cloudSoft.cloudDoc.presentation.client.shared.OrganizationalStructureLabelProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEvent;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventHandler;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.iconproviders.OrganizationalStructureIconProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithDisplayName;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithId;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.OrganizationalStructureTreeSorter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtOrganizationalStructureBusinessUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.DragSource;
import com.extjs.gxt.ui.client.dnd.DropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OrganizationPanel extends ContentPanel implements AppEventHandler {

	private UserDetailsWindow userDetailsWindow;
	private OrgUnitDetailsWindow orgUnitDetailsWindow;
	private FindUserInDirectoryWindow findUserInDirectoryWindow;
	private MoveOrganizationEntityWindow moveOrganizationEntityWindow;
	private DeactivateUserConfirmationWindow deactivateUserConfirmationWindow;
	
	private OrganizationToolBar organizationToolBar;
	private OrganizationTreePanel organizationTreePanel;
	private ContentPanel detailsPanel;	

	public OrganizationPanel() {
		
		initWindow();
		initDetailsWindows();
		
		AppEventController.subscribe(this, AppEventType.OrganizationalStructure);
		AppEventController.subscribe(this, AppEventType.OrgUnit);		
		AppEventController.subscribe(this, AppEventType.User);
	}

	private void initWindow() {
		setHeading(GwtLocaleProvider.getConstants().ADMIN_ORGANIZATION());
		
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTO);

		// toolbar-ul
		organizationToolBar = new OrganizationToolBar(this);
		organizationToolBar.setStyleName("admin_toolbar");
		organizationToolBar.setSpacing(10);
		organizationToolBar.setBorders(true);
		setTopComponent(organizationToolBar);

		// arborele
		organizationTreePanel = new OrganizationTreePanel(this);	
		/*
		 * Trebuie adaugate si niste margini pentru ca altfel, cand arborele
		 * primeste focus, apare un scroll orizontal inutil.
		 */
		add(organizationTreePanel, new FitData(2));

		addListener(Events.Attach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent event) {
				refresh();
			};
		});
	}
	
	private void initDetailsWindows() {
		userDetailsWindow = new UserDetailsWindow();	
		orgUnitDetailsWindow = new OrgUnitDetailsWindow();
		findUserInDirectoryWindow = new FindUserInDirectoryWindow();
		moveOrganizationEntityWindow = new MoveOrganizationEntityWindow();
		deactivateUserConfirmationWindow = new DeactivateUserConfirmationWindow();
	}

	/**
	 * Reincarca toate componentele ferestrei (toolbar-ul, arborele si panoul cu
	 * detalii).
	 */
	public void refresh() {
		organizationToolBar.changeToolBarPerspective(null);
		organizationTreePanel.refreshTree();
	}

	public void dispatch(ModelData model) {
		if (model instanceof OrganizationUnitModel) {
			goToEditOrgUnit((OrganizationUnitModel) model);
		} else if (model instanceof UserModel) {
			goToEditUser((UserModel) model);
		}
	}

	public void goToAddOrgUnit(String parentOrganizationUnitId, String parentOrganizationId) {
		orgUnitDetailsWindow.orgUnitFormPanel.prepareForAdd(parentOrganizationUnitId, parentOrganizationId);
	}

	public void goToAddUser(String organizationUnitId, String organizationId) {
		userDetailsWindow.userFormPanel.prepareForAdd(organizationUnitId, organizationId);
	}

	public void goToEditOrgUnit(OrganizationUnitModel orgUnit) {
		orgUnitDetailsWindow.orgUnitFormPanel.prepareForEdit(orgUnit);
	}

	public void goToEditUser(UserModel user) {
		userDetailsWindow.userFormPanel.prepareForEdit(user);
	}
	
	public void goToAddUserFromDirectory(String organizationIdAsString, String organizationUnitIdAsString) {
		findUserInDirectoryWindow.prepareForSearch(organizationIdAsString, organizationUnitIdAsString);
	}
	
	public void goToMoveEntity(ModelData entityToMove, ModelData parentOfEntityToMove) {
		String displayNameOfEntityToMove = organizationTreePanel.getDisplayLabel(entityToMove);
		moveOrganizationEntityWindow.prepareForMove(entityToMove, parentOfEntityToMove, displayNameOfEntityToMove);
	}
	
	public void goToDeactivateUser(Long userId) {
		deactivateUserConfirmationWindow.prepareForConfirm(userId);
	}
	
	public OrganizationToolBar getOrganizationStructureToolBar() {
		return organizationToolBar;
	}
	public OrganizationTreePanel getOrganizationTreePanel() {
		return organizationTreePanel;
	}
	public ContentPanel getDetailsPanel() {
		return detailsPanel;
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		AppEventType eventType = event.getType();
		if (eventType.equals(AppEventType.OrganizationalStructure)
				|| eventType.equals(AppEventType.OrgUnit)
				|| eventType.equals(AppEventType.User)) {

			if (AdminPanelDispatcher.isActivePanel(this)) {
				refresh();
			}
		}
	}
}

class OrganizationToolBar extends ToolBar {

	private OrganizationPanel mainWindow;

	private Button addButton;
	private Menu addMenu;
	private MenuItem addOrgUnitMenuItem;
	private MenuItem addUserMenuItem;
	private MenuItem addUserFromDirectoryMenuItem;
	
	private Button moveButton;
	
	private Button importButton;

	private Button deleteButton;
	private Menu deleteMenu;
	private MenuItem deleteOrgUnitMenuItem;
	private MenuItem deleteUserMenuItem;

	private Button refreshButton;
	
	private Button toolsButton;
	private Menu toolsMenu;
	private MenuItem deactivateUserMenuItem;
	private MenuItem reactivateUserMenuItem;

	public OrganizationToolBar(OrganizationPanel parentWindow) {
		super();
		mainWindow = parentWindow;

		initButtons();
		add(addButton);
		add(moveButton);
		add(importButton);
		add(deleteButton);
		add(refreshButton);
		add(toolsButton);

		initAddMenu();
		addButton.setMenu(addMenu);

		initDeleteMenu();
		deleteButton.setMenu(deleteMenu);
		
		initToolsMenu();
		toolsButton.setMenu(toolsMenu);

		configureIcons();

		changeToolBarPerspective(null);
	}

	private void initButtons() {
		
		addButton = new Button(GwtLocaleProvider.getConstants().ADD());
		addButton.setStyleName("admin_toolbar_buttons");
		addButton.setBorders(true);
		addButton.setIconStyle("icon-add");
		
		moveButton = new Button(GwtLocaleProvider.getConstants().MOVE());
		moveButton.setStyleName("admin_toolbar_buttons");
		moveButton.setBorders(true);
		moveButton.setIconStyle("icon-move");
		moveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				
				ModelData entityToMove = mainWindow.getOrganizationTreePanel().getSelectedItem();
				if (entityToMove == null) {
					return;
				}				
				if (!GwtOrganizationalStructureBusinessUtils.isEntityToMoveTheRightType(entityToMove)) {
					return;
				}
				
				ModelData parentOfEntityToMove = mainWindow.getOrganizationTreePanel().getParentOfEntity(entityToMove);				
				mainWindow.goToMoveEntity(entityToMove, parentOfEntityToMove);
			}
		});
		
		importButton = new Button(GwtLocaleProvider.getConstants().IMPORT());
		importButton.setStyleName("admin_toolbar_buttons");
		importButton.setBorders(true);
		importButton.setIconStyle("icon-import");
		importButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				
				ComponentUtils.confirm(GwtLocaleProvider.getConstants().IMPORT_ORGANIZATIONAL_STRUCTURE_FROM_DIRECTORY(),
						GwtLocaleProvider.getMessages().CONFIRM_IMPORT_ORGANIZATIONAL_STRUCTURE_FROM_DIRECTORY(),
						new ConfirmCallback() {
					
					@Override
					public void onYes() {
						ComponentUtils.confirm(GwtLocaleProvider.getConstants().IMPORT_ORGANIZATIONAL_STRUCTURE_FROM_DIRECTORY(),
								GwtLocaleProvider.getMessages().CONFIRM_IMPORT_ORGANIZATIONAL_STRUCTURE_FROM_DIRECTORY_ATTENTION(),
								new ConfirmCallback() {

							@Override
							public void onYes() {
								LoadingManager.get().loading();
								GwtServiceProvider.getOrgService().importOrganizationalStructureFromDirectory(new AsyncCallback<Void>() {
									
									@Override
									public void onSuccess(Void nothing) {
										MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(),
											GwtLocaleProvider.getMessages().ORGANIZATIONAL_STRUCTURE_IMPORTED());
										AppEventController.fireEvent(AppEventType.OrganizationalStructure);
										LoadingManager.get().loadingComplete();
									}
									
									@Override
									public void onFailure(Throwable exception) {
										MessageUtils.displayError(exception);
										LoadingManager.get().loadingComplete();
									}
								});
							}
						});
					}
				});
			}
		});
		
		deleteButton = new Button(GwtLocaleProvider.getConstants().DELETE());
		deleteButton.setStyleName("admin_toolbar_buttons");
		deleteButton.setBorders(true);
		deleteButton.setIconStyle("icon-delete");
		
		refreshButton = new Button(GwtLocaleProvider.getConstants().REFRESH());
		refreshButton.setStyleName("admin_toolbar_buttons");
		refreshButton.setBorders(true);
		refreshButton.setIconStyle("icon-refresh");
		
		refreshButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				mainWindow.refresh();
			}
		});
		
		toolsButton = new Button(GwtLocaleProvider.getConstants().TOOLS());
		toolsButton.setStyleName("admin_toolbar_buttons");
		toolsButton.setBorders(true);
		toolsButton.setIconStyle("icon-tools");
	}

	private void initAddMenu() {
		
		addMenu = new Menu();
		
		addOrgUnitMenuItem = new MenuItem(GwtLocaleProvider.getConstants().ORG_UNIT());
		addUserMenuItem = new MenuItem(GwtLocaleProvider.getConstants().USER());
		addUserFromDirectoryMenuItem = new MenuItem(GwtLocaleProvider.getConstants().USER_FROM_DIRECTORY());
		
		addMenu.add(addOrgUnitMenuItem);
		addMenu.add(addUserMenuItem);
		addMenu.add(addUserFromDirectoryMenuItem);
		
		addAddActions();
	}

	private void initDeleteMenu() {
		deleteMenu = new Menu();
		deleteOrgUnitMenuItem = new MenuItem(GwtLocaleProvider.getConstants().ORG_UNIT());
		deleteUserMenuItem = new MenuItem(GwtLocaleProvider.getConstants().USER());
		deleteMenu.add(deleteOrgUnitMenuItem);
		deleteMenu.add(deleteUserMenuItem);
		addDeleteActions();
	}
	
	private void initToolsMenu() {
		
		toolsMenu = new Menu();
		
		deactivateUserMenuItem = new MenuItem(GwtLocaleProvider.getConstants().DEACTIVATE_USER());
		toolsMenu.add(deactivateUserMenuItem);
		
		reactivateUserMenuItem = new MenuItem(GwtLocaleProvider.getConstants().REACTIVATE_USER());
		toolsMenu.add(reactivateUserMenuItem);
		
		addToolsActions();
	}

	private void configureIcons() {
	//	addButton.setIconStyle("icon-add");
	//	deleteButton.setIconStyle("icon-delete");
	//	refreshButton.setIconStyle("icon-refresh");
	}

	/**
	 * Modifica starile optiunilor din meniurile butoanelor in functie de
	 * selectia din arbore. Daca entitatea este nula, inseamna ca nu s-a
	 * selectat nimic.
	 * 
	 * @param selectedModel
	 *            entitatea selectata din arbore
	 */
	public void changeToolBarPerspective(ModelData selectedModel) {
		
		if (selectedModel != null) {
			
			if (selectedModel instanceof OrganizationModel) {
				addOrgUnitMenuItem.setEnabled(true);
				addUserMenuItem.setEnabled(true);
				addUserFromDirectoryMenuItem.setEnabled(true);
				deleteOrgUnitMenuItem.setEnabled(false);
				deleteUserMenuItem.setEnabled(false);
				deactivateUserMenuItem.setEnabled(false);
				reactivateUserMenuItem.setEnabled(false);
			} else if (selectedModel instanceof OrganizationUnitModel) {
				addOrgUnitMenuItem.setEnabled(true);
				addUserMenuItem.setEnabled(true);
				addUserFromDirectoryMenuItem.setEnabled(true);
				deleteOrgUnitMenuItem.setEnabled(true);
				deleteUserMenuItem.setEnabled(false);
				deactivateUserMenuItem.setEnabled(false);
				reactivateUserMenuItem.setEnabled(false);
			} else if (selectedModel instanceof UserModel) {
				addOrgUnitMenuItem.setEnabled(false);
				addUserMenuItem.setEnabled(false);
				addUserFromDirectoryMenuItem.setEnabled(false);
				deleteOrgUnitMenuItem.setEnabled(false);
				deleteUserMenuItem.setEnabled(true);
				deactivateUserMenuItem.setEnabled(true);
				reactivateUserMenuItem.setEnabled(true);
			} else {
				throw new IllegalArgumentException("Entitate necunoscuta: [" + selectedModel.getClass().getName() + "]");
			}
			
			boolean canEntityBeMoved = GwtOrganizationalStructureBusinessUtils.isEntityToMoveTheRightType(selectedModel);
			moveButton.setEnabled(canEntityBeMoved);
		} else {
			
			addOrgUnitMenuItem.setEnabled(false);
			addUserMenuItem.setEnabled(false);
			addUserFromDirectoryMenuItem.setEnabled(false);
			deleteOrgUnitMenuItem.setEnabled(false);
			deleteUserMenuItem.setEnabled(false);
			deactivateUserMenuItem.setEnabled(false);
			reactivateUserMenuItem.setEnabled(false);
			
			moveButton.setEnabled(false);
		}
	}

	private void addAddActions() {
		addOrgUnitMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent event) {
				
				String parentOrganizationUnitId = mainWindow.getOrganizationTreePanel().getIdForSelectedOrganizationUnit();
				String parentOrganizationId = mainWindow.getOrganizationTreePanel().getIdForSelectedOrganization();
				
				mainWindow.goToAddOrgUnit(parentOrganizationUnitId, parentOrganizationId);
			}
		});
		addUserMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent event) {
				
				String organizationUnitId = mainWindow.getOrganizationTreePanel().getIdForSelectedOrganizationUnit();
				String organizationId = mainWindow.getOrganizationTreePanel().getIdForSelectedOrganization();
				
				mainWindow.goToAddUser(organizationUnitId, organizationId);
			}
		});
		addUserFromDirectoryMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
			
			@Override
			public void componentSelected(MenuEvent event) {
				
				String organizationIdAsString = mainWindow.getOrganizationTreePanel().getIdForSelectedOrganization();
				String organizationUnitIdAsString = mainWindow.getOrganizationTreePanel().getIdForSelectedOrganizationUnit();
				
				mainWindow.goToAddUserFromDirectory(organizationIdAsString, organizationUnitIdAsString);
			}
		});
	}

	private void addDeleteActions() {
		deleteOrgUnitMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent event) {
				// Apare o fereastra de confirmare pentru stergere.
				MessageBox.confirm(GwtLocaleProvider.getConstants().DELETE(), GwtLocaleProvider.getMessages().CONFIRM_DELETE_ORG_UNIT(), new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent mbe) {
						if (mbe.getButtonClicked().getItemId().equals(Dialog.YES)) {
							OrganizationUnitModel selectedOrgUnit = (OrganizationUnitModel) mainWindow.getOrganizationTreePanel().getSelectedItem();

							LoadingManager.get().loading();
							GwtServiceProvider.getOrgService().deleteOrgUnit(selectedOrgUnit, new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable exception) {
									MessageUtils.displayError(exception);
									LoadingManager.get().loadingComplete();
								}
								@Override
								public void onSuccess(Void nothing) {
									MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().ORG_UNIT_DELETED());
									mainWindow.refresh();
									LoadingManager.get().loadingComplete();
								}
							});
						}
					}
				});
			};
		});
		deleteUserMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent event) {
				// Apare o fereastra de confirmare pentru stergere.
				MessageBox.confirm(GwtLocaleProvider.getConstants().DELETE(), GwtLocaleProvider.getMessages().CONFIRM_DELETE_USER(), new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent mbe) {
						if (mbe.getButtonClicked().getItemId().equals(Dialog.YES)) {
							final UserModel selectedUser = (UserModel) mainWindow.getOrganizationTreePanel().getSelectedItem();

							LoadingManager.get().loading();
							GwtServiceProvider.getOrgService().deleteUser(selectedUser, new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable exception) {
									MessageUtils.displayError(exception);
									LoadingManager.get().loadingComplete();
								}
								@Override
								public void onSuccess(Void nothing) {
									MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().USER_DELETED());
									mainWindow.refresh();
									LoadingManager.get().loadingComplete();
								}
							});
						}
					}
				});
			};
		});
	}
	
	private void addToolsActions() {
		deactivateUserMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
			
			@Override
			public void componentSelected(MenuEvent event) {
				
				final Long userId = mainWindow.getOrganizationTreePanel().getIdForSelectedUser();
				if (userId == null) {
					return;
				}
				
				mainWindow.goToDeactivateUser(userId);
			}
		});
		reactivateUserMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
			
			@Override
			public void componentSelected(MenuEvent event) {
				
				final Long userId = mainWindow.getOrganizationTreePanel().getIdForSelectedUser();
				if (userId == null) {
					return;
				}
				
				ComponentUtils.confirm(GwtLocaleProvider.getConstants().REACTIVATE_USER(),
						GwtLocaleProvider.getMessages().CONFIRM_REACTIVATE_USER(),
						new ConfirmCallback() {
					
					@Override
					public void onYes() {
						LoadingManager.get().loading();
						GwtServiceProvider.getOrgService().reactivateUserWithId(userId, new AsyncCallback<Void>() {
							
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							
							@Override
							public void onSuccess(Void nothing) {
								MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(),
									GwtLocaleProvider.getMessages().USER_REACTIVATED());
								AppEventController.fireEvent(AppEventType.User);
								LoadingManager.get().loadingComplete();
							}
						});
					}
				});
			}
		});
	}
}

class OrganizationTreePanel extends TreePanel<ModelData> {

	private OrganizationPanel mainWindow;

	public OrganizationTreePanel(OrganizationPanel parentWindow) {
		super(new TreeStore<ModelData>());
		mainWindow = parentWindow;
		
		store.setStoreSorter(new OrganizationalStructureTreeSorter());
		configureIcons();
		// Trebuie sa permita selectarea unui singur element.
		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		// Specific ce camp sa afisez pentru fiecare tip de entitate.
		setLabelProvider(new OrganizationalStructureLabelProvider());
		/*
		 * Nu trebuie incarcate datele in constructor. Datele vor fi incarcate
		 * doar cand se va afisa fereastra principala (prin Window.show()).
		 */
		//refreshTree();
		//addItemSelectAction();
		addItemExpandAction();
		addDragAndDropMoveSupport();
	}

	private void configureIcons() {
		setIconProvider(new OrganizationalStructureIconProvider(store));
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected void onDoubleClick(TreePanelEvent tpe) {
		ModelData selectedItem = tpe.getItem();
		mainWindow.getOrganizationStructureToolBar().changeToolBarPerspective(selectedItem);
		mainWindow.dispatch(selectedItem);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected void onClick(TreePanelEvent tpe) {		
		super.onClick(tpe);
		ModelData selectedItem = tpe.getItem();
		mainWindow.getOrganizationStructureToolBar().changeToolBarPerspective(selectedItem);
	}
	
	public void refreshTree() {
		LoadingManager.get().loading();
		GwtServiceProvider.getOrgService().getOrganization(new AsyncCallback<OrganizationModel>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(OrganizationModel organizationModel) {

				getSelectionModel().deselectAll();
				getStore().removeAll();
				getStore().add(organizationModel, true);	
				expandAll();
				
				LoadingManager.get().loadingComplete();
			}
		});
	}

	private void addItemExpandAction() {
		/*
		 * Cand se expandeaza un element, acesta nu se selecteaza. Elementul
		 * trebuie selectat pentru ca actiunile din toolbar sa functioneze
		 * corect.
		 */
		addListener(Events.Expand, new Listener<TreePanelEvent<ModelData>>() {
			@Override
			public void handleEvent(TreePanelEvent<ModelData> event) {
				getSelectionModel().select(event.getItem(), false);
			}
		});
	}

	private void addDragAndDropMoveSupport() {
		DragSource dragSource = new DragSource(this);
		dragSource.addDNDListener(new DNDListener() {
			@Override
			public void dragStart(DNDEvent event) {
				
				TreeNode nodeForItemToMove = findNode(event.getTarget());
				if (nodeForItemToMove == null) {
					event.setCancelled(true);
					return;
				}
				
				ModelData itemToMove = nodeForItemToMove.getModel();
				if (itemToMove instanceof OrganizationModel) {
					event.setCancelled(true);
					return;
				}
				
				event.setData(itemToMove);
				
				String dragAndDropStatus = GwtLocaleProvider.getConstants().MOVE();
				if (itemToMove instanceof ModelWithDisplayName) {
					String displayNameForItemToMove = ((ModelWithDisplayName) itemToMove).getDisplayName();
					dragAndDropStatus = GwtLocaleProvider.getMessages().MOVE_ENTITY(displayNameForItemToMove);
				}
				event.getStatus().update(dragAndDropStatus);
			}
		});
		DropTarget dropTarget = new DropTarget(this);
		dropTarget.setAllowSelfAsSource(true);
		dropTarget.addDNDListener(new DNDListener() {
			@Override
			public void dragMove(DNDEvent event) {
				// elementul de mutat
				ModelData itemToMove = event.getData();
				// parintele elementului de mutat
				ModelData parentItem = getParentOfEntity(itemToMove);
				// nodul destinatie din arbore
				TreeNode destinationNode = findNode(event.getTarget());
				if (destinationNode == null) {
					event.getStatus().setStatus(false);
					return;
				}
				// elementul destinatie
				ModelData destinationItem = destinationNode.getModel();
				/*
				 * Elementul destinatie trebuie sa nu fie el insusi sau
				 * parintele lui, nici un copil de-al elementului sursa si
				 * trebuie sa fie de tip organizatie sau unit. org.
				 */
				boolean canMove = GwtOrganizationalStructureBusinessUtils.canMoveTo((ModelWithId) itemToMove,
					(ModelWithId) destinationItem, (ModelWithId) parentItem, ComponentUtils.getAncestors(getStore(), destinationItem));
				event.getStatus().setStatus(canMove);
			}
			@Override
			public void dragDrop(DNDEvent event) {
				
				final ModelData itemToMove = event.getData();
				// parintele elementului de mutat
				ModelData parentItem = getParentOfEntity(itemToMove);
				TreeNode destinationNode = findNode(event.getTarget());
				if (destinationNode == null) {
					event.setCancelled(true);
					return;
				}
				ModelData destinationItem = destinationNode.getModel();
				boolean canMove = GwtOrganizationalStructureBusinessUtils.canMoveTo((ModelWithId) itemToMove,
					(ModelWithId) destinationItem, (ModelWithId) parentItem, ComponentUtils.getAncestors(getStore(), destinationItem));
				if (!canMove) {
					event.setCancelled(true);
					return;
				}
				
				LoadingManager.get().loading();
				GwtServiceProvider.getOrgService().move(itemToMove, destinationItem, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(Void nothing) {
						
						String message = null;
						if (itemToMove instanceof UserModel) {
							message = GwtLocaleProvider.getMessages().USER_MOVED();
						} else if (itemToMove instanceof OrganizationUnitModel) {
							message = GwtLocaleProvider.getMessages().ORG_UNIT_MOVED();
						} else {
							throw new IllegalStateException("S-a mutat o entitate necunoscuta: [" + itemToMove.getClass().getName() + "].");
						}
						MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), message);
						
						mainWindow.refresh();
						LoadingManager.get().loadingComplete();
					}
				});
			}
		});
	}

	/**
	 * Returneaza ID-ul unitatii organizatorice selectate SAU null daca nu este selectata o unitate organizatorica.
	 */
	public String getIdForSelectedOrganizationUnit() {
		ModelData selectedModel = getSelectedItem();
		if (selectedModel instanceof OrganizationUnitModel) {
			return ((OrganizationUnitModel) selectedModel).getId();
		} else {
			return null;
		}
	}

	/**
	 * Returneaza ID-ul organizatiei selectate SAU null daca nu este selectata o organizatie.
	 */
	public String getIdForSelectedOrganization() {
		ModelData selectedModel = getSelectedItem();
		if (selectedModel instanceof OrganizationModel) {
			return ((OrganizationModel) selectedModel).getId();
		} else {
			return null;
		}
	}
	
	/**
	 * Returneaza ID-ul utilizatorului selectat SAU null daca nu este selectat un utilizator.
	 */
	public Long getIdForSelectedUser() {
		ModelData selectedModel = getSelectedItem();
		if (selectedModel instanceof UserModel) {
			return ((UserModel) selectedModel).getUserIdAsLong();
		}
		return null;
	}

	public ModelData getSelectedItem() {
		return getSelectionModel().getSelectedItem();
	}
	
	/**
	 * Returneaza parintele entitatii date.
	 * Daca entitatea nu are parinte, atunci va returna null.
	 * 
	 * @throws IllegalArgumentException daca entitatea NU face parte din arbore
	 */
	public ModelData getParentOfEntity(ModelData entity) {
		
		TreeNode entityNode = findNode(entity);
		if (entityNode == null) {
			throw new IllegalArgumentException("Entitatea [" + entity + "] NU face parte din arbore.");
		}
		
		TreeNode parentNode = entityNode.getParent();
		if (parentNode == null) {
			return null;
		}
		
		return parentNode.getModel();
	}
	
	public String getDisplayLabel(ModelData model) {
		return getText(model);
	}
}