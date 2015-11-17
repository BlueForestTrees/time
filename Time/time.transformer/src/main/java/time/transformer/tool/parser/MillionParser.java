package time.transformer.tool.parser;

import java.util.regex.Matcher;

public class MillionParser implements IParser {

    @Override
    public Long from(Matcher matcher) {
        return -(long) (Double.parseDouble(matcher.group("g").replace(',', '.')) * 364250000L);
    }

}
