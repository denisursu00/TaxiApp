package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets;


import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class OrganizationUnitPopupSelectionField extends AdapterField {
	
	private static final Widget DUMMY_WIDGET = new Widget();
	
	private SelectOrganizationUnitWindow selectOrganizationUnitWindow;
	
	private HorizontalPanel horizontalPanel;
	
	private Text selectedOrganizationUnitText;
	
	private Button selectButton;
	
	private String selectedOrganizationUnitId;
	
	public OrganizationUnitPopupSelectionField() {
		
		super(DUMMY_WIDGET);
		
		selectOrganizationUnitWindow = new SelectOrganizationUnitWindow(this);
		
		horizontalPanel = new HorizontalPanel();
		
		TableData tableData = new TableData();
		tableData.setHorizontalAlign(HorizontalAlignment.LEFT);
		tableData.setVerticalAlign(VerticalAlignment.MIDDLE);
		tableData.setPadding(5);
		
		selectedOrganizationUnitText = new Text();
		selectedOrganizationUnitText.setStyleName("textNormal");
		selectedOrganizationUnitText.setText("");
		horizontalPanel.add(this.selectedOrganizationUnitText, tableData);
		
		selectButton = new Button();
		selectButton.setText(GwtLocaleProvider.getConstants().SELECT());
		selectButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent be) {
				Window parentWindow = ComponentUtils.getParentWindow(OrganizationUnitPopupSelectionField.this);
				if (parentWindow != null) {
					parentWindow.toBack();
				}
				selectOrganizationUnitWindow.prepareForSelection();
			}
		});
		horizontalPanel.add(this.selectButton, tableData);
		
		widget = horizontalPanel;
	}
	
	@Override
	public boolean isValid(boolean param) {
		/*
		 * Metoda a trebuit suprascrisa intrucat AdapterField returneaza
		 * intotdeauna true.
		 */
		return validateValue(null);
	}
	
	@Override
	protected boolean validateValue(String param) {
		if (!isVisible()) {
			clearInvalid();
			return true;
		}
		if (selectedOrganizationUnitId == null){
			markInvalid(GwtLocaleProvider.getMessages().VALIDATOR_MANDATORY_FIELD());
			return false;
		}
		
		clearInvalid();
		return true;
	}
	
	public void resetValue() {
		selectedOrganizationUnitId = null;
		selectedOrganizationUnitText.setText("");
		
	}
	
	public String getSelectedOrganizationUnitId() {
		return selectedOrganizationUnitId;
	}

	public void setSelectedOrganizationUnitId(String selectedOrganizationUnitId) {
		this.selectedOrganizationUnitId = selectedOrganizationUnitId;
		
		LoadingManager.get().loading();
		
		GwtServiceProvider.getOrgService().getOrgUnitById(selectedOrganizationUnitId, new AsyncCallback<OrganizationUnitModel>(){
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(OrganizationUnitModel organizationUnit) {
				selectedOrganizationUnitText.setText(organizationUnit.getName());
				LoadingManager.get().loadingComplete();
			}
		});
		
		this.clearInvalid();
	}

	public void setSelectedOrganizationUnit(String selectedOrganizationUnitId, String selectedOrganizationUnitText) {
		this.selectedOrganizationUnitId = selectedOrganizationUnitId;
		this.selectedOrganizationUnitText.setText(selectedOrganizationUnitText);
		clearInvalid();
		
	}
	
}
