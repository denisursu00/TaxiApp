package ro.cloudSoft.cloudDoc.presentation.client.admin;

import java.util.Collection;

import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithId;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtOrganizationalStructureBusinessUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.OrganizationalStructureTreePanel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MoveOrganizationEntityWindow extends Window {

	
	private ContentPanel contentPanel;
	
	private Text entityToMoveDisplayText;
	private CustomOrganizationalStructureTreePanel organizationalStructureTreePanel;
	
	private Button okButton;
	private Button cancelButton;
	
	
	private ModelData entityToMove;
	private ModelData parentOfEntityToMove;
	
	
	public MoveOrganizationEntityWindow() {

		setLayout(new FitLayout());
		setModal(true);
		setSize(500, 400);
		
		contentPanel = new ContentPanel();
		contentPanel.setLayout(new FitLayout());
		contentPanel.setHeaderVisible(false);		
		add(contentPanel);
		
		entityToMoveDisplayText = new Text();
		entityToMoveDisplayText.setStyleName("textNormal");
		setTopComponent(entityToMoveDisplayText);
		
		organizationalStructureTreePanel = new CustomOrganizationalStructureTreePanel();
		contentPanel.add(organizationalStructureTreePanel);
		
		okButton = new Button();
		okButton.setText("OK");
		okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				
				ModelData destinationEntity = organizationalStructureTreePanel.getSelectionModel().getSelectedItem();
				if (destinationEntity == null) {
					return;
				}
				
				Collection<ModelData> ancestorsOfDestinationEntity = ComponentUtils.getAncestors(organizationalStructureTreePanel.getStore(), destinationEntity);
				boolean canMove = GwtOrganizationalStructureBusinessUtils.canMoveTo((ModelWithId) entityToMove,
					(ModelWithId) destinationEntity, (ModelWithId) parentOfEntityToMove, ancestorsOfDestinationEntity);
				if (!canMove) {
					return;
				}
				
				LoadingManager.get().loading();
				GwtServiceProvider.getOrgService().move(entityToMove, destinationEntity, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(Void nothing) {
						
						String message = null;
						if (entityToMove instanceof UserModel) {
							message = GwtLocaleProvider.getMessages().USER_MOVED();
						} else if (entityToMove instanceof OrganizationUnitModel) {
							message = GwtLocaleProvider.getMessages().ORG_UNIT_MOVED();
						} else {
							throw new IllegalStateException("S-a mutat o entitate necunoscuta: [" + entityToMove.getClass().getName() + "].");
						}
						MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), message);
						
						AppEventController.fireEvent(AppEventType.OrganizationalStructure);
						hide();
						LoadingManager.get().loadingComplete();
					}
				});
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
	
	public void prepareForMove(ModelData entityToMove, ModelData parentOfEntityToMove, String displayNameOfEntityToMove) {
		
		this.entityToMove = entityToMove;
		this.parentOfEntityToMove = parentOfEntityToMove;
		
		updateWindowTitle(displayNameOfEntityToMove);
		show();
		organizationalStructureTreePanel.refreshTree();
	}
	
	/**
	 * Actualizeaza titlul ferestrei, adaugand numele entitatii ce se doreste a fi mutata.
	 */
	private void updateWindowTitle(String displayNameOfEntityToMove) {
		String windowTitle = (GwtLocaleProvider.getConstants().MOVE() + " " + displayNameOfEntityToMove);
		setHeading(windowTitle);
	}
	
	private static class CustomOrganizationalStructureTreePanel extends OrganizationalStructureTreePanel {
		
		/**
		 * Permite adaugarea doar de entitati care pot fi alese ca destinatie in mutarea unei entitati organizatorice.
		 */
		@Override
		protected boolean isModelAllowed(ModelData model) {
			return GwtOrganizationalStructureBusinessUtils.isDestinationEntityForMoveTheRightType(model);
		}
	}
}