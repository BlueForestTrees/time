package wiki.test.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



import org.joda.time.DateTime;
import org.junit.Test;

import wiki.test.BaseTest;

public class FindDateTest extends BaseTest {

	private String[] phrases = {
			"L�invention de la roue est estim�e situ�e vers 3500 avant J.-C. � Sumer en basse M�sopotamie.",
			"Les roues � rayons et � jantes, plus l�g�res, seraient apparues environ 2000 ans av. J.-C..",
			"Les revolvers existent depuis au moins le xvie si�cle.",
			"Au XXIe si�cle, la montre se porte majoritairement au poignet, g�n�ralement du bras gauche, et est dite � montre-bracelet �.",
			"En 1571, le comte de Leicester offre un bracelet munie d'une petite montre � la reine �lisabeth Ire2.",
			"1777 : l'horloger suisse Abraham Louis Perrelet cr�e la � montre � secousses � dite perp�tuelle, souvent consid�r�e comme la premi�re montre automatique10."
	};
	private String[] dates = {
			"3500 avant J.-C.",
			"2000 ans av. J.-C.",
			"xvie si�cle",
			"XXIe si�cle",
			"1571",
			"1777"
	};
	private DateTime[] DateTimes = {
			new DateTime().withYear(-3500),
			new DateTime().withYear(-2000),
			new DateTime().withYear(1500),
			new DateTime().withYear(2000),
			new DateTime().withYear(1571),
			new DateTime().withYear(1777)
	};
	private String[] groupNames = {
			"jc",
			"jc",
			"romain",
			"romain",
			"anneeEn",
			"anneeDot"
	};
	

	@Test
	public void testFindDate(){
		String jcGroupName = "jc";
		String jcGN = "?<"+jcGroupName+">";
		String romainGroupName = "romain";
		String romainGN = "?<"+romainGroupName+">";
		String anneeEnGroupName = "anneeEn";
		String anneeEnGN = "?<"+anneeEnGroupName+">";
		String anneeDotGroupName = "anneeDot";
		String anneeDotGN = "?<"+anneeDotGroupName+">";
		String avantJC = "(vers|environ) ("+jcGN+"(\\d{4})( ans)? (avant|av.) J.-C.)";
		String ou = "|";
		String chiffreRomains = "( ("+romainGN+"([ixvlcdmIXVLCDM]+)e si�cle))";
		String anneeEn = "([Ee]n ("+anneeEnGN+"\\d{4}))";
		String anneeDot = "(("+anneeDotGN+"\\d{4}) :)";
		String datePattern = "("+avantJC + ou + chiffreRomains+ou+anneeEn+ou+anneeDot+")";
		
		String regex = datePattern;
		Pattern findDatePattern = Pattern.compile(regex);
		
		for(int i = 0; i < phrases.length; i++){
			String phrase = phrases[i];
			String expectedDate = dates[i];
			String groupName = groupNames[i];
			Matcher matcher = findDatePattern.matcher(phrase);
			String actualDate = null;
			if(matcher.find()){
				actualDate = matcher.group(groupName);
			}
			assertThat(actualDate).isEqualTo(expectedDate);
		}
	}
		
}
