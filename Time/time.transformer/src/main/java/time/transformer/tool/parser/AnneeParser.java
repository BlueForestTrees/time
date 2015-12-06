package time.transformer.tool.parser;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

/**
 * Created by slimane.medini on 17/11/2015.
 */
public class AnneeParser
    implements IParser
{
    @Override
    public long from(final Matcher matcher)
    {
        final int annee = Integer.parseInt(matcher.group("g"));

        return LocalDate.of(annee, Month.JANUARY, 1).toEpochDay() + seventiesInDays;
    }
}
