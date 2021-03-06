package time.analyser.parser;

import java.util.regex.Matcher;

import time.tool.date.Dates;

public class IlYAParser implements IParser {

    @Override
    public Long from(Matcher matcher) {
    	int annees = Integer.parseInt(matcher.group("g").replace(" ", ""));
        if(matcher.group("neg") != null){
            annees = -annees;
        }
        if(matcher.group("mil") != null){
            annees *= 1000;
        }
        return Dates.ilyaToDays(annees);
    }

}
