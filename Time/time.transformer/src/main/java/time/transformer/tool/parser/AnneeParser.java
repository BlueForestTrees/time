package time.transformer.tool.parser;

import java.util.regex.Matcher;

/**
 * Created by slimane.medini on 17/11/2015.
 */
public class AnneeParser
    implements IParser
{
    @Override
    public Long from(final Matcher matcher)
    {
        return (long) (Double.parseDouble(matcher.group("g")) * 364.25);
    }
}
