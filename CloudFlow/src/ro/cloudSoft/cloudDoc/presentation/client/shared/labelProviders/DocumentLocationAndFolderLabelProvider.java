package ro.cloudSoft.cloudDoc.presentation.client.shared.labelProviders;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;

public class DocumentLocationAndFolderLabelProvider implements ModelStringProvider<ModelData> {
	
	@Override
	public String getStringValue(ModelData model, String property) {
		if (model instanceof DocumentLocationModel) {
			return ((DocumentLocationModel) model).getName();
		} else if (model instanceof FolderModel) {
			return ((FolderModel) model).getName();
		} else {
			throw new IllegalArgumentException("Tip necunoscut: [" + model.getClass() + "]");
		}
	}
}