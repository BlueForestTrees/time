package time.transformer.tool.parser;

import java.util.regex.Matcher;

public interface IParser {
    long seventiesInDays = 719528L;
    Long from(final Matcher matcher);
}
