package time.analyser.finder.parser;

import java.util.regex.Matcher;

import time.tool.date.Dates;

public class JCParser implements IParser {

    @Override
    public Long from(Matcher matcher) {
        final String group = matcher.group("g");
        if(group == null){
            return null;
        }
        int annees = Integer.parseInt(group);
		if(matcher.group("neg") != null){
			annees = -annees;
		}
		return Dates.toDays(annees);
    }

}
