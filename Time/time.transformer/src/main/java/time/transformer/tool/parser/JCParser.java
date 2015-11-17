package time.transformer.tool.parser;

import java.util.regex.Matcher;

public class JCParser implements IParser {

    @Override
    public Long from(Matcher matcher) {
        final long result = (long) (Double.parseDouble(matcher.group("g")) * 364.25);
        if(matcher.group("neg") != null){
            return -result;
        }else{
            return result;
        }
    }

}
