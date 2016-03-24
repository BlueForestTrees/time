package time.transformer.phrase.finder.parser;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

import time.tool.date.Dates;

/**
 * Created by slimane.medini on 17/11/2015.
 */
public class AnneeParser
    implements IParser
{
    @Override
    public long from(final Matcher matcher)
    {
        final int annee = Integer.parseInt(matcher.group("g").trim());

        return Dates.toDays(LocalDate.of(annee, Month.JANUARY, 1));
    }

}
