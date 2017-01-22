package time.web.bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.isEmpty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import time.tool.date.Dates;

/**
 * Convertit une requête histoire en mot+from+to
 */
public class TermPeriodFilter {

    private static final Logger LOGGER = LogManager.getLogger(TermPeriodFilter.class);

    private static final int DEFAULT_PERCENT_MARGIN = 10;
    private static final String SPECIAL_HEADER = "@";
    private static final String PARTS_SEP = " ";
    public static final String ET = "et";
    private String words;
    private Long from;
    private Long to;
    private boolean linkMode;

    public boolean isLinkMode() {
        return linkMode;
    }

    private void setLinkMode(final boolean linkMode) {
        this.linkMode = linkMode;
    }

    private TermPeriodFilter() {

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

    public boolean hasWords() {
        return words != null && words.length() > 0;
    }

    public boolean hasFromTo() {
        return from != null && to != null;
    }

    public static TermPeriodFilter fromString(final String request) {
        final TermPeriodFilter termPeriodFilter = new TermPeriodFilter();
        if (!isEmpty(request)) {

            final String terms = transformRequest(request);

            defineWords(termPeriodFilter, terms);
            definePeriods(termPeriodFilter, terms);
            defineLinkMode(termPeriodFilter, terms);
        }
        return termPeriodFilter;
    }

    private static void defineLinkMode(final TermPeriodFilter termPeriodFilter, final String terms) {
        //Les termes contient '@et'
        final boolean containsSpecialET = !Arrays.stream(terms.split(PARTS_SEP))
                                            .filter(part -> part.toLowerCase().startsWith(SPECIAL_HEADER +ET))
                                            .collect(Collectors.toList()).isEmpty();

        final boolean hasTwoWords = wordsStream(terms.toLowerCase()).collect(Collectors.toList()).size() == 2;

        termPeriodFilter.setLinkMode(containsSpecialET && hasTwoWords);
    }

    private static void definePeriods(final TermPeriodFilter termPeriodFilter, final String terms) {
        final List<Period> periods = Arrays.stream(terms.split(PARTS_SEP))
                .filter(part -> part.startsWith(SPECIAL_HEADER))
                .filter(part -> !part.toLowerCase().startsWith(SPECIAL_HEADER + ET))
                .map(TermPeriodFilter::parsePeriod)
                .sorted(Period.byDate)
                .collect(Collectors.toList());
        if (!periods.isEmpty()) {
            final Period firstPeriod = periods.get(0);
            final Period lastPeriod = periods.get(periods.size() - 1);

            termPeriodFilter.setFrom(firstPeriod.getFrom());
            termPeriodFilter.setTo(lastPeriod.getTo());
        }
    }

    private static void defineWords(final TermPeriodFilter termPeriodFilter, final String terms) {
        termPeriodFilter.setWords(wordsStream(terms)
                .collect(Collectors.joining(" ")));
    }

    private static Stream<String> wordsStream(final String terms) {
        return Arrays.stream(terms.split(PARTS_SEP))
                .filter(part -> !part.startsWith(SPECIAL_HEADER));
    }

    private static String transformRequest(String request) {
        return request;
    }

    /**
     * @param part
     * @return
     * @-1000000min
     * @1Mmax
     * @1950
     * @2000+-10%
     * @10/02/1984
     */
    protected static Period parsePeriod(final String part) {
        final Period period = new Period();

        //matche all except dates
        final String value = "(?<value>-?[0-9]+?)(?<decimalValue>[,.]?[0-9]*?)";
        final String suffix = "(?<suffix>[Mmk])?";
        final String percent = "(\\+-(?<percent>[0-9]+?)%)?";
        final String minOrMax = "(?<min>min)?(?<max>max)?";
        final Pattern periodPattern = Pattern.compile("@" + value + suffix + percent + minOrMax);
        final Matcher matcher = periodPattern.matcher(part);
        boolean periodFound = matcher.matches();
        if (periodFound) {
            parsePercentMargin(matcher, period);
            parseValue(matcher, period);
            parseMinMaxMode(matcher, period);
        } else {
            try {
                period.setDate(Dates.toDays(DateTimeFormatter.ofPattern("@d/M/yyyy").parse(part, LocalDate::from)));
            } catch (Exception e) {
                LOGGER.error("error in DateFilter: " + part);
            }
        }
        return period;
    }

    private static void parseMinMaxMode(Matcher matcher, Period period) {
        final boolean minMode = matcher.group("min") != null;
        final boolean maxMode = matcher.group("max") != null;
        period.setMinMode(minMode);
        period.setMaxMode(maxMode);
    }

    private static void parsePercentMargin(final Matcher matcher, final Period period) {
        final String percentMargin = matcher.group("percent");
        int intPercentMargin = 0;
        if (percentMargin != null) {
            intPercentMargin = Integer.parseInt(percentMargin);
        }
        period.setPercentMargin(intPercentMargin);
    }

    private static void parseValue(final Matcher matcher, final Period period) {
        final String decimalValue = matcher.group("decimalValue");
        final String value = matcher.group("value");
        final String suffix = matcher.group("suffix");
        final double doubleValue = Double.parseDouble(value + (decimalValue != null ? decimalValue.replace(',', '.') : ""));

        if (suffix == null) {
            if (doubleValue > -9999) {
                period.setOffset(Period.YEAR_OFFSET);
            } else {
                period.setPercentMargin(DEFAULT_PERCENT_MARGIN);
            }
            period.setDate(Dates.yearsToDays(doubleValue));
        } else if ("M".equals(suffix)) {
            period.setPercentMarginWithoutOverride(DEFAULT_PERCENT_MARGIN);
            period.setDate(Dates.milliardsToDays(doubleValue));
        } else if ("m".equals(suffix)) {
            period.setPercentMarginWithoutOverride(DEFAULT_PERCENT_MARGIN);
            period.setDate(Dates.millionsToDays(doubleValue));
        } else {
            //unknown suffix, ignore it
            period.setDate((long) doubleValue);
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

    public TermPeriodFilter copy() {
        final TermPeriodFilter clone = new TermPeriodFilter();
        clone.setFrom(getFrom());
        clone.setTo(getTo());
        clone.setWords(getWords());
        clone.setLinkMode(isLinkMode());
        return clone;
    }
}
