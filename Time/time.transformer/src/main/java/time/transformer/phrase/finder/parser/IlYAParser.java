package time.transformer.phrase.finder.parser;

import java.util.regex.Matcher;

import time.tool.date.Dates;

public class IlYAParser implements IParser {

    @Override
    public long from(Matcher matcher) {
    	int annees = Integer.parseInt(matcher.group("g").replace(" ", ""));
        if(matcher.group("neg") != null){
            annees = -annees;
        }
        return Dates.ilyaToDays(annees);
    }

}
