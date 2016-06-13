package time.analyser;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.analyser.parser.*;
import time.domain.DatedPhrase;

public class DatedPhraseDetector {

    private static final Logger LOGGER = LogManager.getLogger(DatedPhraseDetector.class);
	public final static String TEXT_NUMBERS = "un|deux|trois|quatre|cinq|six|sept|huit|neuf|dix|onze|douze|treize|quatorze|quinze|seize|dix-sept|dix-huit|dix-neuf|vingt";
	private static final String START = "(^| |,|;)";
	private final static String JOUR = "((?<d>\\d{1,2})(er)? )?";
	private final static String MOIS = "(?<m>(J|j)an(\\.|v\\.|vier)|(F|f)(é|e)v(\\.|rier)|(M|m)ar(\\.|s)|(A|a)vr(\\.|il)|(M|m)ai|(J|j)uin|(J|j)uil(\\.|let)|(A|a)o(u|û)(\\.|t)|(S|s)ep(\\.|t\\.|tembre)|(O|o)ct(\\.|obre)|(N|n)ov(\\.|embre)|(D|d)(é|e)c(\\.|embre))";
	private final static String ANNEE = " (?<y>\\d{3,4})";
	private final static String YEAR = "\\d{2,4}";
    private final static String YEARFOUR = "\\d{4}";
	private final static String NEG_REF = "(?<neg> (avant|av.) (J.-?C.|notre ère|le présent))";
	private static final String NEG_REF_OPT = NEG_REF + "?";
	private final static String TEXT_NUMBERS_ = "(?<gt>(" + TEXT_NUMBERS + "))";
	private static final String NUMBER_FLAG = "?<g>";
	private final static String NUMBERS = "(" + NUMBER_FLAG + "\\d+([,\\.]\\d+)?)";
	private final static String ET = "( (jusque|à|et) (vers )?(environ )?(l'an )?"+ "(" + NUMBER_FLAG + "(-)?\\d{2,9})" + NEG_REF + "?)?";
	private static final String ILYAENVIRON = "([Ii]l y a|[Vv]oici|(datent|vie(ux|ille)) d(e)?|au cours des|dès)([ ']environ)?( quelque)?( près de)?";
	private final static String NOT_IN_DATE_WORDS = "(?<ex>(spectateur|degré|coup|pas|tour|heure|minute|seconde|mois|moto|titre|volt|salarié|arbre|commanderie|étudiant|degré|mètre|mot|ouvrier|mort|partenaire)s?)?";
	private static final String END = "(;|,|\\.| " + NOT_IN_DATE_WORDS + "|$)";
	private static final String END2 = "(;|:|,|\\.|$)";
	private static final String JCSTART = "([Aà] partir de|[Dd]ate(nt)? de|[Dd]ébut|[Aa]près|[Dd]epuis|[Ee]n|[Dd]ans les années) ";
    private final Map<DateType, PhrasesAnalyser> finders;
    private final PhrasesAnalyser[] findersArray;

	public DatedPhraseDetector() {
		this.finders = new HashMap<>();
		build();
		findersArray = finders.values().toArray(new PhrasesAnalyser[finders.values().size()]);
        LOGGER.info(this);
	}

    public PhrasesAnalyser[] getFindersArray() {
        return findersArray;
    }

	private void build() {
		build(DateType.ILYA, "(?<neg>" + ILYAENVIRON + ") (" + NUMBER_FLAG + "\\d{1,3}( ?000)?) (ans|dernières années)", new IlYAParser());
		build(DateType.MILLIARD, "((?<neg>[Dd]ans)|([Vv]oici|[Ii]l y a)) (plus (de |d'))?(environ )?(" + NUMBERS + "|" + TEXT_NUMBERS_ + ") milli(?<s>ard|on)s? d['’]années", new MilliardParser());
		build(DateType.JC, START + JCSTART + "(l'an )?(" + NUMBER_FLAG + "(-)?\\d{2,9})" + NEG_REF_OPT + END, new ExcludingJCParser());
		build(DateType.JCET, "[Ee]ntre (" + NUMBER_FLAG + YEAR + ") et " + YEAR + NEG_REF_OPT + END2, new JCParser());
		build(DateType.JCET2, "[Ee]ntre " + YEAR + " et (" + NUMBER_FLAG + YEAR + ")" + NEG_REF_OPT + END2, new JCParser());
		build(DateType.NEARJC, START + "([Ee]nviron) (" + NUMBER_FLAG + YEAR + ")(,? )ans" + NEG_REF, new JCParser());
		build(DateType.NEARJC2, START + "([Vv]ers l'an) (" + NUMBER_FLAG + "(-)?" + YEAR + ")(,?)" + NEG_REF_OPT, new JCParser());
		build(DateType.NEARJC3, START + "[Vv]ers (" + NUMBER_FLAG + YEAR + ")" + NEG_REF, new JCParser());
		build(DateType.NEARLESS, START + "(remontant à [Ee]nviron) (?<neg>-)(" + NUMBER_FLAG + YEAR + ")", new JCParser());
		build(DateType.ROMAN, " (" + NUMBER_FLAG + "[ixvIXV]+)e siècle" + NEG_REF_OPT, new RomanParser());
		build(DateType.ANNEE2DOT, "^(([Vv]ers|[Ee]nviron|[Ee]n) )?(" + NUMBER_FLAG + "([ -])?\\d{4}) ?:", new AnneeParser());
		build(DateType.PRECISE, "(" + NUMBER_FLAG + JOUR + MOIS + ANNEE + ")" + NEG_REF_OPT, new PreciseParser());
		build(DateType.DOUBLEPARENTHESIS, "\\( ?(" + NUMBER_FLAG + YEAR + ") ?- ?(" + YEAR + " ?) ?\\)", new AnneeParser());
        build(DateType.DOUBLEPARENTHESIS2, "\\( ?(" + YEAR + ") ?- ?(" + NUMBER_FLAG + YEAR + " ?) ?\\)", new AnneeParser());
		build(DateType.TIRET, "(" + NUMBER_FLAG + YEARFOUR + ")-(" + YEARFOUR + " ?)( |\\.|,|;)", new AnneeParser());
		build(DateType.TIRET2, "(" + YEARFOUR + ")-(" + NUMBER_FLAG + YEARFOUR + " ?)( |\\.|,|;)", new AnneeParser());
	}

	public PhrasesAnalyser get(final DateType dateType) {
		return finders.get(dateType);
	}

	private void build(final DateType name, final String regex, final IParser parser) {
		finders.put(name, new PhrasesAnalyser(Pattern.compile(regex), parser, name));
	}

	public List<DatedPhrase> detect(final String phrase) {
		return Arrays.stream(findersArray).map(f -> f.findPhrases(phrase)).flatMap(l -> l.stream()).collect(Collectors.toCollection(ArrayList::new));
	}

    @Override
    public String toString() {
        return "DatedPhraseDetector{}";
    }
}
