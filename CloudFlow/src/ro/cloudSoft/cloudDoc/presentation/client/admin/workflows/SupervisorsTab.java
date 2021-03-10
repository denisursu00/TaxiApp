package ro.cloudSoft.cloudDoc.presentation.client.admin.workflows;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.PlainOrganizationalStructureEntitiesSelectionComponent;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class SupervisorsTab extends WorkflowAbstractTab {
	
	private SupervisorsSelectionComponent supervisorsSelectionComponent = new SupervisorsSelectionComponent();

	public SupervisorsTab() {		
		setText(GwtLocaleProvider.getConstants().SUPERVISORS());
		setLayout(new FitLayout());
		initSupervisorsSelectionComponent();
		addListener(Events.Resize, new Listener<BoxComponentEvent>(){
			public void handleEvent(BoxComponentEvent be) {
				calculateResizePercentage(be.getWidth(), be.getHeight());
			}			
		});
	}
	
	private void initSupervisorsSelectionComponent() {
		add(supervisorsSelectionComponent);
	}
	
	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void reset() {
		// Reseteaza elementul de selectie supervizori.
		supervisorsSelectionComponent.reset();
	}

	@Override
	public void prepareForAdd() {
		reset();

		supervisorsSelectionComponent.populate(null);
	}

	@Override
	public void prepareForEdit(WorkflowModel workflowModel) {
		reset();

		supervisorsSelectionComponent.populate(workflowModel.getSupervisors());
	}

	@Override
	public void populateForSave(WorkflowModel workflowModel) {
		List<OrganizationEntityModel> supervisors = supervisorsSelectionComponent.getSelectedEntitiesAsOrganizationEntities();
		workflowModel.setSupervisors(supervisors);
	}
	
	private void calculateResizePercentage(int w, int h){
		float widthP = (float)w / (float)WorkflowWindow.ORIGINAL_WIDTH;
		float heightP = (float)h / (float)WorkflowWindow.ORIGINAL_HEIGHT;		
		supervisorsSelectionComponent.changeSize(widthP, heightP);
	}
	
}

class SupervisorsSelectionComponent extends PlainOrganizationalStructureEntitiesSelectionComponent {
	
	private static final int LEFT_PANEL_ORIGINAL_WIDTH = 520;
	private static final int LEFT_PANEL_ORIGINAL_HEIGHT = 580;
	private static final int RIGHT_PANEL_ORIGINAL_WIDTH = 520;
	private static final int RIGHT_PANEL_ORIGINAL_HEIGHT = 580;
	private static final int BUTTONS_PANEL_ORIGINAL_WIDTH = 120;
	private static final int BUTTONS_PANEL_ORIGINAL_HEIGHT = 580;
	
	public SupervisorsSelectionComponent() {
		rightPanel.setScrollMode(Scroll.AUTO);
		rightPanel.setHeaderVisible(false);
		selectedEntitiesListView.setBorders(false);
	}

	@Override
	public void changeSize(float wIndex, float hIndex) {
		leftPanel.setSize(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(LEFT_PANEL_ORIGINAL_HEIGHT * hIndex));
		buttonsPanel.setSize(Math.round(BUTTONS_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(BUTTONS_PANEL_ORIGINAL_HEIGHT * hIndex));
		if (wIndex < 1)
			rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex)-30, Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT * hIndex));
		else
			rightPanel.setSize(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex), Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT * hIndex));
	}
}