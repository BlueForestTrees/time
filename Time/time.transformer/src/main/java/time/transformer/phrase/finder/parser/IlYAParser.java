package time.transformer.phrase.finder.parser;

import java.time.LocalDate;
import java.time.Month;
import java.util.regex.Matcher;

public class IlYAParser implements IParser {

    @Override
    public long from(Matcher matcher) {
        if(matcher.group("neg") != null){
            return LocalDate.of(-Integer.parseInt(matcher.group("g")) + JCToNowYears, Month.JANUARY, 1).toEpochDay() + seventiesInDays;
        }else{
            return LocalDate.of(Integer.parseInt(matcher.group("g")) + JCToNowYears, Month.JANUARY, 1).toEpochDay() + seventiesInDays;
        }
    }

}
