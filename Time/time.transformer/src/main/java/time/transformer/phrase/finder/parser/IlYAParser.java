package time.transformer.phrase.finder.parser;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

import time.tool.date.Dates;

public class IlYAParser implements IParser {

    @Override
    public long from(Matcher matcher) {
        if(matcher.group("neg") != null){
            return Dates.toDays(LocalDate.of(-Integer.parseInt(matcher.group("g").replace(" ", "")) + Dates.JCToNowYears, Month.JANUARY, 1));
        }else{
            return Dates.toDays(LocalDate.of(Integer.parseInt(matcher.group("g").replace(" ", "")) + Dates.JCToNowYears, Month.JANUARY, 1));
        }
    }

}
