package time.liveparse.find.parser;

import java.util.regex.Matcher;

public class ExcludingJCParser implements IParser {

	private JCParser jcParser = new JCParser();

	@Override
	public Long from(Matcher matcher) {
		if (matcher.group("ex") != null) {
			return null;
		} else {
			return jcParser.from(matcher);
		}
	}

}
