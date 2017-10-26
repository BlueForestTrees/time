package time.analyser.parser;

import time.tool.date.Dates;

import java.util.regex.Matcher;

public class JCOffsetParser implements IParser {

    @Override
    public Long from(Matcher matcher) {
        final String group = matcher.group("g").replace(" ", "");
        if(group == null){
            return null;
        }
        int annees = Integer.parseInt(group);
		if(matcher.group("neg") != null){
			annees = -annees;
		}else if (annees < 100){
		    annees+=1900;
        }
		return Dates.toDays(annees);
    }

}
