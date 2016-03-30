package time.web.bean;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import time.tool.date.Dates;

public class TermPeriodFilter {
	private static final int DEFAULT_PERCENT_MARGIN = 10;
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
	
	public boolean hasTerm(){
		return words != null && words.length > 0;
	}

	public static TermPeriodFilter build(final String request) {
		final TermPeriodFilter termPeriodFilter = new TermPeriodFilter();
		if (!StringUtils.isEmpty(request)) {
			
			final String terms = transformRequest(request);
			
			final String[] words = Arrays.stream(terms.split(PARTS_SEP))
					                   .filter(part -> !part.startsWith(PERIOD_HEADER))
									   .toArray(String[]::new);
			termPeriodFilter.setWords(words);
			
			final List<Period> periods = Arrays.stream(terms.split(PARTS_SEP))
					                           .filter(part -> part.startsWith(PERIOD_HEADER))
					                           .map(part -> parsePeriod(part))
					                           .sorted(Period.byDate).collect(Collectors.toList());
			if(periods.size() > 0){
				final Period firstPeriod = periods.get(0);
				final Period lastPeriod = periods.get(periods.size()-1);
				
				termPeriodFilter.setFrom(firstPeriod.getFrom());
				termPeriodFilter.setTo(lastPeriod.getTo());
			}
		}
		return termPeriodFilter;
	}

	private static String transformRequest(String request) {
		return request;
	}

	protected static Period parsePeriod(final String part) {
		final Period period = new Period();
		final Pattern pattern = Pattern.compile("@(?<value>-?[0-9]+?)(?<decimalValue>[,.]?[0-9]*?)(?<suffix>[Mmk])?(\\+-(?<pm>[0-9]+?)%)?(?<min>min)?(?<max>max)?");
		final Matcher matcher = pattern.matcher(part);
		
		if(!matcher.matches()){
			return period;
		}

		parsePercentMargin(matcher, period);
		parseValue(matcher, period);
		parseMinMaxMode(matcher, period);
		
		return period;
	}

	private static void parseMinMaxMode(Matcher matcher, Period period) {
		final boolean minMode = matcher.group("min") != null;
		final boolean maxMode = matcher.group("max") != null;
		period.setMinMode(minMode);
		period.setMaxMode(maxMode);
	}

	private static void parsePercentMargin(final Matcher matcher, final Period period) {
		final String percentMargin = matcher.group("pm");
		int intPercentMargin = 0;
		if(percentMargin != null){
			intPercentMargin = Integer.parseInt(percentMargin);
		}
		period.setPercentMargin(intPercentMargin);
	}

	private static void parseValue(final Matcher matcher, final Period period) {
		final String decimalValue = matcher.group("decimalValue");
		final String value = matcher.group("value");
		final String suffix = matcher.group("suffix");
		final double doubleValue = Double.parseDouble(value + (decimalValue != null ? decimalValue.replace(',', '.') : ""));
		
		if(suffix == null){
			if(doubleValue > -9999){
				period.setOffset(Period.YEAR_OFFSET);				
			}else{
				period.setPercentMargin(DEFAULT_PERCENT_MARGIN);
			}
			period.setDate(Dates.yearsToDays(doubleValue));
		}else if("M".equals(suffix)){
			period.setPercentMarginWithoutOverride(DEFAULT_PERCENT_MARGIN);
			period.setDate(Dates.milliardsToDays(doubleValue));
		}else if("m".equals(suffix)){
			period.setPercentMarginWithoutOverride(DEFAULT_PERCENT_MARGIN);
			period.setDate(Dates.millionsToDays(doubleValue));
		}else{
			//unknown suffix, ignore it
			period.setDate((long)doubleValue);
		}
	}

	public boolean hasPeriod() {
		return hasFrom() || hasTo();
	}

	public boolean hasTo() {
		return to != null;
	}

	public boolean hasFrom() {
		return from != null;
	}
}
