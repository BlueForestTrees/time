package wiki.tool.parser;


public class AnneeParser implements IParser{

	/**
	 * "65" vers -23676250000L
	 */
	@Override
	public Long from(String value) {
		return (long)(Double.parseDouble(value) * 364.25);
	}

	/**
	 * Nombre de jours
	 * -23676250000000L vers "il y a 65 millions d'années"
	 */
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
