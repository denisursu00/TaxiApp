package ro.cloudSoft.cloudDoc.presentation.client.shared.renderers;

import ro.cloudSoft.cloudDoc.presentation.client.shared.CustomGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.EnumWithLocalizedLabel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;

public class EnumWithLocalizedLabelGridCellRenderer<M extends ModelData> extends CustomGridCellRenderer<M> {
	
	private final String enumPropertyName;
	
	public EnumWithLocalizedLabelGridCellRenderer(String enumPropertyName) {
		this.enumPropertyName = enumPropertyName;
	}

	@Override
	public Object doRender(M model, String property, ColumnData config, 
		int rowIndex, int colIndex, ListStore<M> store, Grid<M> grid) {
		
		EnumWithLocalizedLabel enumConstant = (EnumWithLocalizedLabel) model.get(enumPropertyName);
		return enumConstant.getLocalizedLabel();
	}
}