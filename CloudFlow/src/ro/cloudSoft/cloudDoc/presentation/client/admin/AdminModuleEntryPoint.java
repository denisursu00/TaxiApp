package ro.cloudSoft.cloudDoc.presentation.client.admin;

import ro.cloudSoft.cloudDoc.presentation.client.shared.GwtUncaughtExceptionHandler;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtConstantsPayload;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SecurityManagerModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.timers.KeepSessionAliveTimer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtExceptionHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.NavigationUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.LocaleSelectorIcons;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class AdminModuleEntryPoint implements EntryPoint {

	// Se ocupa cu afisarea in spatiul util a panoului cerut.
	private AdminPanelDispatcher dispatcher;

	private Viewport viewport;

	// panourile principale
	private HorizontalPanel topPanel;
	private VerticalPanel leftPanel;
	private LayoutContainer contentContainer;

	// elementele din topPanel
	private VerticalPanel logoAndAreaPanel;
	private HorizontalPanel topButtonsPanel;
	private VerticalPanel userAndLanguagePanel;
	private HorizontalPanel usernameAndLogoutPanel;
	private VerticalPanel customerLogoPanel;
	private HorizontalPanel languagePanel;

	
	// elementele din logoAndAreaPanel
	private Image productLogoImage;

	// elementele din topButtonsPanel
	private IconButton clientButton;
	private IconButton archiveButton;

	// elementele din usernameAndLogoutPanel
	private Label usernameLabel;
	private Button logoutButton;

	// elementele din customerLogoPanel
	private Image clientLogoImage;

	// elementele din leftPanel
	private Button organizationalStructureButton;
	private Button userGroupsButton;
	private Button contentButton;
	private Button workflowsButton;
	private Button replacementProfilesButton;
	private Button archivingProfilesButton;
	private Button auditButton;
	private Button logButton;
	private Button helpButton;
	
	// panoul pentru contentButton
	private VerticalPanel contentButtonPanel;

	// elementele din contentButtonPanel
	private Button documentTypesButton;
	private Button mimeTypesButton;

	@Override
	public void onModuleLoad() {
		
		GWT.setUncaughtExceptionHandler(new GwtUncaughtExceptionHandler());
		
		LoadingManager.get().loading();
		GwtServiceProvider.getAclService().getSecurityManager(new AsyncCallback<SecurityManagerModel>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayErrorWithDefaultSettings(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(final SecurityManagerModel userSecurity) {
				LoadingManager.get().loading();
				GwtServiceProvider.getAppService().getConstants(new AsyncCallback<GwtConstantsPayload>() {
					public void onFailure(Throwable exception) {
						MessageUtils.displayErrorWithDefaultSettings(exception);
						LoadingManager.get().loadingComplete();
					}
					public void onSuccess(GwtConstantsPayload constantsPayload) {
						
						GwtRegistryUtils.init(userSecurity,
							constantsPayload.getGuiConstants(),
							constantsPayload.getBusinessConstants(),
							constantsPayload.getCereriDeConcediuConstants(),
							constantsPayload.getWebConstants(),
							constantsPayload.getAppComponentsAvailabilityConstants(),
							constantsPayload.getReplacementProfilesOutOfOfficeConstants(),
							constantsPayload.getSupportedAttachmentTypesForPreviewConstants());
						
						KeepSessionAliveTimer.activate();
						
						initMain();
						AppStoreCache.loadAll();
						
						// Panoul afisat implicit este cel de administrare organizatie.
						dispatcher.goToOrganization();
						
						LoadingManager.get().loadingComplete();
					}
				});
				LoadingManager.get().loadingComplete();
			}
		});
	}

	private void initMain() {
		viewport = new Viewport();
		viewport.setLayout(new BorderLayout());

		initTopPanel();
		initLeftPanel();
		initContentContainer();

		dispatcher = new AdminPanelDispatcher(contentContainer);

		RootPanel.get().add(viewport);
	}

	private void initTopPanel() {
		topPanel = new HorizontalPanel();
		topPanel.setTableWidth("100%");
		topPanel.setId("topPanel");
		// topPanel.setStyleName("admin-header");	

		initLogoAndAreaPanel();
		initTopButtonsPanel();
		initUsernameAndLogoutPanel();
		initCustomerLogoPanel();
		
		viewport.add(topPanel, new BorderLayoutData(LayoutRegion.NORTH, 70));
	}

	private void initLogoAndAreaPanel() {
		logoAndAreaPanel = new VerticalPanel();
		logoAndAreaPanel.setHorizontalAlign(HorizontalAlignment.LEFT);
		logoAndAreaPanel.setVerticalAlign(VerticalAlignment.TOP);
		
		productLogoImage = new Image();
		productLogoImage.setUrl(GWT.getModuleBaseURL() + "images/gray/interfata/logoAdmin.png");

		logoAndAreaPanel.add(productLogoImage);

		TableData logoAndAreaPanelTableData = new TableData();
		logoAndAreaPanelTableData.setWidth("15%");
		logoAndAreaPanelTableData.setHeight("10%");
		logoAndAreaPanelTableData.setHorizontalAlign(HorizontalAlignment.CENTER);
		logoAndAreaPanelTableData.setVerticalAlign(VerticalAlignment. MIDDLE);
		logoAndAreaPanelTableData.setStyleName("admin-header");

		topPanel.add(logoAndAreaPanel, logoAndAreaPanelTableData);
	}

	private void initTopButtonsPanel() {
		topButtonsPanel = new HorizontalPanel();
		topButtonsPanel.setSpacing(10);
		
		int buttonHeight = 40;
		int buttonWidth = 80;

		clientButton = new IconButton();
		clientButton.setStyleName("client");
		clientButton.setSize(buttonWidth, buttonHeight);
		// clientButton.setToolTip("eDoc Client");
		clientButton.setStyleAttribute("cursor","pointer");
		
		clientButton.addListener(Events.OnMouseOver, new Listener<IconButtonEvent>() {
            public void handleEvent(IconButtonEvent be) {
                be.getIconButton().setStyleName("client_over");
            }
        });
		clientButton.addListener(Events.OnMouseOut, new Listener<IconButtonEvent>() {
            public void handleEvent(IconButtonEvent be) {
                be.getIconButton().setStyleName("client");
            }
        });
		NavigationUtils.linkTo(clientButton, NavigationConstants.getClientLink());

		archiveButton = new IconButton();
		archiveButton.setStyleName("archive");
		archiveButton.setSize(buttonWidth, buttonHeight);
		// archiveButton.setToolTip("eDoc Archive");
		archiveButton.setStyleAttribute("cursor","pointer");
		archiveButton.setEnabled(false); // linia se va scoate in momentul in care se dezvolta modulul
		
		archiveButton.addListener(Events.OnMouseOver, new Listener<IconButtonEvent>() {
            public void handleEvent(IconButtonEvent be) {
                be.getIconButton().setStyleName("archive_over");
            }
        });
		archiveButton.addListener(Events.OnMouseOut, new Listener<IconButtonEvent>() {
            public void handleEvent(IconButtonEvent be) {
                be.getIconButton().setStyleName("archive");
            }
        });
		NavigationUtils.linkTo(archiveButton, NavigationConstants.getArchiveLink());

		topButtonsPanel.add(clientButton);
		topButtonsPanel.add(archiveButton);

		TableData topButtonsPanelTableData = new TableData();
		topButtonsPanelTableData.setWidth("50%");
		topButtonsPanelTableData.setHeight("10%");
		topButtonsPanelTableData.setHorizontalAlign(HorizontalAlignment.CENTER);
		topButtonsPanelTableData.setVerticalAlign(VerticalAlignment.TOP);
		topButtonsPanelTableData.setStyleName("admin-header");

		topPanel.add(topButtonsPanel, topButtonsPanelTableData);
	}

	private void initUsernameAndLogoutPanel() {
		userAndLanguagePanel = new VerticalPanel();
		userAndLanguagePanel.setHorizontalAlign(HorizontalAlignment.LEFT);
				
		usernameAndLogoutPanel = new HorizontalPanel();
		usernameAndLogoutPanel.setHorizontalAlign(HorizontalAlignment.LEFT);
		usernameAndLogoutPanel.setVerticalAlign(VerticalAlignment.MIDDLE);
		
		languagePanel = new LocaleSelectorIcons();
		
		usernameLabel = new Label();
		usernameLabel.setStyleName("username");
		usernameLabel.setText(GwtRegistryUtils.getUserSecurity().getDisplayName()+" | ");

		logoutButton = new Button();
		logoutButton.setStyleName("logoutbutton");
		logoutButton.setText(GwtLocaleProvider.getConstants().LOGOUT());
		logoutButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				GwtServiceProvider.getAclService().invalidateSession(new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable exception) {
						if (GwtExceptionHelper.isUnauthenticated(exception)) {
							String module = GwtRegistryUtils.getWebConstants().getModuleAdmin();
							NavigationUtils.logout(module);
						} else {
							MessageUtils.displayError(exception);
						}
					}
					@Override
					public void onSuccess(Void nothing) {
						String module = GwtRegistryUtils.getWebConstants().getModuleAdmin();
						NavigationUtils.logout(module);
					}
				});
			}
		});

		usernameAndLogoutPanel.add(usernameLabel);
		usernameAndLogoutPanel.add(logoutButton);		
		
		userAndLanguagePanel.add(usernameAndLogoutPanel);
		userAndLanguagePanel.add(languagePanel);
	
		TableData usernameAndLogoutPanelTableData = new TableData();
		usernameAndLogoutPanelTableData.setWidth("20%");
		usernameAndLogoutPanelTableData.setHeight("10%");
		usernameAndLogoutPanelTableData.setHorizontalAlign(HorizontalAlignment.LEFT);
		usernameAndLogoutPanelTableData.setVerticalAlign(VerticalAlignment.MIDDLE);
		usernameAndLogoutPanelTableData.setStyleName("admin-header");
		topPanel.add(userAndLanguagePanel, usernameAndLogoutPanelTableData);
	}

	private void initCustomerLogoPanel() {
		customerLogoPanel = new VerticalPanel();
		customerLogoPanel.setHorizontalAlign(HorizontalAlignment.RIGHT);
		customerLogoPanel.setVerticalAlign(VerticalAlignment.TOP);

		clientLogoImage = new Image();
		clientLogoImage.setUrl(GWT.getModuleBaseURL() + "images/gray/interfata/CustomerLogo.png");


		customerLogoPanel.add(clientLogoImage);
		
		TableData logoAndLanguagePanelTableData = new TableData();
		logoAndLanguagePanelTableData.setWidth("15%");
		logoAndLanguagePanelTableData.setHeight("10%");
		logoAndLanguagePanelTableData.setHorizontalAlign(HorizontalAlignment.CENTER);
		logoAndLanguagePanelTableData.setVerticalAlign(VerticalAlignment.MIDDLE);
		logoAndLanguagePanelTableData.setStyleName("admin-header");
		topPanel.add(customerLogoPanel, logoAndLanguagePanelTableData);
	}
	
	private void initLeftPanel() {

		ContentPanel leftCP = new ContentPanel();
		leftCP.setBodyBorder(false);
		leftCP.setBorders(false);
		
		leftPanel = new VerticalPanel();
		leftPanel.setSpacing(0);
		leftPanel.setBorders(true);
		leftPanel.setStyleName("left_panel");
		leftPanel.setId("leftPanel");
		

		int buttonHeight = 40;
		int buttonWidth = 200;

		organizationalStructureButton = new Button();
		organizationalStructureButton.setSize(buttonWidth, buttonHeight);
		organizationalStructureButton.setText(GwtLocaleProvider.getConstants().ORGANIZATIONAL_STRUCTURE());
		organizationalStructureButton.setStyleName("nav_main");
		organizationalStructureButton.setBorders(true);
		organizationalStructureButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				dispatcher.goToOrganization();
			}
		});

		userGroupsButton = new Button();
		userGroupsButton.setSize(buttonWidth, buttonHeight);
		userGroupsButton.setText(GwtLocaleProvider.getConstants().USER_GROUPS());
		userGroupsButton.setStyleName("nav_main");
		userGroupsButton.setBorders(true);
		userGroupsButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				dispatcher.goToGroups();
			}
		});

		contentButton = new Button();
		contentButton.setSize(buttonWidth, buttonHeight);
		contentButton.setText(GwtLocaleProvider.getConstants().CONTENT());
		contentButton.setStyleName("nav_main");
		contentButton.setBorders(true);
		contentButton.setIconStyle("icon_nav_open");
		contentButton.setIconAlign(IconAlign.RIGHT);
		contentButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				contentButtonPanel.setVisible(!contentButtonPanel.isVisible());
				contentButton.setIconStyle(contentButtonPanel.isVisible() ? "icon_nav_close" : "icon_nav_open");
			};
		});
		
		
		initContentButtonPanel();

		workflowsButton = new Button();
		workflowsButton.setSize(buttonWidth, buttonHeight);
		workflowsButton.setText(GwtLocaleProvider.getConstants().WORKFLOWS());
		workflowsButton.setStyleName("nav_main");
		workflowsButton.setBorders(true);
		workflowsButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				dispatcher.goToWorkflows();
			}
		});

		replacementProfilesButton = new Button();
		replacementProfilesButton.setSize(buttonWidth, buttonHeight);
		replacementProfilesButton.setText(GwtLocaleProvider.getConstants().REPLACEMENT_PROFILES());
		replacementProfilesButton.setStyleName("nav_main");
		replacementProfilesButton.setBorders(true);
		replacementProfilesButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				dispatcher.goToReplacementProfiles();
			}
		});
		
		archivingProfilesButton = new Button();
		archivingProfilesButton.setSize(buttonWidth, buttonHeight);
		archivingProfilesButton.setText(GwtLocaleProvider.getConstants().ARCHIVING_PROFILES());
		archivingProfilesButton.setStyleName("nav_main");
		archivingProfilesButton.setBorders(true);
		archivingProfilesButton.setEnabled(false); // linia se va scoate in momentul in care se dezvolta modulul
		
		auditButton = new Button();
		auditButton.setSize(buttonWidth, buttonHeight);
		auditButton.setText(GwtLocaleProvider.getConstants().AUDIT());
		auditButton.setStyleName("nav_main");
		auditButton.setBorders(true);
		auditButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				dispatcher.goToAudit();
			}
		});
		
		logButton = new Button();
		logButton.setSize(buttonWidth, buttonHeight);
		logButton.setText(GwtLocaleProvider.getConstants().HISTORY_AND_ERRORS());
		logButton.setStyleName("nav_main");
		logButton.setBorders(true);
		logButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				dispatcher.goToLog();
			}
		});

		helpButton = new Button();
		helpButton.setSize(buttonWidth, buttonHeight);
		helpButton.setText(GwtLocaleProvider.getConstants().HELP());
		helpButton.setStyleName("nav_main");
		helpButton.setBorders(true);
		helpButton.setEnabled(false); // linia se va scoate in momentul in care se dezvolta modulul
		
		leftPanel.add(organizationalStructureButton);
		leftPanel.add(userGroupsButton);
		leftPanel.add(contentButton);
		leftPanel.add(contentButtonPanel, new TableData(HorizontalAlignment.RIGHT, VerticalAlignment.TOP));
		leftPanel.add(workflowsButton);
		leftPanel.add(replacementProfilesButton);
		leftPanel.add(archivingProfilesButton);
		leftPanel.add(auditButton);
		leftPanel.add(logButton);
		leftPanel.add(helpButton);

		leftCP.add(leftPanel);
		leftCP.setHeading("CLOUD DOC ADMIN");
		leftCP.setStyleName("left_content");
		//leftCP.setBorders(false);
		
		viewport.add(leftCP, new BorderLayoutData(LayoutRegion.WEST, 200));
	}

	private void initContentButtonPanel() {
		contentButtonPanel = new VerticalPanel();
		contentButtonPanel.setHorizontalAlign(HorizontalAlignment.LEFT);
		contentButtonPanel.setBorders(true);
		contentButtonPanel.setId("content_button_panel");
		contentButtonPanel.setStyleName("nav_child_bg");
		contentButtonPanel.setSpacing(2);
		contentButtonPanel.setVisible(false);

		documentTypesButton = new Button();
		documentTypesButton.setStyleName("nav_child");
		documentTypesButton.setText(GwtLocaleProvider.getConstants().DOCUMENT_TYPES());
		documentTypesButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				dispatcher.goToDocumentTypes();
			}
		});

		mimeTypesButton = new Button();
		mimeTypesButton.setStyleName("nav_child");
		mimeTypesButton.setText(GwtLocaleProvider.getConstants().MIME_TYPES());
		mimeTypesButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				dispatcher.goToMimeTypes();
			}
		});
		
		contentButtonPanel.add(documentTypesButton);
		contentButtonPanel.add(mimeTypesButton);
	}

	private void initContentContainer() {
		contentContainer = new LayoutContainer();

		contentContainer.setLayout(new FitLayout());
		contentContainer.setId("contentContainer");
		contentContainer.setBorders(true);
		contentContainer.setStyleName("content_container");
				
		viewport.add(contentContainer, new BorderLayoutData(LayoutRegion.CENTER));
	}
}