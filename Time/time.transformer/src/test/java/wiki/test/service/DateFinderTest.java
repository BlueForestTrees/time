package wiki.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import wiki.config.DateFinderConfig;
import time.repo.bean.Phrase;
import wiki.tool.phrasefinder.DateFinder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DateFinderConfig.class)
public class DateFinderTest{
	
	@Autowired
	private DateFinder milliardFinder;
	
	@Autowired
	private DateFinder millionFinder;
	
	@Autowired
	private DateFinder jcFinder;
	
	@Autowired
	private DateFinder romanFinder;
	
	@Autowired
	private DateFinder enanneFinder;
	
	@Autowired
	private DateFinder annee2DotFinder;

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
		
		
		assertThat(jcFinder.findPhrasesWithDates(phrases)).haveExactly(1, withDate(-3500))
												   		.haveExactly(1, withDate(-2000));
		assertThat(annee2DotFinder.findPhrasesWithDates(phrases)).haveExactly(1, withDate(1777));
		assertThat(enanneFinder.findPhrasesWithDates(phrases)).haveExactly(1, withDate(1571));
		assertThat(milliardFinder.findPhrasesWithDates(phrases)).haveExactly(1, withDate(-2400000000L));
		assertThat(millionFinder.findPhrasesWithDates(phrases)).haveExactly(1, withDate(-65000000));
		assertThat(romanFinder.findPhrasesWithDates(phrases)).haveExactly(1, withDate(1500))
													  .haveExactly(1, withDate(2000));
	}

	private Condition<? super Phrase> withDate(long date) {
		return new Condition<Phrase>() {
			public boolean matches(Phrase phrase) {
				return phrase.getDate() == (long) (date * 364.25);
			}
		};
	}

}
