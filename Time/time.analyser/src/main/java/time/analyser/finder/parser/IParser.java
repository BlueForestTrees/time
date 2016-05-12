package time.analyser.finder.parser;

import java.util.regex.Matcher;

public interface IParser {
    Long from(final Matcher matcher);
}
