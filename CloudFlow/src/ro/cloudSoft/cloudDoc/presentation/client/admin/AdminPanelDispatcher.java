package ro.cloudSoft.cloudDoc.presentation.client.admin;


import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.google.gwt.user.client.ui.Widget;

public class AdminPanelDispatcher {
	
	private static final String IS_ADMIN_PANEL_DATA_NAME = "isAdminPanel";
	private static final String IS_ADMIN_PANEL_DATA_VALUE = "yes";
	
	private final LayoutContainer container;
	
	private OrganizationPanel organizationPanel;
	private GroupsPanel groupsPanel;
	private DocumentTypesPanel documentTypesPanel;
	private MimeTypesPanel mimeTypesPanel;
	private WorkflowsPanel workflowsPanel;
	private ReplacementProfilesPanel replacementProfilesPanel;
	private AuditPanel auditPanel;
	private LogPanel logPanel;
	
	public AdminPanelDispatcher(LayoutContainer adminPanelContainer) {
		container = adminPanelContainer;
		container.setData(IS_ADMIN_PANEL_DATA_NAME, IS_ADMIN_PANEL_DATA_VALUE);
		initPanels();
	}
	
	/** Verifica daca panoul specificat este cel activ din panoul principal de administrare. */
	public static boolean isActivePanel(ContentPanel panel) {
		
		Widget parent = panel.getParent();
		
		if (!(parent instanceof LayoutContainer)) {
			return false;
		}		
		LayoutContainer parentContainer = (LayoutContainer) parent;
		
		Object isAdminPanelDataValue = parentContainer.getData(IS_ADMIN_PANEL_DATA_NAME);
		if (isAdminPanelDataValue == null) {
			return false;
		}
		return isAdminPanelDataValue.equals(IS_ADMIN_PANEL_DATA_VALUE);
	}
	
	private void initPanels() {
		organizationPanel = new OrganizationPanel();
		groupsPanel = new GroupsPanel();
		documentTypesPanel = new DocumentTypesPanel();
		mimeTypesPanel = new MimeTypesPanel();
		workflowsPanel = new WorkflowsPanel();
		replacementProfilesPanel = new ReplacementProfilesPanel();
		auditPanel = new AuditPanel();
		logPanel = new LogPanel();
	}

	private void goToPanel(ContentPanel panel) {
		container.removeAll();
		container.add(panel, new FitData(0, 0, 0, 0));
		container.layout();
	}
	
	public void goToOrganization() {
		organizationPanel.setBorders(false);
		goToPanel(organizationPanel);
		
	}	
	public void goToGroups() {
		goToPanel(groupsPanel);
	}	
	public void goToDocumentTypes() {
		goToPanel(documentTypesPanel);
	}	
	public void goToMimeTypes() {
		goToPanel(mimeTypesPanel);
	}
	public void goToWorkflows() {
		goToPanel(workflowsPanel);
	}
	public void goToReplacementProfiles() {
		goToPanel(replacementProfilesPanel);
	}
	public void goToAudit() {
		goToPanel(auditPanel);
	}
	public void goToLog() {
		goToPanel(logPanel);
	}
}