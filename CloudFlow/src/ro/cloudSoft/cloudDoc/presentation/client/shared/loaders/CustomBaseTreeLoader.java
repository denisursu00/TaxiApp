package ro.cloudSoft.cloudDoc.presentation.client.shared.loaders;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.ModelData;

public class CustomBaseTreeLoader extends BaseTreeLoader<ModelData> {
	
	public CustomBaseTreeLoader(@SuppressWarnings("rawtypes") DataProxy proxy) {
		super(proxy);
	}
	
	@Override
	public boolean hasChildren(ModelData parent) {
		return true;
	}
}