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
        return Dates.toDays(LocalDate.of(Integer.parseInt( matcher.group("g").trim()), Month.JANUARY, 1));
    }

}
