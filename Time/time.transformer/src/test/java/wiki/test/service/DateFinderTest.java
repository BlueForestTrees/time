package wiki.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import time.repo.bean.Phrase;
import time.transformer.config.DateFinderConfig;
import time.transformer.tool.phrasefinder.DateFinder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DateFinderConfig.class)
public class DateFinderTest {

    @Autowired
    private DateFinder milliardFinder;

    @Autowired
    private DateFinder millionFinder;

    @Autowired
    private DateFinder jcFinder;

    @Autowired
    private DateFinder romanFinder;

    @Autowired
    private DateFinder annee2DotFinder;

    @Autowired
    private DateFinder[] finders;

    @Test
    public void testFindDate() {
        final String[] phrases = { "Néolithique, soit vers 4500 av. J.-C. Il s’agit", "L'invention de la roue est estimée située vers 3500 avant J.-C. à Sumer en basse Mésopotamie.", "Les roues à rayons et à jantes, plus légères, seraient apparues environ 2000 ans av. J.-C..",
                "Les revolvers existent depuis au moins le xvie siècle.", "Au XXIe siècle, la montre se porte majoritairement au poignet, généralement du bras gauche, et est dite à montre-bracelet.", 
                "1777 : l'horloger suisse Abraham Louis Perrelet cràe la à montre à secousses à dite perpàtuelle, souvent considàràe comme la premiàre montre automatique10.", "Il débute par un événement bien connu : la limite Crétacé-Tertiaire, il y a environ 65 millions d'années.",
                "La Grande Oxydation, également appelée catastrophe de l'oxygène ou crise de l'oxygène, est une crise écologique qui a eu lieu il y a environ 2,4 milliards d'années, au Paléoprotérozoïque, dans les océans et l'atmosphère terrestre.",
                "ISBN 978-27491216979 : La longueur du numéro ISBN devrait être 10 ou 13 et non 14 -- 27 avril 2014 à 23:13 (CEST)", "Dans la ville de Çatal Höyük, fondée en 7000 av. J.-C.",
                "En 1571, le comte de Leicester offre un bracelet munie d'une petite montre à la reine élisabeth Ire2."};

        assertThat(jcFinder.findPhrasesWithDates(phrases)).
        hasSize(5).haveExactly(1, withDate(1571)).haveExactly(1, withDate(-4500)).
        haveExactly(1, withDate(-3500)).
        haveExactly(1, withDate(-2000)).
        haveExactly(1, withDate(-7000));
        assertThat(annee2DotFinder.findPhrasesWithDates(phrases)).hasSize(1).haveExactly(1, withDate(1777));
        assertThat(milliardFinder.findPhrasesWithDates(phrases)).haveExactly(1, withDate(-2400000000L));
        assertThat(millionFinder.findPhrasesWithDates(phrases)).haveExactly(1, withDate(-65000000));
        assertThat(romanFinder.findPhrasesWithDates(phrases)).haveExactly(1, withDate(1500)).haveExactly(1, withDate(2000));
    }

    @Test
    public void test1(){
        final String[] data = new String[]{"En 1571, le comte de Leicester offre un bracelet munie d'une petite montre à la reine élisabeth Ire2."};
        assertThat(jcFinder.findPhrasesWithDates(data)).haveExactly(1, withDate(1571).as("La date doit être 1571")).hasSize(1);
    }
    
    @Test
    public void testPasDeSiecle() {
        final String phrase = "Pépin II, roi d’Aquitaine, résiste durant un quart de siècle à Charles le Chauve, le roi de Francie occidentale.";
        assertNotAll(new String[] { phrase });
    }

    private void assertNotAll(String[] phrases) {
        assertThat(finders).isNotEmpty();
        for (DateFinder finder : finders) {
            assertThat(finder.findPhrasesWithDates(phrases)).as(finder + " trouve des phrases").isEmpty();
        }
    }

    @Test
    public void testPbSiecle() {
        final String phrase = "Pépin II, roi d’Aquitaine, résiste durant un quart de siècle à Charles le Chauve, le roi de Francie occidentale.";
        assertNotOne(phrase, romanFinder);
    }

    private void assertNotOne(String phrase, DateFinder finder) {
        assertThat(finder.findPhrasesWithDates(new String[] { phrase })).isEmpty();
    }

    private Condition<? super Phrase> withDate(long date) {
        return new Condition<Phrase>() {
            public boolean matches(Phrase phrase) {
                return phrase.getDate() == (long) (date * 364.25);
            }
        };
    }

}
