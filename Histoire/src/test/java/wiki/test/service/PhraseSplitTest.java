package wiki.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

import org.junit.Test;

public class PhraseSplitTest {


	@Test
	public void testPhraseSplit() {
		final String notEndWithThis = "av|mr|dr|jc|JC|J\\.-C";
		final String sep = "[ \r\n]+";
		final String splitRegex = "(?<=(?<!( ("+notEndWithThis+")))\\.)"+sep;
		final Pattern pattern = Pattern.compile(splitRegex);

		final String data = "Bla bla. Bli bli.\r\n\r\nBlou av. b mr. lou\r\nICI blou. Blu blu blu. Blo avant JC. blo LA blo blo.";
		
		String[] expectedPhrases = new String[]{
				"Bla bla.",
				"Bli bli.",
				"Blou av. b mr. lou\r\nICI blou.",
				"Blu blu blu.",
				"Blo avant JC. blo LA blo blo."
		};
		
		String[] actualPhrases = pattern.split(data);
		
		assertThat(actualPhrases).isEqualTo(expectedPhrases);
		
	}

}
