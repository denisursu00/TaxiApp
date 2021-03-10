package ro.cloudSoft.cloudDoc.presentation.client.shared;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public abstract class CustomGridCellRenderer<M extends ModelData> implements GridCellRenderer<M> {
	
	public static final String VALUE_WHEN_NOTHING_TO_RENDER = "";

	@Override
	public Object render(M model, String property, ColumnData config, 
			int rowIndex, int colIndex, ListStore<M> store, Grid<M> grid) {
		
		Object valueToRender = doRender(model, property, config, rowIndex, colIndex, store, grid);
		if (valueToRender == null) {
			// Daca este returnat null, atunci pot aparea erori (NullPointerException) la afisarea modelelor in grid.
			return VALUE_WHEN_NOTHING_TO_RENDER;
		}
		return valueToRender;
	}
	
	public abstract Object doRender(M model, String property, ColumnData config, 
		int rowIndex, int colIndex, ListStore<M> store, Grid<M> grid);
}