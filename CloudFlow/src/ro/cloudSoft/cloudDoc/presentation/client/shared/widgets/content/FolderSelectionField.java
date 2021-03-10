package ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.CustomAdapterField;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.CustomText;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;

public class FolderSelectionField extends CustomAdapterField {
	
	private static final String TEXT_WHEN_NO_FOLDER_SELECTED = "(N/A)";
	
	private FolderSelectionWindow selectionWindow;
	
	private HorizontalPanel horizontalPanel;
	
	private VerticalPanel selectedFolderTextVerticalPanel;
	
	private CustomText nameOfParentDocumentLocationOfSelectedFolderText;
	private CustomText nameOfSelectedFolderText;
	
	private Button selectButton;
	private Button resetButton;
	
	private String parentDocumentLocationRealNameOfSelectedFolder;
	private String idOfSelectedFolder;
	
	public FolderSelectionField() {
		
		super(ComponentUtils.DUMMY_WIDGET);
		
		selectionWindow = new FolderSelectionWindow(this);
		
		horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlign(HorizontalAlignment.LEFT);
		horizontalPanel.setVerticalAlign(VerticalAlignment.MIDDLE);
		horizontalPanel.setSpacing(10);
		
		selectedFolderTextVerticalPanel = new VerticalPanel();
		horizontalPanel.add(selectedFolderTextVerticalPanel);
		
		nameOfParentDocumentLocationOfSelectedFolderText = new CustomText();
		selectedFolderTextVerticalPanel.add(nameOfParentDocumentLocationOfSelectedFolderText);
		
		nameOfSelectedFolderText = new CustomText();
		selectedFolderTextVerticalPanel.add(nameOfSelectedFolderText);
		
		updateSelectedFolderTexts(TEXT_WHEN_NO_FOLDER_SELECTED, TEXT_WHEN_NO_FOLDER_SELECTED);
		
		selectButton = new Button();
		selectButton.setText(GwtLocaleProvider.getConstants().SELECT());
		selectButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent be) {
				Window parentWindow = ComponentUtils.getParentWindow(FolderSelectionField.this);
				if (parentWindow != null) {
					parentWindow.toBack();
				}
				selectionWindow.prepareForSelection();
			}
		});
		horizontalPanel.add(selectButton);
		
		resetButton = new Button();
		resetButton.setText(GwtLocaleProvider.getConstants().RESET());
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				resetSelectedFolder();
			}
		});
		horizontalPanel.add(resetButton);
		
		widget = horizontalPanel;
	}
	
	@Override
	protected boolean validateCustomAdapterField() {
		return true;
	}
	
	public void setSelectedFolder(DocumentLocationModel parentDocumentLocation, FolderModel folder) {
		
		parentDocumentLocationRealNameOfSelectedFolder = parentDocumentLocation.getRealName();
		idOfSelectedFolder = folder.getId();

		updateSelectedFolderTexts(parentDocumentLocation.getName(), folder.getName());
	}
	
	public void resetSelectedFolder() {

		parentDocumentLocationRealNameOfSelectedFolder = null;
		idOfSelectedFolder = null;
		
		updateSelectedFolderTexts(TEXT_WHEN_NO_FOLDER_SELECTED, TEXT_WHEN_NO_FOLDER_SELECTED);
	}
	
	private void updateSelectedFolderTexts(String nameOfParentDocumentLocation, String nameOfSelectedFolder) {
		nameOfParentDocumentLocationOfSelectedFolderText.setText(GwtLocaleProvider.getConstants().WORKSPACE() + ": " + nameOfParentDocumentLocation);
		nameOfSelectedFolderText.setText(GwtLocaleProvider.getConstants().FOLDER() + ": " + nameOfSelectedFolder);
	}
	
	public String getParentDocumentLocationRealNameOfSelectedFolder() {
		return parentDocumentLocationRealNameOfSelectedFolder;
	}
	
	public String getIdOfSelectedFolder() {
		return idOfSelectedFolder;
	}
}