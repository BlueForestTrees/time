package wiki.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import time.transformer.tool.handler.PhraseHandler;

public class PhraseHandlerTest {

	@Test
	public void testGetPhrases() {
		final PhraseHandler phraseHandler = new PhraseHandler();
		// TODO gérer les \r\n
		final String data = "Bla bla. Bli bli. Blou av. b mr. lou. ICI blou. Blu blu blu. Blo avant JC. blo LA blo blo.";
		String[] expectedPhrases = new String[] { "Bla bla.", "Bli bli.", "Blou av. b mr. lou.", "ICI blou.", "Blu blu blu.", "Blo avant JC. blo LA blo blo." };

		String[] actualPhrases = phraseHandler.getPhrases(data);

		assertThat(actualPhrases).isEqualTo(expectedPhrases);

	}

	@Test
	public void testGetCleanText() {
		final String text = "lksddjfhmooriejfmqorf,lmrBibliographie[,qoeirgpiorjfqùormkjfqùpoierzjfoqiregnmoqesjngqerugjshn";
		final String expected = "lksddjfhmooriejfmqorf,lmr";

		final PhraseHandler phraseHandler = new PhraseHandler();
		final String actual = phraseHandler.getCleanText(text);

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void testGetCleanText2() {
		final String text = "lksddjfhmooriejfmqorf,lmr,qoeirgpiorjfqùormkjfqùpoierzjfoqiregnmoqesjngqerugjshn";
		final String expected = "lksddjfhmooriejfmqorf,lmr,qoeirgpiorjfqùormkjfqùpoierzjfoqiregnmoqesjngqerugjshn";

		final PhraseHandler phraseHandler = new PhraseHandler();
		final String actual = phraseHandler.getCleanText(text);

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void testGetCleanText3() {
		final String text = "lksddjfhmooriejfmqorf,lmr,Liens externes[qoeirgpiorjfqùormkjfqLiens externes[ùpoierzjfoqiregBibliographie[nmoqesjngqerugjshn";
		final String expected = "lksddjfhmooriejfmqorf,lmr,";

		final PhraseHandler phraseHandler = new PhraseHandler();
		final String actual = phraseHandler.getCleanText(text);

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void testGetCleanText4() {
		final String text = "lksddjfhmooriejfmqorf,lmNotes et références[r,Liens externes[qoeirgpiorjfqùormkjfqLiens externes[ùpoierzjfoqiregBibliographie[nmoqesjngqerugjshn";
		final String expected = "lksddjfhmooriejfmqorf,lm";

		final PhraseHandler phraseHandler = new PhraseHandler();
		final String actual = phraseHandler.getCleanText(text);

		assertThat(actual).isEqualTo(expected);
	}

}
