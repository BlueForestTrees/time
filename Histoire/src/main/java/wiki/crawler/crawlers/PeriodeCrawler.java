package wiki.crawler.crawlers;

import java.util.regex.Pattern;

import wiki.crawler.ICrawler;
import wiki.entity.Page;

public class PeriodeCrawler implements ICrawler{

	private Pattern pattern;
	
	//pour les tests
	public Pattern getPattern() {
		return pattern;
	}

	public PeriodeCrawler(){
		String sp = " ?";
		String ouv = "\\(";
		String separation = "(-|–)"+sp;
		String ferm = "\\)";
		String jour = "\\d?\\d"+sp;
		String mois = "(janvier|février|mars|avril|mai|juin|juillet|aout|septembre|octobre|novembre|décembre)"+sp;
		String annee = "\\d{4}"+sp;
		pattern = Pattern.compile(ouv+"("+jour+mois+")?"+annee+separation+"("+jour+mois+")?"+annee+ferm,Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
	}
	
	public void visit(Page page) {
		
	}

}
