package time.transformer.tool.parser;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

public class JCParser implements IParser {

    @Override
    public Long from(Matcher matcher) {
        if(matcher.group("neg") != null){
            return -LocalDate.of(Integer.parseInt(matcher.group("g")), Month.JANUARY, 1).toEpochDay();
        }else{
            return LocalDate.of(Integer.parseInt(matcher.group("g")), Month.JANUARY, 1).toEpochDay();
        }
    }

}
