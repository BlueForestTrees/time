package time.transformer.phrase.finder.parser;

import java.util.regex.Matcher;

public interface IParser {
    long from(final Matcher matcher);
}
