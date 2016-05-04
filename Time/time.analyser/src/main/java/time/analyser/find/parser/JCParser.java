package time.analyser.find.parser;

import java.util.regex.Matcher;

import time.tool.date.Dates;

public class JCParser implements IParser {

    @Override
    public Long from(Matcher matcher) {
        int annees = Integer.parseInt(matcher.group("g"));
		if(matcher.group("neg") != null){
			annees = -annees;
		}
		return Dates.toDays(annees);
    }

}
