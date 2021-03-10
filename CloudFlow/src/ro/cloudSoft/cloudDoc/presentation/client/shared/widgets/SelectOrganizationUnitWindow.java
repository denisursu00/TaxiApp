package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;


import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class SelectOrganizationUnitWindow extends Window {

	
	private ContentPanel contentPanel;
	
	private CustomOrganizationalStructureTreePanel organizationalStructureTreePanel;
	
	private Button okButton;
	private Button cancelButton;
	
	private final OrganizationUnitPopupSelectionField organizationUnitSelectionField;
	
	public SelectOrganizationUnitWindow(OrganizationUnitPopupSelectionField organizationUnitSelectionField) {
		
		this.organizationUnitSelectionField = organizationUnitSelectionField;
		
		setLayout(new FitLayout());
		setModal(true);
		setSize(500, 400);
		
		contentPanel = new ContentPanel();
		contentPanel.setLayout(new FitLayout());
		contentPanel.setHeaderVisible(false);		
		add(contentPanel);
		
		organizationalStructureTreePanel = new CustomOrganizationalStructureTreePanel(organizationUnitSelectionField);
		contentPanel.add(organizationalStructureTreePanel);
		
		okButton = new Button();
		okButton.setText("OK");
		okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				
				ModelData selectedEntity = organizationalStructureTreePanel.getSelectionModel().getSelectedItem();
				if ((selectedEntity == null) || !(selectedEntity instanceof OrganizationUnitModel)) {
					return;
				}
				OrganizationUnitModel selectedOU = (OrganizationUnitModel)selectedEntity;
				SelectOrganizationUnitWindow.this.organizationUnitSelectionField.setSelectedOrganizationUnit(selectedOU.getId(), selectedOU.getName());
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
	
	public void prepareForSelection()
	{
		show();
		organizationalStructureTreePanel.refreshTree();
		
	}
	
	private static class CustomOrganizationalStructureTreePanel extends OrganizationalStructureTreePanel {
		
		private final OrganizationUnitPopupSelectionField organizationUnitSelectionField;
		
		public CustomOrganizationalStructureTreePanel(OrganizationUnitPopupSelectionField ouSelectionField) {
			organizationUnitSelectionField = ouSelectionField;
		}
		
		@Override
		protected boolean isModelAllowed(ModelData model) {
			return ((model instanceof OrganizationModel) || (model instanceof OrganizationUnitModel));
		}
		
		@Override
		protected void doAfterOrganizationalStructureLoadingComplete() {
			
			getSelectionModel().deselectAll();
			
			String selectedOrganizationUnitId = organizationUnitSelectionField.getSelectedOrganizationUnitId();
			if (selectedOrganizationUnitId == null) {
				return;
			}
			
			ModelData selectedOrganizationUnitAsModel = getStore().findModel(OrganizationUnitModel.PROPERTY_ID, selectedOrganizationUnitId);
			if (selectedOrganizationUnitAsModel == null) {
				// Unitatea organizatorica selectata nu mai exista in arbore, deci nu selectez nimic.
				return;
			}
			if (!(selectedOrganizationUnitAsModel instanceof OrganizationUnitModel)) {
				String exceptionMessage = "S-a gasit o entitate cu ID-ul unei unitati organizatorice " +
					"(" + selectedOrganizationUnitId + "), insa NU este unitate organizatorica!";
				throw new IllegalStateException(exceptionMessage);
			}

			getSelectionModel().select(selectedOrganizationUnitAsModel, false);
			this.scrollIntoView(selectedOrganizationUnitAsModel);
			
		}
	}
}
