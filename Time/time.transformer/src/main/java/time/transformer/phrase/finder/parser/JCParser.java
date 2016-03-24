package time.transformer.phrase.finder.parser;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

import time.tool.date.Dates;

public class JCParser implements IParser {

    @Override
    public long from(Matcher matcher) {
        if(matcher.group("neg") != null){
            return Dates.toDays(LocalDate.of(-Integer.parseInt(matcher.group("g")), Month.JANUARY, 1));
        }else{
            return Dates.toDays(LocalDate.of(Integer.parseInt(matcher.group("g")), Month.JANUARY, 1));
        }
    }

}
