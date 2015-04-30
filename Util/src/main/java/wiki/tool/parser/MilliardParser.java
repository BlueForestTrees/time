package wiki.tool.parser;

public class MilliardParser implements IParser{

	@Override
	public Long from(String value) {
		return -(long)(Double.parseDouble(value) * 364250000000L);
	}

	@Override
	public String to(Long value) {
		return "il y a " + value / -364250000000L + " milliards d'années";
	}

}
