package time.web.bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

public class PeriodFilter {
	private static final String PERIOD_HEADER = "@";
	private static final String PARTS_SEP = " ";
	private String words;
	private Long from;
	private Long to;

	private PeriodFilter() {

	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
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

	public static PeriodFilter build(final String request) {
		if (!StringUtils.isEmpty(request)) {
			final Stream<String> parts = Arrays.stream(request.split(PARTS_SEP));
			final String words = parts.filter(part -> !part.startsWith(PERIOD_HEADER))
									  .collect(Collectors.joining(PARTS_SEP));
			final List<Period> periods = parts.filter(part -> part.startsWith(PERIOD_HEADER))
				 .map(part -> parsePeriod(part))
				 .sorted(Period.byDate).collect(Collectors.toList());
			final Period firstPeriod = periods.get(0);
			final Period lastPeriod = periods.get(periods.size()-1);
			final PeriodFilter periodFilter = new PeriodFilter();
			
			periodFilter.setFrom(firstPeriod.getFirst());
			periodFilter.setTo(lastPeriod.getLast());
			periodFilter.setWords(words);
			return periodFilter;
		}
		return null;
	}

	// TODO convertir
	protected static Period parsePeriod(final String part) {
		final Period period = new Period();
		//si @numberX@offset% => return number*X,offset
		/* 
		 * @(?<value>-?[0-9][,.]?[0-9])(?<help>[Mmk])?(?<offset>[0-9]%)?
		 */
		return period;
	}
}
