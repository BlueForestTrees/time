package wiki.tool.parser;

public class MilliardParser implements IParser{

	/**
	 * "65" vers -23676250000000L
	 */
	@Override
	public Long from(String value) {
		return -(long)(Double.parseDouble(value) * 364250000000L);
	}

	/**
	 * Nombre de jours.
	 * -23676250000000L vers "il y a 65 milliards d'années"
	 */
	@Override
	public String to(Long value) {
		return "il y a " + value / -364250000000L + " milliards d'années";
	}

}
