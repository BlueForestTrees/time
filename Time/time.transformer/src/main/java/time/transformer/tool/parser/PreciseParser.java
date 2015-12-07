package time.transformer.tool.parser;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

/**
 * Created by slimane.medini on 04/12/2015.
 */
public class PreciseParser
    implements IParser
{
    
    @Override
    public long from(final Matcher matcher)
    {
        final int day = Integer.parseInt(matcher.group("d"));
        final Month month = parseMonth(matcher.group("m"));
        final int year = Integer.parseInt(matcher.group("y"));

        return LocalDate.of(year, month, day).toEpochDay() + seventiesInDays;
    }

    protected Month parseMonth(final String monthString)
    {
        System.out.println(monthString);
        if(monthString.indexOf("jan") == 0 || monthString.indexOf("Jan") == 0){
            return Month.JANUARY;
        }else if(monthString.indexOf("fev") == 0 || monthString.indexOf("Fev") == 0 || monthString.indexOf("fév") == 0 || monthString.indexOf("Fév") == 0){
            return Month.FEBRUARY;
        }else if(monthString.indexOf("mar") == 0 || monthString.indexOf("Mar") == 0){
            return Month.MARCH;
        }else if(monthString.indexOf("avr") == 0 || monthString.indexOf("Avr") == 0){
            return Month.APRIL;
        }else if(monthString.indexOf("mai") == 0 || monthString.indexOf("Mai") == 0){
            return Month.MAY;
        }else if(monthString.indexOf("juin") == 0 || monthString.indexOf("Juin") == 0){
            return Month.JUNE;
        }else if(monthString.indexOf("juil") == 0 || monthString.indexOf("Juil") == 0){
            return Month.JULY;
        }else if(monthString.indexOf("ao") == 0 || monthString.indexOf("Ao") == 0){
            return Month.AUGUST;
        }else if(monthString.indexOf("sep") == 0 || monthString.indexOf("Sep") == 0){
            return Month.SEPTEMBER;
        }else if(monthString.indexOf("oct") == 0 || monthString.indexOf("Oct") == 0){
            return Month.OCTOBER;
        }else if(monthString.indexOf("nov") == 0 || monthString.indexOf("Nov") == 0){
            return Month.NOVEMBER;
        }else if(monthString.indexOf("dec") == 0 || monthString.indexOf("Dec") == 0 || monthString.indexOf("déc") == 0 || monthString.indexOf("Déc") == 0){
            return Month.DECEMBER;
        }
        return null;
    }
}
