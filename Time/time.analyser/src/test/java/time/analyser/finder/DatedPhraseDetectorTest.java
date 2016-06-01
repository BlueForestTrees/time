package time.analyser.finder;

import org.junit.Test;
import time.analyser.DateType;
import time.analyser.DatedPhraseDetector;
import time.analyser.PhrasesAnalyser;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class DatedPhraseDetectorTest {

    private final DatedPhraseDetector datedPhraseDetector;

    public DatedPhraseDetectorTest(){
        datedPhraseDetector = new DatedPhraseDetector();
    }

    @Test
    public void testFindersCount(){
        assertThat(DateType.values().length).as("nombre de finders").isEqualTo(datedPhraseDetector.getFindersArray().length);
    }

    @Test
    public void testNoNullFinder(){
        Arrays.stream(DateType.values()).forEach(finder -> {
            final PhrasesAnalyser actual = datedPhraseDetector.get(finder);
            assertThat(actual).as(finder + " est absent!").isNotNull();
        });
    }

}