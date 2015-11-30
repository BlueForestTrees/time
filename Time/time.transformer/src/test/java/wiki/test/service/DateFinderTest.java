package wiki.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

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
    public void test13(){
        assertNone("en conciërgewoningRonseBlauwesteen 6550° 44′ 38″ Nord");
    }
    
    @Test
    public void test12(){
        assertNone("de Stefano Lonati et Italo Bettiol1965 : Martien 0001 - de");
    }
    
    
    @Test
    public void test11(){
        assertOnly(jcFinder, yearIs(1571), "En 1571, le comte de Leicester offre un bracelet munie d'une petite montre à la reine élisabeth Ire2.");
    }
    @Test
    public void test8(){
        assertOnly(milliardFinder, yearIs(-2400000000L),"La Grande Oxydation, également appelée catastrophe de l'oxygène ou crise de l'oxygène, est une crise écologique qui a eu lieu il y a environ 2,4 milliards d'années, au Paléoprotérozoïque, dans les océans et l'atmosphère terrestre.");
    }
    @Test
    public void test7(){
        assertOnly(millionFinder, yearIs(-65000000),"Il débute par un événement bien connu : la limite Crétacé-Tertiaire, il y a environ 65 millions d'années.");
    }
    @Test
    public void test6(){
        assertOnly(annee2DotFinder, yearIs(1777),"1777 : l'horloger suisse Abraham Louis Perrelet cràe la à montre à secousses à dite perpàtuelle, souvent considàràe comme la premiàre montre automatique10.");
    }
    
    @Test
    public void test5(){
        assertOnly(romanFinder, yearIs(2000),"Au XXIe siècle, la montre se porte majoritairement au poignet, généralement du bras gauche, et est dite à montre-bracelet.");
    }
    
    @Test
    public void test4(){
        assertOnly(romanFinder, yearIs(1500),"Les revolvers existent depuis au moins le xvie siècle.");
    }
    
    @Test
    public void test3(){
        assertOnly(jcFinder, yearIs(-2000),"Les roues à rayons et à jantes, plus légères, seraient apparues environ 2000 ans av. J.-C..");
    }
    
    @Test
    public void test2(){
        assertOnly(jcFinder, yearIs(-3500),"L'invention de la roue est estimée située vers 3500 avant J.-C. à Sumer en basse Mésopotamie.");
    }
    
    @Test
    public void test10(){
        assertOnly(jcFinder, yearIs(-7000), "Dans la ville de Çatal Höyük, fondée en 7000 av. J.-C.");
    }
    @Test
    public void test9(){
        assertNone("ISBN 978-27491216979 : La longueur du numéro ISBN devrait être 10 ou 13 et non 14 -- 27 avril 2014 à 23:13 (CEST)");
    }
    @Test
    public void test0(){
        assertOnly(jcFinder, yearIs(-4500),"Néolithique, soit vers 4500 av. J.-C. Il s’agit");
    }
    
    @Test
    public void test1(){
        assertOnly(jcFinder, yearIs(1571),"En 1571, le comte de Leicester offre un bracelet munie d'une petite montre à la reine élisabeth Ire2.");
    }
    
    @Test
    public void testPasDeSiecle() {
        assertNone("Pépin II, roi d’Aquitaine, résiste durant un quart de siècle à Charles le Chauve, le roi de Francie occidentale.");
    }
    
    @Test
    public void testPasDeCeSiecle() {
        assertNone("Pour justifier le privilège du chapitre et expliquerait son absence du cycle réalisé dans le premier quart de ce siècle.");
    }
    
    @Test
    public void testPasDeChelou() {
        assertNone("fin en 566538 Invention du système décimal.606 Début du règne de Harsavardhana, roi de l'Inde du Nord");
    }   

    @Test
    public void testPbSiecle() {
        final String phrase = "Pépin II, roi d’Aquitaine, résiste durant un quart de siècle à Charles le Chauve, le roi de Francie occidentale.";
        assertNone(new String[]{phrase}, romanFinder);
    }

    private void assertNone(String phrase) {
        assertNone(new String[] { phrase }, finders);
    }
    private void assertNone(String[] phrases, DateFinder[] finders) {
        assertThat(finders).isNotEmpty();
        for (DateFinder finder : finders) {
            assertNone(phrases, finder);
        }
    }
    private void assertNone(String[] phrases, DateFinder finder) {
        assertThat(finder.findPhrasesWithDates(phrases)).as(finder + " trouve des phrases").isEmpty();
    }
    private void assertOnly(final DateFinder finder, Condition<? super Phrase> condition, String phrase) {
        final String[] phrases = new String[]{phrase};
        assertThat(finder.findPhrasesWithDates(phrases)).as(finder + " ne trouve pas de phrases").haveExactly(1, condition);

        final DateFinder[] filteredFinders = Arrays.stream(finders).filter(f -> f != finder).toArray(size -> new DateFinder[size]);
        assertNone(phrases, filteredFinders);
    }
    private Condition<? super Phrase> yearIs(long annee) {
        return new Condition<Phrase>() {
            public boolean matches(Phrase phrase) {
                return phrase.getDate() == (long) (annee * 364.25);
            }
        }.as("année correcte");
    }

}
