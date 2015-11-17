package time.transformer.tool.parser;

import java.util.regex.Matcher;

public class MilliardParser implements IParser {

    @Override
    public Long from(final Matcher matcher) {
        return -(long) (Double.parseDouble(matcher.group("g").replace(',', '.')) * 364250000000L);
    }

}
