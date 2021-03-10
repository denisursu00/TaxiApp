package ro.cloudSoft.cloudDoc.presentation.server.converters;

import ro.cloudSoft.cloudDoc.domain.content.MimeType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;

public class MimeTypeConverter {

	public static MimeTypeModel getModelFromMimeType(MimeType mimeType) {
		MimeTypeModel mimeTypeModel = new MimeTypeModel();
		mimeTypeModel.setId(mimeType.getId());
		mimeTypeModel.setName(mimeType.getName());
		mimeTypeModel.setExtension(mimeType.getExtension());
		return mimeTypeModel;
	}
	
	public static MimeType getMimeTypeFromModel(MimeTypeModel mimeTypeModel) {
		MimeType mimeType = new MimeType();
		mimeType.setId(mimeTypeModel.getId());
		mimeType.setName(mimeTypeModel.getName());
		mimeType.setExtension(mimeTypeModel.getExtension());
		return mimeType;
	}
}