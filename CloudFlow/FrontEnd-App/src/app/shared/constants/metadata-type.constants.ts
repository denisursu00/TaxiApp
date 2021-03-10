import { MetadataDefinitionModel } from "./../model/content/metadata-definition.model";

export class MetadataTypeConstants {

	private static metadataTypeLabelCodeByTypeMap: object = null;

	public static getMetadataTypeLabelCodeByTypeMap(): object {
		if (this.metadataTypeLabelCodeByTypeMap === null) {
			this.metadataTypeLabelCodeByTypeMap = {};
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_TEXT] = "TEXT";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_NUMERIC] = "NUMERIC";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_AUTO_NUMBER] = "AUTO_NUMBER";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_DATE] = "DATE";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_DATE_TIME] = "METADATA_TYPE_DATE_TIME";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_MONTH] = "METADATA_TYPE_MONTH";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_LIST] = "LIST";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_USER] = "METADATA_TYPE_USER";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_TEXT_AREA] = "EXTENDED_TEXT";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_METADATA_COLLECTION] = "METADATA_COLLECTION";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_NOMENCLATOR] = "METADATA_TYPE_NOMENCLATOR";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_GROUP] = "METADATA_TYPE_GROUP";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_DOCUMENT] = "METADATA_TYPE_DOCUMENT";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_CALENDAR] = "METADATA_TYPE_CALENDAR";
			this.metadataTypeLabelCodeByTypeMap[MetadataDefinitionModel.TYPE_PROJECT] = "METADATA_TYPE_PROJECT";
		}
		return this.metadataTypeLabelCodeByTypeMap;
	}
}