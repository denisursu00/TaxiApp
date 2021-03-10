package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.dataProxy.DocumentLocationAndFolderTreeDataProxy;
import ro.cloudSoft.cloudDoc.presentation.client.shared.helpers.AsyncTreePanelSelectionHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.helpers.AsyncTreePanelSelectionHelper.TreeNodeInHierarchy;
import ro.cloudSoft.cloudDoc.presentation.client.shared.iconproviders.DocumentLocationAndFolderIconProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.labelProviders.DocumentLocationAndFolderLabelProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.loaders.CustomBaseTreeLoader;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FolderSelectionWindow extends Window {
	
	private final FolderSelectionField field;

	private TreePanel<ModelData> treePanel;
	
	private AsyncTreePanelSelectionHelper asyncTreePanelSelectionHelper;
	
	private Button okButton;
	private Button cancelButton;
	
	public FolderSelectionWindow(FolderSelectionField field) {
		
		this.field = field;
		
		setHeading(GwtLocaleProvider.getConstants().FOLDER());
		setClosable(false);
		setMaximizable(false);
		setMinimizable(false);
		setModal(true);
		setSize(500, 400);
		setLayout(new FitLayout());

		setBodyStyle("background-color: white");
		
		initTreePanel();
		initButtons();
	}
	
	private void initTreePanel() {

		RpcProxy<List<ModelData>> proxy = new DocumentLocationAndFolderTreeDataProxy();
		TreeLoader<ModelData> treeLoader = new CustomBaseTreeLoader(proxy);
		TreeStore<ModelData> treeStore = new TreeStore<ModelData>(treeLoader);
		
		treePanel = new TreePanel<ModelData>(treeStore) {
			
			@Override
			protected void onRender(Element target, int index) {
				
				/*
				 * La render, daca nu a mai fost incarcat arborele, se va incarca automat.
				 * Avand in vedere ca noi vom incarca manual arborele si ca avem cod cu logica la evenimentele de incarcare
				 * (pentru incarcarea folder-ului selectat), NU vrem sa se incarce arborele altcandva decat atunci cand cerem noi.
				 * 
				 * Pentru aceasta, vom inlocui temporar loader-ul pentru ca handler-ul de render sa nu il foloseasca pe cel real.
				 */
				
				TreeLoader<ModelData> oldLoader = loader;
				loader = null;
				
				super.onRender(target, index);
				
				loader = oldLoader;
			}
		};
		treePanel.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		treePanel.setLabelProvider(new DocumentLocationAndFolderLabelProvider());
		treePanel.setIconProvider(new DocumentLocationAndFolderIconProvider());
		
		asyncTreePanelSelectionHelper = new AsyncTreePanelSelectionHelper(treePanel);
		
		add(treePanel);
	}
	
	private void initButtons() {

		okButton = new Button();
		okButton.setText("OK");
		okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				
				FolderModel selectedFolder = getSelectedFolder();
				if (selectedFolder == null) {
					return;
				}
				
				DocumentLocationModel parentDocumentLocation = getParentDocumentLocation(selectedFolder);

				field.setSelectedFolder(parentDocumentLocation, selectedFolder);
				
				hide();
			}
		});
		getButtonBar().add(okButton);

		cancelButton = new Button();
		cancelButton.setText(GwtLocaleProvider.getConstants().CANCEL());
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				hide();
			}
		});
		getButtonBar().add(cancelButton);
	}
	
	private DocumentLocationModel getParentDocumentLocation(ModelData modelInTree) {
		ModelData currentModelInTree = modelInTree;
		while (currentModelInTree != null) {
			if (currentModelInTree instanceof DocumentLocationModel) {
				return ((DocumentLocationModel) currentModelInTree);
			}
			currentModelInTree = treePanel.getStore().getParent(currentModelInTree);
		}
		return null;
	}
	
	private FolderModel getSelectedFolder() {
		ModelData selectedModel = treePanel.getSelectionModel().getSelectedItem();
		if (selectedModel instanceof FolderModel) {
			return ((FolderModel) selectedModel);
		}
		return null;
	}
	
	public void prepareForSelection() {
		
		final Runnable showAfterLoad = new Runnable() {
			
			@Override
			public void run() {
				show();
				toFront();
			}
		};
		
		final String parentDocumentLocationRealNameOfSelectedFolder = field.getParentDocumentLocationRealNameOfSelectedFolder();
		final String idOfSelectedFolder = field.getIdOfSelectedFolder();
		
		boolean isFolderSelected = ((parentDocumentLocationRealNameOfSelectedFolder != null) || (idOfSelectedFolder != null));
		if (isFolderSelected) {
			LoadingManager.get().loading();
			GwtServiceProvider.getFolderService().getIdsForFolderHierarchy(parentDocumentLocationRealNameOfSelectedFolder, idOfSelectedFolder, new AsyncCallback<List<String>>() {
				
				@Override
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				
				@Override
				public void onSuccess(final List<String> idsForFolderHierarchy) {
					
					List<TreeNodeInHierarchy> treeNodeHierarchyForSelection = buildTreeNodeHierarchyForSelection(parentDocumentLocationRealNameOfSelectedFolder, idOfSelectedFolder, idsForFolderHierarchy);
					asyncTreePanelSelectionHelper.load(treeNodeHierarchyForSelection, showAfterLoad);
					
					LoadingManager.get().loadingComplete();
				}
			});
		} else {
			asyncTreePanelSelectionHelper.load(Collections.<TreeNodeInHierarchy>emptyList(), showAfterLoad);
		}
	}
	
	private List<TreeNodeInHierarchy> buildTreeNodeHierarchyForSelection(String parentDocumentLocationRealNameOfSelectedFolder, String idOfSelectedFolder, List<String> idsForFolderHierarchy) {
		
		List<TreeNodeInHierarchy> treeNodeHierarchy = new ArrayList<TreeNodeInHierarchy>();
		
		TreeNodeInHierarchy nodeForParentDocumentLocation = new TreeNodeInHierarchy(DocumentLocationModel.class, DocumentLocationModel.PROPERTY_REAL_NAME, parentDocumentLocationRealNameOfSelectedFolder);
		treeNodeHierarchy.add(nodeForParentDocumentLocation);
		
		for (String idOfIntermediateFolder : idsForFolderHierarchy) {
			TreeNodeInHierarchy nodeForIntermediateFolder = new TreeNodeInHierarchy(FolderModel.class, FolderModel.PROPERTY_ID, idOfIntermediateFolder);
			treeNodeHierarchy.add(nodeForIntermediateFolder);
		}
		
		TreeNodeInHierarchy nodeForSelectedFolder = new TreeNodeInHierarchy(FolderModel.class, FolderModel.PROPERTY_ID, idOfSelectedFolder);
		treeNodeHierarchy.add(nodeForSelectedFolder);
		
		return treeNodeHierarchy;
	}
}