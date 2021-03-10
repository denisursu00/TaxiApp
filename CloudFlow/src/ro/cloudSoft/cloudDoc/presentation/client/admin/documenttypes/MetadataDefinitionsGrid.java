package ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.CustomGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.MetadataTypeConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;

public class MetadataDefinitionsGrid extends Grid<MetadataDefinitionModel> {
	
	private static final int COLUMN_WIDTH = 150;

	public MetadataDefinitionsGrid() {
		
		super(new ListStore<MetadataDefinitionModel>(), createColumnModel());
		
		setAutoExpandColumn(MetadataDefinitionModel.PROPERTY_NAME);
		setAutoExpandMin(COLUMN_WIDTH);
		
		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	private static ColumnModel createColumnModel() {

		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		
		ColumnConfig nameColumnConfig = new ColumnConfig();
		nameColumnConfig.setHeader(GwtLocaleProvider.getConstants().NAME());
		nameColumnConfig.setId(MetadataDefinitionModel.PROPERTY_NAME);
		nameColumnConfig.setWidth(COLUMN_WIDTH);
		columnConfigs.add(nameColumnConfig);
		
		ColumnConfig labelColumnConfig = new ColumnConfig();
		labelColumnConfig.setHeader(GwtLocaleProvider.getConstants().LABEL());
		labelColumnConfig.setId(MetadataDefinitionModel.PROPERTY_LABEL);
		labelColumnConfig.setWidth(COLUMN_WIDTH);
		columnConfigs.add(labelColumnConfig);
		
		ColumnConfig orderNumberColumnConfig = new ColumnConfig();
		orderNumberColumnConfig.setHeader(GwtLocaleProvider.getConstants().ORDER_NUMBER());
		orderNumberColumnConfig.setId(MetadataDefinitionModel.PROPERTY_ORDER_NUMBER);
		orderNumberColumnConfig.setWidth(COLUMN_WIDTH);
		columnConfigs.add(orderNumberColumnConfig);
		
		ColumnConfig typeColumnConfig = new ColumnConfig();
		typeColumnConfig.setHeader(GwtLocaleProvider.getConstants().TYPE());
		typeColumnConfig.setRenderer(new CustomGridCellRenderer<MetadataDefinitionModel>() {
			public Object doRender(MetadataDefinitionModel model, String property, ColumnData config, int rowIndex, int colIndex, 
					ListStore<MetadataDefinitionModel> store, Grid<MetadataDefinitionModel> grid) {
				
				return MetadataTypeConstants.getMetadataTypeLabelByTypeName().get(model.getType());
			};
		});
		typeColumnConfig.setId(MetadataDefinitionModel.PROPERTY_TYPE);
		typeColumnConfig.setWidth(COLUMN_WIDTH);
		columnConfigs.add(typeColumnConfig);
		
		return new ColumnModel(columnConfigs);
	}
}