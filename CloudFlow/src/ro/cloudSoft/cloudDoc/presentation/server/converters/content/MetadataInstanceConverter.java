package ro.cloudSoft.cloudDoc.presentation.server.converters.content;
import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.MetadataInstance;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;

public class MetadataInstanceConverter {
	
	 public static MetadataInstance getMetadataInstanceFromModel(MetadataInstanceModel model){
	        MetadataInstance metadataInstance = new MetadataInstance();
	        metadataInstance.setMetadataDefinitionId(model.getMetadataDefinitionId());     
	        metadataInstance.setValues(model.getValues());
	        return metadataInstance;
	 }
	 
	 public static MetadataInstanceModel getMetadataInstanceModelFromMetadata(MetadataInstance metadata){
			MetadataInstanceModel mim= new MetadataInstanceModel();
			mim.setMetadataDefinitionId(metadata.getMetadataDefinitionId());
			mim.setValues(metadata.getValues());
			return mim;
	 }
	 
	 public static List<MetadataInstance> getMetadataInstanceList(List<MetadataInstanceModel> metadate ){
		 List<MetadataInstance> listMetadataInstance = new ArrayList<MetadataInstance>();
		 if (metadate!=null&&metadate.size()>0){
			 for(MetadataInstanceModel m:metadate){
				 MetadataInstance mi = getMetadataInstanceFromModel(m);
				 listMetadataInstance.add(mi);
			 }
		 }	 
		return listMetadataInstance;		
	 }
	 
	 public static List<MetadataInstanceModel> getMetadataInstanceModelList( List<MetadataInstance> metadate){
		 List<MetadataInstanceModel> listMetadataInstaceModel = new ArrayList<MetadataInstanceModel>();
		 if ( metadate != null && metadate.size() > 0 ){
			 for (MetadataInstance mi : metadate){
				 MetadataInstanceModel model= getMetadataInstanceModelFromMetadata(mi);
				 listMetadataInstaceModel.add(model);
			 }
		 }
		 return listMetadataInstaceModel;
	 }
}
