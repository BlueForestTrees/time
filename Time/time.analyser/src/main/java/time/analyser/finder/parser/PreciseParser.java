package time.analyser.finder.parser;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.tool.date.Dates;

public class PreciseParser implements IParser {
    
    private static final Logger LOG = LogManager.getLogger(PreciseParser.class);

    @Override
    public Long from(final Matcher matcher) {
        final int day = matcher.group("d") == null ? 1 : Integer.parseInt(matcher.group("d").trim());
        final Month month = parseMonth(matcher.group("m").trim());
        int year = Integer.parseInt(matcher.group("y").trim());

        if(matcher.group("neg") != null){
            year = -year;
        }
        
        try {
            return Dates.toDays(LocalDate.of(year, month, day));
        } catch (DateTimeException e) {
            try {
            	return Dates.toDays(LocalDate.of(year, month, 1));
            } catch (DateTimeException exx) {
                LOG.error("pb pour parser " + matcher.group("g"), exx);
                return 0L;
            }
        }
    }

    protected Month parseMonth(final String monthString) {
        if (monthString.indexOf("jan") == 0 || monthString.indexOf("Jan") == 0) {
            return Month.JANUARY;
        } else if (monthString.indexOf("fev") == 0 || monthString.indexOf("Fev") == 0 || monthString.indexOf("fév") == 0 || monthString.indexOf("Fév") == 0) {
            return Month.FEBRUARY;
        } else if (monthString.indexOf("mar") == 0 || monthString.indexOf("Mar") == 0) {
            return Month.MARCH;
        } else if (monthString.indexOf("avr") == 0 || monthString.indexOf("Avr") == 0) {
            return Month.APRIL;
        } else if (monthString.indexOf("mai") == 0 || monthString.indexOf("Mai") == 0) {
            return Month.MAY;
        } else if (monthString.indexOf("juin") == 0 || monthString.indexOf("Juin") == 0) {
            return Month.JUNE;
        } else if (monthString.indexOf("juil") == 0 || monthString.indexOf("Juil") == 0) {
            return Month.JULY;
        } else if (monthString.indexOf("ao") == 0 || monthString.indexOf("Ao") == 0) {
            return Month.AUGUST;
        } else if (monthString.indexOf("sep") == 0 || monthString.indexOf("Sep") == 0) {
            return Month.SEPTEMBER;
        } else if (monthString.indexOf("oct") == 0 || monthString.indexOf("Oct") == 0) {
            return Month.OCTOBER;
        } else if (monthString.indexOf("nov") == 0 || monthString.indexOf("Nov") == 0) {
            return Month.NOVEMBER;
        } else if (monthString.indexOf("dec") == 0 || monthString.indexOf("Dec") == 0 || monthString.indexOf("déc") == 0 || monthString.indexOf("Déc") == 0) {
            return Month.DECEMBER;
        }
        return null;
    }
}
