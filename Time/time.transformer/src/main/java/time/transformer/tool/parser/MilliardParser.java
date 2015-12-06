package time.transformer.tool.parser;

import java.util.regex.Matcher;

public class MilliardParser implements IParser {

    @Override
    public long from(final Matcher matcher) {
        if("ard".equals(matcher.group("s"))){
            return -(long) (Double.parseDouble(matcher.group("g").replace(',', '.')) * 364250000000L);
        }else{
            return -(long) (Double.parseDouble(matcher.group("g").replace(',', '.')) * 364250000L);
        }
    }

}
