package time.transformer.find.parser;

import java.util.Map;

import org.junit.Test;

public class MilliardParserTest {
	@Test
	public void numberMapTest(){
		final Map<String, Integer> numbersmap = MilliardParser.numbersMap;
		System.out.println(numbersmap);
	}
}
