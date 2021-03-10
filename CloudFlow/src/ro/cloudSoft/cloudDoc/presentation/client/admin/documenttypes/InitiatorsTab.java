package ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.PlainOrganizationalStructureEntitiesSelectionComponent;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

public class InitiatorsTab extends TabItem {

	private static final int ORIGINAL_WIDTH = 1024;
	private static final int ORIGINAL_HEIGHT = 680;

	private FormPanel topForm;
	private InitiatorSelectionComponent initiatorSelectionComponent = new InitiatorSelectionComponent();

	private CheckBox anyInitiatorCheckBox;

	public InitiatorsTab() {

		setText(GwtLocaleProvider.getConstants().INITIATORS());
		setLayout(new FlowLayout());
		
		initVertPanel();
		
		addListener(Events.Resize, new Listener<BoxComponentEvent>(){
			public void handleEvent(BoxComponentEvent be) {
				calculateResizePercentage(be.getWidth(), be.getHeight());				
			}			
		});
	}
	
	private void calculateResizePercentage(int w, int h){
		float widthP = (float)w / (float)ORIGINAL_WIDTH;
		float heightP = (float)h / (float)ORIGINAL_HEIGHT;		
		initiatorSelectionComponent.changeSize(widthP, heightP);
	}
	private void initVertPanel(){
		initTopForm();
		initEntitySelectionComponent();
		layout();
	}
	
	private void initTopForm() {
		topForm = new FormPanel();
		topForm.setBodyBorder(false);
		topForm.setHeaderVisible(false);
		topForm.setLabelWidth(150);

		anyInitiatorCheckBox = new CheckBox();
		anyInitiatorCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().ANY_INITIATOR_ALLOWED());
		anyInitiatorCheckBox.setBoxLabel("");
		anyInitiatorCheckBox.addListener(Events.Change, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent event) {
				if ((anyInitiatorCheckBox.getValue() != null) && (anyInitiatorCheckBox.getValue().equals(Boolean.TRUE))) {
					initiatorSelectionComponent.setEnabled(false);
				} else {
					initiatorSelectionComponent.setEnabled(true);
				}
			}
		});
		topForm.add(anyInitiatorCheckBox);
		add(topForm);
		layout();
	}

	private void initEntitySelectionComponent() {
		add(initiatorSelectionComponent);		
		layout();
	}

	private void reset() {
		// Reseteaza formularul.
		topForm.clear();
		// Reseteaza elementul de selectie entitati.
		initiatorSelectionComponent.reset();
	}

	public boolean isValid() {
		if ((anyInitiatorCheckBox.getValue() != null) && (anyInitiatorCheckBox.getValue().equals(Boolean.TRUE))) {
			return true;
		} else {
			List<ModelData> initiators = initiatorSelectionComponent.getSelectedEntities();
			if (GwtValidateUtils.hasElements(initiators)) {
				return true;
			} else {
				ErrorHelper.addError(GwtLocaleProvider.getMessages().NO_SELECTED_INITIATORS());
			}
		}
		return false;
	}

	public void prepareForAdd() {
		reset();
		initiatorSelectionComponent.populate(null);
	}

	public void prepareForEdit(DocumentTypeModel documentType) {
		reset();
		anyInitiatorCheckBox.setValue(documentType.isAllowAnyInitiator());
		initiatorSelectionComponent.populate(documentType.getInitiators());
	}

	public void populate(DocumentTypeModel documentType) {
		
		documentType.setAllowAnyInitiator(anyInitiatorCheckBox.getValue());

		List<OrganizationEntityModel> initiators = initiatorSelectionComponent.getSelectedEntitiesAsOrganizationEntities();
		documentType.setInitiators(initiators);
	}
}

class InitiatorSelectionComponent extends PlainOrganizationalStructureEntitiesSelectionComponent {

	private static final int LEFT_PANEL_ORIGINAL_WIDTH = 410;
	private static final int LEFT_PANEL_ORIGINAL_HEIGHT = 635;
	private static final int RIGHT_PANEL_ORIGINAL_WIDTH = 410;
	private static final int RIGHT_PANEL_ORIGINAL_HEIGHT = 635;
	private static final int BUTTONS_PANEL_ORIGINAL_WIDTH = 120;
	private static final int BUTTONS_PANEL_ORIGINAL_HEIGHT = 635;

	@Override
	public void changeSize(float wIndex, float hIndex){
		if (hIndex < 1){
			leftPanel.setSize(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(LEFT_PANEL_ORIGINAL_HEIGHT * hIndex)-45);
			if (wIndex<1)
				rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex-10), Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT* hIndex)-47);
			else
				rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT* hIndex)-47);		
		} else {
			leftPanel.setSize(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(LEFT_PANEL_ORIGINAL_HEIGHT * hIndex));
			rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT* hIndex));		
		}
		buttonsPanel.setSize(Math.round(BUTTONS_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(BUTTONS_PANEL_ORIGINAL_HEIGHT * hIndex));		
		layout();
	}
}