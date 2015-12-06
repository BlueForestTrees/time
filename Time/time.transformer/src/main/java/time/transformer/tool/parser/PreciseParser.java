package time.transformer.tool.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;

/**
 * Created by slimane.medini on 04/12/2015.
 */
public class PreciseParser
    implements IParser
{
    
    final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH);
    final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH);
    
    @Override
    public long from(final Matcher matcher)
    {
        final String dateString = matcher.group("g");
        
        LocalDate date = null; 
        
        try{
            date = LocalDate.parse(dateString, formatter1);
        }catch(DateTimeParseException e){
            try{
                date = LocalDate.parse(dateString, formatter2);
            }catch(DateTimeParseException e2){
            }
        }

        return date.toEpochDay() + seventiesInDays;
    }
}
