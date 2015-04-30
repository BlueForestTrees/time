package wiki.tool.parser;

public class MillionParser implements IParser{

	/**
	 * "65" vers -23676250000L
	 */
	@Override
	public Long from(String value) {
		return -(long)(Double.parseDouble(value) * 364250000L);
	}

	/**
	 * Nombre de jours
	 * -23676250000000L vers "il y a 65 millions d'années"
	 */
	@Override
	public String to(Long value) {
		return "il y a " + value / -364250000L + " millions d'années";
	}

}
