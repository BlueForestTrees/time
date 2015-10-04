package time.web.enums;

public enum Scale {
	TEN("dateByTen"), TEN3("dateByTen3"), TEN6("dateByTen6"), TEN9("dateByTen9");

	private final String field;
	
	private Scale(String field){
		this.field = field;
	}
	
	public String getField() {
		return field;
	}
}
