package time.transformer.phrase.finder.parser;

import java.util.regex.Matcher;

import time.tool.date.Dates;

public class JCParser implements IParser {

    @Override
    public long from(Matcher matcher) {
        int annees = Integer.parseInt(matcher.group("g"));
		if(matcher.group("neg") != null){
			annees = -annees;
		}
		return Dates.toDays(annees);
    }

}
