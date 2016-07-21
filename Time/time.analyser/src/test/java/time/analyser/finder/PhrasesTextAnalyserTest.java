package time.analyser.finder;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Ignore;
import org.junit.Test;
import time.analyser.DateType;
import time.analyser.DatedPhraseDetector;
import time.analyser.PhrasesAnalyser;
import time.domain.DatedPhrase;
import time.tool.date.Dates;
import time.tool.string.Strings;


import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// https://fr.wikipedia.org/wiki/Wikimedia_Foundation contient un tableau qui est mal crawlé
// La période qui va des années 70 000 à 30 000 vit l’invention des bateaux
// de communiquer, entre 70 000 et 30 000 ans, constitue

public class PhrasesTextAnalyserTest {

	private final PhrasesAnalyser[] finders;
	private final DatedPhraseDetector datedPhraseDetector;

	public PhrasesTextAnalyserTest() {
		datedPhraseDetector = new DatedPhraseDetector();
		finders = datedPhraseDetector.getFindersArray();
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



	@Test
	public void ilYA15() {
		assertOnly(DateType.ILYA, yearIs(-2000), "Le chow-chow est une race de chien qui s'est déployée en Chine il y a environ 4000 ans.");
	}

	@Test
	public void ilYA14() {
		assertOnly(DateType.ILYA, ilyaYearIs(-12000), "Tombe vieille de 12 000 ans découverte dans le nord d’Israël.");
	}

	@Test
	public void ilYA11() {
		assertOnly(DateType.ILYA, yearIs(-3000), "La Grande Encyclopédie soviétique affirme que le soja est originaire de Chine ; il y a environ 5000 ans.");
	}

	@Test
	public void ilYA1() {
		assertOnly(DateType.ILYA, ilyaYearIs(-400000), "Voici 400 000 ans seulement que plusieurs espèces d’hommes ont commencé à chasser régulièrement le gros gibier");
	}

	@Test
	public void ilYA2() {
		assertOnly(DateType.ILYA, ilyaYearIs(-600000), "Voici 600000 ans seulement que plusieurs espèces d’hommes ont commencé à chasser régulièrement le gros gibier");
	}

	@Test
	public void ilYA3() {
		assertOnly(DateType.ILYA, ilyaYearIs(-800000), "Il y a 800 000 ans, déjà, certaines espèces humaines faisaient peut-être, à l’occasion, du feu.");
	}

	@Test
	public void ilYA4() {
		assertOnly(DateType.ILYA, ilyaYearIs(-300000), "Voici environ 300 000 ans, Homo erectus, les Neandertal et les ancêtres d’Homo sapiens faisaient quotidiennement du feu");
	}

	@Test
	public void ilYA5() {
		assertOnly(DateType.ILYA, ilyaYearIs(-150000), "Malgré les bénéfices du feu, les humains d’il y a 150 000 ans étaient encore des créatures marginales.");
	}

	@Test
	public void ilYA8() {
		assertOnly(DateType.ILYA, ilyaYearIs(-50000), "Les derniers restes d’Homo soloensis voici 50 000 ans.");
	}

	@Test
	public void ilYA9() {
		assertOnly(DateType.ILYA, ilyaYearIs(-50000), "Les derniers restes d’Homo soloensis voici environ 50 000 ans.");
	}

	@Test
	public void ilYA10() {
		assertOnly(DateType.ILYA, ilyaYearIs(-50000), "Les derniers restes d’Homo soloensis vieux de 50 000 ans.");
	}

	@Test
	public void ilYA12() {
		assertOnly(DateType.ILYA, ilyaYearIs(-50000), "Les derniers restes d’Homo soloensis datent d'environ 50 000 ans.");
	}

	@Test
	public void ilYA6() {
		assertOnly(DateType.ILYA, ilyaYearIs(-50000), "Les derniers restes d’Homo soloensis vieux d'environ 50 000 ans.");
	}

	@Test
	public void ilYA7() {
		assertOnly(DateType.ILYA, ilyaYearIs(-40000), "L’Homo denisova disparut peu après, il y a quelque 40 000 ans.");
	}

    @Test
	public void ilYAquelque21() {
		assertOnly(DateType.ILYA, ilyaYearIs(-70000), "Sapiens moderne a acquises voici quelque 70 millénaires lui");
	}


	@Test
	public void ilYA16() {
		assertOnly(DateType.ILYA, ilyaYearIs(-15000), "mais nous avons des preuves incontestables de la domestication des chiens il y a près de 15 000 ans.");
	}

	@Test
	public void ilYA17() {
		assertOnly(DateType.ILYA, ilyaYearIs(-30000), "des outils ne s’est pas sensiblement améliorée au cours des 30 000 dernières années.");
	}

	@Test
	public void ilYA18() {
		assertOnly(DateType.ILYA, ilyaYearIs(-30000), "intercontinentaux pourvus d’ogives nucléaires alors que, voici 30 000 ans, nous n’avions");
	}

	@Test
	public void ilYA19() {
		assertOnly(DateType.ILYA, ilyaYearIs(-80), "il y a 80 ans");
	}

	@Test
	public void ilYA20() {
		assertOnly(DateType.ILYA, ilyaYearIs(-45000), "Les villages de pêche ont pu apparaître sur les côtes des îles indonésiennes dès 45 000 ans.");
	}

	@Test
	public void near0() {
		assertOnly(DateType.NEARJC2, yearIs(2000), "Vers l'an 2000, gros bug.");
	}

	@Test
	public void precise1() {
		assertOnly(DateType.PRECISE, dateIs(date(9, Month.MARCH, 1968)), "Il a disputé son premier test match le 9 Mars 1968, contre l'équipe d'Irlande, et son dernier test match fut contre l'équipe d'Australie.");
	}

	@Test
	public void precise2() {
		assertOnly(DateType.PRECISE, dateIs(date(9, Month.MARCH, 968)), "Il a disputé son premier test match le 9 mars 968, contre l'équipe d'Irlande, et son dernier test match fut contre l'équipe d'Australie.");
	}

	@Test
	public void precise3() {
		assertTwo(DateType.PRECISE, dateIs(date(9, Month.MARCH, 1968)), dateIs(date(21, Month.JUNE, 1969)), "\"Il a disputé son premier test match le 9 mars 1968, contre l'équipe d'Irlande, et son dernier test match fut contre l'équipe d'Australie, le 21 juin 1969.");
	}

	@Test
	public void precise4() {
		assertOnly(DateType.PRECISE, dateIs(date(9, Month.FEBRUARY, 968)), "Il a disputé son premier test match le 9 fév. 968, contre l'équipe d'Irlande, et son dernier test match fut contre l'équipe d'Australie.");
	}

	@Test
	public void precise5() {
		assertOnly(DateType.PRECISE, dateIs(date(9, Month.DECEMBER, 968)), "Il a disputé son premier test match le 9 déc. 968, contre l'équipe d'Irlande, et son dernier test match fut contre l'équipe d'Australie.");
	}

	@Test
	public void precise6() {
		assertOnly(DateType.PRECISE, dateIs(date(9, Month.DECEMBER, 968)), "9 déc. 968, contre l'équipe d'Irlande, et son dernier test match fut contre l'équipe d'Australie.");
	}

	@Test
	public void precise7() {
		assertOnly(DateType.PRECISE, dateIs(date(1, Month.JUNE, 2014)), "En juin 2014, Alibaba achète les 34 % qu'il ne détient pas dans UCWeb, entreprise chinoise de services pour l'internet mobile, opération valorisant UCWeb (en) à 1,9 milliard d'euros");
	}

	@Test
	public void precise8() {
		assertTwo(DateType.PRECISE, dateIs(date(1, Month.FEBRUARY, 1919)), dateIs(date(1, Month.MARCH, 1921)), "La guerre soviéto-polonaise, ou guerre russo-polonaise (février 1919 - mars 1921) est l'une des conséquences de la Première Guerre mondiale.");
	}

	@Test
	public void precise9() {
		assertTwo(DateType.PRECISE, dateIs(date(1, Month.FEBRUARY, 1919)), dateIs(date(1, Month.MARCH, 1921)), "La guerre soviéto-polonaise, ou guerre russo-polonaise (février 1919-mars 1921) est l'une des conséquences de la Première Guerre mondiale.");
	}

	@Test
	public void precise11() {
		assertOnly(DateType.PRECISE, dateIs(date(1, Month.DECEMBER, 1935)), "Woody Allen, est un réalisateur, scénariste, acteur et humoriste américain, né le 1er décembre 1935 à New York.");
	}

	@Test
	public void jc27() {
		assertOnly(DateType.JC, yearIs(1790), "L'invention du Camenbert date de 1790");
	}

	@Test
	public void precise10() {
		assertOnly(DateType.PRECISE, dateIs(date(1, Month.FEBRUARY, 1919)), "La guerre soviéto-polonaise, ou guerre russo-polonaise (février 1919) est l'une des conséquences de la Première Guerre mondiale.");
	}

	@Test
	public void twoDot1() {
		assertOnly(DateType.ANNEE2DOT, yearIs(1650), "1650: retour vers le futur.");
	}

	@Test
	public void twoDot2() {
		assertOnly(DateType.ANNEE2DOT, yearIs(1650), "En 1650: retour vers le futur.");
	}

	@Test
	public void twoDot3() {
		assertOnly(DateType.ANNEE2DOT, yearIs(1777), "1777 : l'horloger suisse Abraham Louis Perrelet cràe la à montre à secousses à dite perpàtuelle, souvent considàràe comme la premiàre montre automatique10.");
	}

	@Test
	public void jc17() {
		assertOnly(DateType.JC, yearIs(-3100), "En 3100 avant notre ère, toute la vallée du Nil inférieur était unie dans le premier royaume égyptien.");
	}

	@Test
	public void jc18() {
		assertOnly(DateType.JC, yearIs(-350), "En 350 avant notre ère, aucun Romain n’aurait imaginé faire voile droit sur la Grande-Bretagne et la conquérir.");
	}

	@Test
	public void jc20() {
		assertOnly(DateType.JC, yearIs(1873), "En 1873, Jules Verne imagina que Phileas Fogg, riche aventurier britannique, pourrait faire le tour du monde en 80 jours.");
	}

	@Test
	public void jc0() {
		assertOnly(DateType.JC, yearIs(1827),
				"En 1827, dans une Encyclopédie allemande Nitzsch propose la création de nouveaux genres de paramécies (« M. Dutrochet, en France, avait étudié les Rotifères et les Tubicolaires; M. Leclerc avait fait connaître les Difflugies; et Losana , en Italie , avait décrit des Amibes, des Kolpodes et des Cyclides dont il multipliait les espèces sans raison et sans mesure », selon Dujardin (1841).");
	}

	@Test
	public void jc1() {
		assertOnly(DateType.JC, yearIs(1650), "En 1650, retour vers le futur.");
	}

	@Test
	public void jc3() {
		assertOnly(DateType.JC, yearIs(1571), "En 1571, le comte de Leicester offre un bracelet munie d'une petite montre à la reine élisabeth Ire2.");
	}

	@Test
	public void jc4() {
		assertOnly(DateType.JC, yearIs(-1600), "En 1600 avant J.C., le comte de Leicester offre un bracelet munie d'une petite montre à la reine élisabeth Ire2.");
	}

	@Test
	public void nearJc5() {
		assertOnly(DateType.NEARJC, yearIs(-2000), "Les roues à rayons et à jantes, plus légères, seraient apparues environ 2000 ans av. J.-C..");
	}

	@Test
	public void nearJc5bis() {
		assertOnly(DateType.NEARJC, yearIs(-2001), "Les roues à rayons et à jantes, plus légères, seraient apparues environ 2001 ans av. J.-C..");
	}

	@Test
	public void jc6() {
		assertOnly(DateType.NEARJC3, yearIs(-3500), "L'invention de la roue est estimée située vers 3500 avant J.-C. à Sumer en basse Mésopotamie.");
	}

	@Test
	public void jc7() {
		assertOnly(DateType.JC, yearIs(-7000), "Dans la ville de Çatal Höyük, fondée en 7000 av. J.-C.");
	}

	@Test
	public void jc8() {
		assertOnly(DateType.NEARJC3, yearIs(-4500), "Néolithique, soit vers 4500 av. J.-C. Il s’agit");
	}

	@Test
	public void jc9() {
		assertOnly(DateType.JC, yearIs(1571), "En 1571, le comte de Leicester offre un bracelet munie d'une petite montre à la reine élisabeth Ire2.");
	}

	@Test
	public void jc10() {
		assertOnly(DateType.NEARJC3, yearIs(-2300), "La civilisation olmèque a débuté avec une production en abondance de poterie, vers 2300 avant notre ère");
	}

	@Test
	public void nearLess12() {
		assertOnly(DateType.NEARLESS, yearIs(-5300), "Des indices de culture de céréales et de domestication du chien, du cochon, du mouton et de la chèvre dans la région de Tétouan, remontant à environ -5300, constituent la plus ancienne trace connue d'agriculture en Afrique du Nord.");
	}

	@Test
	public void jc13() {
		assertOnly(DateType.NEARJC3, yearIs(-3500), "au sein des chasseurs-cueilleurs de la culture Botaï, vers 3500 av. J.-C., cool.");
	}

	@Test
	public void jc15() {
		assertOnly(DateType.PRECISE, dateIs(date(17, Month.JANUARY, -3380)), "L' éclipse de Lune est celle du 17 janvier 3380 av. J.-C., qui aurait été décrite par les Mayas en Amérique centrale.");
	}

	@Test
	public void jc16() {
		assertOnly(DateType.JC, yearIs(-500), "A partir de l'an 500 avant J.C., il s'est passé des choses.");
	}

	@Test
	public void jc19() {
		assertOnly(DateType.APRES, yearIs(1990), "Or, après 1990, on a eu la surprise de constater le contraire.");
	}

	@Test
	public void jc22() {
		assertOnly(DateType.JC, yearIs(500), "date de 500, il s'est passé des choses.");
	}

	@Test
	public void jc25() {
		assertOnly(DateType.JC, yearIs(1500), "cela apparaît à partir de 1500.");
	}

	@Test
	public void jc29() {
		assertOnly(DateType.JC, yearIs(2012), "Début 2012, j’étais en Champagne avec mon ami Michel Guillard");
	}

	@Test
	public void jc30() {
		assertOnly(DateType.JC, yearIs(1904), "Le sang versé en 1904 aurait épargné.");
	}

//	@Test
//	public void tiret() {
//		assertTwoDifferents(DateType.TIRET, yearIs(1870), DateType.TIRET2, yearIs(1871), "dont une édition populaire de la guerre franco allemande de 1870-1871.");
//	}

	@Test
	public void tiret2() {
		assertNoDateIn("dont une édition populaire de la guerre franco allemande de 0870-1871.");
	}

	@Test
	public void entre() {
		assertTwoDifferents(DateType.JC_ENTRE, yearIs(1909), DateType.JC_ENTRE_SUITE, yearIs(1910), "Entre 1909 et 1910, ma situation s'était modifiée et je n'avais plus à gagner ma vie comme manœuvre.");
	}

    @Test
	public void entre2() {
		assertTwoDifferents(DateType.JC_ENTRE, yearIs(1960), DateType.JC_ENTRE_SUITE, yearIs(1975), "De 1960 à 1975, le Grand Larousse encyclopédique en 10 volumes et 2 suppléments est publié, avec un logo dessiné par Jean Picart Le Doux.");
	}


	@Test
	public void jc23() {
		assertOnly(DateType.JC, yearIs(1500), "cela apparaît à partir de 1500 souvent.");
	}

	@Test
	public void jc21() {
		assertOnly(DateType.JC, yearIs(1769), "L'invention de l'automobile date de 1769 (Charles Cugnot).");
	}

	@Test
	public void jc24() {
		assertOnly(DateType.JC, yearIs(-500), "A partir de l'an 500 avant J.C. pour la plupart il s'est passé des choses.");
	}

	@Test
	public void jc28() {
		assertOnly(DateType.JC, yearIs(1456), "Les chats sont bleus depuis 1456.");
	}

    @Test
    public void jc31(){
        assertOnly(DateType.JC, yearIs(1725),"Les Quatre Saisons de Vivaldi datent de 1725.");
    }

    @Test
    public void jc26() {
        assertOnly(DateType.JC_PREV, yearIs(1960), "du Paraguay jusque dans les années 1960, offrent un aperçu");
    }

    @Test
    public void jc32(){
        assertOnly(DateType.JC_PREV, yearIs(1980),"Hong Kong est divisé dans les années 1980 en 18 districts.");
    }

	@Test
	public void testMilliard0() {
		assertOnly(DateType.MILLIARD, milliardYearIs(1), "Il débute par un événement bien connu : la limite Crétacé-Tertiaire, il y a environ 1 milliard d'années.");
	}

	@Test
	public void testMilliard1() {
		assertOnly(DateType.MILLIARD, milliardYearIs(2.4d), "La Grande Oxydation, également appelée catastrophe de l'oxygène ou crise de l'oxygène, est une crise écologique qui a eu lieu il y a environ 2,4 milliards d'années, au Paléoprotérozoïque, dans les océans et l'atmosphère terrestre.");
	}

	@Test
	public void testMilliard2() {
		assertOnly(DateType.MILLIARD, milliardYearIs(25.4d), "Super il y a environ 25,4 milliards d'années, à une autre époque.");
	}

	@Test
	public void testMilliard3() {
		assertOnly(DateType.MILLIARD, milliardYearIs(25.49d), "Super il y a environ 25,49 milliards d'années, à une autre époque.");
	}

	@Test
	public void testMilliard4() {
		assertOnly(DateType.MILLIARD, milliardYearIs(25.49d), "Super il y a plus de 25,49 milliards d'années, à une autre époque.");
	}

	@Test
	public void testMilliard5() {
		assertOnly(DateType.MILLIARD, milliardYearIs(25.49d), "Super il y a plus d'25,49 milliards d'années, à une autre époque.");
	}

	@Test
	public void testMillion1() {
		assertOnly(DateType.MILLIARD, millionYearIs(65), "Il débute par un événement bien connu : la limite Crétacé-Tertiaire, il y a environ 65 millions d'années.");
	}

	@Test
	public void testMillion4() {
		assertOnly(DateType.MILLIARD, millionYearIs(100), "Les abeilles sont apparues il y a 100 millions d'années.");
	}

	@Test
	public void testMillion2() {
		assertOnly(DateType.MILLIARD, millionYearIs(2.5d), "Il débute par un événement bien connu : la limite Crétacé-Tertiaire, voici environ 2,5 millions d'années, issus d’un genre antérieur de singe,");
	}

	@Test
	public void testMillion3() {
		assertOnly(DateType.MILLIARD, millionYearIs(2.5d), "Les humains sont apparus en Afrique de l’Est voici environ 2,5 millions d'années, issus d’un genre antérieur de singe,");
	}

	@Test
	public void testMillion5() {
		assertOnly(DateType.MILLIARD, millionYearIs(3d), "Les humains sont apparus en Afrique de l’Est voici environ trois millions d’années, issus d’un genre antérieur de singe,");
	}

	@Test
	public void testMillion6() {
		assertOnly(DateType.MILLIARD, milliardYearIs(-5d), "Notre Soleil, selon nos calculs, va mourir dans environ cinq milliards d’années.");
	}

	@Test
	public void testMillion7() {
		assertOnly(DateType.MILLIARD, milliardYearIs(-5d), "Jusqu’à la mort du Soleil dans cinq milliards d’années environ.");
	}

	@Test
	public void testMillion8() {
		assertOnly(DateType.MILLIARD, milliardYearIs(4.5d), "au moment de la naissance du Soleil, il y a 4,5 milliards d’années pendant le degel.");
	}

	@Test
	public void testMillion9() {
		assertOnly(DateType.MILLIARD, milliardYearIs(4.5d), "Par exemple, pour savoir comment était l’Univers au moment de la naissance du Soleil, il y a 4,5 milliards d’années, il suffit d’observer des astres qui sont à 5 milliards d’années-lumière de nous.");
	}

	@Test
	public void roman1000() {
		assertOnly(DateType.ROMAN, yearIs(-2000), "Daté du XXIe siècle av. J.-C., a été retrouvé dans les environs un ostracon sur lequel");
	}

	@Test
	public void roman0() {
		assertOnly(DateType.ROMAN, yearIs(-700), "Daté du VIIIe siècle av. J.-C., a été retrouvé dans les environs un ostracon sur lequel");
	}

	@Test
	public void roman1() {
		assertOnly(DateType.ROMAN, yearIs(2000), "Au XXIe siècle, la montre se porte majoritairement au poignet, généralement du bras gauche, et est dite à montre-bracelet.");
	}

	@Test
	public void roman2() {
		assertOnly(DateType.ROMAN, yearIs(1500), "Les revolvers existent depuis au moins le xvie siècle.");
	}

	@Test
	public void roman3() {
		assertOnly(DateType.ROMAN, yearIs(1700), "des rabbins juifs polonais du XVIIIe siècle aux puritains brûleurs");
	}

	@Test
	public void roman4() {
		assertOnly(DateType.ROMAN, yearIs(1600), "Elle avait pris l’habitude depuis le XVIIe siècle et");
	}

	@Test
	public void none1() {
		assertNoDateIn("ISBN 978-27491216979 : La longueur du numéro ISBN devrait être 10 ou 13 et non 14 -- 27  2014 à 23:13 (CEST)");
	}

	@Test
	public void none2() {
		assertNoDateIn("Pépin II, roi d’Aquitaine, résiste durant un quart de siècle à Charles le Chauve, le roi de Francie occidentale.");
	}

	@Test
	public void none3() {
		assertNoDateIn("Pour justifier le privilège du chapitre et expliquerait son absence du cycle réalisé dans le premier quart de ce siècle.");
	}

	@Test
	public void none5() {
		assertNoDateIn("Pépin II, roi d’Aquitaine, résiste durant un quart de siècle à Charles le Chauve, le roi de Francie occidentale.");
	}

	@Test
	public void none6() {
		assertNoDateIn("en conciërgewoningRonseBlauwesteen 6550° 44′ 38″ Nord");
	}

	@Test
	public void none7() {
		assertNoDateIn("Leur système de base 6 nous a laissé plusieurs héritages importants, comme la division du jour en 24 heures et celle du cercle en 360 degrés.) L’autre type de signes représentait des hommes, des animaux, des marchandises, des territoires, des dates et ainsi de suite.");
	}

	@Test
	public void none8() {
		assertNoDateIn("mais c'est pas pour de suite, j'ai/nous avons d'abord environ 3500 autres articles à traduire avant d'arriver à celui-là !");
	}

	@Test
	public void none9() {
		assertNoDateIn("Il serait d'origine alsacienne et deux parties sont manquantes : les vers 5479 à 5624 et les vers 7524 à 7716");
	}

	@Test
	public void none10() {
		assertNoDateIn("Il pouvait accueillir environ 9000 spectateurs répartis dans une cavea sem");
	}

	@Test
	public void none13() {
		assertNoDateIn("Il pouvait accueillir en 9000 spectateurs répartis dans une cavea sem");
	}

	@Test
	public void none11() {
		assertNoDateIn("de Stefano Lonati et Italo Bettiol1965 : Martien 0001 - de");
	}

	@Test
	public void none12() {
		assertNoDateIn("Les chasseurs-cueilleurs du Kalahari – ne travaillent en moyenne que 35 à 45 heures par semaine.");
	}

	@Test
	public void none14() {
		assertNoDateIn(" briller pendant dix milliards d’années, plus de problèmes avec l’âge des dinosaures !");
	}

	@Test
	public void none15() {
		assertNoDateIn("Nous avons sous les yeux toutes les étapes de la vie de notre Soleil, son passé et son avenir, sans avoir à attendre les cinq milliards d’années de la fin de sa vie.");
	}

	@Test
	public void none16() {
		assertNoDateIn("Une variété d’uranium, l’uranium-235, se casse en moyenne après un milliard d’années.");
	}

	@Test
	public void none17() {
		assertNoDateIn("Ce direct fermera exceptionnellement un peu après 20 heures, Didier Deschamps");
	}

	@Test
	public void none18() {
		assertNoDateIn("mur d'enceinte et poterne », au profit dudit comte, pour environ 3600 m² par an");
	}


	@Test
	public void none19() {
		assertNoDateIn("Portail comptant entre 100 et 999 articlesPage de portail ou de projet thématique");
	}

    @Test
    public void none20() {
        assertNoDateIn("De la même manière, la grande majorité des vandalismes détectés avaient été corrigés après 15 révisions au plus.");
    }


    @Test
    public void none21() {
        assertNoDateIn("Le groupe BCC s'organise aujourd'hui en 16 fédérations sur le territoire italien.");
    }

    @Test
    public void none22() {
        assertNoDateIn("Après 18 jours de combats acharnés");
    }

	@Test
	public void none23() {
		assertNoDateIn("Sa fréquence d'horloge est ralentie de 25 à 12,5 MHz");
	}

	@Test @Ignore
	public void none24() {
		assertNoDateIn("à un nouveau-né tous les un à six ans, après 10 à 17 mois de gestation");
	}

	@Test
	public void none25() {
		assertNoDateIn("a plus fréquentée, la ligne 50, relie Aix et Marseille en 30 à 65 minutes suivant");
	}


	@Test
	public void precise12() {
		assertOnly(DateType.PRECISE, dateIs(date(10, Month.FEBRUARY, 1984)), "Slimane Médini est né le 10 Février 1984 à Gournay-en-bray");
	}

	private final void assertNoDateIn(String phrase) {
		assertNoDateIn(phrase, finders);
	}

	private final void assertNoDateIn(String phrases, PhrasesAnalyser[] finders) {
		Assertions.assertThat(finders).isNotEmpty().as("pas de finders");
		for (PhrasesAnalyser finder : finders) {
			assertNoDateIn(phrases, finder);
		}
	}

	private final void assertNoDateIn(String phrase, PhrasesAnalyser finder) {
		assertThat(finder.findDatesInPhrases(phrase)).as("Ne devrait pas trouver de phrase: " + finder + " dans \"" + phrase + "\"").isEmpty();
	}

	private final void assertOnly(final DateType dateTypeName, Condition<? super DatedPhrase> condition, String phrase) {
        final PhrasesAnalyser finder = datedPhraseDetector.get(dateTypeName);
		assertThat(finder.findDatesInPhrases(phrase)).as("Devrait trouver des phrases: " + finder + " dans \"" + phrase + "\"").haveExactly(1, condition);

		final PhrasesAnalyser[] filteredFinders = Arrays.stream(finders).filter(f -> f != finder).toArray(size -> new PhrasesAnalyser[size]);
		assertNoDateIn(phrase, filteredFinders);
	}

	private final void assertTwo(final DateType dateTypeName, Condition<? super DatedPhrase> condition1, Condition<? super DatedPhrase> condition2, String phrase) {
        final PhrasesAnalyser finder = datedPhraseDetector.get(dateTypeName);
        final List<DatedPhrase> actualPhrases = finder.findDatesInPhrases(phrase);

		assertThat(actualPhrases).as("doit trouver deux dates").hasSize(2);

		assertThat(actualPhrases.get(0)).as("première date").has(condition1);
		assertThat(actualPhrases.get(1)).as("deuxième date").has(condition2);

		final PhrasesAnalyser[] filteredFinders = Arrays.stream(finders).filter(f -> f != finder).toArray(size -> new PhrasesAnalyser[size]);
		assertNoDateIn(phrase, filteredFinders);
	}

    private final void assertTwoDifferents(final DateType type1, Condition<? super DatedPhrase> sameDate1, final DateType type2, Condition<? super DatedPhrase> sameDate2, String phrase) {
        final PhrasesAnalyser finder1 = datedPhraseDetector.get(type1);
        final List<DatedPhrase> actualPhrases = finder1.findDatesInPhrases(phrase);
		final PhrasesAnalyser finder2 = datedPhraseDetector.get(type2);
        final List<DatedPhrase> actualPhrases2 = finder2.findDatesInPhrases(phrase);

		assertThat(actualPhrases).as(finder1 + "Devrait trouver une date dans \"" + phrase +"\"").hasSize(1);
		assertThat(actualPhrases.get(0)).as("première date").has(sameDate1);
		assertThat(actualPhrases2).as(finder2 + "Devrait trouver une date").hasSize(1);
        assertThat(actualPhrases2.get(0)).as("deuxième date").has(sameDate2);


        final PhrasesAnalyser[] filteredFinders = Arrays.stream(finders).filter(f -> (f != finder1 && f != finder2)).toArray(size -> new PhrasesAnalyser[size]);
        assertNoDateIn(phrase, filteredFinders);
    }

	private final Condition<? super DatedPhrase> milliardYearIs(double expectedAnnee) {
		final long expectedJour = Dates.milliardsToDays(expectedAnnee);
		return new Condition<DatedPhrase>() {
			public boolean matches(DatedPhrase phrase) {
				return phrase.getDate() == expectedJour;
			}
		}.as("jours correct : " + expectedJour);
	}

	private final Condition<? super DatedPhrase> millionYearIs(double expectedAnnee) {
		final long expectedJour = Dates.millionsToDays(expectedAnnee);
		return new Condition<DatedPhrase>() {
			public boolean matches(DatedPhrase phrase) {
				return phrase.getDate() == expectedJour;
			}
		}.as("jours correct : " + expectedJour);
	}

	private final Condition<? super DatedPhrase> yearIs(int expectedAnnee) {
		final long expectedJour = Dates.toDays(expectedAnnee);
		return new Condition<DatedPhrase>() {
			public boolean matches(DatedPhrase phrase) {
				return phrase.getDate() == expectedJour;
			}
		}.as("jours correct : " + expectedJour);
	}

	private final Condition<? super DatedPhrase> ilyaYearIs(int expectedAnnee) {
		final long expectedJour = Dates.ilyaToDays(expectedAnnee);
		return new Condition<DatedPhrase>() {
			public boolean matches(DatedPhrase phrase) {
				return phrase.getDate() == expectedJour;
			}
		}.as("il y a jours correct : " + expectedJour);
	}

	private final Condition<? super DatedPhrase> dateIs(final LocalDate expectedDate) {
		long expectedJour = Dates.toDays(expectedDate);
		return new Condition<DatedPhrase>() {
			public boolean matches(DatedPhrase phrase) {
				return phrase.getDate() == expectedJour;
			}
		}.as("expectedJour : " + expectedJour);
	}

	@Test
	public void testPreparePhrase() {
		testPreparePhrase("AAA[g]BBB[sdfg]CCC", "AAABBBCCC");
		testPreparePhrase("sdf[g]seef[sdfg]", "sdfseef");
		testPreparePhrase("[tt]sdf[g]seef[sdfg]sddf", "sdfseefsddf");

	}

	private final void testPreparePhrase(final String text, final String expectedText) {
		final String actualText = Strings.clean(text);
		assertThat(actualText).as("phrase mal préparée").isEqualTo(expectedText);
	}

	@Test
	public void testToJours() {
		testToJours(date(1, Month.JANUARY, 1970), Dates.seventiesInDays);
		testToJours(date(16, Month.JANUARY, 1970), Dates.seventiesInDays + 15);
		testToJours(date(16, Month.FEBRUARY, 1970), Dates.seventiesInDays + 15 + 31);
		testToJours(date(16, Month.FEBRUARY, 1971), Dates.seventiesInDays + 15 + 31 + 365);
		testToJours(date(1, Month.JANUARY, 0), 0);
	}

	private final void testToJours(final LocalDate localDate, final long expectedJours) {
		final long actualJours = Dates.toDays(localDate);
		assertThat(actualJours).isEqualTo(expectedJours);
	}

	private final LocalDate date(final int dayOfMonth, final Month month, final int year) {
		return LocalDate.of(year, month, dayOfMonth);
	}

}
