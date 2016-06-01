package time.analyser.parser;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

import time.tool.date.Dates;

public class AnneeParser
    implements IParser
{
    @Override
    public Long from(final Matcher matcher)
    {
        final int annee = Integer.parseInt(matcher.group("g").trim());

        return Dates.toDays(LocalDate.of(annee, Month.JANUARY, 1));
    }

}
