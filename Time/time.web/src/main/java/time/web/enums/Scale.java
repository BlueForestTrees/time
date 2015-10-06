package time.web.enums;

public enum Scale {
	TEN("dateByTen", 10L), TEN3("dateByTen3", 10000L), TEN6("dateByTen6", 10000000L), TEN9("dateByTen9", 10000000000L);

	private final String field;
	private final Long multiplier;
	
	private Scale(String field, Long multiplier){
		this.field = field;
		this.multiplier = multiplier;
	}
	
	public String getField() {
		return field;
	}

	public Long getMultiplier() {
		return multiplier;
	}
}
