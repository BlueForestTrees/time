package time.transformer.tool.parser;

import java.util.regex.Matcher;

public interface IParser {
    long seventiesInDays = 719528L;
    int JCToNowYears = 2000;
    long from(final Matcher matcher);
}
