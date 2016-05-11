package time.liveparse.find;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class DatedPhrasesFindersTest {

    private final DatedPhrasesFinders datedPhrasesFinders;

    public DatedPhrasesFindersTest(){
        datedPhrasesFinders = new DatedPhrasesFinders();
    }

    @Test
    public void testFindersCount(){
        assertThat(Finder.values().length).as("nombre de finders").isEqualTo(datedPhrasesFinders.getFindersArray().length);
    }

    @Test
    public void testNoNullFinder(){
        Arrays.stream(Finder.values()).forEach(finder -> {
            final DatedPhrasesFinder actual = datedPhrasesFinders.get(finder);
            assertThat(actual).as(finder + " est absent!").isNotNull();
        });
    }

}
