package time.web.bean;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import time.tool.date.Dates;

public class TermPeriodFilter {
	private static final String PERIOD_HEADER = "@";
	private static final String PARTS_SEP = " ";
	private String[] words;
	private Long from;
	private Long to;

	private TermPeriodFilter() {

	}

	public String[] getWords() {
		return words;
	}

	public void setWords(String[] words) {
		this.words = words;
	}

	public Long getFrom() {
		return from;
	}

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getTo() {
		return to;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	public static TermPeriodFilter build(final String request) {
		if (!StringUtils.isEmpty(request)) {
			final String[] parts = request.split(PARTS_SEP);
			
			final String[] words = Arrays.stream(parts)
					                   .filter(part -> !part.startsWith(PERIOD_HEADER))
									   .toArray(String[]::new);
			
			final List<Period> periods = Arrays.stream(parts)
					                           .filter(part -> part.startsWith(PERIOD_HEADER))
					                           .map(part -> parsePeriod(part))
					                           .sorted(Period.byDate).collect(Collectors.toList());
			
			final Period firstPeriod = periods.get(0);
			final Period lastPeriod = periods.get(periods.size()-1);
			final TermPeriodFilter periodFilter = new TermPeriodFilter();
			
			periodFilter.setFrom(firstPeriod.getFrom());
			periodFilter.setTo(lastPeriod.getTo());
			periodFilter.setWords(words);
			return periodFilter;
		}
		return null;
	}

	protected static Period parsePeriod(final String part) {
		final Period period = new Period();
		final Pattern pattern = Pattern.compile("@(?<value>-?[0-9]+?)(?<decimalValue>[,.]?[0-9]*?)(?<suffix>[Mmk])?(\\+-(?<offset>[0-9]+?)%)?");
		final Matcher matcher = pattern.matcher(part);
		
		if(!matcher.matches()){
			return period;
		}

		period.setDate(parseValue(matcher));
		period.setOffset(parseOffset(matcher));
		
		return period;
	}

	private static int parseOffset(final Matcher matcher) {
		final String offset = matcher.group("offset");
		int intOffset = 0;
		if(offset != null){
			intOffset = Integer.parseInt(offset);
		}
		return intOffset;
	}

	private static long parseValue(final Matcher matcher) {
		final String decimalValue = matcher.group("decimalValue");
		final String value = matcher.group("value");
		final String suffix = matcher.group("suffix");		
		final double doubleValue = Double.parseDouble(value + (decimalValue != null ? decimalValue.replace(',', '.') : ""));
		
		if(suffix == null){
			return (long)doubleValue;
		}else if("M".equals(suffix)){
			return Dates.milliardsToDays(doubleValue);
		}else if("m".equals(suffix)){
			return Dates.millionsToDays(doubleValue);
		}else{
			//unknown suffix, ignore it
			return (long)doubleValue;
		}
	}
}
