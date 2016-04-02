package time.transformer.phrase.finder.parser;

import java.util.regex.Matcher;

public interface IParser {
    Long from(final Matcher matcher);
}
