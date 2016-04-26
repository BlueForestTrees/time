package time.transformer.phrase.finder.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.IntStream;

import time.tool.date.Dates;
import time.transformer.phrase.finder.DateFindersFactory;

public class MilliardParser implements IParser {

	//une map <"un",1>,<"deux",2> pour gérer les chiffres écrits en texte
	public static final Map<String, Integer> numbersMap = new HashMap<>();
	static {
		String[] nums = DateFindersFactory.TEXT_NUMBERS.split("\\|");
		IntStream.range(1, nums.length + 1).forEach(num -> numbersMap.put(nums[num-1], num));
	}

	@Override
	public Long from(final Matcher matcher) {
		boolean textMode = matcher.group("gt") != null;
		double count;
		if (textMode) {
			count = numberFromText(matcher.group("gt"));
		} else {
			count = Double.parseDouble(matcher.group("g").replace(',', '.'));
		}

		if ("ard".equals(matcher.group("s"))) {
			return Dates.milliardsToDays(count);
		} else {
			return Dates.millionsToDays(count);
		}
	}

	private double numberFromText(String numberText) {
		final Integer number = numbersMap.get(numberText);
		
		return number;
	}

}
