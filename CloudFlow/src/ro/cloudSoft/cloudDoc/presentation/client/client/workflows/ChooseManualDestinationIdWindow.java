package ro.cloudSoft.cloudDoc.presentation.client.client.workflows;

import ro.cloudSoft.cloudDoc.presentation.client.shared.OrganizationalStructureLabelProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.iconproviders.OrganizationalStructureIconProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.OrganizationalStructureTreeSorter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class ChooseManualDestinationIdWindow extends Window {

	private ContentPanel contentPanel;
	private OrganizationTreePanel organizationTreePanel;
	private Button okButton;
	
	public ChooseManualDestinationIdWindow() {
		setLayout(new FitLayout());
		setHeading(GwtLocaleProvider.getConstants().ORGANIZATIONAL_STRUCTURE());
		setSize(500, 400);
		setModal(true);
		setClosable(false);
		
		contentPanel = new ContentPanel();
		contentPanel.setLayout(new FitLayout());
		contentPanel.setHeaderVisible(false);		
		add(contentPanel);		
		
		initOrganizationTreePanel();
		initButtons();
		
		addListener(Events.Show, new Listener<BaseEvent>(){
			@Override
			public void handleEvent(BaseEvent be) {
				organizationTreePanel.refreshTree();
			}
		});
	}
	
	private void initOrganizationTreePanel() {		
		organizationTreePanel = new OrganizationTreePanel(this);
		contentPanel.add(organizationTreePanel);
	}
	
	private void initButtons() {
		okButton = new Button("  OK  ");
		okButton.setEnabled(false);
		okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				dispatch(organizationTreePanel.getSelectedItem());
			}
		});
		contentPanel.addButton(okButton);
	}
	
	public void dispatch(ModelData data) {
		if (data instanceof UserModel) {
			UserModel user = (UserModel)data;
			String destinationId = user.getUserId();
			onSendDestinationIdToWorkflow(destinationId);
			hide();
		}
	}
	
	public void changePerspective(ModelData data) {
		okButton.setEnabled(data instanceof UserModel);
	}
	
	public abstract void onSendDestinationIdToWorkflow(String destinationId);
	
}

class OrganizationTreePanel extends TreePanel<ModelData> {

	private ChooseManualDestinationIdWindow parentWindow;

	public OrganizationTreePanel(ChooseManualDestinationIdWindow parentWindow) {
		super(new TreeStore<ModelData>());
		
		this.parentWindow = parentWindow;
		
		store.setStoreSorter(new OrganizationalStructureTreeSorter());
		configureIcons();		
		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		setLabelProvider(new OrganizationalStructureLabelProvider());
		addItemExpandAction();
	}

	private void configureIcons() {
		setIconProvider(new OrganizationalStructureIconProvider(store));
	}
	
	@Override
	protected void onDoubleClick(@SuppressWarnings("rawtypes") TreePanelEvent tpe) {
		ModelData selectedItem = tpe.getItem();
		parentWindow.dispatch(selectedItem);
	}
		
	@Override
	protected void onClick(@SuppressWarnings("rawtypes") TreePanelEvent tpe) {		
		super.onClick(tpe);
		ModelData selectedItem = tpe.getItem();
		parentWindow.changePerspective(selectedItem);
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
				getStore().removeAll();
				getStore().add(organizationModel, true);	
				expandAll();
				LoadingManager.get().loadingComplete();
			}
		});
	}

	private void addItemExpandAction() {
		addListener(Events.Expand, new Listener<TreePanelEvent<ModelData>>() {
			@Override
			public void handleEvent(TreePanelEvent<ModelData> event) {
				getSelectionModel().select(event.getItem(), false);
			}
		});
	}

	public ModelData getSelectedItem() {
		return getSelectionModel().getSelectedItem();
	}
	
}
