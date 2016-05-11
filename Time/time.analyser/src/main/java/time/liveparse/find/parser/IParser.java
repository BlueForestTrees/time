package time.liveparse.find.parser;

import java.util.regex.Matcher;

public interface IParser {
    Long from(final Matcher matcher);
}
