package wiki.tool.parser;

public class MilliardParser implements IParser{

	@Override
	public Long from(final String value) {
		return -(long)(Double.parseDouble(value.replace(',', '.')) * 364250000000L);
	}

	@Override
	public String to(final long value) {
		return "il y a " + value / -364250000000L + " milliards d'années";
	}

}
