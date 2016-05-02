package time.transformer.find.parser;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

import time.tool.date.Dates;

public class RomanParser implements IParser {

    @Override
    public Long from(final Matcher matcher) {
        final String romanNumber = matcher.group("g");
        int annee = Dates.toYear(romanNumber);
        if(matcher.group("neg") != null){
        	annee = -annee;
        }
        return Dates.toDays(LocalDate.of(annee, Month.JANUARY, 1));
    }
}
