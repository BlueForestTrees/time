package wiki.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import wiki.test.BaseTest;
import wiki.tool.phrasefinder.PhraseFinder;

public class PhraseFinderTest extends BaseTest {

	private String[] phrases = {
			"L'invention de la roue est estimée située vers 3500 avant J.-C. à Sumer en basse Mésopotamie.",
			"Les roues à rayons et à jantes, plus légères, seraient apparues environ 2000 ans av. J.-C..",
			"Les revolvers existent depuis au moins le xvie siècle.",
			"Au XXIe siècle, la montre se porte majoritairement au poignet, généralement du bras gauche, et est dite à montre-bracelet.",
			"En 1571, le comte de Leicester offre un bracelet munie d'une petite montre à la reine élisabeth Ire2.",
			"1777 : l'horloger suisse Abraham Louis Perrelet cràe la à montre à secousses à dite perpàtuelle, souvent considàràe comme la premiàre montre automatique10.",
			"Il débute par un événement bien connu : la limite Crétacé-Tertiaire, il y a environ 65 millions d'années.",
			"La Grande Oxydation, également appelée catastrophe de l'oxygène ou crise de l'oxygène, est une crise écologique qui a eu lieu il y a environ 2,4 milliards d'années, au Paléoprotérozoïque, dans les océans et l'atmosphère terrestre."
	};
	private String[] datesString = {
			"3500 avant J.-C.",
			"2000 ans av. J.-C.",
			"xvie siècle",
			"XXIe siècle",
			"1571",
			"1777",
			"il y a environ 65 millions d'années",
			"il y a environ 2,4 milliards d'années"
	};
	private String[] datesNbString = {
			"3500",
			"2000",
			"xvi",
			"XXI",
			"1571",
			"1777",
			"65",
			"2,4"
	};
	
	private Long[] datesJourLong = getDatesJourLong();
	private Long[] getDatesJourLong() {
		Long[] datesAnnees = {
				-3500L,
				-2000L,
				1500L,
				2000L,
				1571L,
				1777L,
				-65000000L,
				-2400000000L
		};
		List<Long> datesJourLong = new ArrayList<Long>();
		for(Long dateAnnees : datesAnnees){
			Long dateJour = (long)(dateAnnees * 364.25);
			datesJourLong.add(dateJour);
		}
		return datesJourLong.toArray(new Long[datesJourLong.size()]);
	}
	
	@Test
	public void testFindDate(){
		assertThat(PhraseFinder.JC.findPhrase(phrases)).hasSize(2);
		assertThat(PhraseFinder.ANNEE2DOT.findPhrase(phrases)).hasSize(1);
		assertThat(PhraseFinder.ENANNEE.findPhrase(phrases)).hasSize(1);
		assertThat(PhraseFinder.MILLIARD.findPhrase(phrases)).hasSize(1);
		assertThat(PhraseFinder.MILLION.findPhrase(phrases)).hasSize(1);
		assertThat(PhraseFinder.ROMAN.findPhrase(phrases)).hasSize(2);
	}
	
}
