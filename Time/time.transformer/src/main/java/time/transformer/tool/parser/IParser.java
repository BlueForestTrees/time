package time.transformer.tool.parser;

public interface IParser {
	Long from(final String value);
	String to(final long value);
}
