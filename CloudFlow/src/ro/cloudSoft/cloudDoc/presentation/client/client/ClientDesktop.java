package ro.cloudSoft.cloudDoc.presentation.client.client;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtLanguageConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentCreationInDefaultLocationViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtExceptionHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.NavigationUtils;

import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.desktop.client.StartMenu;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ClientDesktop extends Desktop {
	
	private static final String PROPERTY_WINDOW = "window";
	
	private SelectionListener<MenuEvent> menuListener;
	//private SelectionListener<ComponentEvent> shortcutListener;
	
	private MyActivitiesWindow myActivitiesWindow;
	private WorkspacePanelWindow workspacePanelWindow;
	private SearchesAndReportsWindow searchesAndReportsWindow;
	private PerformanceMonitoringWindow performanceMonitoringWindow;
	private ReplacementProfilesWindow replacementProfilesWindow;
	private HelpWindow helpWindow;
	
	public ClientDesktop() 
	{
		configureBehavior();
		initWindows();
		initShortcuts();
		initStartMenu();
	}
	
	private void initWindows() {
		myActivitiesWindow = new MyActivitiesWindow();
		workspacePanelWindow = new WorkspacePanelWindow();
		searchesAndReportsWindow = new SearchesAndReportsWindow();
		performanceMonitoringWindow = new PerformanceMonitoringWindow();
		replacementProfilesWindow = new ReplacementProfilesWindow();
		helpWindow = new HelpWindow();
	}
	
	private void initShortcuts() {}
	
	private void initStartMenu() 
	{
		getTaskBar().setStartLabel("CloudDoc");
		getTaskBar().getStartMenu().setHeading(GwtRegistryUtils.getUserSecurity().getDisplayName());
		getTaskBar().getStartMenu().setIconStyle("icon-startMenu-user");
		initMenuItems();
		initTools();
	}
	
	private void initMenuItems() {
		StartMenu startMenu = getTaskBar().getStartMenu();
		
		CreateDocumentMenuItem createDocumentMenuItem = new CreateDocumentMenuItem();
		createDocumentMenuItem.setText(GwtLocaleProvider.getConstants().CREATE_DOCUMENT());
		createDocumentMenuItem.setIconStyle("icon-startMenu-createDocument");
				
		MenuItem myActivitiesMenuItem = new MenuItem();
		myActivitiesMenuItem.setText(GwtLocaleProvider.getConstants().MY_ACTIVITIES());
		myActivitiesMenuItem.setIconStyle("icon-startMenu-myActivities");
		myActivitiesMenuItem.addSelectionListener(menuListener);
		myActivitiesMenuItem.setData(PROPERTY_WINDOW, myActivitiesWindow);
		
		MenuItem workspacePanelMenuItem = new MenuItem();
		workspacePanelMenuItem.setText(GwtLocaleProvider.getConstants().WORKSPACE_PANEL());
		workspacePanelMenuItem.setIconStyle("icon-startMenu-workspacePanel");
		workspacePanelMenuItem.addSelectionListener(menuListener);
		workspacePanelMenuItem.setData(PROPERTY_WINDOW, workspacePanelWindow);
		
		MenuItem searchesAndReportsMenuItem = new MenuItem();
		searchesAndReportsMenuItem.setText(GwtLocaleProvider.getConstants().SEARCHES_AND_REPORTS());
		searchesAndReportsMenuItem.setIconStyle("icon-startMenu-searchesAndReports");
		searchesAndReportsMenuItem.addSelectionListener(menuListener);
		searchesAndReportsMenuItem.setData(PROPERTY_WINDOW, searchesAndReportsWindow);
		
		MenuItem performanceMonitoringMenuItem = new MenuItem();
		performanceMonitoringMenuItem.setText(GwtLocaleProvider.getConstants().PERFORMANCE_MONITORING());
		performanceMonitoringMenuItem.setIconStyle("icon-startMenu-performanceMonitoring");
		performanceMonitoringMenuItem.addSelectionListener(menuListener);
		performanceMonitoringMenuItem.setData(PROPERTY_WINDOW, performanceMonitoringWindow);
		performanceMonitoringMenuItem.setEnabled(false); // folosita doar pana la dezvoltarea modulului
				
		MenuItem replacementProfilesMenuItem = new MenuItem();
		replacementProfilesMenuItem.setText(GwtLocaleProvider.getConstants().REPLACEMENT_PROFILES());
		replacementProfilesMenuItem.setIconStyle("icon-startMenu-replacementProfile");
		replacementProfilesMenuItem.addSelectionListener(menuListener);
		replacementProfilesMenuItem.setData(PROPERTY_WINDOW, replacementProfilesWindow);
				
		MenuItem helpMenuItem = new MenuItem();
		helpMenuItem.setText(GwtLocaleProvider.getConstants().HELP());
		helpMenuItem.setIconStyle("icon-startMenu-help");
		helpMenuItem.addSelectionListener(menuListener);
		helpMenuItem.setData(PROPERTY_WINDOW, helpWindow);
		helpMenuItem.setEnabled(false);
				
		startMenu.add(createDocumentMenuItem);
		startMenu.add(myActivitiesMenuItem);
		startMenu.add(workspacePanelMenuItem);
		startMenu.add(searchesAndReportsMenuItem);
		startMenu.add(performanceMonitoringMenuItem);
		startMenu.add(replacementProfilesMenuItem);
		startMenu.add(helpMenuItem);
		
		createDocumentMenuItem.populate();
	}
	
	private void initTools() {
		StartMenu startMenu = getTaskBar().getStartMenu();
		
		MenuItem adminTool = new MenuItem();
		adminTool.setText(GwtLocaleProvider.getConstants().ADMIN());
		adminTool.setIconStyle("icon-startMenu-admin");
		NavigationUtils.linkTo(adminTool, NavigationConstants.getAdminLink());
		if (!GwtRegistryUtils.getUserSecurity().isUserAdmin()){
			adminTool.setEnabled(false);
		}
		MenuItem archiveTool = new MenuItem();
		archiveTool.setText(GwtLocaleProvider.getConstants().ARCHIVE());
		archiveTool.setIconStyle("icon-startMenu-archive");
		NavigationUtils.linkTo(archiveTool, NavigationConstants.getArchiveLink());
		archiveTool.setEnabled(false); // se va decomenta cand se dezvolta modulul
		
		MenuItem langRomanianTool = new MenuItem();
		langRomanianTool.setText(GwtLanguageConstants.LABEL_ROMANIAN);
		langRomanianTool.setIconStyle("icon-startMenu-languageRomanian");
		langRomanianTool.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent me) {
				GwtServiceProvider.getAppService().setLocale(GwtLanguageConstants.LOCALE_ROMANIAN, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
					}
					@Override
					public void onSuccess(Void nothing) {
						NavigationUtils.refresh();
					}
				});
			}
		});
		
		MenuItem langEnglishTool = new MenuItem();
		langEnglishTool.setText(GwtLanguageConstants.LABEL_ENGLISH);
		langEnglishTool.setIconStyle("icon-startMenu-languageEnglish");
		langEnglishTool.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent me) {
				GwtServiceProvider.getAppService().setLocale(GwtLanguageConstants.LOCALE_ENGLISH, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
					}
					@Override
					public void onSuccess(Void nothing) {
						NavigationUtils.refresh();
					}
				});
			}
		});
		
		MenuItem logoutTool = new MenuItem();
		logoutTool.setText(GwtLocaleProvider.getConstants().LOGOUT());
		logoutTool.setIconStyle("icon-startMenu-logout");
		logoutTool.addSelectionListener(new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent event) {
				GwtServiceProvider.getAclService().invalidateSession(new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable exception) {
						if (GwtExceptionHelper.isUnauthenticated(exception)) {
							String module = GwtRegistryUtils.getWebConstants().getModuleClient();
							NavigationUtils.logout(module);
						} else {
							MessageUtils.displayError(exception);
						}
					}
					@Override
					public void onSuccess(Void nothing) {
						String module = GwtRegistryUtils.getWebConstants().getModuleClient();
						NavigationUtils.logout(module);
					}
				});
			}
		});

		startMenu.addTool(adminTool);
		startMenu.addTool(archiveTool);
		startMenu.addToolSeperator();
		startMenu.addTool(langRomanianTool);
		startMenu.addTool(langEnglishTool);
		startMenu.addToolSeperator();
		startMenu.addTool(logoutTool);
	}
	
	private void configureBehavior() {
		menuListener = new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent event) {
				itemSelected(event);
			}
		};
		/*shortcutListener = new SelectionListener<ComponentEvent>() {
			@Override
			public void componentSelected(ComponentEvent event) {
				itemSelected(event);
			}
		};*/
	}
	
	private void itemSelected(ComponentEvent event) {
		
		Window window = null;
		
		if (event instanceof MenuEvent) {
			MenuEvent menuEvent = (MenuEvent) event;
			window = menuEvent.getItem().getData(PROPERTY_WINDOW);
		} else {
			window = event.getComponent().getData(PROPERTY_WINDOW);
		}
		
		if (!getWindows().contains(window)) {
			addWindow(window);
		}
		
		if ((window != null)) {
			if (!window.isVisible()) {
				// Afisez fereastra.
				window.show();
				// O marchez ca activa in bara de Start.
				window.setActive(true);
			} else {
				// Daca exista o fereastra activa...
				if (this.activeWindow != null) {
					// O mut in spate.
					this.activeWindow.toBack();
				}
				// Mut fereastra selectata in fata.
				window.toFront();
				// O marchez ca activa in bara de Start.
				window.setActive(true);
			}
		}
	}
	
	public static class CreateDocumentMenuItem extends MenuItem {
		
		private Menu createDocumentMenu;
		
		public CreateDocumentMenuItem() {
			createDocumentMenu = new Menu();
			setSubMenu(createDocumentMenu);
		}
		
		public void populate() {
			
			createDocumentMenu.removeAll();
			
			LoadingManager.get().loading();
			GwtServiceProvider.getDocumentTypeService().getDocumentCreationInDefaultLocationViews(new AsyncCallback<List<DocumentCreationInDefaultLocationViewModel>>() {
				
				@Override
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					CreateDocumentMenuItem.this.setEnabled(false);
					LoadingManager.get().loadingComplete();
				}
				
				@Override
				public void onSuccess(List<DocumentCreationInDefaultLocationViewModel> views) {

					CreateDocumentMenuItem.this.setEnabled(!views.isEmpty());
					
					for (final DocumentCreationInDefaultLocationViewModel view : views) {
						
						MenuItem createDocumentOfTypeMenuItem = new MenuItem();
						createDocumentOfTypeMenuItem.setText(view.getDocumentTypeName());
						createDocumentOfTypeMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
							
							@Override
							public void componentSelected(MenuEvent e) {
								
								Long documentTypeId = view.getDocumentTypeId();
								String documentLocationRealName = view.getParentDocumentLocationRealNameForDefaultLocation();
								String folderId = view.getFolderIdForDefaultLocation();
								
								WindowManager.getDocumentWindow().prepareForAdd(documentTypeId, documentLocationRealName, folderId);
							}
						});
						
						createDocumentMenu.add(createDocumentOfTypeMenuItem);
					}
					
					LoadingManager.get().loadingComplete();
				}
			});
		}
	}
}