package time.web.bean;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

public class Filter {
	private static final String PERIOD_HEADER = "@";
	private static final String PARTS_SEP = " ";
	private String words;
	private Long from;
	private Long to;

	private Filter() {

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

	public static Filter build(final String request) {
		Filter result = null;
		if (!StringUtils.isEmpty(request)) {
			final Stream<String> parts = Arrays.stream(request.split(PARTS_SEP));
			final String words = parts.filter(part -> !part.startsWith(PERIOD_HEADER))
					.collect(Collectors.joining(PARTS_SEP));
			final Long[] periods = parts.filter(part -> part.startsWith(PERIOD_HEADER)).map(part -> parsePeriod(part))
					.toArray(Long[]::new);
		}
		return result;
	}

	// TODO convertir
	private static Long parsePeriod(String part) {
		/*
		 * @-1M
		 * 
		 * @-1,5m
		 * 
		 * @-1.6M @-3M
		 * 
		 * @-1M @-7m
		 * 
		 * @-300k @-200k
		 * 
		 * @-2K
		 * 
		 * @-10000 @-2000
		 * 
		 * +/-20%
		 * 
		 * @-?[0-9][,.]?[0-9][Mmk]
		 */
		return null;
	}
}
