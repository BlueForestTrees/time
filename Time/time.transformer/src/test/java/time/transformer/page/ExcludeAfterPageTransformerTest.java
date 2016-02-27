package time.transformer.page;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import time.repo.bean.Page;
import time.transformer.page.transformer.WikiExcludeAfterPageTransformer;

public class ExcludeAfterPageTransformerTest {
    @Test
    public void testGetCleanText() {
        final String text = "lksddjfhmooriejfmqorf,lmrBibliographie[,qoeirgpiorjfqùormkjfqùpoierzjfoqiregnmoqesjngqerugjshn";
        final String expected = "lksddjfhmooriejfmqorf,lmr";
        final Page page = new Page();
        page.setText(text);

        final WikiExcludeAfterPageTransformer phraseHandler = new WikiExcludeAfterPageTransformer();
        final String actual = phraseHandler.transform(page).getTextString();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetCleanText2() {
        final String text = "lksddjfhmooriejfmqorf,lmr,qoeirgpiorjfqùormkjfqùpoierzjfoqiregnmoqesjngqerugjshn";
        final String expected = "lksddjfhmooriejfmqorf,lmr,qoeirgpiorjfqùormkjfqùpoierzjfoqiregnmoqesjngqerugjshn";
        final Page page = new Page();
        page.setText(text);

        final WikiExcludeAfterPageTransformer phraseHandler = new WikiExcludeAfterPageTransformer();
        final String actual = phraseHandler.transform(page).getTextString();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetCleanText3() {
        final String text = "lksddjfhmooriejfmqorf,lmr,Liens externes[qoeirgpiorjfqùormkjfqLiens externes[ùpoierzjfoqiregBibliographie[nmoqesjngqerugjshn";
        final String expected = "lksddjfhmooriejfmqorf,lmr,";
        final Page page = new Page();
        page.setText(text);

        final WikiExcludeAfterPageTransformer phraseHandler = new WikiExcludeAfterPageTransformer();
        final String actual = phraseHandler.transform(page).getTextString();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetCleanText4() {
        final String text = "lksddjfhmooriejfmqorf,lmNotes et références[r,Liens externes[qoeirgpiorjfqùormkjfqLiens externes[ùpoierzjfoqiregBibliographie[nmoqesjngqerugjshn";
        final String expected = "lksddjfhmooriejfmqorf,lm";
        final Page page = new Page();
        page.setText(text);

        final WikiExcludeAfterPageTransformer phraseHandler = new WikiExcludeAfterPageTransformer();
        final String actual = phraseHandler.transform(page).getTextString();

        assertThat(actual).isEqualTo(expected);
    }
}
