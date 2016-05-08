package time.analyser.find;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import time.repo.bean.DatedPhrase;
import time.tool.string.Strings;
import time.analyser.find.parser.*;

public class DatedPhrasesFinders {

	public final static String TEXT_NUMBERS = "un|deux|trois|quatre|cinq|six|sept|huit|neuf|dix|onze|douze|treize|quatorze|quinze|seize|dix-sept|dix-huit|dix-neuf|vingt";
	private static final String START = "(^| |,|;)";
	private final static String JOUR = "((?<d>\\d{1,2})(er)? )?";
	private final static String MOIS = "(?<m>(J|j)an(\\.|v\\.|vier)|(F|f)(é|e)v(\\.|rier)|(M|m)ar(\\.|s)|(A|a)vr(\\.|il)|(M|m)ai|(J|j)uin|(J|j)uil(\\.|let)|(A|a)o(u|û)(\\.|t)|(S|s)ep(\\.|t\\.|tembre)|(O|o)ct(\\.|obre)|(N|n)ov(\\.|embre)|(D|d)(é|e)c(\\.|embre))";
	private final static String ANNEE = " (?<y>\\d{3,4})";
	private final static String YEAR = "\\d{2,4}";
	private final static String REF = "(?<neg> (avant|av.) (J.-?C.|notre ère|le présent))";
	private final static String TEXT_NUMBERS_ = "(?<gt>(" + TEXT_NUMBERS + "))";
	private final static String NUMBERS = "(?<g>\\d+([,\\.]\\d+)?)";
	private static final String ILYAENVIRON = "([Ii]l y a|[Vv]oici|(datent|vie(ux|ille)) d(e)?|au cours des|dès)([ ']environ)?( quelque)?( près de)?";

	private final String notInDateWords;
	private final Map<Finder, DatedPhrasesFinder> finders;
    private final DatedPhrasesFinder[] findersArray;

	public DatedPhrasesFinders(String notInDateWords) {
		this.notInDateWords = "(?<ex>(" + Strings.withPipe(notInDateWords) + "spectateur|degré|coup|pas|tour|heure|minute|seconde|mois|moto|titre|volt|salarié|arbre|commanderie|étudiant|degré|mètre|mot|ouvrier|mort|partenaire)s?)?";
		this.finders = new HashMap<>();
		build();
		findersArray = finders.values().toArray(new DatedPhrasesFinder[finders.values().size()]);
	}

    public DatedPhrasesFinder[] getFindersArray() {
        return findersArray;
    }

	private void build() {
		build(Finder.ILYA, "(?<neg>" + ILYAENVIRON + ") (?<g>\\d{1,3}( ?000)?) (ans|dernières années)", new IlYAParser());
		build(Finder.MILLIARD, "(" + NUMBERS + "|" + TEXT_NUMBERS_ + ") milli(?<s>ard|on)s? d['’]années", new MilliardParser());
		build(Finder.JC, START + "([Aà] partir de|date de|[Dd]ébut|[Dd]epuis|[Ee]n|dans les années) (l'an )?(?<g>(-)?\\d{2,9})" + REF + "?(;|,|\\.| " + notInDateWords + "|$)", new ExcludingJCParser());
		build(Finder.NEARJC, START + "([Ee]nviron) (?<g>" + YEAR + ")(,? )ans" + REF, new JCParser());
		build(Finder.NEARJC2, START + "([Vv]ers l'an|après) (?<g>(-)?" + YEAR + ")(,?)" + REF + "?", new JCParser());
		build(Finder.NEARJC3, START + "[Vv]ers (?<g>" + YEAR + ")" + REF, new JCParser());
		build(Finder.NEARLESS, START + "(remontant à [Ee]nviron) (?<neg>-)(?<g>" + YEAR + ")", new JCParser());
		build(Finder.ROMAN," (?<g>[ixvIXV]+)e siècle" + REF + "?", new RomanParser());
		build(Finder.ANNEE2DOT, "^(([Vv]ers|[Ee]nviron|[Ee]n) )?(?<g>([ -])?\\d{4}) ?:", new AnneeParser());
		build(Finder.PRECISE, "(?<g>" + JOUR + MOIS + ANNEE + ")" + REF + "?", new PreciseParser());
		//TODO marche marcher.
		build(Finder.DOUBLEPARENTHESIS, "( ?" + YEAR + " ?- ?" + YEAR + " ?)", new AnneeParser());
	}

	public DatedPhrasesFinder get(final Finder finder) {
		return this.get(finder.name());
	}

	public DatedPhrasesFinder get(final String key) {
		return finders.get(key);
	}

	private void build(final Finder name, final String regex, final IParser parser) {
		finders.put(name, new DatedPhrasesFinder(Pattern.compile(regex), parser, name));
	}

	public List<DatedPhrase> detect(final String phrase) {
		return Arrays.stream(findersArray).map(f -> f.findPhrases(phrase)).flatMap(l -> l.stream()).collect(Collectors.toCollection(ArrayList::new));
	}

}
