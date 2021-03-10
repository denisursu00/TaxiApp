package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;

import ro.cloudSoft.cloudDoc.presentation.client.shared.OrganizationalStructureLabelProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.iconproviders.OrganizationalStructureIconProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.sorters.OrganizationalStructureTreeSorter;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Reprezinta un arbore cu structura organizatorica.
 */
public class OrganizationalStructureTreePanel extends TreePanel<ModelData> {

	public OrganizationalStructureTreePanel() {
		
		super(new TreeStore<ModelData>());

		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		getStore().setStoreSorter(new OrganizationalStructureTreeSorter());
		setIconProvider(new OrganizationalStructureIconProvider(store));
		setLabelProvider(new OrganizationalStructureLabelProvider());
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
				addModelAndChildrenToStore(null, organizationModel);
				
				expandAll();
				doAfterOrganizationalStructureLoadingComplete();
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private void addModelAndChildrenToStore(ModelData parentModel, ModelData model) {
		
		if (!isModelAllowed(model)) {
			return;
		}
		
		if (parentModel != null) {
			getStore().add(parentModel, model, false);
		} else {
			getStore().add(model, false);
		}
		
		if (model instanceof TreeModel) {
			TreeModel treeModel = (TreeModel) model;
			for (ModelData childModel : treeModel.getChildren()) {
				addModelAndChildrenToStore(model, childModel);
			}
		}
	}
	
	/**
	 * Verifica daca este permisa adaugarea modelului in arbore.
	 * Subclasele pot modifica comportamentul a.i. sa restrictioneze elementele afisate in arbore.
	 */
	protected boolean isModelAllowed(ModelData model) {
		return true;
	}
	
	protected void doAfterOrganizationalStructureLoadingComplete() {
	}
}