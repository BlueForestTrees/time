package wiki.tool.parser;

public class AnneeParser implements IParser{

	@Override
	public Long from(String value) {
		return (long)(Double.parseDouble(value) * 364.25);
	}

	@Override
	public String to(Long value) {
		StringBuilder sb = new StringBuilder();
		sb.append("en ");
		sb.append(Math.round(value / 364.25));
		if(value < 0)
			sb.append(" av. JC");
		return sb.toString();
	}

}
