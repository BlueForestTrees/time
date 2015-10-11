package wiki.tool.parser;

public class JCParser implements IParser {

	private final AnneeParser anneeParser = new AnneeParser();
	
	@Override
	public Long from(String value) {
		return -anneeParser.from(value);
	}

	@Override
	public String to(long value) {
		return anneeParser.to(value);
	}

}
