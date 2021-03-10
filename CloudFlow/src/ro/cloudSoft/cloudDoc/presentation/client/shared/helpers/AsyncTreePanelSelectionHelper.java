package ro.cloudSoft.cloudDoc.presentation.client.shared.helpers;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtCompareUtils;

import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeLoadEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;

/**
 * Clasa ajutatoare pentru selectarea unui element dintr-un arbore asincron
 * 
 * Deoarece arborele este asincron, ascendentii elementului de selectat nu sunt incarcati.
 * Asadar, trebuie incarcati pe rand toti ascendentii elementului de selectat; apoi, dupa ce s-au incarcat toti, se va selecta elementul.
 * 
 * ATENTIE: Arborele trebuie sa NU se incarce de mai multe ori decat necesar pentru ca loader-ul sa functioneze corect.
 */
public class AsyncTreePanelSelectionHelper {

	private final TreePanel<ModelData> treePanel;
	
	public AsyncTreePanelSelectionHelper(TreePanel<ModelData> treePanel) {
		this.treePanel = treePanel;
	}
	
	/**
	 * Incarca arborele, selectand un nod (care poate fi "ascuns" adang in arbore).
	 * Daca ierarhia elementului de selectat este goala, atunci nu va fi selectat nici un nod.
	 * Daca nu se gaseste un nod din ierarhie, atunci nu va fi selectat nici un nod.
	 * 
	 * @param hierarchyForNodeToSelect o lista cu ierarhia nodului de selectat, in care primul element este un nod din radacina arborelui,
	 * ascendent al nodului de selectat, ultimul nod este chiar nodul de selectat, iar intre ele sunt nodurile intermediare, ascendente ale nodului de selectat
	 * @param doAfterLoad ce sa ruleze dupa ce s-a apelat incarcarea arborelui.
	 */
	public void load(final List<TreeNodeInHierarchy> hierarchyForNodeToSelect, Runnable doAfterLoad) {
		if (!hierarchyForNodeToSelect.isEmpty()) {
			treePanel.getStore().getLoader().addListener(Loader.Load, new Listener<TreeLoadEvent>() {
				
				@Override
				public void handleEvent(TreeLoadEvent e) {

					List<ModelData> childModelsLoadedInTree = e.getData();
					
					TreeNodeInHierarchy nextNodeInHierarchy = hierarchyForNodeToSelect.get(0);
					
					boolean nextNodeFound = false;
					
					for (ModelData childModelLoadedInTree : childModelsLoadedInTree) {
						
						boolean isTheRightType = isTheRightType(childModelLoadedInTree, nextNodeInHierarchy);
						boolean hasSameIdentifier = hasSameIdentifier(childModelLoadedInTree, nextNodeInHierarchy);
						
						if (isTheRightType && hasSameIdentifier) {
							
							nextNodeFound = true;

							boolean isNodeTheOneToSelect = (hierarchyForNodeToSelect.size() == 1);
							if (isNodeTheOneToSelect) {
								
								treePanel.getSelectionModel().select(childModelLoadedInTree, false);
								treePanel.scrollIntoView(childModelLoadedInTree);

								treePanel.getStore().getLoader().removeListener(Loader.Load, this);
							} else {
								hierarchyForNodeToSelect.remove(0);
								treePanel.getStore().getLoader().loadChildren(childModelLoadedInTree);
							}
							
							break;
						}
					}
					
					if (!nextNodeFound) {
						treePanel.getStore().getLoader().removeListener(Loader.Load, this);
						doWhenNodeInHierarchyNotFoundInTree();
					}
				}
			});	
		}
		
		treePanel.getStore().getLoader().load();
		doAfterLoad.run();
	}
	
	private static boolean isTheRightType(ModelData model, TreeNodeInHierarchy node) {
		return model.getClass().getName().equals(node.getNodeTypeClass().getName());
	}
	
	private static boolean hasSameIdentifier(ModelData model, TreeNodeInHierarchy node) {
		
		Object modelIdentifier = model.get(node.getIdentifierPropertyName());
		Object nodeIdentifier = node.getIdentifierValue();
		
		return GwtCompareUtils.areEqual(modelIdentifier, nodeIdentifier);
	}
	
	protected void doWhenNodeInHierarchyNotFoundInTree() {
		// Nu face nimic. Pentru a customiza, se va suprascrie metoda.
	}

	@SuppressWarnings("rawtypes")
	public static class TreeNodeInHierarchy {
		
		private final Class nodeTypeClass;
		private final String identifierPropertyName;
		private final Object identifierValue;
		
		public TreeNodeInHierarchy(Class nodeTypeClass, String identifierPropertyName, Object identifierValue) {
			this.nodeTypeClass = nodeTypeClass;
			this.identifierPropertyName = identifierPropertyName;
			this.identifierValue = identifierValue;
		}

		public Class getNodeTypeClass() {
			return nodeTypeClass;
		}
		
		public String getIdentifierPropertyName() {
			return identifierPropertyName;
		}
		
		public Object getIdentifierValue() {
			return identifierValue;
		}
	}
}