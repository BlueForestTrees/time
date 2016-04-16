package time.transformer.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import time.transformer.phrase.finder.PhraseFinder;
import time.transformer.phrase.finder.parser.AnneeParser;
import time.transformer.phrase.finder.parser.IParser;
import time.transformer.phrase.finder.parser.IlYAParser;
import time.transformer.phrase.finder.parser.JCParser;
import time.transformer.phrase.finder.parser.MilliardParser;
import time.transformer.phrase.finder.parser.PreciseParser;
import time.transformer.phrase.finder.parser.RomanParser;

public class DateFindersFactory {

	public final static String TEXT_NUMBERS = "un|deux|trois|quatre|cinq|six|sept|huit|neuf|dix|onze|douze|treize|quatorze|quinze|seize|dix-sept|dix-huit|dix-neuf|vingt";
	private static final String START = "(^| |,|;)";
	private final static String JOUR = "((?<d>\\d{1,2})(er)? )?";
	private final static String MOIS = "(?<m>(J|j)an(\\.|v\\.|vier)|(F|f)(é|e)v(\\.|rier)|(M|m)ar(\\.|s)|(A|a)vr(\\.|il)|(M|m)ai|(J|j)uin|(J|j)uil(\\.|let)|(A|a)o(u|û)(\\.|t)|(S|s)ep(\\.|t\\.|tembre)|(O|o)ct(\\.|obre)|(N|n)ov(\\.|embre)|(D|d)(é|e)c(\\.|embre))";
	private final static String ANNEE = " (?<y>\\d{3,4})";
	private final static String REF = "(?<neg> (avant|av.) (J.-?C.|notre ère|le présent))";
	private final static String TEXT_NUMBERS_ = "(?<gt>(" + TEXT_NUMBERS + "))";
	private final static String NUMBERS = "(?<g>\\d+([,\\.]\\d+)?)";
	private static final String ILYAENVIRON = "([Ii]l y a|[Vv]oici|(datent|vie(ux|ille)) d(e)?|au cours des|dès)([ ']environ)?( quelque)?( près de)?";
	private static final String EXCLUDEDS = "(?<ex>(degré|coup|pa|tour|heure|minute|seconde|mois)s?)?";

	private Map<String, PhraseFinder> finders;

	public DateFindersFactory() {
		finders = new HashMap<>();
		build("(?<neg>" + ILYAENVIRON + ") (?<g>\\d{1,3}( ?000)?) (ans|dernières années)", "ilYAFinder", new IlYAParser());
		build("(" + NUMBERS + "|" + TEXT_NUMBERS_ + ") milli(?<s>ard|on)s? d['’]années", "milliardFinder", new MilliardParser());
		build(START + "([Aà] partir de|date de|depuis|[Ee]n|dans les années) (l'an )?(?<g>(-)?\\d{2,9})" + REF + "?(;|,|\\.| " + EXCLUDEDS + "|$)", "jcFinder", new JCParser());
		build(START + "([Ee]nviron) (?<g>\\d{2,4})(,? )ans" + REF, "nearJcFinder", new JCParser());
		build(START + "([Vv]ers l'an|après) (?<g>(-)?\\d{2,4})(,?)" + REF + "?", "nearJcFinder2", new JCParser());
		build(START + "[Vv]ers (?<g>\\d{2,4})" + REF, "nearJcFinder3", new JCParser());
		build(START + "([Ee]nviron) (?<neg>-)(?<g>\\d{2,4})", "nearLessFinder", new JCParser());
		build(" (?<g>[ixvIXV]+)e siècle" + REF + "?", "romanFinder", new RomanParser());
		build("^(([Vv]ers|[Ee]nviron|[Ee]n) )?(?<g>([ -])?\\d{4}) ?:", "annee2DotFinder", new AnneeParser());
		build("(?<g>" + JOUR + MOIS + ANNEE + ")" + REF + "?", "preciseFinder", new PreciseParser());
	}

	private void build(final String regex, final String name, final IParser parser) {
		finders.put(name, new PhraseFinder(Pattern.compile(regex), parser, name));
	}

	public PhraseFinder[] finders() {
		return finders.values().toArray(new PhraseFinder[finders.values().size()]);
	}

	public PhraseFinder get(final String key) {
		return finders.get(key);
	}

}
