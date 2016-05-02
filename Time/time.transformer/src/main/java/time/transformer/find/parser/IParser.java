package time.transformer.find.parser;

import java.util.regex.Matcher;

public interface IParser {
    Long from(final Matcher matcher);
}
