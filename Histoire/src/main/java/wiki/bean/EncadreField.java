package wiki.bean;

public enum EncadreField {
	NOM_COMPLET("Nom complet"),
	NOM_DE_NAISSANCE("Nom de naissance"),
	DATE_NAISSANCE("Date de naissance"),
	SURNOM("Surnom");
	
	private final String value;
	
	public String getValue() {
		return value;
	}

	private EncadreField(String value){
		this.value = value;
	}
	
}
