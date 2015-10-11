package wiki.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import wiki.tool.handler.PhraseHandler;

public class PhraseHandlerTest {

	@Test
	public void testGetPhrases() {
		PhraseHandler phraseHandler = new PhraseHandler();
		//TODO gérer les \r\n
		final String data = "Bla bla. Bli bli. Blou av. b mr. lou. ICI blou. Blu blu blu. Blo avant JC. blo LA blo blo.";
		String[] expectedPhrases = new String[]{
				"Bla bla.",
				"Bli bli.",
				"Blou av. b mr. lou.",
				"ICI blou.",
				"Blu blu blu.",
				"Blo avant JC. blo LA blo blo."
		};
		
		String[] actualPhrases = phraseHandler.getPhrases(data);
		
		assertThat(actualPhrases).isEqualTo(expectedPhrases);
		
	}

}
