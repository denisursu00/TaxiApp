package ro.cloudSoft.cloudDoc.domain.nomenclator;

public enum NomenclatorAttributeTypeEnum {
	
	TEXT("TEXT"),
	NUMERIC("NUMERIC"),
	DATE("DATE"),
	BOOLEAN("BOOLEAN"),
	NOMENCLATOR("NOMENCLATOR");
	
	private final String type;

	private NomenclatorAttributeTypeEnum(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
