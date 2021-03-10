package ro.cloudSoft.cloudDoc.presentation.client.shared.iconproviders;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DocumentLocationAndFolderIconProvider implements ModelIconProvider<ModelData> {
	
	@Override
	public AbstractImagePrototype getIcon(ModelData model) {
		if (model instanceof DocumentLocationModel) {
			return IconHelper.createStyle("icon-documentLocation", 16, 16);
		} else if (model instanceof FolderModel) {
			return IconHelper.createStyle("icon-folder", 16, 16);
		} else {
			throw new IllegalArgumentException("Tip necunoscut: [" + model.getClass() + "]");
		}
	}
}