package wiki.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.assertj.core.api.Condition;
import org.junit.Test;

import wiki.entity.Phrase;
import wiki.test.BaseTest;
import wiki.tool.phrasefinder.Datation;

public class PhraseFinderTest extends BaseTest {

	private String[] phrases = {
			"L'invention de la roue est estimée située vers 3500 avant J.-C. à Sumer en basse Mésopotamie.",
			"Les roues à rayons et à jantes, plus légères, seraient apparues environ 2000 ans av. J.-C..",
			"Les revolvers existent depuis au moins le xvie siècle.",
			"Au XXIe siècle, la montre se porte majoritairement au poignet, généralement du bras gauche, et est dite à montre-bracelet.",
			"En 1571, le comte de Leicester offre un bracelet munie d'une petite montre à la reine élisabeth Ire2.",
			"1777 : l'horloger suisse Abraham Louis Perrelet cràe la à montre à secousses à dite perpàtuelle, souvent considàràe comme la premiàre montre automatique10.",
			"Il débute par un événement bien connu : la limite Crétacé-Tertiaire, il y a environ 65 millions d'années.",
			"La Grande Oxydation, également appelée catastrophe de l'oxygène ou crise de l'oxygène, est une crise écologique qui a eu lieu il y a environ 2,4 milliards d'années, au Paléoprotérozoïque, dans les océans et l'atmosphère terrestre." };

	@Test
	public void testFindDate() {
		
		
		assertThat(Datation.JC.findPhrase(phrases)).haveExactly(1, withDate(-3500))
												   .haveExactly(1, withDate(-2000));
		assertThat(Datation.ANNEE2DOT.findPhrase(phrases)).haveExactly(1, withDate(1777));
		assertThat(Datation.ENANNEE.findPhrase(phrases)).haveExactly(1, withDate(1571));
		assertThat(Datation.MILLIARD.findPhrase(phrases)).haveExactly(1, withDate(-2400000000L));
		assertThat(Datation.MILLION.findPhrase(phrases)).haveExactly(1, withDate(-65000000));
		assertThat(Datation.ROMAN.findPhrase(phrases)).haveExactly(1, withDate(1500))
													  .haveExactly(1, withDate(2000));
	}

	private Condition<? super Phrase> withDate(long date) {
		return new Condition<Phrase>() {
			public boolean matches(Phrase phrase) {
				return phrase.getDate() == (long) (date * 364.25);
			}
		};
	}

	private String[] getText(Phrase[] phrases) {
		return Arrays.stream(phrases).map(p -> p.getText()).toArray(String[]::new);
	}
}
